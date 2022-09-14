package hr.fer.tel.rassus.server.beans;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="sensors")
public class Sensor {
  //  TODO
	@Id
	private long id;
	
	@Column
	private double latitude;
	
	@Column 
	private double longitude;
	
	@Column
	private String ip;
	
	@Column
	private int port;

	public Sensor(long id,double latitude, double longitude, String ip, int port) {
		super();
		this.id=id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.ip = ip;
		this.port = port;
	}

	public Sensor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	
	
	
	
	
	
	
}
