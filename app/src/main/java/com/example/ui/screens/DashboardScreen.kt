package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SeismoViewModel
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderMuted
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryBlue
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.RiskCritical
import com.example.ui.theme.RiskCriticalLight
import com.example.ui.theme.RiskCriticalText
import com.example.ui.theme.RiskHigh
import com.example.ui.theme.RiskHighLight
import com.example.ui.theme.RiskHighText
import com.example.ui.theme.RiskMedium
import com.example.ui.theme.RiskMediumLight
import com.example.ui.theme.RiskMediumText

@Composable
fun DashboardScreen(
    viewModel: SeismoViewModel,
    onNavigateToNewSurvey: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val surveyList by viewModel.surveys.collectAsState()

    // Calculations based on the dynamic database
    val totalCount = surveyList.size
    val criticalCount = surveyList.count { it.risk_level.equals("Critical", ignoreCase = true) }
    val highCount = surveyList.count { it.risk_level.equals("High", ignoreCase = true) }
    val mediumCount = surveyList.count { it.risk_level.equals("Medium", ignoreCase = true) }

    // Initials resolved from name
    val initials = if (viewModel.userName.isNotBlank()) {
        viewModel.userName.split(" ")
            .filter { it.isNotBlank() }
            .map { it.first().uppercase() }
            .take(2)
            .joinToString("")
    } else {
        "RS" // Researcher
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.resetForm()
                    onNavigateToNewSurvey()
                },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .testTag("start_survey_fab")
                    .padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Start Survey", modifier = Modifier.size(28.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Status Bar Mimic
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("12:45", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .border(1.5.dp, Color(0xFF64748B), shape = CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color(0xFF64748B), shape = RoundedCornerShape(2.dp))
                    )
                }
            }

            // 2. Top App Bar (Material 3 Minimal Styling)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SeismoSafe Pakistan",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Dir City • Zone III (Ss 1.112g)",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryBlue, shape = CircleShape)
                        .clickable { onNavigateToProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // Cloud Sync Capsule
            val syncMsg = if (viewModel.isLoggedIn && viewModel.userToken.isNotEmpty()) {
                "Cloud Sync Active"
            } else {
                "Offline Local Mode (Active)"
            }
            val badgeColor = if (viewModel.isLoggedIn && viewModel.userToken.isNotEmpty()) {
                Color(0xFF22C55E)
            } else {
                Color(0xFFF97316)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 2.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .border(1.dp, BorderMuted, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(badgeColor, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(syncMsg, color = Color(0xFF475569), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Summary Assessment Card styled as beautifully as HTML template
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BorderMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text("Total Assessments", fontSize = 13.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$totalCount",
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "+${surveyList.take(8).size} active",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF3B82F6),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFEFF6FF), shape = RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tri-color horizontal bar segment mimicking HTML design
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9)),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (totalCount == 0) {
                            Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFE2E8F0)))
                        } else {
                            if (criticalCount > 0) {
                                Box(modifier = Modifier.weight(criticalCount.toFloat()).fillMaxHeight().background(RiskCritical))
                            }
                            if (highCount > 0) {
                                Box(modifier = Modifier.weight(highCount.toFloat()).fillMaxHeight().background(RiskHigh))
                            }
                            if (mediumCount > 0) {
                                Box(modifier = Modifier.weight(mediumCount.toFloat()).fillMaxHeight().background(RiskMedium))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Risk Breakdown Vertical List matching Tailwind styles
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Critical Row
                RiskRowItem(
                    title = "Critical Risk (D4-D5)",
                    subtitle = "Immediate Retrofit Required",
                    count = criticalCount,
                    dotColor = RiskCritical,
                    containerColor = RiskCriticalLight,
                    borderColor = Color(0xFFFCA5A5),
                    textColor = RiskCriticalText,
                    subtextColor = Color(0xFFB91C1C),
                    onClick = {
                        viewModel.setRiskFilter("Critical")
                        onNavigateToHistory()
                    }
                )

                // High Row
                RiskRowItem(
                    title = "High Risk (D3-D4)",
                    subtitle = "Detailed Assessment Needed",
                    count = highCount,
                    dotColor = RiskHigh,
                    containerColor = RiskHighLight,
                    borderColor = Color(0xFFFED7AA),
                    textColor = RiskHighText,
                    subtextColor = Color(0xFFC2410C),
                    onClick = {
                        viewModel.setRiskFilter("High")
                        onNavigateToHistory()
                    }
                )

                // Medium Row
                RiskRowItem(
                    title = "Medium Risk (D2-D3)",
                    subtitle = "Regular Inspection Only",
                    count = mediumCount,
                    dotColor = RiskMedium,
                    containerColor = RiskMediumLight,
                    borderColor = Color(0xFFBBF7D0),
                    textColor = RiskMediumText,
                    subtextColor = Color(0xFF15803D),
                    onClick = {
                        viewModel.setRiskFilter("Medium")
                        onNavigateToHistory()
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Start Survey Banner Callout
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .testTag("start_survey_banner_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, BorderMuted),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(RiskHighLight, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alert Symbol",
                            tint = RiskHigh,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Assess Building Risk", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PrimaryBlue)
                        Text("Rapid visual seismic evaluation", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                    Button(
                        onClick = {
                            viewModel.resetForm()
                            onNavigateToNewSurvey()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = ButtonDefaults.ContentPadding
                    ) {
                        Text("Start", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 6. Quick Actions Area
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Quick Actions",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionTile(
                        icon = Icons.AutoMirrored.Filled.List,
                        label = "History List",
                        sublabel = "View & Filter",
                        backgroundColor = Color.White,
                        iconTint = SecondaryBlue,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.setRiskFilter("All")
                                onNavigateToHistory()
                            }
                    )

                    QuickActionTile(
                        icon = Icons.Default.CloudSync,
                        label = "Profile Sync",
                        sublabel = "Sync Supabase",
                        backgroundColor = Color.White,
                        iconTint = AccentTeal,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToProfile() }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Research context details styled elegantly
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, BorderMuted),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFFEEF2F6), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info Icon",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Research & Study Context",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "This application implements a Modified RVS (Rapid Visual Screening) hazard calculation strictly calibrated for Khyber Pakhtunkhwa structures, based on engineering study parameters from Dir Upper.",
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Grid of metadata attributes
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ResearchItem(label = "Study Reference Area", value = "Dir City, District Dir Upper, KPK, Pakistan")
                            ResearchItem(label = "Seismic Zoning Designation", value = "Seismic Zone III (Moderate–High Hazard)")
                            ResearchItem(label = "Calibration Baseline", value = "Modified FEMA P-154 RVS Method")
                            ResearchItem(label = "Field Sample Size", value = "100 reference buildings (March–June 2024)")
                            ResearchItem(label = "Spectral Accelerations", value = "Ss = 1.1123g, S1 = 0.4104g, Soil Class B")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
fun RiskRowItem(
    title: String,
    subtitle: String,
    count: Int,
    dotColor: Color,
    containerColor: Color,
    borderColor: Color,
    textColor: Color,
    subtextColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .border(1.dp, borderColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(dotColor, shape = CircleShape)
                )
                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = subtextColor
                    )
                }
            }
            Text(
                text = "$count",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun RiskSummaryCard(
    title: String,
    grade: String,
    count: Int,
    cardColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = grade,
                fontSize = 10.sp,
                color = contentColor.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$count",
                fontSize = 24.sp,
                color = contentColor,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun QuickActionTile(
    icon: ImageVector,
    label: String,
    sublabel: String,
    backgroundColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, BorderMuted),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconTint.copy(alpha = 0.1F), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PrimaryBlue)
                Text(sublabel, fontSize = 10.sp, color = Color(0xFF64748B))
            }
        }
    }
}

@Composable
fun ResearchItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(6.dp)
                .background(PrimaryBlue, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 12.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
        }
    }
}
