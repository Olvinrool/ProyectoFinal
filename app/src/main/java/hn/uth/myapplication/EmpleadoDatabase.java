package hn.uth.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Empleado.class}, version = 1, exportSchema = false)
public abstract class EmpleadoDatabase extends RoomDatabase {


    public abstract EmpleadoDao empleadoDao();

    private static volatile EmpleadoDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //GENERADOR DE INSTANCIA USANDO PATRÓN SINGLETON
    static EmpleadoDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (EmpleadoDatabase.class){
                if(INSTANCE == null){
                    RoomDatabase.Callback miCallback = new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            databaseWriteExecutor.execute(() -> {
                                EmpleadoDao dao = INSTANCE.empleadoDao();
                                dao.deleteAll();

                                Empleado nuevo = new Empleado("Olvin", "Rodriguez", 18000);
                                dao.insert(nuevo);
                            });

                        }
                    };
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EmpleadoDatabase.class, "Empleado_database").addCallback(miCallback).build();

                }
            }
        }
        return INSTANCE;
    }






}