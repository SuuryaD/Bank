package com.bank.service;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Server {
	
	static ServerSocket ss;
    static Socket s;

	public void run(int port)  {
		
		
		Scanner scan = new Scanner(System.in);
	    try{
	    	

	    	ss = new ServerSocket(port);
//	    	System.out.println("Server Started with port: " + ss.getLocalPort());
	    	do {
	    		
	    		Receivemsg receive = new Receivemsg();
		    	Sendmsg send = new Sendmsg();
		    	Thread receiver = new Thread(receive);
		    	Thread sender = new Thread(send);
			       	     	
		    	System.out.println("Waiting for Customer");
		    	
		    	s = ss.accept();
				        
		    	System.out.println("Connection Established with client: " + s.getRemoteSocketAddress());
		    	System.out.println("** Note: Type 'bye' and press Enter to disconnect **");
			
		    	receiver.start();
		    	sender.start();
		    	
		    	receiver.join();
		    	
		    	
		    	sender.interrupt();
		    	if(sender.isAlive()) {
		    		
		    		sender.join();
		    	}
		    		
		    	
		    	System.out.println("Do you want to exit?");
		    	System.out.println("1. Exit");
		    	System.out.println("2. No");
		    	char ch = scan.nextLine().charAt(0);
		    	if(ch == '1')
		    		break;
	    	}while(true);
	    	ss.close();

        }catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
	
}
	
class Sendmsg implements Runnable {
    @Override
    public void run() {
        String input;
        PrintWriter out = null;
        try {
            out = new PrintWriter(Server.s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
//        	!(input = br.readLine()).equals("bye") ||
			while (!Thread.interrupted()){
				
				ExecutorService ex = Executors.newSingleThreadExecutor();
				Future<String> future = ex.submit(() -> {
					String temp = "";
					if(br.ready())
					 temp = br.readLine();
					return temp;
				});
				try {
					input = future.get(10, TimeUnit.SECONDS);
					
					if(input != "") {
						System.out.println(input);
						out.println(input);
					}
						
					
				} catch( InterruptedException  e) {
					
					
					future.cancel(true);
					ex.shutdownNow();
					break;
				} catch( ExecutionException e) {

					future.cancel(true);
					ex.shutdownNow();
				}
				catch( TimeoutException e) {

					future.cancel(true);
					ex.shutdownNow();
				}
				finally {
					ex.shutdownNow();
				}
				
				
            }
			
            out.println("Server disconnected");
            Server.s.close();
        } catch (IOException e ) {
            System.out.println("Disconnected");
        }
    }
}

class Receivemsg implements Runnable {
    @Override
    public void run() {
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(Server.s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while((line = in.readLine()) != null) {
                if(line.equals("Customer disconnected")){
                    System.out.println("> Client: bye");
                    System.out.println(line);
                    
                    break;
                }
                System.out.println("> Customer: " + line);
            }
            
        } catch (IOException e) {
            System.out.println("Disconnected");
        }
    }
}
