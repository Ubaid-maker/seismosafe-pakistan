package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {

    @Query("SELECT * FROM surveys ORDER BY id DESC")
    fun getAllSurveys(): Flow<List<SurveyEntity>>

    @Query("SELECT * FROM surveys WHERE synced = 0")
    suspend fun getUnsyncedSurveys(): List<SurveyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: SurveyEntity): Long

    @Update
    suspend fun updateSurvey(survey: SurveyEntity)

    @Query("UPDATE surveys SET synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)

    @Query("SELECT * FROM surveys WHERE id = :id")
    suspend fun getSurveyById(id: Int): SurveyEntity?

    @Query("DELETE FROM surveys")
    suspend fun deleteAllSurveys()
}
