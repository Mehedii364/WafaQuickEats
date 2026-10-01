package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        GpsLocationEntity::class,
        CartEntity::class,
        OrderEntity::class,
        RiderNoteEntity::class,
        SupportTicketEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gpsDao(): GpsDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun riderNoteDao(): RiderNoteDao
    abstract fun supportTicketDao(): SupportTicketDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wafa_quickeats.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
