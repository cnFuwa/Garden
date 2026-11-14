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
                Home(
                    gotoNoteList = { backStack.add(NoteListRoute) },
                    gotoMusicList = { backStack.add(MusicListRoute) },
                    gotoMateList = { backStack.add(MateListRoute) },
                    gotoMetaList = { backStack.add(MetaListRoute) },
                    gotoRead = { backStack.add(ActionReadRoute) },
                    gotoLocalMusic = { backStack.add(LocalMusicRoute) }
                )
            }
            
            // Note
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
            
            // Mate
            entry<MateListRoute> {
                MateList(
                    gotoMateEdit = { id -> backStack.add(MateEditRoute(id)) },
                    gotoMateRead = { id -> backStack.add(MateReadRoute(id)) }
                )
            }

            entry<MateEditRoute> { route ->
                MateEdit(
                    mateId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }
            
            entry<MateReadRoute> { route ->
                MateRead(
                    mateId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }

            // Music
            entry<MusicListRoute> {
                MusicList(
                    gotoMusicEdit = { id -> backStack.add(MusicEditRoute(id)) },
                    gotoMusicRead = { id -> backStack.add(MusicReadRoute(id)) }
                )
            }

            entry<MusicEditRoute> { route ->
                MusicEdit(
                    musicId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }
            
            entry<MusicReadRoute> { route ->
                MusicRead(
                    musicId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }

            // Meta
            entry<MetaListRoute> {
                MetaList(
                    gotoMetaEdit = { id -> backStack.add(MetaEditRoute(id)) },
                    gotoMetaRead = { id -> backStack.add(MetaReadRoute(id)) }
                )
            }

            entry<MetaEditRoute> { route ->
                MetaEdit(
                    metaId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }
            
            entry<MetaReadRoute> { route ->
                MetaRead(
                    metaId = route.id,
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }

            //Action Read
            entry<ActionReadRoute> { route ->
                Read(
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }
            
            //LocalMusic
            entry<LocalMusicRoute> { route ->
                LocalMusicScreen(
                    gotoBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
