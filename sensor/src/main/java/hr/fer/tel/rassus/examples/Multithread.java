package hr.fer.tel.rassus.examples;

import java.io.IOException;

public class Multithread {
	 public static void main(String[] args) throws IOException, InterruptedException
	    {	int port=3000;
	        int n = 10; // Number of threads
	        for (int i = 0; i < n; i++) {
	        	port=port+1;
	            Thread object= new Thread(new SensorServer(port));
	            object.run();
	        }
	    }
}
