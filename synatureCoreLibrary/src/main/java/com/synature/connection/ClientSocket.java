package com.synature.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket implements ISocketConnection {
	public static final String TAG = ClientSocket.class.getSimpleName();
	
	private Socket mConnect;
	
	private PrintWriter mWriter;
	private BufferedReader mReader;

	public ClientSocket(String ip, int port) throws IOException {
		InetAddress iNetAddr = InetAddress.getByName(ip);
		mConnect = new Socket(iNetAddr, port);
		mWriter = new PrintWriter(new OutputStreamWriter(
				mConnect.getOutputStream()));
		mReader = new BufferedReader(new InputStreamReader(
				mConnect.getInputStream()));
	}

	@Override
	public void send(String msg) throws Exception{
		mWriter.write(msg);
		mWriter.flush();
	}

	@Override
	public String receive() {
		try {
			return mReader.readLine();
		} catch (IOException ex) {
			return null;
		}
	}

	@Override
	public void close() {
	}
}
