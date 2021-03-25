package ds.docusheet.table.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {DocumentEntity.class,SavedDataEntity.class,ColumnDetailsEntity.class}, version = 1, exportSchema = false)
public abstract class DatabaseClass extends RoomDatabase {

    public abstract DaoDocument getDocumentDao();
    public abstract DaoSavedData getSavedDataDao();
    public abstract DaoColumnDetails getColumnDetails();

    private static volatile DatabaseClass INSTANCE;
    private static volatile DatabaseClass INSTANCEFORDATA;
    private static volatile DatabaseClass INSTANCEFORColumns;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DatabaseClass getDatabaseDocument(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseClass.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseClass.class,"document")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public static DatabaseClass getDatabaseData(final Context context) {
        if (INSTANCEFORDATA == null) {
            synchronized (DatabaseClass.class) {
                if (INSTANCEFORDATA == null) {
                    INSTANCEFORDATA = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseClass.class,"saveddata")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCEFORDATA;
    }
    public static DatabaseClass getDatabaseColumns(final Context context) {
        if (INSTANCEFORColumns == null) {
            synchronized (DatabaseClass.class) {
                if (INSTANCEFORColumns == null) {
                    INSTANCEFORColumns = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseClass.class,"column_details")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCEFORColumns;
    }

}
