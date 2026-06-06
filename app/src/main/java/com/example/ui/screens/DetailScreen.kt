package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Foundation
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SurveyEntity
import com.example.ui.SeismoViewModel
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.RiskCritical
import com.example.ui.theme.RiskCriticalLight
import com.example.ui.theme.RiskHigh
import com.example.ui.theme.RiskHighLight
import com.example.ui.theme.RiskMedium
import com.example.ui.theme.RiskMediumLight

@Composable
fun DetailScreen(
    viewModel: SeismoViewModel,
    onNavigateBack: () -> Unit
) {
    val survey = viewModel.selectedSurvey

    if (survey == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No survey details loaded.")
        }
        return
    }

    val (statusColor, containerColor) = when (survey.risk_level) {
        "Critical" -> Pair(RiskCritical, RiskCriticalLight)
        "High" -> Pair(RiskHigh, RiskHighLight)
        else -> Pair(RiskMedium, RiskMediumLight)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryBlue)
                    .padding(horizontal = 4.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Assessment Details",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title & Cloud Sync Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = containerColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "RISK SUMMARY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor.copy(alpha = 0.8f),
                            letterSpacing = 1.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (survey.synced) {
                                Icon(Icons.Default.CloudDone, "Synced", tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Cloud Synchronized", fontSize = 10.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.OfflinePin, "Offline Only", tint = Color(0xFFFABF00), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Offline Storage Only", fontSize = 10.sp, color = statusColor, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = survey.building_name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Address: ${survey.address}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SurfaceItem(label = "FINAL SCORE", value = String.format("%.2f", survey.final_score), labelColor = statusColor, modifier = Modifier.weight(1f))
                        SurfaceItem(label = "GRADE", value = survey.damage_grade, labelColor = statusColor, modifier = Modifier.weight(1f))
                        SurfaceItem(label = "RISK", value = survey.risk_level, labelColor = statusColor, modifier = Modifier.weight(1f))
                    }
                }
            }

            // Quick Actions info Box (Recommendation)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, "Recommendation Alert", tint = statusColor, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Recommendation", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = survey.recommendation,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )
                }
            }

            // SECTION 1: General & Contact
            SectionHeader(title = "Building Info & Owner Contact", icon = Icons.Default.Info)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(label = "Building Number", value = survey.building_no)
                    DetailRow(label = "Owner / Contact Person", value = survey.owner_name)
                    DetailRow(label = "Owner Phone", value = survey.phone_no)
                    DetailRow(label = "Occupancy Classification", value = survey.occupancy)
                    DetailRow(label = "Screening Timestamp", value = survey.created_at)
                }
            }

            // SECTION 2: Coordinates
            SectionHeader(title = "GPS Capture & Geographic Location", icon = Icons.Default.LocationOn)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(label = "Locality Region", value = survey.locality)
                    DetailRow(label = "Latitude Bounds", value = String.format("%.6f", survey.latitude))
                    DetailRow(label = "Longitude Bounds", value = String.format("%.6f", survey.longitude))
                }
            }

            // SECTION 3: Mechanical & Sizing
            SectionHeader(title = "Sizing Dimensions & Materials", icon = Icons.Default.Foundation)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(label = "Year Built", value = "${survey.year_built}")
                    DetailRow(label = "Number of Stories", value = "${survey.number_of_stories} floor levels")
                    DetailRow(label = "Total Floor Area", value = "${survey.total_floor_area_sqft} sqft")
                    DetailRow(label = "Floor Level Height (ft)", value = "${survey.story_height} ft")
                    DetailRow(label = "Structural Framing System", value = survey.structural_system)
                    DetailRow(label = "Standard Level Type", value = survey.structural_type)
                    DetailRow(label = "Roof Structure Model", value = survey.roof_type)
                    DetailRow(label = "Floor Slab Construction", value = survey.floor_type)
                    DetailRow(label = "Substructure / Foundation", value = survey.foundation_type)
                    DetailRow(label = "Seismic Connection Band", value = survey.seismic_band)
                    DetailRow(label = "Original Drawings Filed?", value = if (survey.building_drawings_available) "Yes" else "No")
                }
            }

            // SECTION 4: Irregularities
            SectionHeader(title = "Morphological Site & Irregularity Details", icon = Icons.Default.Build)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(label = "Local Soil Type", value = "Class ${survey.soil_type}")
                    DetailRow(label = "Terrain Morphology", value = survey.morphology_of_site)
                    DetailRow(label = "Horizontal Plan Abnormality", value = survey.plan_irregularities)
                    DetailRow(label = "Elevation Vertical Abnormality", value = survey.vertical_irregularities)
                    DetailRow(label = "Parapets Exterior Hazard", value = survey.exterior_falling_hazard)
                    DetailRow(label = "Visible Ground Settlement?", value = if (survey.settlement_of_foundation_present) "Yes" else "No")
                }
            }

            // SECTION 5: Cracks and Damage Observation
            SectionHeader(title = "Visual Damages & Defects Log", icon = Icons.Default.Warning)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(label = "Current Exterior Quality", value = survey.current_visual_condition)
                    DetailRow(label = "Post-Earthquake Defect", value = survey.post_earthquake_condition)
                    DetailRow(label = "Severe Masonry Cracks?", value = if (survey.cracks_present) "Yes" else "No")
                    DetailRow(label = "Indoor Dampness / Seepage?", value = if (survey.dampness_present) "Yes" else "No")
                    DetailRow(label = "Collapse / Tilt Hazard?", value = if (survey.collapse_signs_present) "Yes" else "No")
                    DetailRow(label = "Field Observations Notes", value = survey.notes.ifEmpty { "Nil" })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(0.45F))
        Text(
            text = value,
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.55F)
        )
    }
}

@Composable
fun SurfaceItem(
    label: String,
    value: String,
    labelColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = labelColor)
        }
    }
}
