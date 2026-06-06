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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.SeismoViewModel
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.RiskHighLight
import com.example.ui.theme.SecondaryBlue

@Composable
fun ProfileScreen(
    viewModel: SeismoViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
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
                    text = "Researcher Profile",
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
            // Upper Profile Avatar Layout
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, "User Avatar", tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = viewModel.userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Authorized SeismoSafe Civil Assessor",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Light
                    )
                }
            }

            // SECTION: Researcher details
            Text("Assessor Parameters", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryBlue)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileDetailRow(icon = Icons.Default.Email, label = "Email Address", value = viewModel.userEmail)
                    ProfileDetailRow(icon = Icons.Default.Phone, label = "Mobile Phone", value = viewModel.userPhone)
                    ProfileDetailRow(icon = Icons.Default.Security, label = "Role Designation", value = "KPK Provincial Surveyor")
                }
            }

            // SECTION: Cloud Connection & DB Management
            Text("Cloud Synchronization Management", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryBlue)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CloudDone, "Cloud Connection Status", tint = SecondaryBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Supabase Syncing", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                        
                        val syncBadgeActive = viewModel.isLoggedIn && viewModel.userToken.isNotEmpty()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (syncBadgeActive) Color(0xFFE8F5E9) else RiskHighLight)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (syncBadgeActive) "ONLINE SYNC" else "OFFLINE LOCAL",
                                color = if (syncBadgeActive) Color(0xFF388E3C) else Color(0xFFF57C00),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Unsynchronized surveys recorded in the offline local database will push automatically to the active remote table when cloud synchronization is manually triggered below.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Sync Status message feedback
                    if (viewModel.syncStatusMessage.isNotEmpty()) {
                        Text(
                            text = viewModel.syncStatusMessage,
                            color = PrimaryBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE8EAF6), shape = RoundedCornerShape(6.dp))
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    if (viewModel.isSyncing) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PrimaryBlue)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.triggerSync() },
                            modifier = Modifier.fillMaxWidth().testTag("profile_sync_supabase_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryBlue),
                            shape = RoundedCornerShape(8.dp),
                            enabled = viewModel.isLoggedIn && viewModel.userToken.isNotEmpty()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CloudSync, "Sync")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SYNC SURVEYS NOW", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Logout Action Button
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().testTag("profile_logout_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, "Logout Icon", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("LOGOUT ASSESSOR SESSION", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFEEEEEE), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
        }
    }
}
