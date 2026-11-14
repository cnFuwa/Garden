// LocalMusic
package cn.primeessence.garden

import android.net.Uri

data class LocalMusic(
    val id: Long,
    val title: String,
    val artist: String,
    val uri: Uri
)