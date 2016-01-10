package me.JavaExcption.NetworkManager.NetworkClient;

import me.JavaExcption.NetworkManager.NetworkServer.NetworkServer;
import me.JavaExcption.NetworkManager.Packet.Packet;
import me.JavaExcption.NetworkManager.Packet.PacketAddress;

public class NetworkClientI {

	private String name;
	private PacketAddress address;
	private int port;
	private final int ID;
	private int attempt = 0;

	public NetworkClientI(String name, PacketAddress address, final int ID) {
		this.name = name;
		this.address = address;
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public PacketAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public int getAttempt() {
		return attempt;
	}

	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

    public void sendPacket(NetworkServer server, Packet packet){
        server.sendPacket(packet,getAddress());
    }
}
