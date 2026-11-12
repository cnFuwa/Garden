package cn.primeessence.garden

import androidx.compose.ui.graphics.Color

const val BATCH_SIZE = 500

const val CHINA_RED = Color(0xFFDE2910)
const val CHINA_BLUE = Color(0xFF0052CC)
const val GRASS_GREEN = Color(0xFF4CAF50)

object AppNumItem {
    val NOTE_MAX_NUM: Long = 512L
    val MUSIC_MAX_NUM: Long = 256L
    val META_MAX_NUM: Long = 32L
}

object CateIdItem {
    val HANZI: Long = 99L
    val GUOXUE: Long = 101L
    val NUMERIC: Long = 102L
    val VARIANT: Long = 103L
    val READX: Long = 105L
    val CHILDREN: Long = 101L
    val YOUNG: Long = 102L
    val CLASSIC: Long = 99L
    val WORK: Long = 103L
    val SOFT: Long = 105L
}

object NoteItem {
    val TITLE: String = ""
    val CATE_ID: Long = 1L
    val CONTENT: String = ""
    val ENABLED: Int = 0
    val IS_DELETED: Int = 0
}

object MateItem {
    val TITLE: String = ""
    val CATE_ID: Long = 1L
    val CONTENT: String = ""
    val ENABLED: Int = 0
    val IS_DELETED: Int = 0
}

object MusicItem {
    val TITLE: String = ""
    val CATE_ID: Long = 1L
    val CONTENT: String = ""
    val ENABLED: Int = 0
    val IS_DELETED: Int = 0
}

object MetaItem {
    val TITLE: String = ""
    val CONTENT: String = "1"
    val ENABLED: Int = 0
    val IS_DELETED: Int = 0
}

