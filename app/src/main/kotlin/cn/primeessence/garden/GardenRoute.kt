// GardenRoute

package cn.primeessence.garden

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : NavKey

@Serializable
data object NoteListRoute : NavKey
@Serializable
data class NoteReadRoute(val id: Long) : NavKey
@Serializable
data class NoteEditRoute(val id: Long) : NavKey

@Serializable
data object MateListRoute : NavKey
@Serializable
data class MateReadRoute(val id: Long) : NavKey
@Serializable
data class MateEditRoute(val id: Long) : NavKey

@Serializable
data object MusicListRoute : NavKey
@Serializable
data class MusicReadRoute(val id: Long) : NavKey
@Serializable
data class MusicEditRoute(val id: Long) : NavKey

@Serializable
data object MetaListRoute : NavKey
@Serializable
data class MetaReadRoute(val id: Long) : NavKey
@Serializable
data class MetaEditRoute(val id: Long) : NavKey

@Serializable
data object ActionReadRoute : NavKey

@Serializable
data object LocalMusicRoute : NavKey