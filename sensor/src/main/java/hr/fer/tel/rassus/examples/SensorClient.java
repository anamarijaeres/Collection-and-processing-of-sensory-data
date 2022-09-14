package hr.fer.tel.rassus.examples;

import io.grpc.ManagedChannel;

import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Converter.Factory;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;

/**
 * The type Sensor client.
 */
public class SensorClient {


  private static final Logger logger = Logger.getLogger(SensorClient.class.getName());

  private final ManagedChannel channel;
  private final UppercaseGrpc.UppercaseBlockingStub uppercaseBlockingStub;
  
//  private final double latitude;
//  private final double longitude;
  private List<List<String>> readings;
  private List<String> reading;
  private double start;
  private int row;
  private String ip;
  private int port;
  private Sensor myNeighbour;
  private Sensor me;
  
  private WebService webService;

  /**
   * Instantiates a new Sensor client.
   *
   * @param host the host
   * @param port the port
   * @throws IOException 
 * @throws InterruptedException 
   */
  public SensorClient(String host, int port,Sensor sensor,Sensor neighbour) throws IOException, InterruptedException {
    this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    uppercaseBlockingStub = UppercaseGrpc.newBlockingStub(channel);
    
    this.ip=sensor.getIp();
    this.port=sensor.getPort();
    this.start=System.currentTimeMillis();
    //this.longitude=pickRand(15.87,16);
    //this.latitude=pickRand(45.75,45.85);
   
    readCSV();
    
    Retrofit retrofit = new Retrofit.Builder()
    	    .baseUrl("http://localhost:8090/")
    	    .addConverterFactory(GsonConverterFactory.create())
    	    .build();
    
    this.webService = retrofit.create(WebService.class);
    //registerOnServer(); vec je registriran
    //findNeighbour();		imam susjeda i sebe
    
    //start the loop with readings generation
    this.me=sensor;
    this.myNeighbour=neighbour;
    while(true) {
    	if(System.currentTimeMillis()-start>20000)break;
    	Thread.sleep(5000);
    	this.requestUppercase();
    }
  }
  
//  private void findNeighbour() throws InterruptedException, IOException {
//	  Thread.sleep(8000);
//	  Call<Sensor> neighbourSensor=webService.getNeighbour(this.me.getId());
//	  Response<Sensor> responseNeighbour=neighbourSensor.execute();
//	  this.myNeighbour=responseNeighbour.body();
//	  System.out.println("My neighbour:" +this.myNeighbour);
//  }

  private void readCSV() throws IOException {
	  this.readings = new ArrayList<>();
	  try (BufferedReader br = new BufferedReader(new FileReader("mjerenja.csv"))) {
	      String line;
	      while ((line = br.readLine()) != null) {
	    	  if(!line.contains("Temperature")) {
	          String[] values = line.split(",");
	          this.readings.add(Arrays.asList(values));
	    	  }
	      }
	  }
  }

//  private void registerOnServer() throws IOException {
//	  Call<List<Sensor>> sensors = webService.listSensors();
//		Response<List<Sensor>> ihavethem=sensors.execute();
//		List<Sensor> allSensors=ihavethem.body();
//		int len=allSensors.size();
//	  this.me=new Sensor(len,this.latitude, this.longitude, this.ip, this.port);
//		Call<ResponseEntity<String>> s=webService.registerSensor(me);
//		Response<ResponseEntity<String>> response=s.execute();
//		System.out.println(response.body());
//		System.out.println(response.message());
//  }

//  private double pickRand(double start, double end) {
//		  double random = new Random().nextDouble();
//		  double temp= start + (random * (end - start));
//		  return  Math.round(temp*100.0)/100.0;
//  }

  /**
   * Stop the client.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void stop() throws InterruptedException {
//    Initiates an orderly shutdown in which preexisting calls continue but new calls are
//    immediately cancelled. Waits for the channel to become terminated, giving up if the timeout
//    is reached.
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /**
   * Request uppercase.
 * @throws IOException 
   */
  public void requestUppercase() throws IOException {

	this.reading=generateReading();
	
    Reading request = Reading.newBuilder().build();

    logger.info("Sending request!");
    try {
      Reading response = uppercaseBlockingStub.requestUppercase(request);
      logger.info("Received reading with: temperature: " + response.getTemperature()+ " pressure: "+ response.getPressure()+
      		" humidity: "+response.getHumidity()+" co: "+response.getCo()+" no2: "+response.getNo2()+" so2: "+response.getSo2());
      calibrateWithNeighbour(response);
    } catch (StatusRuntimeException e) {
      logger.info("RPC failed: " + e.getMessage());
      calibrateWithoutNeighbour();
    }
  }
  
