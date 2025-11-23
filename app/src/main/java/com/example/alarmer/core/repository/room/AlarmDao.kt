package com.example.alarmer.core.repository.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AlarmDao {

    /**
     * Insert new alarm.
     * Returns generated ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: AlarmEntity): Long

    /**
     * Update existing alarm.
     */
    @Update
    suspend fun update(alarm: AlarmEntity)

    /**
     * Delete alarm.
     */
    @Delete
    suspend fun delete(alarm: AlarmEntity)

    /**
     * Get alarm by id.
     */
    @Query("SELECT * FROM alarms WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AlarmEntity?

    /**
     * Get all alarms sorted by time.
     */
    @Query("SELECT * FROM alarms ORDER BY offset_minutes")
    suspend fun getAll(): List<AlarmEntity>

    /**
     * Disable / enable alarm quickly.
     */
    @Query("UPDATE alarms SET is_enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Int, enabled: Boolean)

    /**
     * Remove all alarms (not required but useful).
     */
    @Query("DELETE FROM alarms")
    suspend fun deleteAll()

    /**
     * Observe all alarms.
     */
    @Query("SELECT * FROM alarms ORDER BY order_index ASC")
    fun observeAll(): Flow<List<AlarmEntity>>

    @Query("SELECT COALESCE(MAX(order_index), -1) FROM alarms")
    suspend fun getMaxOrderIndex(): Int

    @Query("SELECT order_index FROM alarms WHERE id = :id LIMIT 1")
    suspend fun getOrderIndexByParentId(id: Int): Int

}
