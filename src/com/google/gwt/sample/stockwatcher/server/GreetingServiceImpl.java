package com.google.gwt.sample.stockwatcher.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	
	public static String hashSHA1CharAry (String c) {
    	try {
    		MessageDigest md=MessageDigest.getInstance("SHA-1");
    		md.reset();
    		md.update(c.getBytes());
    		return new String(md.digest());
    	} catch (Exception e) {}
    	return "";
    }
	
	public String[][] greetServer(String sn, Date sd, Date ed) throws IllegalArgumentException {
		try {
			
			Socket sc=new Socket("10.100.2.56",40001);
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("37");
			toSend.add(sn);
			toSend.add(LocalDateTime.of(1990,1,1,0,0));
			toSend.add(LocalDateTime.now());
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(toSend);
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			ArrayList<Object []> data=(ArrayList<Object []>)ois.readObject();
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String [][] newData = new String[data.size()][2];
			
			for(int i=0;i<data.size();i++)
			{
		    	newData[i][0]=df.format((LocalDateTime)data.get(i)[0]);
		    	newData[i][1]=data.get(i)[1].toString();
			}
			
			return newData;
		} catch (Exception e) {return null;}
	}
	
	public boolean userLogin(String username, String password) throws IllegalArgumentException {
		try {

	        final byte[] salt = "chichilol".getBytes();
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec spec = new PBEKeySpec("chi-admin".toCharArray(), salt, 1024, 128);
	        SecretKey secret=new SecretKeySpec(factory.generateSecret(spec).getEncoded(),"AES");
	        Cipher encrypter=Cipher.getInstance("AES");
	        encrypter.init(Cipher.ENCRYPT_MODE,secret);
	        Cipher decrypter=Cipher.getInstance("AES");
	        decrypter.init(Cipher.DECRYPT_MODE,secret);
			
			String hashedPassword = hashSHA1CharAry(password);
			Socket sc=new Socket("10.100.2.56",40001);
			ArrayList<Object> toSend=new ArrayList<>();
			toSend.add("2");
			toSend.add(username);
			toSend.add(hashedPassword);
			OutputStream os = sc.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(new SealedObject(toSend,encrypter));
			
			InputStream is=sc.getInputStream();
			ObjectInputStream ois=new ObjectInputStream(is);
			@SuppressWarnings("unchecked")
			String data=(String)(((SealedObject) ois.readObject()).getObject(decrypter));
			
			oos.close();
			os.close();
			ois.close();
			is.close();
			
			sc.close();
			
			return data.equals("ACTIVATED");
		} catch (Exception e) {return false;}
	}

}
