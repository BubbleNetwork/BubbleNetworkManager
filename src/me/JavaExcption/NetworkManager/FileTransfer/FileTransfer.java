package me.JavaExcption.NetworkManager.FileTransfer;

public class FileTransfer {

	private String localPath, remotePath;
	private boolean connected = false;
	
	public FileTransfer(String localPath, String remotePath) {
		
	}
	
	public String getLocalPath() {
		return localPath;
	}
	
	public String getRemotePath() {
		return remotePath;
	}
	
	public boolean isConnected() {
		return connected;
	}
}
