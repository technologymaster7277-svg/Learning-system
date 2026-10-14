package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.model.StudyLanguage
import com.example.data.model.UserProfile
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.AsiNavyDarkest
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AsiMainApp()
            }
        }
    }
}

@Composable
fun AsiMainApp() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var currentDestination by remember { mutableStateOf(AppDestination.DASHBOARD) }
    var userProfile by remember { mutableStateOf(UserProfile()) }

    // Android Hardware & Gesture Back Handler
    BackHandler(enabled = drawerState.isOpen || currentDestination != AppDestination.DASHBOARD) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else if (currentDestination != AppDestination.DASHBOARD) {
            currentDestination = AppDestination.DASHBOARD
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AsiDrawerContent(
                currentDestination = currentDestination,
                userProfile = userProfile,
                onNavigate = { destination ->
                    currentDestination = destination
                    coroutineScope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AsiTopAppBar(
                    userProfile = userProfile,
                    title = when (currentDestination) {
                        AppDestination.DASHBOARD -> "ASI RESEARCH CENTER"
                        AppDestination.GLOBAL_TAXONOMY -> "Global Knowledge Taxonomy"
                        AppDestination.HIGHER_ED -> "University & Engineering"
                        AppDestination.RESEARCH_LAB -> "PhD Research Workspace"
                        AppDestination.COMPETITIVE_EXAMS -> "Competitive Exam Intelligence"
                        AppDestination.QUADRATIC_DEMO -> "Class 10 Demo: Quadratic"
                        AppDestination.AI_TUTOR -> "AI Study Tutor"
                        AppDestination.FORMULA_MASTER -> "Formula Master"
                        AppDestination.QUESTION_BANK -> "Question Bank"
                        AppDestination.VIRTUAL_LAB -> "Virtual Science Lab"
                        AppDestination.BOOK_INTELLIGENCE -> "Book Intelligence"
                        AppDestination.EXAM_PREP -> "Board Exam Lab"
                        AppDestination.SMART_NOTES -> "Smart Notes Engine"
                        AppDestination.SUBJECTS, AppDestination.MY_CLASSES -> "Subjects & Chapters"
                        AppDestination.REVISION_PLANNER -> "Revision Planner"
                        AppDestination.PROGRESS -> "My Learning Progress"
                        AppDestination.SAVED -> "Saved Materials"
                        AppDestination.SETTINGS -> "Profile & Settings"
                    },
                    onMenuClick = {
                        coroutineScope.launch { drawerState.open() }
                    },
                    onToggleLanguage = {
                        val newLang = if (userProfile.preferredLanguage == StudyLanguage.ENGLISH) {
                            StudyLanguage.HINDI
                        } else {
                            StudyLanguage.ENGLISH
                        }
                        userProfile = userProfile.copy(preferredLanguage = newLang)
                    }
                )
            },
            bottomBar = {
                AsiBottomNavigationBar(
                    currentDestination = currentDestination,
                    onNavigate = { currentDestination = it }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (currentDestination) {
                    AppDestination.DASHBOARD -> {
                        DashboardScreen(
                            userProfile = userProfile,
                            onNavigate = { currentDestination = it }
                        )
                    }
                    AppDestination.GLOBAL_TAXONOMY -> {
                        GlobalTaxonomyScreen(
                            onNavigate = { currentDestination = it }
                        )
                    }
                    AppDestination.HIGHER_ED -> {
                        HigherEdScreen()
                    }
                    AppDestination.RESEARCH_LAB -> {
                        ResearchWorkspaceScreen()
                    }
                    AppDestination.COMPETITIVE_EXAMS -> {
                        CompetitiveExamsScreen()
                    }
                    AppDestination.QUADRATIC_DEMO -> {
                        QuadraticDemoScreen(
                            language = userProfile.preferredLanguage
                        )
                    }
                    AppDestination.AI_TUTOR -> {
                        AiTutorScreen(
                            language = userProfile.preferredLanguage
                        )
                    }
                    AppDestination.FORMULA_MASTER -> {
                        FormulaMasterScreen()
                    }
                    AppDestination.QUESTION_BANK -> {
                        QuestionBankScreen(
                            language = userProfile.preferredLanguage
                        )
                    }
                    AppDestination.BOOK_INTELLIGENCE -> {
                        BookIntelligenceScreen()
                    }
                    AppDestination.EXAM_PREP -> {
                        ExamLabScreen(
                            language = userProfile.preferredLanguage
                        )
                    }
                    AppDestination.SMART_NOTES -> {
                        SmartNotesScreen(
                            language = userProfile.preferredLanguage
                        )
                    }
                    AppDestination.VIRTUAL_LAB -> {
                        VirtualLabScreen()
                    }
                    AppDestination.SUBJECTS, AppDestination.MY_CLASSES -> {
                        SubjectsScreen(
                            userProfile = userProfile,
                            onNavigateToDemo = { currentDestination = AppDestination.QUADRATIC_DEMO }
                        )
                    }
                    AppDestination.REVISION_PLANNER -> {
                        RevisionPlannerScreen()
                    }
                    AppDestination.PROGRESS -> {
                        ProgressScreen(userProfile = userProfile)
                    }
                    AppDestination.SAVED -> {
                        SavedMaterialsScreen()
                    }
                    AppDestination.SETTINGS -> {
                        SettingsScreen(
                            userProfile = userProfile,
                            onUpdateProfile = { userProfile = it }
                        )
                    }
                }
            }
        }
    }
}
