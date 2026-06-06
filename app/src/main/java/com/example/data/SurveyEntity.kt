package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "surveys")
data class SurveyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user_id: String,
    val building_no: String,
    val building_name: String,
    val address: String,
    val owner_name: String,
    val phone_no: String,
    val occupancy: String,
    val latitude: Double,
    val longitude: Double,
    val locality: String,
    val year_built: Int,
    val number_of_stories: Int,
    val total_floor_area_sqft: Double,
    val story_height: Double,
    val structural_system: String,
    val structural_type: String,
    val roof_type: String,
    val floor_type: String,
    val foundation_type: String,
    val seismic_band: String,
    val building_drawings_available: Boolean,
    val soil_type: String,
    val morphology_of_site: String,
    val plan_irregularities: String,
    val vertical_irregularities: String,
    val exterior_falling_hazard: String,
    val settlement_of_foundation_present: Boolean,
    val current_visual_condition: String,
    val post_earthquake_condition: String,
    val cracks_present: Boolean,
    val dampness_present: Boolean,
    val collapse_signs_present: Boolean,
    val notes: String,
    val basic_score: Double,
    val final_score: Double,
    val damage_grade: String,
    val risk_level: String,
    val recommendation: String,
    val retrofit_priority: String,
    val created_at: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
    val synced: Boolean = false
)
