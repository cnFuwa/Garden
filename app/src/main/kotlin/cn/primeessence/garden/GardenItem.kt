package cn.primeessence.garden

import androidx.compose.ui.graphics.Color

object AppColor {
    val CHINA_RED = Color(0xFFDE2910)
    val CHINA_BLUE = Color(0xFF0052CC)
    val GRASS_GREEN = Color(0xFF4CAF50)
}

object AppNumItem {
    val BATCH_SIZE = 500
    val NOTE_MAX_NUM: Long = 100L
    val MATE_MAX_NUM: Long = 100L
    val MUSIC_MAX_NUM: Long = 100L
    val META_MAX_NUM: Long = 32L
}

object NoteCateItem {
    val HANZI: Long = 99L
    val GUOXUE: Long = 101L
    val NUMERIC: Long = 102L
    val VARIANT: Long = 103L
    val READX: Long = 105L
}

object MateCateItem {
    val SHENHUA: Long = 99L
    val HISTORY: Long = 101L
    val MODERN: Long = 102L
    val STAR: Long = 103L
    val CREATOR: Long = 105L
}

object MusicCateItem {
    val CLASSIC: Long = 99L
    val CHILDREN: Long = 101L
    val YOUNG: Long = 102L
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

