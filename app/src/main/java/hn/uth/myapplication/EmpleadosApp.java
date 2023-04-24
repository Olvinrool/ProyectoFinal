package hn.uth.myapplication;

import android.app.Application;

public class EmpleadosApp extends Application {

    private static EmpleadosApp instance;

    public static EmpleadosApp getInstance(){
        if(instance == null){
            synchronized (EmpleadosApp.class){
                if(instance == null){
                    instance = new EmpleadosApp();
                }
            }
        }
        return instance;
    }

}