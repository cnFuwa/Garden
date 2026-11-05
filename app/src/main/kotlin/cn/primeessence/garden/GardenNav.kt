// GardenNav

package cn.primeessence.garden

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider

@Composable
fun GardenNav(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<HomeRoute> {
                HomeScreen(
                    gotoNoteList = { backStack.add(NoteListRoute) },
                )
            }
          
            entry<NoteListRoute> {
                NoteList(
                    gotoNoteEdit = { id -> backStack.add(NoteEditRoute(id)) },
                    gotoNoteRead = { id -> backStack.add(NoteReadRoute(id)) }
                )
            }

            entry<NoteEditRoute> { route ->
                NoteEdit(
                    noteId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }
            
            entry<NoteReadRoute> { route ->
                NoteRead(
                    noteId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }
 
        }
    )
}
