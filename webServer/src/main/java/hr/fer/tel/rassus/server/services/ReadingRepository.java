package hr.fer.tel.rassus.server.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.fer.tel.rassus.server.beans.Reading;
import hr.fer.tel.rassus.server.beans.Sensor;

public interface ReadingRepository extends JpaRepository<Reading,Long>  {
	List<Reading> findAllBysensorId(Long sensorId);
}
