package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SeismoViewModel
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.PrimaryBlue

@Composable
fun NewSurveyScreen(
    viewModel: SeismoViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToResult: () -> Unit
) {
    val context = LocalContext.current

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
                    text = "New Assessment",
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
        ) {
            // Step Progress Indicator
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP ${viewModel.currentStep} OF 6",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = when (viewModel.currentStep) {
                            1 -> "Building Info"
                            2 -> "GPS Location"
                            3 -> "Building Details"
                            4 -> "Structural Info"
                            5 -> "Site & Irregularities"
                            else -> "Damage Observation"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = viewModel.currentStep / 6f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PrimaryBlue,
                    trackColor = Color(0xFFE3E2E6)
                )

                // Inline Horizontal Dot Stepper Indicator
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (i in 1..6) {
                        val active = i == viewModel.currentStep
                        val completed = i < viewModel.currentStep
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (active) PrimaryBlue else if (completed) PrimaryBlue.copy(
                                        alpha = 0.5f
                                    ) else Color(0xFFE0E0E0)
                                )
                        )
                    }
                }
            }

            // Scrollable step forms content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (viewModel.currentStep) {
                    1 -> Step1BuildingInfo(viewModel)
                    2 -> Step2GpsLocation(viewModel)
                    3 -> Step3BuildingDetails(viewModel)
                    4 -> Step4StructuralInfo(viewModel)
                    5 -> Step5SiteAndIrregularities(viewModel)
                    6 -> Step6DamageObservation(viewModel)
                }
            }

            // Stepper Navigation Buttons Bottom Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (viewModel.currentStep > 1) {
                    Button(
                        onClick = { viewModel.currentStep-- },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3E2E6), contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NavigateBefore, "Prev Icon")
                            Text("Previous", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (viewModel.currentStep < 6) {
                    Button(
                        onClick = {
                            if (validateStep(viewModel, viewModel.currentStep, context)) {
                                viewModel.currentStep++
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Next Step", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.NavigateNext, "Next Icon")
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.calculateRvsScore()
                            onNavigateToResult()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("calculate_rvs_score_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, "Score Icon", tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Calculate RVS", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun validateStep(viewModel: SeismoViewModel, step: Int, context: android.content.Context): Boolean {
    if (step == 1) {
        if (viewModel.formBuildingNo.isBlank()) {
            Toast.makeText(context, "Please enter a Building Number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewModel.formBuildingName.isBlank()) {
            Toast.makeText(context, "Please enter a Building Name", Toast.LENGTH_SHORT).show()
            return false
        }
    } else if (step == 2) {
        if (viewModel.formLocality.isBlank()) {
            Toast.makeText(context, "Please enter a Area Locality name", Toast.LENGTH_SHORT).show()
            return false
        }
    } else if (step == 3) {
        if (viewModel.formYearBuilt.toIntOrNull() == null) {
            Toast.makeText(context, "Please enter a valid numeric Year Built", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewModel.formNumberOfStories.toIntOrNull() == null) {
            Toast.makeText(context, "Please enter number of stories", Toast.LENGTH_SHORT).show()
            return false
        }
    }
    return true
}

// STEP 1 - Building Info (building_no, building_name, address, owner_name, phone_no, occupancy)
@Composable
fun Step1BuildingInfo(viewModel: SeismoViewModel) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("General Information", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)

            OutlinedTextField(
                value = viewModel.formBuildingNo,
                onValueChange = { viewModel.formBuildingNo = it },
                label = { Text("Building Number (e.g., BLDG-101)") },
                modifier = Modifier.fillMaxWidth().testTag("bldg_no_input"),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = viewModel.formBuildingName,
                onValueChange = { viewModel.formBuildingName = it },
                label = { Text("Building Structural Name") },
                modifier = Modifier.fillMaxWidth().testTag("bldg_name_input"),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = viewModel.formAddress,
                onValueChange = { viewModel.formAddress = it },
                label = { Text("Street Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = viewModel.formOwnerName,
                onValueChange = { viewModel.formOwnerName = it },
                label = { Text("Owner / Contact Person Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = viewModel.formPhoneNo,
                onValueChange = { viewModel.formPhoneNo = it },
                label = { Text("Contact Phone No") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Text("Occupancy Category", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            RowSelector(
                options = listOf("Residential", "Commercial", "Government", "Religious"),
                selected = viewModel.formOccupancy,
                onSelected = { viewModel.formOccupancy = it }
            )
        }
    }
}

// STEP 2 - GPS Location (Auto capture, locality)
@Composable
fun Step2GpsLocation(viewModel: SeismoViewModel) {
    val context = LocalContext.current
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Geographic Coordinates", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)

            // Dynamic Informative box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EAF6), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Info, "Loc Info", tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Calibrated study boundaries center coordinates around Upper Dir, KPK region. Captured coordinates will reflect localized scientific positioning.",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.captureGps(context)
                    Toast.makeText(context, "GPS Coordinates Captured Successfully!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().testTag("capture_gps_button"),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, "GPS Info")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Auto Capturing GPS Coordinates")
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = String.format("%.6f", viewModel.formLatitude),
                    onValueChange = { viewModel.formLatitude = it.toDoubleOrNull() ?: 35.1977 },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    readOnly = true
                )

                OutlinedTextField(
                    value = String.format("%.6f", viewModel.formLongitude),
                    onValueChange = { viewModel.formLongitude = it.toDoubleOrNull() ?: 71.8749 },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    readOnly = true
                )
            }

            OutlinedTextField(
                value = viewModel.formLocality,
                onValueChange = { viewModel.formLocality = it },
                label = { Text("Locality / Sector Name (e.g., Dir City)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

// STEP 3 - Building Details (year_built, number_of_stories, total_floor_area_sqft, story_height)
@Composable
fun Step3BuildingDetails(viewModel: SeismoViewModel) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Sizing & Dimensional Metrics", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)

            OutlinedTextField(
                value = viewModel.formYearBuilt,
                onValueChange = { viewModel.formYearBuilt = it },
                label = { Text("Year Built / Estimated Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("year_built_input"),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = viewModel.formNumberOfStories,
                onValueChange = { viewModel.formNumberOfStories = it },
                label = { Text("Number of Stories (Levels)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = viewModel.formTotalFloorArea,
                onValueChange = { viewModel.formTotalFloorArea = it },
                label = { Text("Total Floor Area (sqft)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = viewModel.formStoryHeight,
                onValueChange = { viewModel.formStoryHeight = it },
                label = { Text("Average Story Height (ft)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

// STEP 4 - Structural Info (structural_system, structural_type, roof, floor, foundation, band, drawings)
@Composable
fun Step4StructuralInfo(viewModel: SeismoViewModel) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Structural Characteristics", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)

            Column {
                Text("Structural Framing System (- modifiers vary)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("URM", "CM", "Stone Masonry", "C3"),
                    selected = viewModel.formStructuralSystem,
                    onSelected = { viewModel.formStructuralSystem = it }
                )
            }

            Column {
                Text("Engineering Standard", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Non Engineered", "Semi Engineered", "Engineered"),
                    selected = viewModel.formStructuralType,
                    onSelected = { viewModel.formStructuralType = it }
                )
            }

            Column {
                Text("Roof Structure Framing", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Wooden Truss", "Steel Truss", "RCC", "Timber"),
                    selected = viewModel.formRoofType,
                    onSelected = { viewModel.formRoofType = it }
                )
            }

            Column {
                Text("Floor Deck Composition", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("PCC", "RCC", "Other"),
                    selected = viewModel.formFloorType,
                    onSelected = { viewModel.formFloorType = it }
                )
            }

            Column {
                Text("Substructure / Foundation Type", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Plain Concrete", "Reinforced Concrete", "Stone Masonry"),
                    selected = viewModel.formFoundationType,
                    onSelected = { viewModel.formFoundationType = it }
                )
            }

            Column {
                Text("Horizontal Seismic Band", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("None", "Roof", "Plinth", "Lintel", "Roof + Plinth", "Roof + Lintel"),
                    selected = viewModel.formSeismicBand,
                    onSelected = { viewModel.formSeismicBand = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Engineering Drawings Available?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Access to original drawings files", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(
                    checked = viewModel.formDrawingsAvailable,
                    onCheckedChange = { viewModel.formDrawingsAvailable = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = PrimaryBlue)
                )
            }
        }
    }
}

// STEP 5 - Site and Irregularities (soil, morphology, plan_irr, vert_irr, hazard, settlement_bool)
@Composable
fun Step5SiteAndIrregularities(viewModel: SeismoViewModel) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Geological & Plan Irregularities", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)

            Column {
                Text("Soil Class Classification", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("A", "B", "C", "D"),
                    selected = viewModel.formSoilType,
                    onSelected = { viewModel.formSoilType = it }
                )
            }

            Column {
                Text("Site Slope Morphology", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Horizontal", "Mild Slope", "Steep Slope"),
                    selected = viewModel.formMorphology,
                    onSelected = { viewModel.formMorphology = it }
                )
            }

            Column {
                Text("Plan Configuration Irregularity", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Nil", "Torsion", "Re-entrant Corners", "Non-Parallel System"),
                    selected = viewModel.formPlanIrregularities,
                    onSelected = { viewModel.formPlanIrregularities = it }
                )
            }

            Column {
                Text("Vertical Elevation Irregularity", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Nil", "Sloping site", "Soft Story", "Short Column"),
                    selected = viewModel.formVerticalIrregularities,
                    onSelected = { viewModel.formVerticalIrregularities = it }
                )
            }

            Column {
                Text("Exterior Falling hazard", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Nil", "Parapets"),
                    selected = viewModel.formFallingHazard,
                    onSelected = { viewModel.formFallingHazard = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Settlement of Foundation Present?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Visible uneven ground depression", fontSize = 12.sp, color = Color.Gray)
                }
                Switch(
                    checked = viewModel.formSettlementPresent,
                    onCheckedChange = { viewModel.formSettlementPresent = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = PrimaryBlue)
                )
            }
        }
    }
}

// STEP 6 - Damage Observation (visual, cracks, dampness, collapse_signs, notes)
@Composable
fun Step6DamageObservation(viewModel: SeismoViewModel) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Visual Condition & Cracks", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)

            Column {
                Text("Current Visual Structural Condition", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Excellent", "Good", "Damaged", "Collapsed"),
                    selected = viewModel.formVisualCondition,
                    onSelected = { viewModel.formVisualCondition = it }
                )
            }

            Column {
                Text("Post-Earthquake Defect Condition", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                RowSelector(
                    options = listOf("Good", "Minor Cracks", "Damaged", "Collapse signs"),
                    selected = viewModel.formPostEarthquake,
                    onSelected = { viewModel.formPostEarthquake = it }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomToggleRow("Cracks Present in Masonry/Plaster?", viewModel.formCracksPresent) {
                    viewModel.formCracksPresent = it
                }
                CustomToggleRow("Dampness / Seepage Present?", viewModel.formDampnessPresent) {
                    viewModel.formDampnessPresent = it
                }
                CustomToggleRow("Severe Collapse Signs Present?", viewModel.formCollapseSignsPresent) {
                    viewModel.formCollapseSignsPresent = it
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = viewModel.formNotes,
                onValueChange = { viewModel.formNotes = it },
                label = { Text("Field Notes / Structural Observations") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun RowSelector(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) PrimaryBlue else Color(0xFFF0F1F5))
                    .clickable { onSelected(option) }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White else Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun CustomToggleRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = PrimaryBlue)
        )
    }
}
