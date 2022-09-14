package hr.fer.tel.rassus.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import hr.fer.tel.rassus.server.beans.ResponseMessage;
import hr.fer.tel.rassus.server.beans.Sensor;
import io.micrometer.core.ipc.http.HttpSender.Response;

@Service
public class SensorService {
	@Autowired
	private SensorRepository sensorRepository;

	public ResponseMessage registerSensor(Sensor registerSensor) {
		try { 
		sensorRepository.save(registerSensor);
		return new ResponseMessage("201 Created" ,"HttpStatus.CREATED");
		}catch(Exception exception) {
			return new ResponseMessage("FAIL", "HttpStatus.FORBIDDEN");
		}
	}

	public List<Sensor> getSensorsList() {
		return sensorRepository.findAll();
	}

	public Sensor getNearestNeighbour(long id) {
		List<Sensor> sensors=getSensorsList();
		
		Sensor mySensor=sensorRepository.findByid(id);
		sensors.remove((int)id);
		Map<Sensor,Double> distances=new LinkedHashMap<>();
		int R=6371;
		double dlon,dlat,a,c,d;
		for(Sensor s:sensors) {
			dlon=s.getLongitude()-mySensor.getLongitude();
			dlat=s.getLatitude()-mySensor.getLatitude();
			a=Math.pow(Math.sin(dlat/2), 2)+Math.cos(mySensor.getLatitude())*Math.cos(s.getLatitude())*Math.pow(Math.sin(dlon/2),2);
			c=2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
			d=R*c;
			distances.put(s,Double.valueOf(d));
		}
		
		Entry<Sensor, Double> min = null;
		for (Entry<Sensor, Double> entry : distances.entrySet()) {
		    if (min == null || min.getValue() > entry.getValue()) {
		        min = entry;
		    }
		}
		if(min==null)return null;
		return min.getKey();
		
	}
}
