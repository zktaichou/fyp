package com.google.gwt.sample.stockwatcher.server;

import org.joda.time.DateTime;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	private static String tempFileName = "prediction.arff";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat yearlyFormat = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat monthlyFormat = new SimpleDateFormat("yyyy-MM");
	private static final SimpleDateFormat dailyFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static long difference;
	private static String[][] data;

	  public static String[][] predict(String incoming[][], int incomingStep, Boolean isAppend) {
		  if(incoming.length==1)
		  {
			  return incoming;
		  }
	      steps = incomingStep;
	  try {
		  	if(isAppend)
		  	{
		  		data = append(data,incoming);
		  	}
	    	else
	    	{
	    		data = incoming;
	    	}
		  	try {
	    	  File lol = new File(tempFileName);
	    	  if (lol.exists()) {
	    		  lol.delete();     
	    	  }
	    	  
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(tempFileName)));
				pw.println("@relation sample");
				pw.println();
				pw.println("@attribute Date date 'yyyy-MM-dd'");
				pw.println("@attribute value numeric");
				pw.println();
				pw.println("@data");
				
				int count=1;
				
				for(int i=0; i<data.length;i++){
					String cLine = "";
					for(int j=0; j<data[i].length;j++)
					{
						if(j==0)
						{
							cLine+=(count++)+"-01-01,";
						}
						else
						{
							cLine+=data[i][j]+",";
						}
					}
					pw.println(cLine.substring(0, cLine.length()-1));
				}
				pw.close();
				
				difference = dateFormat.parse(data[data.length-1][0]).getTime() - dateFormat.parse(data[data.length-2][0]).getTime();
				
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
	      forecaster.getTSLagMaker().setMaxLag(data.length); //get only past x number of data
	      
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

	      // forecast for x number of units (months) beyond the end of the
	      // training data
	      List<List<NumericPrediction>> forecast = forecaster.forecast(steps, System.out);
	      DateTime currentDt = getCurrentDateTime(forecaster.getTSLagMaker());

	      // output the predictions. Outer list is over the steps; inner list is over
	      // the targets
	      ArrayList<String> dresult = new ArrayList<>();
	      ArrayList<String> presult = new ArrayList<>();
	      Date d = new Date();
	      d.setTime(dateFormat.parse(data[data.length-1][0]).getTime());

	      for (int i = 0; i < steps; i++) {
	        List<NumericPrediction> predsAtStep = forecast.get(i);
	        NumericPrediction predForTarget = predsAtStep.get(0);
	        presult.add("" + predForTarget.predicted());
	        
	        d.setTime(d.getTime()+difference);
		    dresult.add(dateFormat.format(d));
//		    String time = currentDt.toString("yyyy-MM-dd");
		    currentDt = advanceTime(forecaster.getTSLagMaker(), currentDt);
	      }
	      
	      String[][] predictedData = new String[steps][2];
	      
	      for (int i = 0; i < presult.size(); i++) {
		        predictedData[i][0]=dresult.get(i);
		        predictedData[i][1]=presult.get(i);
	      }
	      
	      String[][] finalData = append(data,predictedData);
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValues.txt")));
				int i=0;
				for(String temp: dresult){
				pw.println(temp+","+presult.get(i++));
				}
				pw.close();
			} catch (Exception f) {}
	      
	      // we can continue to use the trained forecaster for further forecasting
	      // by priming with the most recent historical data (as it becomes available).
	      // At some stage it becomes prudent to re-build the model using current
	      // historical data.
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValuesAll.txt")));
				for(int i=0; i<finalData.length;i++)
				{
					pw.println(finalData[i][0]+","+finalData[i][1]);
				}
				pw.close();
			} catch (Exception f) {}

	      return finalData;
	      
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      return null;
	    }
	  }
	  
	  public static String[][] predictDaily(String incoming[][], int incomingStep, Boolean isAppend) {
		  if(incoming.length==1)
		  {
			  return incoming;
		  }
	      steps = incomingStep;
	  try {
		  	if(isAppend)
		  	{
		  		data = append(data,incoming);
		  	}
	    	else
	    	{
	    		data = incoming;
	    	}
		  	try {
	    	  File lol = new File(tempFileName);
	    	  if (lol.exists()) {
	    		  lol.delete();     
	    	  }
	    	  
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(tempFileName)));
				pw.println("@relation sample");
				pw.println();
				pw.println("@attribute Date date 'yyyy-MM-dd'");
				pw.println("@attribute value numeric");
				pw.println();
				pw.println("@data");
				
				int count=1;
				
				for(int i=0; i<data.length;i++){
					String cLine = "";
					for(int j=0; j<data[i].length;j++)
					{
						if(j==0)
						{
							cLine+=(count++)+"-01-01,";
						}
						else
						{
							cLine+=data[i][j]+",";
						}
					}
					pw.println(cLine.substring(0, cLine.length()-1));
				}
				pw.close();
				
				difference = dailyFormat.parse(data[data.length-1][0]).getTime() - dailyFormat.parse(data[data.length-2][0]).getTime();
				
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
	      forecaster.getTSLagMaker().setMaxLag(data.length); //get only past x number of data
	      
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

	      // forecast for x number of units (months) beyond the end of the
	      // training data
	      List<List<NumericPrediction>> forecast = forecaster.forecast(steps, System.out);
	      DateTime currentDt = getCurrentDateTime(forecaster.getTSLagMaker());

	      // output the predictions. Outer list is over the steps; inner list is over
	      // the targets
	      ArrayList<String> dresult = new ArrayList<>();
	      ArrayList<String> presult = new ArrayList<>();
	      Date d = new Date();
	      d.setTime(dailyFormat.parse(data[data.length-1][0]).getTime());

	      for (int i = 0; i < steps; i++) {
	        List<NumericPrediction> predsAtStep = forecast.get(i);
	        NumericPrediction predForTarget = predsAtStep.get(0);
	        presult.add("" + predForTarget.predicted());
	        
	        d.setTime(d.getTime()+difference);
		    dresult.add(dailyFormat.format(d));
//		    String time = currentDt.toString("yyyy-MM-dd");
		    currentDt = advanceTime(forecaster.getTSLagMaker(), currentDt);
	      }
	      
	      String[][] predictedData = new String[steps][2];
	      
	      for (int i = 0; i < presult.size(); i++) {
		        predictedData[i][0]=dresult.get(i);
		        predictedData[i][1]=presult.get(i);
	      }
	      
	      String[][] finalData = append(data,predictedData);
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValues.txt")));
				int i=0;
				for(String temp: dresult){
				pw.println(temp+","+presult.get(i++));
				}
				pw.close();
			} catch (Exception f) {}
	      
	      // we can continue to use the trained forecaster for further forecasting
	      // by priming with the most recent historical data (as it becomes available).
	      // At some stage it becomes prudent to re-build the model using current
	      // historical data.
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValuesAll.txt")));
				for(int i=0; i<finalData.length;i++)
				{
					pw.println(finalData[i][0]+","+finalData[i][1]);
				}
				pw.close();
			} catch (Exception f) {}

	      return finalData;
	      
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      return null;
	    }
	  }
	  
	  public static String[][] predictMonthly(String incoming[][], int incomingStep, Boolean isAppend) {
		  if(incoming.length==1)
		  {
			  return incoming;
		  }
	      steps = incomingStep;
	  try {
		  	if(isAppend)
		  	{
		  		data = append(data,incoming);
		  	}
	    	else
	    	{
	    		data = incoming;
	    	}
		  	try {
	    	  File lol = new File(tempFileName);
	    	  if (lol.exists()) {
	    		  lol.delete();     
	    	  }
	    	  
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(tempFileName)));
				pw.println("@relation sample");
				pw.println();
				pw.println("@attribute Date date 'yyyy-MM'");
				pw.println("@attribute value numeric");
				pw.println();
				pw.println("@data");
				
				int count=1;
				
				for(int i=0; i<data.length;i++){
					String cLine = "";
					for(int j=0; j<data[i].length;j++)
					{
						if(j==0)
						{
							cLine+=(count++)+"-01,";
						}
						else
						{
							cLine+=data[i][j]+",";
						}
					}
					pw.println(cLine.substring(0, cLine.length()-1));
				}
				pw.close();
				
				difference = monthlyFormat.parse(data[data.length-1][0]).getTime() - monthlyFormat.parse(data[data.length-2][0]).getTime();
				
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
	      forecaster.getTSLagMaker().setMaxLag(data.length); //get only past x number of data
	      
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

	      // forecast for x number of units (months) beyond the end of the
	      // training data
	      List<List<NumericPrediction>> forecast = forecaster.forecast(steps, System.out);
	      DateTime currentDt = getCurrentDateTime(forecaster.getTSLagMaker());

	      // output the predictions. Outer list is over the steps; inner list is over
	      // the targets
	      ArrayList<String> dresult = new ArrayList<>();
	      ArrayList<String> presult = new ArrayList<>();
	      Date d = new Date();
	      d.setTime(monthlyFormat.parse(data[data.length-1][0]).getTime());

	      for (int i = 0; i < steps; i++) {
	        List<NumericPrediction> predsAtStep = forecast.get(i);
	        NumericPrediction predForTarget = predsAtStep.get(0);
	        presult.add("" + predForTarget.predicted());
	        
	        d.setTime(d.getTime()+difference);
		    dresult.add(monthlyFormat.format(d));
//		    String time = currentDt.toString("yyyy-MM-dd");
		    currentDt = advanceTime(forecaster.getTSLagMaker(), currentDt);
	      }
	      
	      String[][] predictedData = new String[steps][2];
	      
	      for (int i = 0; i < presult.size(); i++) {
		        predictedData[i][0]=dresult.get(i);
		        predictedData[i][1]=presult.get(i);
	      }
	      
	      String[][] finalData = append(data,predictedData);
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValues.txt")));
				int i=0;
				for(String temp: dresult){
				pw.println(temp+","+presult.get(i++));
				}
				pw.close();
			} catch (Exception f) {}
	      
	      // we can continue to use the trained forecaster for further forecasting
	      // by priming with the most recent historical data (as it becomes available).
	      // At some stage it becomes prudent to re-build the model using current
	      // historical data.
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValuesAll.txt")));
				for(int i=0; i<finalData.length;i++)
				{
					pw.println(finalData[i][0]+","+finalData[i][1]);
				}
				pw.close();
			} catch (Exception f) {}

	      return finalData;
	      
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      return null;
	    }
	  }
	  
	  public static String[][] predictYearly(String incoming[][], int incomingStep, Boolean isAppend) {
		  if(incoming.length==1)
		  {
			  return incoming;
		  }
	      steps = incomingStep;
	  try {
		  	if(isAppend)
		  	{
		  		data = append(data,incoming);
		  	}
	    	else
	    	{
	    		data = incoming;
	    	}
		  	try {
	    	  File lol = new File(tempFileName);
	    	  if (lol.exists()) {
	    		  lol.delete();     
	    	  }
	    	  
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(tempFileName)));
				pw.println("@relation sample");
				pw.println();
				pw.println("@attribute Date date 'yyyy'");
				pw.println("@attribute value numeric");
				pw.println();
				pw.println("@data");
				
				int count=1;
				
				for(int i=0; i<data.length;i++){
					String cLine = "";
					for(int j=0; j<data[i].length;j++)
					{
						if(j==0)
						{
							cLine+=(count++)+",";
						}
						else
						{
							cLine+=data[i][j]+",";
						}
					}
					pw.println(cLine.substring(0, cLine.length()-1));
				}
				pw.close();
				
				difference = yearlyFormat.parse(data[data.length-1][0]).getTime() - yearlyFormat.parse(data[data.length-2][0]).getTime();
				
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
	      forecaster.getTSLagMaker().setMaxLag(data.length); //get only past x number of data
	      
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

	      // forecast for x number of units (months) beyond the end of the
	      // training data
	      List<List<NumericPrediction>> forecast = forecaster.forecast(steps, System.out);
	      DateTime currentDt = getCurrentDateTime(forecaster.getTSLagMaker());

	      // output the predictions. Outer list is over the steps; inner list is over
	      // the targets
	      ArrayList<String> dresult = new ArrayList<>();
	      ArrayList<String> presult = new ArrayList<>();
	      Date d = new Date();
	      d.setTime(yearlyFormat.parse(data[data.length-1][0]).getTime());

	      for (int i = 0; i < steps; i++) {
	        List<NumericPrediction> predsAtStep = forecast.get(i);
	        NumericPrediction predForTarget = predsAtStep.get(0);
	        presult.add("" + predForTarget.predicted());
	        
	        d.setTime(d.getTime()+difference);
		    dresult.add(yearlyFormat.format(d));
//		    String time = currentDt.toString("yyyy-MM-dd");
		    currentDt = advanceTime(forecaster.getTSLagMaker(), currentDt);
	      }
	      
	      String[][] predictedData = new String[steps][2];
	      
	      for (int i = 0; i < presult.size(); i++) {
		        predictedData[i][0]=dresult.get(i);
		        predictedData[i][1]=presult.get(i);
	      }
	      
	      String[][] finalData = append(data,predictedData);
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValues.txt")));
				int i=0;
				for(String temp: dresult){
				pw.println(temp+","+presult.get(i++));
				}
				pw.close();
			} catch (Exception f) {}
	      
	      // we can continue to use the trained forecaster for further forecasting
	      // by priming with the most recent historical data (as it becomes available).
	      // At some stage it becomes prudent to re-build the model using current
	      // historical data.
	      
	      try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter("predictedValuesAll.txt")));
				for(int i=0; i<finalData.length;i++)
				{
					pw.println(finalData[i][0]+","+finalData[i][1]);
				}
				pw.close();
			} catch (Exception f) {}

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