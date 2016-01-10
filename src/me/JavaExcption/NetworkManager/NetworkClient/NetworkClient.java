package me.JavaExcption.NetworkManager.NetworkClient;

import me.JavaExcption.NetworkManager.Packet.Packet;
import me.JavaExcption.NetworkManager.Packet.PacketAddress;
import me.JavaExcption.NetworkManager.Packet.PacketType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkClient implements Runnable {

	private DatagramSocket socket;

    private PacketAddress address;

	private String serverName;
	private int ID = -1;

	private Thread send, listen, run;
	private boolean running = false;
	
	public NetworkClient(String serverName, String address, int port) {
		this.serverName = serverName;
		boolean connect = openConnection(address, port);
		if(!connect) {
			System.err.println("Connection failed!");
		}
		sendPacket(new Packet(PacketType.CONNECT,getServerName()));
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	
	
	public PacketAddress getAddress() {
		return address;
	}
	
	public boolean openConnection(String address, int port) {
		try {
			socket = new DatagramSocket();
			this.address = new PacketAddress(InetAddress.getByName(address),port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false; 
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while(running) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);

                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
				}
			}
		};
		listen.start();
	}

	public void process(DatagramPacket data){
        PacketAddress address = new PacketAddress(data.getAddress(),data.getPort());
		try {
			Packet packet = new Packet(data);
            packet.process(this,address);
		} catch (Exception e) {
            System.err.println("Could not receive packet from server");
		}
	}


    public void sendPacket(final Packet packet){
		send = new Thread("Send") {
			public void run() {
				DatagramPacket data = packet.createData(address);
				try {
					socket.send(data);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		};
		send.start();
	}

    public void sendMessage(String s){
        sendPacket(new Packet(PacketType.MESSAGE,getServerName() + ":" + s));
    }

    public void sendMessageAll(String s){
        sendPacket(new Packet(PacketType.MESSAGE,s));
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