package com.itamadersomajinc.banglatype.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itamadersomajinc.banglatype.interfaces.PredictionsDao
import com.itamadersomajinc.banglatype.models.Bigram
import com.itamadersomajinc.banglatype.models.LearnedWord

@Database(entities = [LearnedWord::class, Bigram::class], version = 1)
abstract class PredictionDatabase : RoomDatabase() {

    abstract fun PredictionsDao(): PredictionsDao

    companion object {
        private var db: PredictionDatabase? = null

        fun getInstance(context: Context): PredictionDatabase {
            if (db == null) {
                synchronized(PredictionDatabase::class) {
                    if (db == null) {
                        db = Room.databaseBuilder(context, PredictionDatabase::class.java, "predictions.db").build()
                        db!!.openHelper.setWriteAheadLoggingEnabled(true)
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
