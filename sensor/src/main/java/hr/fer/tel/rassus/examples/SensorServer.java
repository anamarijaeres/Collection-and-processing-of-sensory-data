package hr.fer.tel.rassus.examples;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;


/**
 * The type Simple unary rpc server.
 */
public class SensorServer implements Runnable {
	
  private static final Logger logger = Logger.getLogger(SensorServer.class.getName());

  private Server server;
  private final UppercaseService service;
  private final int port;
  
  private final double latitude;
  private final double longitude;
  public List<List<String>> readings;
  public double start;
  public int row;
  private String ip;
  private Sensor myNeighbour;
  private Sensor me;
  
  
  private WebService webService;
  /**
   * Instantiates a new Simple unary rpc server.
   *
   * @param service the service
   * @param port    the port
 * @throws IOException 
 * @throws InterruptedException 
   */
  public SensorServer(int port) throws IOException, InterruptedException {
	this.service=new UppercaseService(this);
    this.port = port;
    
    this.ip="127.0.0.1";
    this.start=System.currentTimeMillis();
    this.longitude=pickRand(15.87,16);
    this.latitude=pickRand(45.75,45.85);
   // za generiranje readinga
    readCSV();
    
    Retrofit retrofit = new Retrofit.Builder()
    	    .baseUrl("http://localhost:8090/")
    	    .addConverterFactory(GsonConverterFactory.create())
    	    .build();
    
    this.webService = retrofit.create(WebService.class);
    //registriraj senzora na web 
    registerOnServer();
    //pokreni rad serveraSenzora 
    this.start();
    this.myNeighbour=null;
    for(int i=0;i<5;++i) {
    	if(this.myNeighbour==null) {
    		this.myNeighbour=findNeighbour();
    	}else {
    		break;
    	}
    }
    
    
  }
  private Sensor findNeighbour() throws InterruptedException, IOException {
	  Thread.sleep(500);
	  Call<List<Sensor>> sensors = webService.listSensors();
	  Response<List<Sensor>> ihavethem=sensors.execute();
	  List<Sensor> allSensors=ihavethem.body();
	  if(allSensors.size()>1) {
		  Call<Sensor> neighbourSensor=webService.getNeighbour(this.me.getId());
		  Response<Sensor> responseNeighbour=neighbourSensor.execute();
		  this.myNeighbour=responseNeighbour.body();
		  if(this.myNeighbour!=null) {
		  System.out.println("My neighbour: id" +this.myNeighbour.getId()+ "latitude: "+ this.myNeighbour.getLatitude()+"longitude: "+ this.myNeighbour.getLongitude());
		
		  startNeighbour();
		  }
	  }
	  return this.myNeighbour;
  	}
  
  
private void startNeighbour() throws IOException, InterruptedException {
	  SensorClient client = new SensorClient("127.0.0.1", this.port,this.myNeighbour,this.me);
	
  }
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

  private void registerOnServer() throws IOException {
	  Call<List<Sensor>> sensors = webService.listSensors();
		Response<List<Sensor>> ihavethem=sensors.execute();
		List<Sensor> allSensors=ihavethem.body();
		int len=allSensors.size();
	  this.me=new Sensor(len,this.latitude, this.longitude, this.ip, this.port);
		Call<ResponseEntity<String>> s=webService.registerSensor(me);
		Response<ResponseEntity<String>> response=s.execute();
		System.out.println(response.body());
		System.out.println(response.message());
  }

  private double pickRand(double start, double end) {
	  double random = new Random().nextDouble();
	  double temp= start + (random * (end - start));
	  return  Math.round(temp*100.0)/100.0;
  }

  /**
   * Start the server.
   *
   * @throws IOException the io exception
   */
  public void start() throws IOException {
	List<String> reading=generateReading();
    // Register the service
    server = ServerBuilder.forPort(port)
        .addService(service)
        .build()
        .start();
    logger.info("Server started on " + port);

    //  Clean shutdown of server in case of JVM shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.err.println("Shutting down gRPC server since JVM is shutting down");
      try {
        SensorServer.this.stop();
      } catch (InterruptedException e) {
        e.printStackTrace(System.err);
      }
      System.err.println("Server shut down");
    }));
  }

  private List<String> generateReading() {
	  long seconds= Math.round((System.currentTimeMillis()-start)/1000);
	  row=(int)((seconds%100)+1);
	  return this.readings.get(row);
  }

  /**
   * Stops the server.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  /**
   * Await termination on the main thread
   *
   * @throws InterruptedException the interrupted exception
   */
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws IOException          the io exception
   * @throws InterruptedException the interrupted exception
   */
  public static void main(String[] args) throws IOException, InterruptedException {
//	  Thread.sleep(5000);
	//int[] arr=new int[] {3000,3001,3002,3003,3004,3005};
//	  int port=3000;
//	  SensorServer server;
//	  List<SensorServer> listOfSensorf=new ArrayList<>();
//	  while(true) {
//		port=port+1;
//		Thread.sleep(500);
//		server = new SensorServer(port);
//		listOfSensorf.add(server);
	//}
    //server.start();
    //server.blockUntilShutdown();
  }
	@Override
	public void run() {
		try {
			this.findNeighbour();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}