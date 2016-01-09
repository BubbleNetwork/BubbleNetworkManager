package me.JavaExcption.NetworkManager.NetworkServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import me.JavaExcption.NetworkManager.NetworkClient.NetworkClientI;
import me.JavaExcption.NetworkManager.NetworkClient.UniqueIdentifier;

public class NetworkServer {
	
	private List<NetworkClientI> clients = new ArrayList<NetworkClientI>();
	private List<Integer> clientResponse = new ArrayList<Integer>();
	
	private DatagramSocket socket;
	private boolean running = false;
	private int port;
	private boolean raw = false;
	
	private Thread run, manage, send, receive;
	
	private final int MAX_ATTEMPTS = 5;
	
	public NetworkServer(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		running = true;
		run = new Thread("Server");
		run.start();
		run();
	}
	
	public void run() {
		running = true;
		System.out.println("Proxy server has been successfully started on: " + port);
		manageClients();
		receive();
	}
	
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while(running) {
					sendToAllNetworkClients("/i/server");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int i = 0; i < clients.size(); i++) {
						NetworkClientI networkClientI = clients.get(i);
						if(!clientResponse.contains(networkClientI.getID())) {
							if(networkClientI.attempt >= MAX_ATTEMPTS) {
								disconnect(networkClientI.getID(), false);
							} else {
								networkClientI.attempt++;
							}
						} else {
							clientResponse.remove(new Integer(networkClientI.getID()));
							networkClientI.attempt = 0;
						}
					}
				}
			}
		};
		manage.start();
	}
	
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while(running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					process(packet);
				}
			}
		};
		receive.start();
	}
	
	private void sendToAllNetworkClients(String message) {
		if(message.startsWith("/m/")) {
			String text = message.substring(3);
			text = text.split("/e/")[0];
			System.out.println(message);   
		}
		for(int i = 0; i < clients.size(); i++) {
			NetworkClientI networkClient = clients.get(i);
			send(message.getBytes(), networkClient.address, networkClient.port);
		}
	}
	
	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		};
		send.start();
	}
	
	private void disconnect(int id, boolean status) {
		NetworkClientI networkClient = null;
		for(int i = 0; i < clients.size(); i++) {
			if(clients.get(i).getID() == id) {
				networkClient = clients.get(i);
			   clients.remove(i);
			   break;
			}
		}
		String message = "";
		if(status) {
			message = "NetworkClient " + networkClient.name +  " @" + networkClient.address.toString() + ":" + networkClient.port + " disconnected.";
		} else {
			message = "NetworkClient " + networkClient.name + " @" + networkClient.address.toString() + ":" + networkClient.port + " timed out.";
		}
		System.out.println(message);
	}
	
	private void send(String message, final InetAddress address, int port) {
		message += "/e/";
		send(message.getBytes(), address, port);
	}
	
	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		if(raw) System.out.println(string);
		if(string.startsWith("/c/")) {
			int id = UniqueIdentifier.getIdentifier();
			System.out.println(string.substring(3, string.length()) + "(" + packet.getAddress() + ")" + " has connected! (ID: " + id + ")");
			clients.add(new NetworkClientI(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), UniqueIdentifier.getIdentifier()));
			String ID = "/c/" + id;
			send(ID, packet.getAddress(), packet.getPort());
		} if(string.startsWith("/m/")) {
			sendToAllNetworkClients(string);
		} else if(string.startsWith("/d/")) {
			String id = string.split("/d/|/e/")[1];
			disconnect(Integer.parseInt(id), true);
		} else if(string.startsWith("/i/")) {
			clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
		} else {
			System.err.println("Could not proccess packet: " + string);
		}
	}
}