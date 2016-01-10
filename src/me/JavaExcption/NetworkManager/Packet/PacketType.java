package me.JavaExcption.NetworkManager.Packet;

import java.util.HashMap;
import java.util.Map;

import me.JavaExcption.NetworkManager.NetworkClient.NetworkClient;
import me.JavaExcption.NetworkManager.NetworkClient.NetworkClientI;
import me.JavaExcption.NetworkManager.NetworkClient.UniqueIdentifier;
import me.JavaExcption.NetworkManager.NetworkServer.NetworkServer;

public class PacketType {
	
	private static final Map<Character,PacketType> types = new HashMap<Character, PacketType>();

    public static final PacketType
    CONNECT = new PacketType('c'){

        public void process(NetworkServer server,PacketAddress address,String message) {
            int id = UniqueIdentifier.getIdentifier();
            System.out.println("(" + address.getAddress() + ")" + " has connected! (ID: " + id + ")");
            NetworkClientI client = new NetworkClientI(message,address,id);
            server.addClient(client);
            client.sendPacket(server,new Packet(PacketType.CONNECT,String.valueOf(id)));
        }

        public void process(NetworkClient client, PacketAddress address, String message){
            int id = Integer.parseInt(message);
            client.setID(id);
        }
    },
    DISCONNECT = new PacketType('d'){
        public void process(NetworkServer server,PacketAddress address,String message) {
            try {
                server.disconnect(server.getClient(address), true);
            }
            catch(Exception ex){
                System.err.println("Could not disconnect client with address " + address.getAddress().toString() + ":" + address.getPort());
            }
        }
    },
    MESSAGE = new PacketType('m'){
        public void process(NetworkServer server,PacketAddress address,String message) {
            server.sendToAllNetworkClients(message);
        }
    },
    INFORMATION = new PacketType('i'){
        public void process(NetworkServer server,PacketAddress address,String message) {
            server.addClientResponse(Integer.parseInt(message));
        }
    }
    ;

    public static PacketType getType(char c) throws Exception{
        if(!isValid(c))throw new Exception("Char is not valid");
        return types.get(c);
    }

    public static boolean isValid(char b){
        return types.containsKey(b);
    }

    private final char value;

    private PacketType(char value) {
        if(isValid(value))throw new RuntimeException("Packet with " + value + " already exists");
        types.put(value,this);
        this.value = value;
    }

    protected char getChar(){
        return value;
    }

    public void process(NetworkServer server,PacketAddress address,String message){}
    public void process(NetworkClient client, PacketAddress address, String message){}
}