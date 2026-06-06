package com.example.data

import android.util.Log
import kotlinx.coroutines.flow.Flow

class SurveyRepository(private val surveyDao: SurveyDao) {

    val allSurveys: Flow<List<SurveyEntity>> = surveyDao.getAllSurveys()

    suspend fun insertLocalSurvey(survey: SurveyEntity): Long {
        return surveyDao.insertSurvey(survey)
    }

    suspend fun updateLocalSurvey(survey: SurveyEntity) {
        surveyDao.updateSurvey(survey)
    }

    suspend fun getSurveyById(id: Int): SurveyEntity? {
        return surveyDao.getSurveyById(id)
    }

    suspend fun deleteAll() {
        surveyDao.deleteAllSurveys()
    }

    /**
     * Synchronizes any unsynced local surveys to Supabase.
     * Returns the count of successfully synced surveys.
     */
    suspend fun syncWithSupabase(token: String): Int {
        val unsynced = surveyDao.getUnsyncedSurveys()
        Log.d("SurveyRepository", "Found ${unsynced.size} unsynced surveys.")
        var count = 0
        for (survey in unsynced) {
            val success = SupabaseClient.insertSurvey(survey, token)
            if (success) {
                surveyDao.markAsSynced(survey.id)
                count++
            }
        }
        return count
    }
}
