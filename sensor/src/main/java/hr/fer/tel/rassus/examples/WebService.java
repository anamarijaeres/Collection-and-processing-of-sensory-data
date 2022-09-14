package hr.fer.tel.rassus.examples;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import org.springframework.http.ResponseEntity;
public interface WebService {
	 @GET("sensors")
	 Call<List<Sensor>> listSensors();
	 
	 @POST("registerSensor")
	 Call<ResponseEntity<String>> registerSensor(@Body Sensor sensor);
	 
	 @GET("neighbour/{id}")
	 Call<Sensor> getNeighbour(@Path("id") long id);
	 
	 @POST("postReading")
	 Call<ResponseEntity<String>> postRead(@Body Reading1 reading1);
}
