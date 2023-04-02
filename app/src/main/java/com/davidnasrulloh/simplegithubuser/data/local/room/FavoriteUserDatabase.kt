package com.davidnasrulloh.simplegithubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.davidnasrulloh.simplegithubuser.data.local.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FavoriteUserDatabase: RoomDatabase(){
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteUserDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteUserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteUserDatabase::class.java, "database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as FavoriteUserDatabase
        }
    }
}
