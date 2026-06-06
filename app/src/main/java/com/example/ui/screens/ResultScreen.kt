package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ResultScreen(
    viewModel: SeismoViewModel,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val result = viewModel.calculationResult
    val context = LocalContext.current

    if (result == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No results calculated yet.")
        }
        return
    }

    // Color codes based on risk
    val (statusColor, containerColor) = when (result.riskLevel) {
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
                    text = "Screening Result",
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
            // General Details banner
            Card(
                colors = CardDefaults.cardColors(containerColor = containerColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "RISK LEVEL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor.copy(alpha = 0.8f),
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = result.riskLevel.uppercase() + " RISK",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = statusColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Damage Grade: ${result.damageGrade}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = statusColor
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Big Score Circle
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "RVS SCORE",
                                fontSize = 9.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = String.format("%.2f", result.finalScore),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
                    }
                }
            }

            // Recommendation Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, "Recs", tint = statusColor, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Recommendation", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = result.recommendation,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Retrofit Priority
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF4F5F7), shape = RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Build, "Retrofit", tint = statusColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retrofit Priority", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Text(
                            text = result.retrofitPriority.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = statusColor
                        )
                    }
                }
            }

            // Modifiers Breakdown List
            Text(
                text = "Triggered Modifiers Details",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = PrimaryBlue,
                modifier = Modifier.padding(top = 4.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Base Score
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Basic Model Score (${viewModel.formStructuralSystem})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            text = "+${result.basicScore}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = RiskMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEEEEEE)))
                    Spacer(modifier = Modifier.height(8.dp))

                    if (result.modifiers.isEmpty()) {
                        Text("No environmental modifiers triggered.", color = Color.Gray, fontSize = 12.sp)
                    } else {
                        result.modifiers.forEach { modifier ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(modifier.name, fontSize = 12.sp, color = Color.DarkGray)
                                val modifierColor = if (modifier.value < 0.0) RiskCritical else RiskMedium
                                val sign = if (modifier.value > 0.0) "+" else ""
                                Text(
                                    text = "$sign${modifier.value}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = modifierColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFE0E0E0)))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Final Math Calculation Result", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            text = String.format("%.2f", result.finalScore),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Save Action Button
            Button(
                onClick = {
                    viewModel.saveCompletedSurvey { generatedId ->
                        Toast.makeText(context, "Assessment Saved Successfully!", Toast.LENGTH_SHORT).show()
                        onSaveSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_rvs_survey_button"),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Save, "Save Icon", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SAVE TO DATABASE", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
