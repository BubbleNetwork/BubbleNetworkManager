package me.JavaExcption.NetworkManager.Packet;

<<<<<<< HEAD
import java.net.DatagramPacket;

import me.JavaExcption.NetworkManager.NetworkClient.NetworkClient;
import me.JavaExcption.NetworkManager.NetworkServer.NetworkServer;

public class Packet {
	
	private PacketType type;
=======
import me.JavaExcption.NetworkManager.NetworkClient.NetworkClient;
import me.JavaExcption.NetworkManager.NetworkServer.NetworkServer;

import java.net.DatagramPacket;

/**
 * The Bubble Network 2016
 * BubbleNetworkManager-master
 * 10/01/2016 {12:08}
 * Created January 2016
 */
public class Packet {
    private PacketType type;
>>>>>>> origin/master
    private String data;

    public Packet(DatagramPacket packet) throws Exception{
        String s = new String(packet.getData());
        type = PacketType.getType(s.charAt(1));
        data = s.substring(3);
    }

    public Packet(PacketType type, String data){
        this.type = type;
        this.data = data;
    }

    public String getData(){
        return data;
    }

    public PacketType getType(){
        return type;
    }

    public DatagramPacket createData(PacketAddress address){
        String buffer = "/" + String.valueOf(type.getChar()) + "/" + data;
        return new DatagramPacket(buffer.getBytes(),buffer.length(),address.getAddress(),address.getPort());
    }

    public void process(NetworkServer server,PacketAddress address){
        getType().process(server,address,getData());
    }

    public void process(NetworkClient client,PacketAddress address){
        getType().process(client,address,getData());
    }
}
