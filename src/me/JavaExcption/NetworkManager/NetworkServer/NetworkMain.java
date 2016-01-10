package me.JavaExcption.NetworkManager.NetworkServer;

public class NetworkMain {

	public int port;
	public NetworkServer server;
		
	public NetworkMain(int port) {
		this.port = port;
		System.out.println("Creating network proxy server with port: " + port);
		server = new NetworkServer(port);
	}
	
	public static void main(String[] args) {
		int port;
		if(args.length != 1) {
			System.out.println("Usage: java -jar Digital15-Server-Manager.jar [port]");
			return;
		}
		port = Integer.parseInt(args[0]);
		new NetworkMain(port);
	}
}