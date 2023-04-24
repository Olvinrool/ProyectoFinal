package hn.uth.myapplication.planilla;

import android.app.Application;

public class PlanillaApp extends Application {

    private static PlanillaApp instance;

    public static PlanillaApp getInstance(){
        if(instance == null){
            synchronized (PlanillaApp.class){
                if(instance == null){
                    instance = new PlanillaApp();
                }
            }
        }
        return instance;
    }
}
