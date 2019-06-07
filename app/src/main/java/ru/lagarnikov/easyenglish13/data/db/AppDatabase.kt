package ru.lagarnikov.easyenglish13.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room



@Database(entities = arrayOf(DataSql::class), version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun sqlDao(): SqlDao

    private var INSTANCE: AppDatabase? = null



    companion object {
        private var INSTANCE: AppDatabase? = null
        private var lastTableName:String=""

        fun getInstance(context: Context, tableName:String): AppDatabase? {
            if (INSTANCE == null || !tableName.equals(lastTableName)) {
                lastTableName =tableName

                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, tableName)
                        .allowMainThreadQueries()
                        .build()
                }

            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}