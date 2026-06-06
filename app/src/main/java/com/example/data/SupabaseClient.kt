package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object SupabaseClient {
    private const val TAG = "SupabaseClient"

    // Retrieve values from BuildConfig loaded via Secrets plugin from env
    val supabaseUrl: String = BuildConfig.SUPABASE_URL.ifEmpty { "https://vinwtfrqxvtoofhbjlcq.supabase.co" }
    val supabaseKey: String = BuildConfig.SUPABASE_KEY.ifEmpty { "sb_publishable_aQYc3aBdwwWodqKMLJBcEQ_1XmE5wY9" }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    sealed class AuthResult {
        data class Success(
            val token: String,
            val userId: String,
            val email: String,
            val name: String,
            val phone: String
        ) : AuthResult()
        data class Error(val message: String) : AuthResult()
    }

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        phone: String
    ): AuthResult = withContext(Dispatchers.IO) {
        try {
            val url = "$supabaseUrl/auth/v1/signup"

            // Construct payload with fallback metadata representations to ensure it aligns with standard Supabase configurations
            val dataObj = JSONObject().apply {
                put("name", name)
                put("phone", phone)
            }
            val payload = JSONObject().apply {
                put("email", email)
                put("password", password)
                put("data", dataObj)
                put("options", JSONObject().apply {
                    put("data", dataObj)
                })
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", supabaseKey)
                .addHeader("Content-Type", "application/json")
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string()
                Log.d(TAG, "signUp response: code=${response.code}, body=$bodyStr")

                if (response.isSuccessful && !bodyStr.isNullOrEmpty()) {
                    val json = JSONObject(bodyStr)
                    val userObj = json.optJSONObject("user")
                    val userId = userObj?.optString("id") ?: ""
                    
                    // Supabase often returns session on signup, but sometimes asks for email confirmation.
                    // If access_token isn't returned, we treat it as Success with empty token to allow login or assume success.
                    val accessToken = json.optString("access_token") ?: ""

                    if (userId.isNotEmpty()) {
                        AuthResult.Success(
                            token = accessToken,
                            userId = userId,
                            email = email,
                            name = name,
                            phone = phone
                        )
                    } else {
                        AuthResult.Error("Registration succeeded but no User ID was returned.")
                    }
                } else {
                    val errMsg = parseErrorMsg(bodyStr) ?: "Signup failed (HTTP ${response.code})"
                    AuthResult.Error(errMsg)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "signUp failed", e)
            AuthResult.Error(e.localizedMessage ?: "Unknown server communication error")
        }
    }

    suspend fun signIn(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val url = "$supabaseUrl/auth/v1/token?grant_type=password"

            val payload = JSONObject().apply {
                put("email", email)
                put("password", password)
            }

            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", supabaseKey)
                .addHeader("Content-Type", "application/json")
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
                .build()

            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string()
                Log.d(TAG, "signIn response: code=${response.code}, body=$bodyStr")

                if (response.isSuccessful && !bodyStr.isNullOrEmpty()) {
                    val json = JSONObject(bodyStr)
                    val token = json.getString("access_token")
                    val userObj = json.getJSONObject("user")
                    val userId = userObj.getString("id")
                    val userMetadata = userObj.optJSONObject("user_metadata")

                    val name = userMetadata?.optString("name") ?: ""
                    val phone = userMetadata?.optString("phone") ?: ""

                    AuthResult.Success(
                        token = token,
                        userId = userId,
                        email = email,
                        name = name,
                        phone = phone
                    )
                } else {
                    val errMsg = parseErrorMsg(bodyStr) ?: "Invalid email or password."
                    AuthResult.Error(errMsg)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "signIn failed", e)
            AuthResult.Error(e.localizedMessage ?: "Network connection error")
        }
    }

    suspend fun insertSurvey(survey: SurveyEntity, token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = "$supabaseUrl/rest/v1/surveys"

            val payload = JSONObject().apply {
                put("user_id", survey.user_id)
                put("building_no", survey.building_no)
                put("building_name", survey.building_name)
                put("address", survey.address)
                put("owner_name", survey.owner_name)
                put("phone_no", survey.phone_no)
                put("occupancy", survey.occupancy)
                put("latitude", survey.latitude)
                put("longitude", survey.longitude)
                put("locality", survey.locality)
                put("year_built", survey.year_built)
                put("number_of_stories", survey.number_of_stories)
                put("total_floor_area_sqft", survey.total_floor_area_sqft)
                put("story_height", survey.story_height)
                put("structural_system", survey.structural_system)
                put("structural_type", survey.structural_type)
                put("roof_type", survey.roof_type)
                put("floor_type", survey.floor_type)
                put("foundation_type", survey.foundation_type)
                put("seismic_band", survey.seismic_band)
                put("building_drawings_available", survey.building_drawings_available)
                put("soil_type", survey.soil_type)
                put("morphology_of_site", survey.morphology_of_site)
                put("plan_irregularities", survey.plan_irregularities)
                put("vertical_irregularities", survey.vertical_irregularities)
                put("exterior_falling_hazard", survey.exterior_falling_hazard)
                put("settlement_of_foundation_present", survey.settlement_of_foundation_present)
                put("current_visual_condition", survey.current_visual_condition)
                put("post_earthquake_condition", survey.post_earthquake_condition)
                put("cracks_present", survey.cracks_present)
                put("dampness_present", survey.dampness_present)
                put("collapse_signs_present", survey.collapse_signs_present)
                put("notes", survey.notes)
                put("basic_score", survey.basic_score)
                put("final_score", survey.final_score)
                put("damage_grade", survey.damage_grade)
                put("risk_level", survey.risk_level)
                put("recommendation", survey.recommendation)
                put("retrofit_priority", survey.retrofit_priority)
                put("created_at", survey.created_at)
            }

            val requestBuilder = Request.Builder()
                .url(url)
                .addHeader("apikey", supabaseKey)
                .addHeader("Content-Type", "application/json")
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))

            if (token.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            client.newCall(requestBuilder.build()).execute().use { response ->
                Log.d(TAG, "insertSurvey response code: ${response.code}")
                response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e(TAG, "insertSurvey failed", e)
            false
        }
    }

    private fun parseErrorMsg(body: String?): String? {
        if (body.isNullOrEmpty()) return null
        return try {
            val json = JSONObject(body)
            json.optString("error_description")
                .ifEmpty { json.optString("message") }
                .ifEmpty { json.optString("error") }
                .ifEmpty { null }
        } catch (e: Exception) {
            null
        }
    }
}
