package skills.future.planer.db;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import skills.future.planer.db.task.TaskData;
import skills.future.planer.db.task.database.TaskDataTabDao;

@Database(entities = {TaskData.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    private static final String DB_NAME = "planer.db";
    private static AppDatabase sInstance;

    public abstract TaskDataTabDao taskDataTabDao();

    public static AppDatabase getInstance(final Context context/*, final AppExecutors executors*/) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME).allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }

//    /**
//     * Build the database. {@link Builder#build()} only sets up the database configuration and
//     * creates a new instance of the database.
//     * The SQLite database is only created when it's accessed for the first time.
//     */
//    private static AppDatabase buildDatabase(final Context appContext,
//                                             final AppExecutors executors) {
//        return Room.databaseBuilder(appContext, AppDatabase.class, DB_NAME)
//                .addCallback(new Callback() {
//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//                        executors.diskIO().execute(() -> {
//                            // Add a delay to simulate a long-running operation
//                            addDelay();
//                            // Generate the data for pre-population
//                            AppDatabase database = AppDatabase.getInstance(appContext, executors);
//                            database.setDatabaseCreated();
//                        });
//                    }
//                })
//                .addMigrations(MIGRATION_1_2)
//                .build();
//    }

//    /**
//     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
//     */
//    private void updateDatabaseCreated(final Context context) {
//        if (context.getDatabasePath(DB_NAME).exists()) {
//            setDatabaseCreated();
//        }
//    }
//
//    private void setDatabaseCreated() {
//        mIsDatabaseCreated.postValue(true);
//    }
//
////    private static void insertData(final AppDatabase database, final List<ProductEntity> products,
////                                   final List<CommentEntity> comments) {
////        database.runInTransaction(() -> {
//////            database.productDao().insertAll(products);
//////            database.commentDao().insertAll(comments);
////        });
////    }
//
//    private static void addDelay() {
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException ignored) {
//        }
//    }
//
//    public LiveData<Boolean> getDatabaseCreated() {
//        return mIsDatabaseCreated;
//    }
//
//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `productsFts` USING FTS4("
//                    + "`name` TEXT, `description` TEXT, content=`products`)");
//            database.execSQL("INSERT INTO productsFts (`rowid`, `name`, `description`) "
//                    + "SELECT `id`, `name`, `description` FROM products");
//
//        }
//    };
}
