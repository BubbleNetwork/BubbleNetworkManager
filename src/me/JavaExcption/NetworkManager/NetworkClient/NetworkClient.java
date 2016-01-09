package me.JavaExcption.NetworkManager.NetworkClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkClient implements Runnable {

	private DatagramSocket socket;
	private InetAddress ip;
	
	public int port;
	private String serverName, address;
	private int ID = -1;

	private Thread send, listen, run;
	private boolean running = false;
	
	public NetworkClient(String serverName, String address, int port) {
		this.serverName = serverName;
		this.address = address;
		this.port = port;
		boolean connect = openConnection(address, port);
		if(!connect) {
			System.err.println("Connection failed!");
		}
		String connection = "/c/" + serverName;
		send(connection.getBytes());
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	
	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean openConnection(String address, int port) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false; 
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void send(String message) {
		if(message.equals("")) return;
		message = getServerName() + ": " + message;
		message = "/m/" + message;
		send(message.getBytes());
	}
	
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while(running) {
					String message = receive();
					if(message.startsWith("/c/")) {
						setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						System.out.println("Successfully connected to server! ID: " + getID());
					} else if(message.startsWith("/m/")) {
						String text = message.split("/m/|/e/")[1];
						sendToAll(text);
					} 
				}
			}
		};
		listen.start();
	}
	
	public String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		return message;
	}
	
	public void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
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
	
	public void sendToAll(String message) {
		if(message.equals("")) return;
		message = "/m/" + message;
		send(message.getBytes());
	}
	
	public void run() {
		listen();
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	
	public int getID() {
		return ID;
	}
}