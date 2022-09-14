package hr.fer.tel.rassus.server.controllers;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.tel.rassus.server.beans.ResponseMessage;
import hr.fer.tel.rassus.server.beans.Sensor;
import hr.fer.tel.rassus.server.services.SensorRepository;
import hr.fer.tel.rassus.server.services.SensorService;

@RestController
public class SensorsController {
	
	@Autowired
	private SensorService sensorService;
	
	//  TODO 4.1  Registracija
	
	@PostMapping("/registerSensor")
	public ResponseMessage registerSensor(@RequestBody Sensor sensorRegister) {
		ResponseMessage res=sensorService.registerSensor(sensorRegister);
		return res;
	}
	
	//  TODO 4.2  Najblizi susjed
	
	@GetMapping("neighbour/{id}")
	public Sensor getNeighbour(@PathVariable("id") long id) {
		return sensorService.getNearestNeighbour(id);
	
	}
		
	//  TODO 4.4  Popis senzora
	
	@GetMapping("/sensors")
	public List<Sensor> getSensors(){
		return sensorService.getSensorsList();
	}
	
	
	
	
	
    

    

    

}
