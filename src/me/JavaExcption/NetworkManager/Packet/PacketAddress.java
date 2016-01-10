package me.JavaExcption.NetworkManager.Packet;

import java.net.InetAddress;

public class PacketAddress {

	private InetAddress address;
	private int port;
	
	public PacketAddress(InetAddress address, int port) {
		this.address = address;
		this.port = port;
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
			if(getAddress().equals(address.getAddress()) && getPort() == address.getPort()) return true;
		}
		return false;
	}
}
