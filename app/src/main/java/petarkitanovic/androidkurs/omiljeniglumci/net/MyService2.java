package petarkitanovic.androidkurs.omiljeniglumci.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService2 {

    public static Retrofit getRetrofitInstance() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyServiceContract.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static MyApiEndpointInterface apiInterface() {
        MyApiEndpointInterface apiService = getRetrofitInstance().create(MyApiEndpointInterface.class);

        return apiService;
    }
}

