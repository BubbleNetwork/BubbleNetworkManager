package me.JavaExcption.NetworkManager;

import me.JavaExcption.NetworkManager.NetworkClient.NetworkClient;

public class NetworkManager {
	
	public static NetworkClient networkClient;
	
	public NetworkManager() {
		System.out.println("Network client has been started! (" + networkClient.getServerName() + ")");
	}
	
	public static void main(String[] args) {
		networkClient = new NetworkClient("test", "localhost", 8192);
		new NetworkManager();
	}
}
