package hr.fer.tel.rassus.examples;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

public class Reading1 {
	private Long sensorId;
	private String temperature;
	private String humidity;
	private String pressure;
	private String co;
	private String no2;
	private String so2;
	
	public Reading1() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Reading1(Long sensorId, String temperature, String humidity, String pressure, String co, String no2,
			String so2) {
		super();
		this.sensorId = sensorId;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.co = co;
		this.no2 = no2;
		this.so2 = so2;
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
		return "SensorsController [ temperature=" + temperature + ", humidity=" + humidity + ", pressure="
				+ pressure + ", co=" + co + ", no2=" + no2 + ", so2=" + so2 + "]";
	}

}

