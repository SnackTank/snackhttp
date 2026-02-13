package net.snacktank.httpserver.request;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import net.snacktank.httpserver.WebServer;

public class Post {
	
	String fileName;
	
	public Post() throws IOException {
		int bodyLength = 0;
		boolean letThatFileIN = false;
    	fileName = WebServer.tokenizedLine.nextToken();
    	if(WebServer.method.equals("PUT") && WebServer.putEnable) {
    		letThatFileIN = true;
    	}
    	if (fileName.startsWith("/post/") == true || WebServer.method.equals("PUT")) {
    		fileName  = fileName.substring(1);
    		String content;

    		//Read request from client, and figure out length of body for parsing.
    		
    		while((content = WebServer.inFromClient.readLine()) != null) {
    			/* TODO change file extension with content-type
    			if(content.startsWith("Content-Type:")) {
    				System.out.println(content);
    			}
    			*/
    			if(content.startsWith("Content-Length:")) {
    				//Find out how long the body is
    				bodyLength = Integer.parseInt(content.replaceAll("[^0-9]", ""));
    			}
    			if(content.isEmpty()) {
    				//Headers are now over, time for the body
    				break;
    			}
    		}

    		//TODO Support any binary file
    		//Takes the BufferedInput into an array of chars to be stored.
    		String date = null;
    		char[] bodyChars = new char[bodyLength];
			int readChars = 0;
			while (readChars < bodyLength) {
			    int r = WebServer.inFromClient.read(bodyChars, readChars, bodyLength - readChars);
			    readChars += r;
			    if(r == -1) {
			    	WebServer.SendStatus(500, true);
			    	WebServer.connectionSocket.close();
	            	return;
			    }
			}
			String toWrite = new String(bodyChars);
			try {
				if(WebServer.method.equals("POST")) {
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
				WebServer.SendStatus(400, true);
				WebServer.connectionSocket.close();
            	return;
			}

    		/*
    		for(int i = 0; i < bodyLength; i++) {
    			System.out.print(bodyChars[i]);
    		}
    		//To print the body of the POST request
    		*/
			String logName = null;
			//To tell the client that the file has been created!
			WebServer.SendStatus(201, false);		
			if(WebServer.method.equals("POST")) {
				WebServer.outToClient.writeBytes("Location: /uploads/request-" + date + ".txt\r\n");
				logName = "Location: /uploads/request-" + date + ".txt\r\n";
				} else if (letThatFileIN){
				WebServer.outToClient.writeBytes("Location: " +"/" + fileName + "\r\n");
				logName = "Location: " +"/" + fileName + "\r\n";
			}
			WebServer.outToClient.writeBytes("\r\n");
			System.out.println(WebServer.connectionSocket.getInetAddress() + " : " + logName);
    	} else {
    		WebServer.SendStatus(400, true);
    	}
	}
}
