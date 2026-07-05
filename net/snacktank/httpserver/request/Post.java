package net.snacktank.httpserver.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import net.snacktank.httpserver.WebServer;

public class Post extends Request{

	public Post(BufferedReader in, DataOutputStream out, Socket connection, String msg) {
		this.inFromClient = in;
    	this.outToClient = out;
    	this.connectionSocket = connection;
    	this.requestMessage = msg;
    	try {
			postMain();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void postMain() throws IOException{
		int bodyLength = 0;
		boolean letThatFileIN = false;
    	StringTokenizer tokenizedLine = new StringTokenizer(requestMessage);
    	String method = tokenizedLine.nextToken();
    	String fileName = tokenizedLine.nextToken();
    	
    	if(method.equals("PUT") && WebServer.putEnable) {
    		letThatFileIN = true;
    	}
    	
    	if (fileName.startsWith("/uploads/") == true || method.equals("PUT")) {
    		fileName = fileName.substring(1);
    		String content;
    		
    		while((content = WebServer.inFromClient.readLine()) != null) {
    			if(content.startsWith("Content-Length:")) {
    				//Find out how long the body is
    				bodyLength = Integer.parseInt(content.replaceAll("[^0-9]", ""));
    			}
    			if(content.isEmpty()) {
    				//Headers are now over, time for the body
    				break;
    			}
    		}
    		//Takes the BufferedInput into an array of chars to be stored.
    		String date = null;
    		char[] bodyChars = new char[bodyLength];
			int readChars = 0;
			while (readChars < bodyLength) {
			    int r = inFromClient.read(bodyChars, readChars, bodyLength - readChars);
			    readChars += r;
			    if(r == -1) {
			    	sendStatus(500, true);
			    	connectionSocket.close();
	            	return;
			    }
			}
			String toWrite = new String(bodyChars);
			try {
				if(method.equals("POST")) {
					date = Long.toString(System.currentTimeMillis());
					FileWriter writer = new FileWriter("uploads/request-" + date + ".txt");
					writer.write(toWrite);
					writer.close();
				} else if(letThatFileIN) {
					FileWriter writer = new FileWriter(fileName);
					writer.write(toWrite);
					writer.close();
				}
			} catch(FileNotFoundException e) {
				sendStatus(400, true);
				connectionSocket.close();
            	return;
			}
			//To tell the client that the file has been created!
			sendStatus(201, false);		
			if(method.equals("POST")) {
				outToClient.writeBytes("Location: /uploads/request-" + date + ".txt\r\n");
				} else if (letThatFileIN){
				outToClient.writeBytes("Location: " +"/" + fileName + "\r\n");
			}
    		outToClient.writeBytes("Server: " + WebServer.name + "\r\n");
    		outToClient.writeBytes("Date: " + ZonedDateTime.now(java.time.ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\r\n");
			outToClient.writeBytes("\r\n");    		
    	}
    	connectionSocket.close();
	}
}
