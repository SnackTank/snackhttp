package net.snacktank.httpserver.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

import net.snacktank.httpserver.WebServer;

//OG code snippet here: http://www2.ic.uff.br/~michael/kr1999/2-application/2_08-webserver.htm

public class Get extends Request{
	    
    public Get(BufferedReader in, DataOutputStream out, Socket connection, String msg) {
    	this.inFromClient = in;
    	this.outToClient = out;
    	this.connectionSocket = connection;
    	this.requestMessage = msg;
    	getMain();
    }
    
    public void getMain() {
		try {
	    	StringTokenizer tokenizedLine = new StringTokenizer(requestMessage);
	    	String method = tokenizedLine.nextToken();
	    	String fileName = tokenizedLine.nextToken().substring(1);
	    	
    		//Load the default file if no file is requested.
    		if(fileName.equals("")) {
    			fileName = "index.html";
    		}
    		
    		File file = new File(fileName);
            
            int numOfBytes = (int) file.length();
            FileInputStream inFile = null;
    		
    		inFile  = new FileInputStream (fileName);
    	
    		byte[] fileInBytes = new byte[numOfBytes];
    		inFile.read(fileInBytes);

			sendStatus(200, false);
    
    		//TODO add content-type back
    
    		outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
    		outToClient.writeBytes("Server: " + WebServer.name + "\r\n");
    		outToClient.writeBytes("Date: " + ZonedDateTime.now(java.time.ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\r\n");

    		if(method.equals("GET")) {
    			outToClient.writeBytes("\r\n");
    			outToClient.write(fileInBytes, 0, numOfBytes);
    		}
    		inFile.close();
    		
	    	
		} catch (IOException e) {
			e.printStackTrace();
			sendStatus(404, true);
		}
		try {
			connectionSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
