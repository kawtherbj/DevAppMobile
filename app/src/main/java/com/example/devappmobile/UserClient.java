package com.example.devappmobile;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserClient {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("isi/oauth2/token")
    Call<User>  login(@Body Login login) ;

    @GET("api/1.0/isi/case/start-cases")
    Call<List<ProcessList>> getSecret(@Header("Authorization") String authToken);

    @GET("api/1.0/isi/cases/draft")
    Call<List<Draft>> getDraft(@Header("Authorization") String authToken);

    @GET("api/1.0/isi/project/{pro_uid}/activity/{tas_uid}/steps")
    Call<List<Step>> getStep(@Header("Authorization") String authToken, @Path("pro_uid") String pro_uid , @Path("tas_uid") String tas_uid);

    @GET("api/1.0/isi/project/{pro_uid}/dynaform/{step_uid}")
    Call<Dynaform> getDynaform(@Header("Authorization") String authToken, @Path("pro_uid") String pro_uid , @Path("step_uid") String step_uid);

    @POST("api/1.0/isi/cases")
    Call<NewProcessRep> postCase(@Header("Authorization") String authToken, @Body NewProcess body);

   /* @POST("api/1.0/isi/cases")
    Call<JSONObject> postCases(@Header("Authorization") String authToken, @Body JSONObject body);
*/
}
