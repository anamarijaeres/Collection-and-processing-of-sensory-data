package hr.fer.tel.rassus.server.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import hr.fer.tel.rassus.server.services.ReadingRepository;

@Entity
@Table(name="readings")
public class Reading {
  //  TODO

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long sensorId;
	@Column
	private String temperature;
	@Column
	private String humidity;
	@Column
	private String pressure;
	@Column
	private String co;
	@Column
	private String no2;
	@Column 
	private String so2;
	
	

	public Reading() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Reading(String temperature,String humidity,String pressure,String co,String no2, String so2) {
		super();
		this.temperature=temperature;
		this.humidity=humidity;
		this.pressure=pressure;
		this.co=co;
		this.no2=no2;
		this.so2=so2;
		
	}
	

	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	}

	public String getSo2() {
		return so2;
	}

	public void setSo2(String so2) {
		this.so2 = so2;
	}

	@Override
	public String toString() {
		return "SensorsController [id=" + id + ", temperature=" + temperature + ", humidity=" + humidity + ", pressure="
				+ pressure + ", co=" + co + ", no2=" + no2 + ", so2=" + so2 + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}

