package io;

import java.io.IOException;

import entities.ParkNameAndTimes;
import entities.Subscriber;
import entities.Worker;
import modules.Property;
import modules.ServerRequest;
import modules.ServerRequest.Manager;
import ocsf.client.AbstractClient;

/** a class representing the client controller */
public class clientController extends AbstractClient {

	/** Default connection Port Number */
	public static int DEFAULT_SERVER_PORT = 5555;
	/** Default connection Server Host */
	public static String DEFAULT_SERVER_HOST = "localhost";

	// connection response parameters
	private static boolean awaitResponse = false;
	private static String response = "";

	// instance of the connection for the client
	public static clientController client = null;

	// common data
	public ParkNameAndTimes[] openingTimes;
	public String[] parkNames;
	public Property<Worker> logedInWorker = null;
	public Property<Subscriber> logedInSunscriber = null;
	public Property<String> visitorID = null;

	/**
	 * creates a {@link clientController}
	 * 
	 * @param host the server's host name.
	 * @param port the port number.
	 */
	public clientController(String host, int port) throws IOException {
		super(host, port);

		this.openConnection();
		client = this;

		// save the names and opening hours for all of the parks

		String response = sendRequestAndResponse(new ServerRequest(Manager.Park, "get all parks data", ""));
		openingTimes = ServerRequest.gson.fromJson(response, ParkNameAndTimes[].class);
		parkNames = new String[openingTimes.length];
		for (int i = 0; i < openingTimes.length; i++) {
			parkNames[i] = openingTimes[i].parkID;// ParkID for key
		}
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		response = (String) msg;
		System.out.println("message received : " + response);
		awaitResponse = false;
	}

	/**
	 * Refactor - use sendRequestAndResponse instead, added the return value to the
	 * request
	 * 
	 * @deprecated use {@link #sendRequestAndResponse(ServerRequest)}
	 */// TODO delete
	public void sendRequest(ServerRequest request) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(ServerRequest.toJson(request));
			// wait for response
			while (awaitResponse && this.isConnected()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not send message to server: Terminating client." + e);
		}
	}

	/**
	 * when request sent the client will wait for response
	 * <p>
	 * and the response will be returned
	 * 
	 * @param request the {@link ServerRequest} to be sent to server
	 */
	public String sendRequestAndResponse(ServerRequest request) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(ServerRequest.toJson(request));
			// wait for response
			while (awaitResponse && this.isConnected()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not send message to server: Terminating client." + e);
		}
		String msg = response;
		response = "";
		return msg;
	}

	/**
	 * get respone from server, reset to \"\" in the end
	 * 
	 * @return the response from the server
	 * @deprecated the method {@link #sendRequestAndResponse(ServerRequest)} returns
	 *             the response
	 */// TODO delete

	public static String consumeResponse() {
		String msg = response;
		response = "";
		return msg;
	}
}
