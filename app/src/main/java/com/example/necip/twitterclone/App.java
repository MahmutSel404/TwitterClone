package com.example.necip.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {


        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("T2us9kmQabKqVPFZzSweEqbSLDxWvi5Lrt2PrYSs")
                // if defined
                .clientKey("8T45PfesicCZg4SljWaJNaxZMDh1bUmESTQMyf3u")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
