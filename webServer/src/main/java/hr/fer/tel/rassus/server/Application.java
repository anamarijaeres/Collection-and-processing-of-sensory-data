package hr.fer.tel.rassus.server;

import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.opencsv.bean.CsvToBeanBuilder;

import hr.fer.tel.rassus.server.beans.Reading;
import hr.fer.tel.rassus.server.beans.Sensor;
import hr.fer.tel.rassus.server.controllers.SensorsController;
import hr.fer.tel.rassus.server.services.ReadingRepository;
import hr.fer.tel.rassus.server.services.SensorRepository;


@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private ReadingRepository readingRepo;
	
	
	
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
 
  }

  	@Override
	public void run(String... args) throws Exception {
  		List<Reading> readings = new CsvToBeanBuilder<Reading>(new FileReader("mjerenja.csv"))
  		       .withType(Reading.class).build().parse();
  		
  		long l=0;
  		for(Reading reading: readings)
  			reading.setId(l++);
  		
  		//readingRepo.saveAll(readings);
  			
  		readings.stream().forEach(System.out::println);
		
	}
}
