package cn.primeessence.garden

import androidx.compose.ui.graphics.Color

object AppColor {
    val CHINA_RED = Color(0xFFDE2910)
    val CHINA_BLUE = Color(0xFF0052CC)
    val GRASS_GREEN = Color(0xFF4CAF50)
}

object AppNumItem {
    val BATCH_SIZE = 5000
    val NOTE_MAX_NUM: Long = 32L
    val MATE_MAX_NUM: Long = 32L
    val MUSIC_MAX_NUM: Long = 32L
    val META_MAX_NUM: Long = 32L
    val FAVOR_MAX_NUM: Long = 32L
    val DRAFT_CATE_ID: Long = 2001L 
}


object NoteCateItem {
    val XUEKE: Long = 99L
    val HANZI: Long = 100L
    val GUOXUE: Long = 101L
    val NUMERIC: Long = 102L
    val VARIANT: Long = 105L
    val READX: Long = 106L
}

object MateCateItem {
    val PERSON: Long = 99L
    val SHENHUA: Long = 100L
    val HISTORY: Long = 101L
    val MODERN: Long = 102L
    val STAR: Long = 105L
    val CREATOR: Long = 106L
}

object MusicCateItem {
    val MUSIC: Long = 99L
    val CLASSIC: Long = 100L
    val CHILDREN: Long = 101L
    val YOUNG: Long = 102L
    val WORK: Long = 105L
    val SOFT: Long = 106L
}

object FavorCateItem {
    val MATH: Long = 99L
    val COMPUTER: Long = 100L
    val NONGXUE: Long = 101L
    val GONGXUE: Long = 102L
    val ZHEXUE: Long = 103L
}

object FavorItem {
    val TITLE: String = ""
    val CATE_ID: Long = 1L
    val NOTE_ID: Long = 0L
    val CONTENT: String = ""
    val ENABLED: Int = 0
    val IS_DELETED: Int = 0
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

