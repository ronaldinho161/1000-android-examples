package eu.aplusteam.retrofit.data.remote;


import eu.aplusteam.retrofit.data.model.SOAnswersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by IntelliJ IDEA.<br/>
 * Proxy to our service <br/>
 * User: anton<br/>
 * Date: 28.01.17<br/>
 * Time: 22:09<br/>
 */
public interface SOService {

    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    Call<SOAnswersResponse> getAnswers();

    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);

}
