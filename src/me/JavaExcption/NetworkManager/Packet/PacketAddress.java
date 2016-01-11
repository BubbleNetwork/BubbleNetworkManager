package me.JavaExcption.NetworkManager.Packet;

import java.net.InetAddress;

import me.JavaExcption.NetworkManager.NetworkClient.NetworkClientI;
import me.JavaExcption.NetworkManager.NetworkServer.NetworkServer;

public class PacketAddress {

	private InetAddress address;
	private int port;
	private NetworkClientI client;
	
	public PacketAddress(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	public NetworkClientI getClient(NetworkServer instance) throws Exception{
		if(client == null)client = instance.getClient(this);
		return client;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof PacketAddress) {
			PacketAddress address = (PacketAddress) o;
			if(getAddress().getCanonicalHostName().equals(address.getAddress().getCanonicalHostName()) && getPort() == address.getPort()) return true;
		}
		return false;
	}
}
