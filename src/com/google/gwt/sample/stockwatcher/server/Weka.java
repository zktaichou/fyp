package com.google.gwt.sample.stockwatcher.server;

import org.joda.time.DateTime;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import weka.core.Instances;
import weka.filters.supervised.attribute.TSLagMaker;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;

/**
 * Example of using the time series forecasting API. To compile and
 * run the CLASSPATH will need to contain:
 *
 * weka.jar (from your weka distribution)
 * pdm-timeseriesforecasting-ce-TRUNK-SNAPSHOT.jar (from the time series package)
 * jcommon-1.0.14.jar (from the time series package lib directory)
 * jfreechart-1.0.13.jar (from the time series package lib directory)
 */
public class Weka {
	 // new forecaster
    private static WekaForecaster forecaster = new WekaForecaster();
	private static int steps = 1;
	private static String tempFileName = "temp.txt";
	
	  public static String[][] predict(String incoming[][]) {
	    try {
	      String[][] data = incoming;
	      try {
	    	  File lol = new File(tempFileName);
	    	  if (lol.exists()) {
	    		  lol.delete();     
	    	  }
	    	  
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(tempFileName)));
				pw.println("@relation sample");
				pw.println();
				pw.println("@attribute Date date 'dd-MM-yyyy'");
				pw.println("@attribute value numeric");
				pw.println();
				pw.println("@data");
				
				for(int i=0; i<data.length;i++){
					String cLine = "";
					for(int j=0; j<data[i].length;j++)
					{
						cLine+=data[i][j]+",";
					}
					pw.println(cLine.substring(0, cLine.length()-1));
				}
				pw.close();
			} catch (Exception f) {}
	      
	      // load the wine data
	      Instances dataSet = new Instances(new BufferedReader(new FileReader(tempFileName)));
	      
	      // set the targets we want to forecast. This method calls
	      // setFieldsToLag() on the lag maker object for us
	      forecaster.setFieldsToForecast("value");

	      // default underlying classifier is SMOreg (SVM) - we'll use
	      // gaussian processes for regression instead
	      forecaster.setBaseForecaster(new GaussianProcesses());
	      
	      forecaster.getTSLagMaker().setTimeStampField("Date"); // date identifier in the data source file
	      forecaster.getTSLagMaker().setMinLag(1);
	      forecaster.getTSLagMaker().setMaxLag(steps); //get only past 12 data
	      
	      //some unexplained settings even in official docs, go figure
	      forecaster.getTSLagMaker().setAddMonthOfYear(true);
	      forecaster.getTSLagMaker().setAddQuarterOfYear(true);
	      
	      // build the model
	      forecaster.buildForecaster(dataSet, System.out);

	      // prime the forecaster with enough recent historical data
	      // to cover up to the maximum lag. In our case, we could just supply
	      // the 12 most recent historical instances, as this covers our maximum
	      // lag period
	      forecaster.primeForecaster(dataSet);

	      // forecast for 12 units (months) beyond the end of the
	      // training data
	      List<List<NumericPrediction>> forecast = forecaster.forecast(steps, System.out);
	      DateTime currentDt = getCurrentDateTime(forecaster.getTSLagMaker());

	      // output the predictions. Outer list is over the steps; inner list is over
	      // the targets
	      ArrayList<String> presult = new ArrayList<>();
	      ArrayList<String> dresult = new ArrayList<>();
	      
	      for (int i = 0; i < steps; i++) {
	        List<NumericPrediction> predsAtStep = forecast.get(i);
	        for (int j = 0; j < 1; j++) {
	          NumericPrediction predForTarget = predsAtStep.get(j);
	          presult.add("" + predForTarget.predicted());
	        }
	        String time = currentDt.toString("dd-MM-yyyy");
	        dresult.add(time);
	        currentDt = advanceTime(forecaster.getTSLagMaker(), currentDt);
	      }
	      
	      String[][] predictedData = new String[incoming.length][presult.size()];
	      
	      for (int i = 0; i < presult.size(); i++) {
		        predictedData[i][0]=dresult.get(i);
		        predictedData[i][1]=presult.get(i);
	      }
	      
	      String[][] finalData = append(data,predictedData);
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValues.txt")));
				for(String temp: presult){
				pw.println(temp);
				}
				pw.close();
			} catch (Exception f) {}
	      
//	      try {
//	    	    FileReader in = new FileReader(tempFileName);
//				BufferedReader br=new BufferedReader(in);
//				
//				String line = "";
//				Boolean flag = false;
//				
//				while ((line = br.readLine()) != null) {
//			        System.out.println(line);
//			    }
//			    in.close();
//			} catch (Exception f) {}
	      // we can continue to use the trained forecaster for further forecasting
	      // by priming with the most recent historical data (as it becomes available).
	      // At some stage it becomes prudent to re-build the model using current
	      // historical data.

	      return finalData;
	      
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      return null;
	    }
	  }
	  
	  private static String[][] append(String[][] a, String[][] b) {
		  String[][] result = new String[a.length + b.length][];
	        System.arraycopy(a, 0, result, 0, a.length);
	        System.arraycopy(b, 0, result, a.length, b.length);
	        return result;
	    }
	  
	  private static DateTime getCurrentDateTime(TSLagMaker lm) throws Exception {
		    return new DateTime((long)lm.getCurrentTimeStampValue());
		  }

	  private static DateTime advanceTime(TSLagMaker lm, DateTime dt) {
		    return new DateTime((long)lm.advanceSuppliedTimeValue(dt.getMillis()));
		  }
}