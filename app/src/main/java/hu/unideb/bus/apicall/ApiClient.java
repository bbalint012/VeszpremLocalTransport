package hu.unideb.bus.apicall;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.unideb.bus.R;
import hu.unideb.bus.app.App;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

class ApiClient {
    private static Retrofit obaRetrofit;
    private static Retrofit otaRetrofit;

    private static final String BASE_OBA_URL = App.getAppContext().getResources().getString(R.string.obaBaseUrl);
    private static final String BASE_OTP_URL = App.getAppContext().getResources().getString(R.string.otpBaseUrl);
    private static final String API_KEY = App.getAppContext().getResources().getString(R.string.apiKey);
    private static final String AGENCY_ID = App.getAppContext().getResources().getString(R.string.agencyId); //TODO: need to change ENYKK

    static Retrofit getObaClient() {
        if (obaRetrofit != null) {
            return obaRetrofit;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .build();

        obaRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_OBA_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Gson
                .client(client)
                .build();

        return obaRetrofit;
    }

    static Retrofit getOtpClient() {
        if (otaRetrofit != null) {
            return otaRetrofit;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .build();

        ObjectMapper jacksonMapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        otaRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_OTP_URL)
                .addConverterFactory(JacksonConverterFactory.create(jacksonMapper)) //Jackson
                .client(client)
                .build();

        return otaRetrofit;
    }


    static String getApiKey() {
        return API_KEY;
    }

    static String getAgencyId() {
        return AGENCY_ID;
    }
}
