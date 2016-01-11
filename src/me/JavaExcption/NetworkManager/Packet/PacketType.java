package me.JavaExcption.NetworkManager.Packet;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import me.JavaExcption.NetworkManager.NetworkClient.NetworkClient;
import me.JavaExcption.NetworkManager.NetworkClient.NetworkClientI;
import me.JavaExcption.NetworkManager.NetworkServer.NetworkServer;
import me.JavaExcption.NetworkManager.Util.UniqueIdentifier;

public class PacketType {
	
	private static final Map<Character,PacketType> types = new HashMap<Character, PacketType>();
	private final char value;
	
    public static final PacketType 
    
    CONNECT = new PacketType('c') {
    	public void process(NetworkServer server,PacketAddress address,String message) {
            Integer id = UniqueIdentifier.getIdentifier();
            System.out.println("(" + address.getAddress() + ")" + " has connected! (ID: " + id.toString() + ")");
            NetworkClientI client = new NetworkClientI(message,address,id);
            server.addClient(client);
            client.sendPacket(server,new Packet(PacketType.CONNECT,id.toString()));
        }

        public void process(NetworkClient client, PacketAddress address, String message){
            int id;
            try {
                id = NumberFormat.getInstance().parse(message).intValue();
            } catch (ParseException e) {
                //Automatic Catch Statement
                e.printStackTrace();
                System.err.println("Could not parse ID!");
                return;
            }
            client.setID(id);
        }
    },
    
    DISCONNECT = new PacketType('d') {
        public void process(NetworkServer server, PacketAddress address,String message) {
            boolean status = Boolean.valueOf(message);
            try {
                server.disconnect(address.getClient(server), status);
            }
            catch(Exception ex){
                ex.printStackTrace();
                System.err.println("Could not disconnect client with address " + address.getAddress().toString() + ":" + String.valueOf(address.getPort()));
            }
        }

        public void process(NetworkClient client, PacketAddress address, String message) {
            client.exit();
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
    };

    public static PacketType getType(char c) throws Exception{
        if(!isValid(c))throw new Exception("Char is not valid");
        return types.get(c);
    }

    public static boolean isValid(char b){
        return types.containsKey(b);
    }

    private PacketType(char value) {
        if(isValid(value))throw new RuntimeException("Packet with " + value + " already exists");
        types.put(value,this);
        this.value = value;
    }

    protected Character getChar(){
        return value;
    }

    public void process(NetworkServer server,PacketAddress address,String message) {}
    public void process(NetworkClient client, PacketAddress address, String message) {}
}