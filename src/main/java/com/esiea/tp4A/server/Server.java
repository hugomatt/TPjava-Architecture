package com.esiea.tp4A.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	public static DataProvider DATAPROVIDER;
	private ServerSocket server;
	private boolean isRunning = true;

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
		System.out.println("Server started.");
	}

	public Server() {
		try {
			DATAPROVIDER = new DataProvider();
		} catch (Exception e) {
			System.out.println("Server data provider creation failed : " + e.getMessage());
			e.printStackTrace();
		}
		try {
			server = new ServerSocket(8081, 250);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(isRunning == true) {
					try {
						//On attend une connexion d'un client. La m�thode accept() bloque le thread.
						Socket client = server.accept();
						Thread clientHandlerThread = new Thread(new ClientRequestHandler(client));
						clientHandlerThread.start();

					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}

				try {
					server.close();
				} catch (IOException exception) {
					exception.printStackTrace();
					server = null;
				}
			}
		});

		thread.start();
	}

	public void close() { //Au cas ou, pour pouvoir fermer le serveur
		isRunning = false;
	}

}
