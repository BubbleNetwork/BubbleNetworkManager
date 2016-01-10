package me.JavaExcption.NetworkManager.NetworkServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import me.JavaExcption.NetworkManager.NetworkClient.NetworkClientI;
<<<<<<< HEAD
=======
import me.JavaExcption.NetworkManager.NetworkClient.UniqueIdentifier;
>>>>>>> origin/master
import me.JavaExcption.NetworkManager.Packet.Packet;
import me.JavaExcption.NetworkManager.Packet.PacketAddress;
import me.JavaExcption.NetworkManager.Packet.PacketType;

public class NetworkServer {
	
	private List<NetworkClientI> clients = new ArrayList<NetworkClientI>();
	private List<Integer> clientResponse = new ArrayList<Integer>();
	
	private DatagramSocket socket;
	private boolean running = false;
	private int port;
	
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
					sendToAllNetworkClients(new Packet(PacketType.INFORMATION,"server"));
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int i = 0;i < clients.size(); i++) {
                        NetworkClientI networkClientI = clients.get(i);
						if(!clientResponse.contains(networkClientI.getID())) {
							if(networkClientI.getAttempt() >= MAX_ATTEMPTS) {
								disconnect(networkClientI, false);
							} else {
								networkClientI.setAttempt(networkClientI.getAttempt()+1);;
							}
						} else {
							clientResponse.remove(new Integer(networkClientI.getID()));
							networkClientI.setAttempt(0);
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

    public void sendToAllNetworkClients(String message){
        sendToAllNetworkClients(new Packet(PacketType.MESSAGE,message));
    }

    public void sendToAllNetworkClients(Packet packet) {
		for(NetworkClientI networkClient:clients) {
			sendPacket(packet, networkClient.getAddress());
		}
	}

    public void disconnect(NetworkClientI networkClient,boolean status){
        clients.remove(networkClient);
        System.out.println("NetworkClient " + networkClient.getName() +  " @" + networkClient.getAddress().getAddress().toString() + ":" + networkClient.getAddress().getPort() + " " + (status ? "disconnected" : "timed out") + ".");
    }


	private void sendMessage(String message, PacketAddress address) {
		sendPacket(new Packet(PacketType.MESSAGE,message), address);
	}

    public void sendPacket(final Packet packet, final PacketAddress address){
        send = new Thread("Send") {
            public void run() {
                try {
                    socket.send(packet.createData(address));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        };
<<<<<<< HEAD
        send.start();
=======
>>>>>>> origin/master
    }

    public void addClient(NetworkClientI i){
        clients.add(i);
    }

    public void addClientResponse(int i){
        clientResponse.add(i);
    }
<<<<<<< HEAD
    
=======

>>>>>>> origin/master
    public NetworkClientI getClient(PacketAddress address) throws Exception{
        for(NetworkClientI i:clients){
            if(i.getAddress().equals(address))return i;
        }
        throw new Exception("Client not found");
    }

    public NetworkClientI getClient(int id) throws Exception{
        for(NetworkClientI i:clients){
            if(i.getID() == id)return i;
        }
        throw new Exception("Client not found");
    }

<<<<<<< HEAD
    public void getListOfClients() {
    	for(NetworkClientI i : clients) {
    		System.out.println(i);
    	}
    }
    
=======
>>>>>>> origin/master
    private void process(DatagramPacket data){
        Packet packet;
        PacketAddress address = new PacketAddress(data.getAddress(),data.getPort());
        try {
            packet = new Packet(data);
        } catch (Exception e) {
            System.err.println("Could not process data " + new String(data.getData()));
            return;
        }
        packet.process(this,address);
    }
}