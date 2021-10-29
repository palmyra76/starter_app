package com.zitlab.palmyra.jetty;

import com.biocliq.fluwiz.server.api.PalmyraServer;

public class ApiServer {

	public static void main(String[] args) throws Exception {
		PalmyraServer.start(5050);
	}

}
