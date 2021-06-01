package com.bank.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {


    static Socket s;
    
	public void run(int port)  {
		
		Receivemsg1 receive = new Receivemsg1();
        Sendmsg1 send = new Sendmsg1();

        Thread receiver = new Thread(receive);
        Thread sender = new Thread(send);
        
        try{
        	s = new Socket("localhost", port);
            System.out.println("Connected to: " + s.getRemoteSocketAddress());
            System.out.println("** Note: Type 'bye' and press Enter to disconnect **");

            receiver.start();
            sender.start();
            sender.join();
            
            receiver.interrupt();

           
        }catch(IOException | InterruptedException e){
//            e.printStackTrace();
        	System.out.println("Error Occured");
        }
        
	}
	
}

class Sendmsg1 implements Runnable {
    @Override
    public void run() {
        String input;
        PrintWriter out = null;
        try {
            out = new PrintWriter(Client.s.getOutputStream(), true);
        } catch (IOException e) {
//            e.printStackTrace();
        	System.out.println("error Occured");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (!(input = br.readLine()).equals("bye")){
                out.println(input);
            }
            out.println("Customer disconnected");
            
        } catch (IOException e) {
            System.out.println("Disconnected");
        }
    }
}


class Receivemsg1 implements Runnable {
    @Override
    public void run() {
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(Client.s.getInputStream()));
        } catch (IOException e) {
//            e.printStackTrace();
        	System.out.println("Error Occured");
        }
        try {
//        	(line = in.readLine()) != null || 
        	
            while(!Thread.interrupted()) {
            	line = in.readLine();
                if(line.equals("Server disconnected")){

                    break;
                }
                System.out.println("> Support: " + line);
            }
            
           
            Client.s.close();
        
        } catch (IOException e) {
			System.out.println("Disconnected");
		} 
    }
}
