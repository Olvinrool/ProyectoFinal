package hn.uth.myapplication.planilla;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Planilla.class}, version = 1, exportSchema = false)
public abstract class PlanillaDatabase extends RoomDatabase {
    public abstract PlanillaDao planillaDao();

    private static volatile PlanillaDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //GENERADOR DE INSTANCIA USANDO PATRÓN SINGLETON
        static PlanillaDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (PlanillaDatabase.class){
                if(INSTANCE == null){
                    Callback miCallback = new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            databaseWriteExecutor.execute(() -> {
                                PlanillaDao dao = INSTANCE.planillaDao();
                                dao.deleteAll();

                                Planilla nuevo = new Planilla("Maria", "12/02/10", 2023);
                                dao.insert(nuevo);
                            });

                        }
                    };
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PlanillaDatabase.class, "planilla_database").addCallback(miCallback).build();

                }
            }
        }
        return INSTANCE;
    }


}
