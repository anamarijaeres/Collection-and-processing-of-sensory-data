package hr.fer.tel.rassus.examples;


public class Sensor {
	
	private long id;
	private double latitude;
	private double longitude;
	private String ip;
	private int port;

	public Sensor(long id,double latitude, double longitude, String ip, int port) {
		super();
		this.id=id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.ip = ip;
		this.port = port;
	}
	
	public Sensor(double latitude, double longitude, String ip, int port) {
		super();
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
