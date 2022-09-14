package hr.fer.tel.rassus.examples;

import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The type Uppercase service.
 */
public class UppercaseService extends UppercaseGrpc.UppercaseImplBase {
  private static final Logger logger = Logger.getLogger(UppercaseService.class.getName());
  private SensorServer s;
  
  public UppercaseService(SensorServer server) {
	this.s=server;
  }

  @Override
  public void requestUppercase(Reading request, StreamObserver<Reading> responseObserver) {
    logger.info("Got a new message: " + request.getTemperature());
    
    long seconds= Math.round((System.currentTimeMillis()-s.start)/1000);
	int row=(int)((seconds%100)+1);
	if(row>100) {
		 Random r=new Random();
		 row=(int)(1 + (int)(Math.random() * ((100 - 1) + 1)));
		  
	 }
    
    List<String> myReading=s.readings.get(row);
    // Create response
    Reading response = Reading.newBuilder().setTemperature(myReading.get(0))
    										.setPressure(myReading.get(1))
    										.setHumidity(myReading.get(2))
    										.setCo(myReading.get(3))
    										.setNo2(myReading.get(4))
    										.setSo2(myReading.get(5))
    										.build();
    // Send response
    responseObserver.onNext(response);

    logger.info("Responding with: temperature: " + response.getTemperature()+ " pressure: "+ response.getPressure()+
    		" humidity: "+response.getHumidity()+" co: "+response.getCo()+" no2: "+response.getNo2()+" so2: "+response.getSo2());
    // Send a notification of successful stream completion.
    responseObserver.onCompleted();
  }
  
  
}
