package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GpsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: GpsLocationEntity): Long

    @Query("SELECT * FROM gps_queue WHERE isSynced = 0 ORDER BY timestamp ASC LIMIT 50")
    suspend fun getUnsyncedLocations(): List<GpsLocationEntity>

    @Query("UPDATE gps_queue SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)

    @Query("SELECT * FROM gps_queue WHERE orderId = :orderId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestLocationForOrder(orderId: String): Flow<GpsLocationEntity?>

    @Query("DELETE FROM gps_queue WHERE isSynced = 1 AND timestamp < :olderThan")
    suspend fun cleanOldSynced(olderThan: Long)
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartEntity)

    @Delete
    suspend fun delete(item: CartEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders_cache ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders_cache WHERE orderId = :orderId LIMIT 1")
    fun getOrderById(orderId: String): Flow<OrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(order: OrderEntity)

    @Query("UPDATE orders_cache SET status = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)
}

@Dao
interface RiderNoteDao {
    @Query("SELECT * FROM rider_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<RiderNoteEntity>>

    @Query("SELECT * FROM rider_notes WHERE orderId = :orderId ORDER BY timestamp DESC")
    fun getNotesForOrder(orderId: String): Flow<List<RiderNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: RiderNoteEntity)

    @Delete
    suspend fun delete(note: RiderNoteEntity)
}

@Dao
interface SupportTicketDao {
    @Query("SELECT * FROM support_tickets ORDER BY createdAt DESC")
    fun getAllTickets(): Flow<List<SupportTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: SupportTicketEntity)
}
