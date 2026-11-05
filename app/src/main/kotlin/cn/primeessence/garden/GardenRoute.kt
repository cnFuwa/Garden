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
