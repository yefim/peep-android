package com.chat.peep;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by geoff on 6/10/15.
 */
public class PeepApi {
    public interface PeepService {
        @POST("/login_or_register")
        public void login_or_register(@Body Person u, Callback<User> cb);
    }

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint("https://peep-api.herokuapp.com/api")
            .build();

    private static final PeepService PEEP_SERVICE = REST_ADAPTER.create(PeepService.class);

    public static PeepService getService() {
        return PEEP_SERVICE;
    }
}
