package com.esiea.tp4A.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.esiea.tp4A.mypackage.Rover;

public class ClientRequestHandler implements Runnable {
	private Socket sock;
	private PrintWriter writer;
	private BufferedInputStream reader;
	private String playerName;
	Rover player = null;
	Thread handlePlayerLife = null;
	boolean flag = false;

	public ClientRequestHandler(Socket pSock) {
		sock = pSock;
	}

	public void run(){
		//informatif :
		InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();							
		System.out.println(remote.getAddress().getHostAddress() + " connected on port " + remote.getPort());

		while(!sock.isClosed()){
			try {				
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());				
				//On attend une commande du client, le read va bloquer le thread.
				String clientCommand = read();

				if(clientCommand.startsWith("POST /api/player/")) {
					if(player == null) {
						playerName = clientCommand.substring(17);						
						if(playerName.length()<2 || playerName.length()>35) {
							sendMessageToClient("Name too short or too long. Disconnecting.");
							closeConnection();
						}
						try {
							player = Server.DATAPROVIDER.newPlayer(playerName);				
						} catch(Exception e) {
							sendMessageToClient(e.getMessage());
							closeConnection();
							break;						
						}						
						Thread handlePlayerLife = new Thread(new Runnable() {
							public void run() {
								while(!sock.isClosed()) {
									if(!player.isAlive()) {
										if(!flag) {
											sendMessageToClient("Your rover has been destroyed.");
											flag = true; //on utilise un flag pour n'afficher le message qu'une fois.
										}
									}
								}
							}

						});
						handlePlayerLife.start();
						sendMessageToClient("201");	
						sendMessageToClient(Server.DATAPROVIDER.getJson(player).toString());						
					} else {
						sendMessageToClient("Error : you already have a rover : " + player.getName());
					}

				} else if(clientCommand.startsWith("GET /api/player/")) {
					if(!isPlayerValid()) {
						sendMessageToClient("Invalid username. Disconnecting.");
						closeConnection();
						break;
					}				
					sendMessageToClient("200");
					sendMessageToClient(Server.DATAPROVIDER.getJson(player).toString());					
				} else if(clientCommand.startsWith("PATCH /api/player/")) {
					if(!isPlayerValid()) {
						sendMessageToClient("Invalid player data. Disconnecting.");
						closeConnection();
						break;
					}
					if(player.isAlive()) {
						clientCommand = clientCommand.substring(clientCommand.lastIndexOf("/")+1);
						Server.DATAPROVIDER.move(clientCommand, player);
						sendMessageToClient("200");
						sendMessageToClient(Server.DATAPROVIDER.getJson(player).toString());
					} else {
						sendMessageToClient("You cannot move as your rover has been destroyed.");
					}
				} else if(clientCommand.contains("CLOSE")) {
					if(player!=null)
						player.die();
					closeConnection();
				} else if(clientCommand.equals("")) { //pour �viter une boucle infinie apr�s une d�connexion brutale
					if(player!=null)
						player.die();
					closeConnection();					
				} else {
					sendMessageToClient("Invalid command.");
				}		

			} catch(SocketException e) {
				System.err.println("Connection interrupted.");
				break;
			} catch(IOException e) {
				e.printStackTrace();
			}         
		}
	}

	private boolean isPlayerValid() throws IOException {
		if(player == null)
			return false;
		else
			return true;
	}

	private void sendMessageToClient(String message) {
		if(writer!=null) {
			writer.write(message);
			writer.flush();
			if(message.length() < 100)
				System.out.println("Message sent to client : " + message);
			else
				System.out.println("Json data sent to client.");
		}
	}

	private void closeConnection() throws IOException {
		System.out.println("Connection with " + playerName + " closed.");
		writer = null;
		reader = null;		
		sock.close();
	}

	private String read() throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		if(stream > 0) 
			response = new String(b, 0, stream);
		return response;
	}

}
