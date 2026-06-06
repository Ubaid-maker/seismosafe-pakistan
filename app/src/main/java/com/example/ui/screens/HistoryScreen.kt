package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun HistoryScreen(
    viewModel: SeismoViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: () -> Unit
) {
    val surveyList by viewModel.filteredSurveys.collectAsState()
    val activeFilter by viewModel.riskFilter.collectAsState()

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
                    text = "Assessments History",
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
            // Filter Horizontal Scrollable Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(label = "All", count = null, active = activeFilter == "All") {
                    viewModel.setRiskFilter("All")
                }
                FilterChipItem(label = "Critical", count = null, active = activeFilter == "Critical") {
                    viewModel.setRiskFilter("Critical")
                }
                FilterChipItem(label = "High", count = null, active = activeFilter == "High") {
                    viewModel.setRiskFilter("High")
                }
                FilterChipItem(label = "Medium", count = null, active = activeFilter == "Medium") {
                    viewModel.setRiskFilter("Medium")
                }
            }

            // Survey list or Empty placeholder
            if (surveyList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color(0xFFE3E2E6), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Info, null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Surveys Found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (activeFilter == "All") {
                                "Tap the floating button '+' or start action on home screen to conduct your very first seismic evaluation."
                            } else {
                                "No records found matching '$activeFilter' risk level filter."
                            },
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("surveys_lazy_column"),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(surveyList) { survey ->
                        SurveyRowCard(
                            survey = survey,
                            onClick = {
                                viewModel.selectSurvey(survey)
                                onNavigateToDetail()
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(
    label: String,
    count: Int?,
    active: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) PrimaryBlue else Color.White)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (active) PrimaryBlue else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = if (active) Color.White else Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
            count?.let {
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .background(
                            if (active) Color.White.copy(alpha = 0.2f) else Color.LightGray,
                            shape = CircleShape
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("$it", fontSize = 10.sp, color = if (active) Color.White else Color.Black)
                }
            }
        }
    }
}

@Composable
fun SurveyRowCard(
    survey: SurveyEntity,
    onClick: () -> Unit
) {
    val (statusColor, containerColor) = when (survey.risk_level) {
        "Critical" -> Pair(RiskCritical, RiskCriticalLight)
        "High" -> Pair(RiskHigh, RiskHighLight)
        else -> Pair(RiskMedium, RiskMediumLight)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("survey_item_${survey.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Status Pillar Indicator
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(statusColor)
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Info Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = survey.building_name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PrimaryBlue
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "No: ${survey.building_no}  •  ${survey.structural_system}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Loc Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = survey.locality,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }

            // Sync Status Icon & Risk Badge
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Sync status indicator
                if (survey.synced) {
                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = "Synced with Cloud",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.OfflinePin,
                        contentDescription = "Saved Offline Only",
                        tint = Color(0xFFFABF00),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Severity label box
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(containerColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${survey.risk_level} (${survey.damage_grade})",
                        color = statusColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
