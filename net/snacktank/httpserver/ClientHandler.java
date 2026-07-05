package net.snacktank.httpserver;

import java.io.*;
import java.net.*;
import java.util.*;

import net.snacktank.httpserver.request.Get;
import net.snacktank.httpserver.request.Post;

public class ClientHandler extends Thread{
	
	public BufferedReader inFromClient;
	public DataOutputStream outToClient;
	public Socket connectionSocket;
	public String requestMessageLine;
	
	public ClientHandler(BufferedReader in, DataOutputStream out, Socket connection) {
		outToClient = out;
		inFromClient = in;
		connectionSocket = connection;
	}
	
	@Override
	public void run() {
		try {
			//Find out method
			requestMessageLine = inFromClient.readLine();
			StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
	        String method = tokenizedLine.nextToken();
	        
	        //Do what is needed for each Method
	        switch(method) {
	        case "GET":
	        	new Get(inFromClient, outToClient, connectionSocket, requestMessageLine);
	        	break;
	        case "HEAD":
	        	new Get(inFromClient, outToClient, connectionSocket, requestMessageLine);
	        	break;
	        case "POST":
	        	new Post(inFromClient, outToClient, connectionSocket, requestMessageLine);
	        	break;
	        case "PUT":
	        	new Post(inFromClient, outToClient, connectionSocket, requestMessageLine);
	        	break;
	        default:
	        	break;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
