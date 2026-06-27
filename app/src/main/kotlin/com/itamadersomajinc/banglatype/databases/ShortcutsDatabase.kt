package com.itamadersomajinc.banglatype.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itamadersomajinc.banglatype.interfaces.ShortcutsDao
import com.itamadersomajinc.banglatype.models.Shortcut

@Database(entities = [Shortcut::class], version = 1)
abstract class ShortcutsDatabase : RoomDatabase() {

    abstract fun ShortcutsDao(): ShortcutsDao

    companion object {
        private var db: ShortcutsDatabase? = null

        fun getInstance(context: Context): ShortcutsDatabase {
            if (db == null) {
                synchronized(ShortcutsDatabase::class) {
                    if (db == null) {
                        db = Room.databaseBuilder(context, ShortcutsDatabase::class.java, "shortcuts.db")
                            .fallbackToDestructiveMigration()
                            .build()
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
