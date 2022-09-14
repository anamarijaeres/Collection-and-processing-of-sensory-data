package hr.fer.tel.rassus.server.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.tel.rassus.server.beans.Reading;
import hr.fer.tel.rassus.server.beans.Sensor;
import hr.fer.tel.rassus.server.controllers.SensorsController;

public interface SensorRepository extends JpaRepository<Sensor,Long> {
	Sensor findByip(String ip);
	Sensor findBylatitude(double latitude);
	Sensor findByid(long id);
}
