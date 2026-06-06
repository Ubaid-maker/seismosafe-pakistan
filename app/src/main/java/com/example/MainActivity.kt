package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.SeismoViewModel
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NewSurveyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SignupScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: SeismoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. Splash Screen Route
        composable("splash") {
            SplashScreen(onTimeout = {
                // If the user was already logged in, skip Login and go directly to the Dashboard.
                // Otherwise, show the Login screen first to secure access.
                if (viewModel.isLoggedIn && viewModel.userId != "offline_user") {
                    navController.navigate("dashboard") {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            })
        }

        // 2. Login Screen Route
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToSignup = {
                    navController.navigate("signup")
                },
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBypass = {
                    // Set guest credentials in VM & Navigate
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 3. Signup Screen Route
        composable("signup") {
            SignupScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onSignupSuccess = {
                    // Navigate directly to dashboard if signup yields success
                    navController.navigate("dashboard") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        // 4. Home Dashboard Route
        composable("dashboard") {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToNewSurvey = {
                    navController.navigate("new_survey")
                },
                onNavigateToHistory = {
                    navController.navigate("history")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        // 5. New Survey Form Wizard Route
        composable("new_survey") {
            NewSurveyScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToResult = {
                    navController.navigate("result_survey")
                }
            )
        }

        // 6. Screening Result Route
        composable("result_survey") {
            ResultScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    // Pop back to dashboard after saving completed assessments
                    navController.navigate("dashboard") {
                        popUpTo("new_survey") { inclusive = true }
                    }
                }
            )
        }

        // 7. Assessments History Registry Route
        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetail = {
                    navController.navigate("detail_survey")
                }
            )
        }

        // 8. Survey Details Screen Route
        composable("detail_survey") {
            DetailScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // 9. Profile and Sync Route
        composable("profile") {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
