package hr.fer.tel.rassus.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hr.fer.tel.rassus.server.beans.Reading;
import hr.fer.tel.rassus.server.beans.ResponseMessage;
import hr.fer.tel.rassus.server.beans.Sensor;

@Service
public class ReadingService {
	@Autowired
	private ReadingRepository readingRepository;
	@Autowired
	private SensorRepository sensorRepository;
	
	public ResponseMessage saveReading(Reading readingToSave) {
		try { 
		long sensrId=readingToSave.getSensorId();
		boolean okay=sensorRepository.existsById(sensrId);
		if(okay==true) {
		readingRepository.save(readingToSave);
		return new ResponseMessage("201 Created", "HttpStatus.CREATED");
		}else {
			System.out.println("I'm here");
		return new ResponseMessage("204 No content", "HttpStatus.FORBIDDEN");
		}
		}catch(Exception exception) {
			return new ResponseMessage("FAIL", "HttpStatus.FORBIDDEN");
		}
	}

	public List<Reading> readReadingsForId(Long valueOf) {
		return readingRepository.findAllBysensorId(valueOf);
	}
}