  private void calibrateWithoutNeighbour() throws IOException {
	Call<List<Sensor>> sensors = webService.listSensors();
	
	
	List<Double> sendCalibratedReadings=new ArrayList<>();
	  for(String parameter:this.reading) {
		if(parameter!="null") {
			sendCalibratedReadings.add(Double.parseDouble(parameter));
		}else {
			sendCalibratedReadings.add(null);
		}
	}
	  
  List<String> val=new ArrayList<>();
	for(Double d:sendCalibratedReadings) {
		if(d==null) {
			val.add("null");
		}else{
			val.add(d.toString());
		}
	}
	
	 Reading1 r=new Reading1(Long.valueOf(me.getId()),val.get(0),val.get(1),val.get(2),val.get(3),val.get(4),val.get(5));
	 
	Call<ResponseEntity<String>> s=webService.postRead(r);
	Response<ResponseEntity<String>> res=s.execute();
	System.out.println(res.body());
	System.out.println(res.message());
	
  }

  private void calibrateWithNeighbour(Reading response) throws IOException {
	Call<List<Sensor>> sensors = webService.listSensors();
	Response<List<Sensor>> ihavethem=sensors.execute();
	List<Sensor> allSensors=ihavethem.body();
	
//	for(Sensor s:allSensors) {
//		System.out.println(s.getId());
//		System.out.println(s.getIp());
//		System.out.println(s.getPort());
//	}
	List<Double> myReading=new ArrayList<>();
	List<Double> neighbourReading=new ArrayList<>();
	List<Double> sendCalibratedReadings=new ArrayList<>();
	for(String parameter:this.reading) {
		if(!parameter.equals("null")) {
			myReading.add(Double.parseDouble(parameter));
		}else {
			myReading.add(null);
		}
	}
	if(!response.getTemperature().equals("null")) { neighbourReading.add(Double.parseDouble(response.getTemperature()));} 
	else {neighbourReading.add(null);}
	if(!response.getPressure().equals("null")) { neighbourReading.add(Double.parseDouble(response.getPressure()));}
	else {neighbourReading.add(null);}
	if(!response.getHumidity().equals("null")) { neighbourReading.add(Double.parseDouble(response.getHumidity()));}
	else {neighbourReading.add(null);}
	if(!response.getCo().equals("null")) { neighbourReading.add(Double.parseDouble(response.getCo()));}
	else {neighbourReading.add(null);}
	if(!response.getNo2().equals("null")) { neighbourReading.add(Double.parseDouble(response.getNo2()));}
	else {neighbourReading.add(null);}
	if(!response.getSo2().equals("null")) { neighbourReading.add(Double.parseDouble(response.getSo2()));}
	else {neighbourReading.add(null);}
	
	for(int i=0;i<6;++i) {
		if(myReading.get(i)==null || myReading.get(i)==0 ) {
			if(neighbourReading.get(i)==null || neighbourReading.get(i)==0) {
				sendCalibratedReadings.add(null);
			}
			else {
				sendCalibratedReadings.add(neighbourReading.get(i));
			}
		}else {
			if(neighbourReading.get(i)==null || neighbourReading.get(i)==0) {
				sendCalibratedReadings.add(myReading.get(i));
			}
			else {
				sendCalibratedReadings.add((neighbourReading.get(i)+myReading.get(i))/2);
			}
		}
	}
	List<String> val=new ArrayList<>();
	for(Double d:sendCalibratedReadings) {
		if(d==null) {
			val.add("null");
		}else{
			val.add(d.toString());
		}
	}
	
	 Reading1 r=new Reading1(Long.valueOf(me.getId()),val.get(0),val.get(1),val.get(2),val.get(3),val.get(4),val.get(5));
	 
	Call<ResponseEntity<String>> s=webService.postRead(r);
	Response<ResponseEntity<String>> res=s.execute();
	System.out.println(res.body());
	System.out.println(res.message());
	
	
  }

  private List<String> generateReading() {
	  long seconds= Math.round((System.currentTimeMillis()-start)/1000);
	  row=(int)((seconds%100)+1);
	  if(row>100) {
		  Random r=new Random();
		  row=(int)(1 + (int)(Math.random() * ((100 - 1) + 1)));
		  
	  }
	  return this.readings.get(row);
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws InterruptedException the interrupted exception
 * @throws IOException 
   */
  public static void main(String[] args) throws InterruptedException, IOException {
	int[] arr=new int[] {3000,3001,3002};
	Random r=new Random();
	int i=(int)(0 + (int)(Math.random() * ((2 - 0) + 1)));
	
    //SensorClient client = new SensorClient("127.0.0.1", arr[i]);

    //client.requestUppercase();
    //client.stop();
  }
}
