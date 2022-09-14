package hr.fer.tel.rassus.server.controllers;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.tel.rassus.server.beans.Reading;
import hr.fer.tel.rassus.server.beans.ResponseMessage;
import hr.fer.tel.rassus.server.beans.Sensor;
import hr.fer.tel.rassus.server.services.ReadingService;
import hr.fer.tel.rassus.server.services.SensorRepository;

@RestController
public class ReadingController {
	@Autowired
	private SensorRepository repo;
	@Autowired
	private ReadingService readingService;
	

	public ReadingController(SensorRepository repo) {
		super();
		this.repo = repo;
	}
	
	// TODO 4.3  Spremanje ocitanja pojedinog senzora
	
	@PostMapping("/postReading")
	public ResponseMessage postRead(@RequestBody Reading readingToSave) {
		ResponseMessage res=readingService.saveReading(readingToSave);
		return res;
	}
	
	 // TODO 4.5  Popis ocitanja pojedinog senzora
	
	@GetMapping("/reading/{sensorId}")
	public List<Reading> getReadingsForId(@PathVariable("sensorId") Long sensorId){
		return readingService.readReadingsForId(Long.valueOf(sensorId));
	}
	
	
	@GetMapping("/currentReading")
	public Optional<Sensor> getCurrentReading() {
		LocalTime time=LocalTime.now();
		long id=time.getHour()*4+time.getMinute()/15;
		System.out.println(id);
		return repo.findById(id);
	}
	@GetMapping("/{id}")
	public Optional<Sensor> get(@PathVariable("id") Integer id) {
		return repo.findById(Long.valueOf(id));
	}
	
	
  

 

}