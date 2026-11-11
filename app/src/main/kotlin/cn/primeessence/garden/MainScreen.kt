// MainScreen

package cn.primeessence.garden

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

enum class Tab {
    Home,
    Note,
    Mate,
    Music,
    Meta
}

@Composable
fun MainScreen() {
    val homeStack: NavBackStack<NavKey> = rememberNavBackStack(HomeRoute)
    val noteStack: NavBackStack<NavKey> = rememberNavBackStack(NoteListRoute)
    val mateStack: NavBackStack<NavKey> = rememberNavBackStack(MateListRoute)
    val musicStack: NavBackStack<NavKey> = rememberNavBackStack(MusicListRoute)
    val metaStack: NavBackStack<NavKey> = rememberNavBackStack(MetaListRoute)
    
    var selectedTab by remember { mutableStateOf(Tab.Home) }

    val currentBackStack = when(selectedTab){
        Tab.Home -> homeStack
        Tab.Note -> noteStack
        Tab.Mate -> mateStack
        Tab.Music -> musicStack
        Tab.Meta -> metaStack
    }

    BackHandler(enabled = currentBackStack.size > 1) {
        currentBackStack.removeLastOrNull()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == Tab.Home,
                    onClick = { selectedTab = Tab.Home },
                    label = { Text("Home") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.Note,
                    onClick = { selectedTab = Tab.Note },
                    label = { Text("Note") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.Mate,
                    onClick = { selectedTab = Tab.Mate },
                    label = { Text("Mate") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.Music,
                    onClick = { selectedTab = Tab.Music },
                    label = { Text("Music") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == Tab.Meta,
                    onClick = { selectedTab = Tab.Meta },
                    label = { Text("Meta") },
                    icon = {}
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (selectedTab) {
                Tab.Home -> GardenNav(homeStack)
                Tab.Note -> GardenNav(noteStack)
                Tab.Mate -> GardenNav(mateStack)
                Tab.Music -> GardenNav(musicStack)
                Tab.Meta -> GardenNav(metaStack)
            }
        }
    }
}