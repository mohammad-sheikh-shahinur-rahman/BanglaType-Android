package com.itamadersomajinc.banglatype.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itamadersomajinc.banglatype.interfaces.PhrasesDao
import com.itamadersomajinc.banglatype.models.Phrase
import com.itamadersomajinc.banglatype.models.Juktakkhor

@Database(entities = [Phrase::class, Juktakkhor::class], version = 1)
abstract class PhrasesDatabase : RoomDatabase() {

    abstract fun PhrasesDao(): PhrasesDao

    companion object {
        private var db: PhrasesDatabase? = null

        fun getInstance(context: Context): PhrasesDatabase {
            if (db == null) {
                synchronized(PhrasesDatabase::class) {
                    if (db == null) {
                        db = Room.databaseBuilder(context, PhrasesDatabase::class.java, "phrases.db")
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return db!!
        }

        fun destroyInstance() {
            db = null
        }
    }
}
