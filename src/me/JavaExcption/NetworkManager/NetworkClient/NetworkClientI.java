package me.JavaExcption.NetworkManager.NetworkClient;

import me.JavaExcption.NetworkManager.NetworkServer.NetworkServer;
import me.JavaExcption.NetworkManager.Packet.Packet;
import me.JavaExcption.NetworkManager.Packet.PacketAddress;

public class NetworkClientI {
	
	public String name;
	private PacketAddress address;
	private final int ID;
	public int attempt = 0;
	
	public NetworkClientI(String name, PacketAddress address, int ID) {
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

	public int getAttempt() {
		return attempt;
	}
	
	public void setConnectionAttempt(int attempt) {
		this.attempt = attempt;
	}
	
	public void resetConnectionAttempt() {
		this.attempt = 0;
	}
	
	public void sendPacket(NetworkServer server, Packet packet) {
		server.sendPacket(packet, getAddress());
	}
}
