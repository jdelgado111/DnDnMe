package com.example.janethdelgado.dndnme.Models;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //register parse models
        ParseObject.registerSubclass(Profile.class);
        ParseObject.registerSubclass(Questions.class);
        ParseObject.registerSubclass(Stats.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("dndnme") // should correspond to APP_ID env variable
                .clientKey("sdpdnd")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://dndnme.herokuapp.com/parse/").build());
    }
}