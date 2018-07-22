package com.cleon.gygreviews.gygreviews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cleon.gygreviews.gygreviews.model.Review


@Database(entities = [(Review::class)], version = 2)
abstract class AppDataBase : RoomDatabase() {

    abstract fun reviewDataDao(): ReviewDao

    companion object {

        private var INSTANCE: AppDataBase? = null

        private const val DB_NAME = "gygapp_database"

        fun getInstance(context: Context): AppDataBase? {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDataBase::class.java, DB_NAME)
                            .build()
                }
            }
            return INSTANCE
        }
    }

}