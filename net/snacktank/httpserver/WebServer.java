package net.snacktank.httpserver;

import java.io.*;
import java.net.*;

//specifications        http://www.rfc-editor.org/rfc/rfc1945

/* TODO
 * DELETE
 * LINK
 * UNLINK
 */

public class WebServer {
	public static ServerSocket listenSocket;
	public static Socket connectionSocket;
	public static BufferedReader inFromClient;
	public static DataOutputStream outToClient;
	public static boolean putEnable = false;
	public static boolean postEnable = true;
	public static int port = 8080;
	public static String name = "snackhttp/0.2.0";
	
	public static void main(String argv[]) throws Exception  {
				
		//Start listening on a port.
		listenSocket = new ServerSocket(port);
		
    	while(true) {
        
        
        connectionSocket = listenSocket.accept();
        System.out.println("Oh boy a connection!");
        
        //Read the request
        InputStream in = connectionSocket.getInputStream();
        inFromClient = new BufferedReader(new InputStreamReader(in));
        //Send data back to client.
        outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        
        //Pass on to another thread
        ClientHandler ch = new ClientHandler(inFromClient, outToClient, connectionSocket);
        ch.start();
    	} 
   }
}