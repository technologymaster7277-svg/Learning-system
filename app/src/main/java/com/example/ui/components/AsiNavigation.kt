package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.theme.AsiCyan
import com.example.ui.theme.AsiNavyBorder
import com.example.ui.theme.AsiNavyCardElevated
import com.example.ui.theme.AsiNavyDarkest

enum class AppDestination(val title: String, val icon: ImageVector, val group: String) {
    DASHBOARD("Learning Dashboard", Icons.Default.Dashboard, "CORE"),
    GLOBAL_TAXONOMY("Universal Taxonomy (School to PhD)", Icons.Default.Public, "GLOBAL INTELLIGENCE"),
    HIGHER_ED("University & Engineering (B.Tech/B.Sc)", Icons.Default.AccountBalance, "GLOBAL INTELLIGENCE"),
    RESEARCH_LAB("PhD Research Workspace", Icons.Default.Biotech, "GLOBAL INTELLIGENCE"),
    COMPETITIVE_EXAMS("Competitive Exam Hub (UPSC/JEE/NEET)", Icons.Default.WorkspacePremium, "GLOBAL INTELLIGENCE"),

    MY_CLASSES("My Classes & Boards", Icons.Default.School, "CORE"),
    SUBJECTS("Subjects & Chapters", Icons.Default.MenuBook, "CORE"),

    QUADRATIC_DEMO("Demo: Quadratic Eq", Icons.Default.Functions, "DEMO & TOOLS"),
    AI_TUTOR("AI Study Tutor", Icons.Default.Psychology, "DEMO & TOOLS"),
    SMART_NOTES("Smart Notes Engine", Icons.Default.Description, "DEMO & TOOLS"),
    FORMULA_MASTER("Formula Master", Icons.Default.Calculate, "DEMO & TOOLS"),

    QUESTION_BANK("Question Bank", Icons.Default.Quiz, "PRACTICE & TESTS"),
    BOOK_INTELLIGENCE("Book Intelligence", Icons.Default.AutoStories, "PRACTICE & TESTS"),
    EXAM_PREP("Exam Preparation Lab", Icons.Default.Assignment, "PRACTICE & TESTS"),

    VIRTUAL_LAB("Virtual Science Lab", Icons.Default.Science, "ADVANCED"),
    REVISION_PLANNER("Revision Planner", Icons.Default.EventRepeat, "ADVANCED"),
    PROGRESS("My Progress & Stats", Icons.Default.BarChart, "ADVANCED"),
    SAVED("Saved Materials", Icons.Default.Bookmark, "SYSTEM"),
    SETTINGS("Profile & Settings", Icons.Default.Settings, "SYSTEM")
}

@Composable
fun AsiDrawerContent(
    currentDestination: AppDestination,
    userProfile: UserProfile,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier.widthIn(max = 330.dp),
        drawerContainerColor = AsiNavyDarkest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AsiNavyCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_asi_symbol),
                        contentDescription = "ASI Logo",
                        modifier = Modifier.size(36.dp)
                    )
                }
                Column {
                    Text(
                        text = "ASI RESEARCH CENTER",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "AI Learning Intelligence",
                        style = MaterialTheme.typography.labelSmall,
                        color = AsiCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Student profile chip
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = userProfile.studentName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${userProfile.selectedClass.displayName} • ${userProfile.selectedBoard.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = AsiNavyCardElevated,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${userProfile.studyStreakDays}d Streak 🔥",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AsiCyan,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = AsiNavyBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Grouped Navigation Items
            val groups = listOf("CORE", "GLOBAL INTELLIGENCE", "DEMO & TOOLS", "PRACTICE & TESTS", "ADVANCED", "SYSTEM")
            groups.forEach { group ->
                Text(
                    text = group,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                )

                AppDestination.values().filter { it.group == group }.forEach { dest ->
                    val isSelected = currentDestination == dest
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = dest.icon,
                                contentDescription = dest.title,
                                tint = if (isSelected) AsiCyan else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = dest.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AsiCyan else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        selected = isSelected,
                        onClick = { onNavigate(dest) },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AsiNavyCardElevated,
                            unselectedContainerColor = androidx.compose.ui.graphics.Color.Transparent
                        ),
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .testTag("nav_item_${dest.name.lowercase()}")
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun AsiBottomNavigationBar(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit
) {
    val bottomItems = listOf(
        AppDestination.DASHBOARD,
        AppDestination.AI_TUTOR,
        AppDestination.QUADRATIC_DEMO,
        AppDestination.QUESTION_BANK,
        AppDestination.VIRTUAL_LAB
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars
    ) {
        bottomItems.forEach { dest ->
            val isSelected = currentDestination == dest
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(dest) },
                icon = {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = dest.title
                    )
                },
                label = {
                    Text(
                        text = when (dest) {
                            AppDestination.DASHBOARD -> "Dashboard"
                            AppDestination.AI_TUTOR -> "AI Tutor"
                            AppDestination.QUADRATIC_DEMO -> "Class 10 Demo"
                            AppDestination.QUESTION_BANK -> "Questions"
                            AppDestination.VIRTUAL_LAB -> "Science Lab"
                            else -> dest.title
                        },
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AsiCyan,
                    selectedTextColor = AsiCyan,
                    indicatorColor = AsiNavyCardElevated,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("bottom_nav_${dest.name.lowercase()}")
            )
        }
    }
}
