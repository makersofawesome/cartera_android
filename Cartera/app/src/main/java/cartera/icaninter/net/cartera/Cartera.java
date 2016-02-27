package cartera.icaninter.net.cartera;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by Juzer on 2/27/2016.
 */
public class Cartera extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("master")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://cartera-server.herokuapp.com/parse/")
                .enableLocalDataStore()
                .build());


    }



}
