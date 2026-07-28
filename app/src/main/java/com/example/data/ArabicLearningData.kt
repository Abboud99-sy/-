package com.example.data

data class LetterModel(
    val id: Int,
    val symbol: String,               // e.g. "ب"
    val name: String,                 // e.g. "بَاء"
    val fatha: String,                // e.g. "بَ"
    val damma: String,                // e.g. "بُ"
    val kasra: String,                // e.g. "بِ"
    val initialForm: String,          // e.g. "بـ"
    val medialForm: String,           // e.g. "ـبـ"
    val finalForm: String,            // e.g. "ـب"
    val sampleWord: String,           // e.g. "بَقَرَة"
    val sampleWordMeaning: String,    // e.g. "Cow"
    val sampleWordEmoji: String,      // e.g. "🐄"
    val colorHex: Long                // Card theme color
)

data class SyllableWordModel(
    val id: Int,
    val word: String,                 // Full word e.g. "بَقَرَة"
    val wordPhonetic: String,         // Pronunciation helper
    val emoji: String,                // "🐄"
    val category: String,             // "حيوانات" / "فواكه" / "أشياء"
    val correctSyllables: List<String>,// ["بَـ", "قَـ", "رَ", "ة"]
    val distractorSyllables: List<String> // ["تُـ", "مَ", "سْ"]
)

sealed class QuizQuestion {
    data class MissingLetter(
        val id: Int,
        val word: String,             // e.g. "أ_نَب"
        val fullWord: String,         // "أَرْنَب"
        val options: List<String>,    // ["رْ", "مْ", "سْ"]
        val correctIndex: Int,
        val emoji: String
    ) : QuizQuestion()

    data class AssembleSyllables(
        val id: Int,
        val targetWordModel: SyllableWordModel
    ) : QuizQuestion()

    data class SoundIdentification(
        val id: Int,
        val targetLetter: String,     // e.g. "ش"
        val targetSound: String,      // "شَ"
        val options: List<String>,    // ["س", "ش", "ص", "ض"]
        val correctIndex: Int
    ) : QuizQuestion()
}

object ArabicLearningData {

    val letters = listOf(
        LetterModel(1, "أ", "أَلِف", "أَ", "أُ", "إِ", "أَـ", "ـأَـ", "ـأَ", "أَرْنَب", "أرنب سريع", "🐇", 0xFFFFE4E6),
        LetterModel(2, "ب", "بَاء", "بَ", "بُ", "بِ", "بَـ", "ـبَـ", "ـبَ", "بَقَرَة", "بقرة نَشيطَة", "🐄", 0xFFE0F2FE),
        LetterModel(3, "ت", "تَاء", "تَ", "تُ", "تِ", "تَـ", "ـتَـ", "ـتَ", "تُفَّاحَة", "تفاحة لَذِيذَة", "🍎", 0xFFFCE7F3),
        LetterModel(4, "ث", "ثَاء", "ثَ", "ثُ", "ثِ", "ثَـ", "ـثَـ", "ـثَ", "ثَعْلَب", "ثعلب ذَكِي", "🦊", 0xFFFEF3C7),
        LetterModel(5, "ج", "جِيم", "جَ", "جُ", "جِ", "جَـ", "ـجَـ", "ـجَ", "جَمَل", "جمل صَبُور", "🐪", 0xFFDCFCE7),
        LetterModel(6, "ح", "حَاء", "حَ", "حُ", "حِ", "حَـ", "ـحَـ", "ـحَ", "حِصَان", "حصان سَرِيع", "🐎", 0xFFF3E8FF),
        LetterModel(7, "خ", "خَاء", "خَ", "خُ", "خِ", "خَـ", "ـخَـ", "ـخَ", "خُبْز", "خبز طَازَج", "🍞", 0xFFFFEDD5),
        LetterModel(8, "د", "دَال", "دَ", "دُ", "دِ", "دَ", "ـدَ", "ـدَ", "دَرَّاجَة", "دراجة جَمِيلَة", "🚲", 0xFFCFFAFE),
        LetterModel(9, "ذ", "ذَال", "ذَ", "ذُ", "ذِ", "ذَ", "ـذَ", "ـذَ", "ذُرَة", "ذرة صَفْرَاء", "🌽", 0xFFFEF9C3),
        LetterModel(10, "ر", "رَاء", "رَ", "رُ", "رِ", "رَ", "ـرَ", "ـرَ", "رُمَّان", "رمان حُلْو", "🧃", 0xFFFFE4E6),
        LetterModel(11, "ز", "زَاي", "زَ", "زُ", "زِ", "زَ", "ـزَ", "ـزَ", "زَهْرَة", "زهراء عَطِرَة", "🌸", 0xFFFCE7F3),
        LetterModel(12, "س", "سِين", "سَ", "سُ", "سِ", "سَـ", "ـسَـ", "ـسَ", "سَمَكَة", "سمكة سَبَّاحَة", "🐟", 0xFFE0F2FE),
        LetterModel(13, "ش", "شِين", "شَ", "شُ", "شِ", "شَـ", "ـشَـ", "ـشَ", "شَمْس", "شمس سَاطِعَة", "☀️", 0xFFFEF3C7),
        LetterModel(14, "ص", "صَاد", "صَ", "صُ", "صِ", "صَـ", "ـصَـ", "ـصَ", "صَقْر", "صقر قَوِي", "🦅", 0xFFE0E7FF),
        LetterModel(15, "ض", "ضَاد", "ضَ", "ضُ", "ضِ", "ضَـ", "ـضَـ", "ـضَ", "ضَفْدَع", "ضفدع قَفَّاز", "🐸", 0xFFDCFCE7),
        LetterModel(16, "ط", "طَاء", "طَ", "طُ", "طِ", "طَـ", "ـطَـ", "ـطَ", "طَائِرَة", "طائرة مُحَلِّقَة", "✈️", 0xFFF3E8FF),
        LetterModel(17, "ظ", "ظَاء", "ظَ", "ظُ", "ظِ", "ظَـ", "ـظَـ", "ـظَ", "ظَرْف", "ظرف بَرِيدِي", "✉️", 0xFFFFEDD5),
        LetterModel(18, "ع", "عَيْن", "عَ", "عُ", "عِ", "عَـ", "ـعَـ", "ـعَ", "عَصْفُور", "عصفور مَغَرِّد", "🐦", 0xFFE0F2FE),
        LetterModel(19, "غ", "غَيْن", "غَ", "غُ", "غِ", "غَـ", "ـغَـ", "ـغَ", "غَزَال", "غزال رَشِيق", "🦌", 0xFFFEF9C3),
        LetterModel(20, "ف", "فَاء", "فَ", "فُ", "فِ", "فَـ", "ـفَـ", "ـفَ", "فَرَاشَة", "فراشة زَاهِيَة", "🦋", 0xFFFCE7F3),
        LetterModel(21, "ق", "قَاف", "قَ", "قُ", "قِ", "قَـ", "ـقَـ", "ـقَ", "قِطَّة", "قطة أَلِيفَة", "🐱", 0xFFFFE4E6),
        LetterModel(22, "ك", "كَاف", "كَ", "كُ", "كِ", "كَـ", "ـكَـ", "ـك", "كِتَاب", "كتاب مَفِيد", "📚", 0xFFDCFCE7),
        LetterModel(23, "ل", "لاَم", "لَ", "لُ", "لِ", "لَـ", "ـلَـ", "ـل", "لَيْمُون", "ليمون حَامِض", "🍋", 0xFFFEF3C7),
        LetterModel(24, "م", "مِيم", "مَ", "مُ", "مِ", "مَـ", "ـمَـ", "ـم", "مَوْز", "موز لَذِيذ", "🍌", 0xFFFFEDD5),
        LetterModel(25, "ن", "نُون", "نَ", "نُ", "نِ", "نَـ", "ـنَـ", "ـن", "نَجْمَة", "نجمة بَرَّاقَة", "⭐", 0xFFFEF9C3),
        LetterModel(26, "هـ", "هَاء", "هَ", "هُ", "هـِ", "هَـ", "ـهَـ", "ـه", "هَدِيَّة", "هدية جَمِيلَة", "🎁", 0xFFF3E8FF),
        LetterModel(27, "و", "وَاو", "وَ", "وُ", "وِ", "وَ", "ـوَ", "ـوَ", "وَرْدَة", "وردة حَمْرَاء", "🌹", 0xFFFFE4E6),
        LetterModel(28, "ي", "يَاء", "يَ", "يُ", "يِ", "يَـ", "ـيَـ", "ـي", "يَد", "يد صَغِيرَة", "🖐️", 0xFFE0F2FE)
    )

    val syllableWords = listOf(
        SyllableWordModel(
            id = 1,
            word = "أَرْنَب",
            wordPhonetic = "أَ - رْ - نَ - ب",
            emoji = "🐇",
            category = "حيوانات",
            correctSyllables = listOf("أَ", "رْ", "نَـ", "ب"),
            distractorSyllables = listOf("مَ", "سْ", "لَ")
        ),
        SyllableWordModel(
            id = 2,
            word = "بَقَرَة",
            wordPhonetic = "بَـ - قَـ - رَ - ة",
            emoji = "🐄",
            category = "حيوانات",
            correctSyllables = listOf("بَـ", "قَـ", "رَ", "ة"),
            distractorSyllables = listOf("تُـ", "مَ", "نْ")
        ),
        SyllableWordModel(
            id = 3,
            word = "تُفَّاحَة",
            wordPhonetic = "تُـ - فَّا - حَ - ة",
            emoji = "🍎",
            category = "فواكه",
            correctSyllables = listOf("تُـ", "فَّا", "حَ", "ة"),
            distractorSyllables = listOf("كِـ", "مُـ", "لَ")
        ),
        SyllableWordModel(
            id = 4,
            word = "جَمَل",
            wordPhonetic = "جَـ - مَ - ل",
            emoji = "🐪",
            category = "حيوانات",
            correctSyllables = listOf("جَـ", "مَ", "ل"),
            distractorSyllables = listOf("فَ", "رْ")
        ),
        SyllableWordModel(
            id = 5,
            word = "سَمَكَة",
            wordPhonetic = "سَـ - مَ - كَ - ة",
            emoji = "🐟",
            category = "حيوانات",
            correctSyllables = listOf("سَـ", "مَ", "كَـ", "ة"),
            distractorSyllables = listOf("شَـ", "نُـ")
        ),
        SyllableWordModel(
            id = 6,
            word = "قِطَّة",
            wordPhonetic = "قِـ - طَّ - ة",
            emoji = "🐱",
            category = "حيوانات",
            correctSyllables = listOf("قِـ", "طَّ", "ة"),
            distractorSyllables = listOf("صَـ", "كْ")
        ),
        SyllableWordModel(
            id = 7,
            word = "كِتَاب",
            wordPhonetic = "كِـ - تَا - ب",
            emoji = "📚",
            category = "أدوات",
            correctSyllables = listOf("كِـ", "تَا", "ب"),
            distractorSyllables = listOf("قَـ", "رْ")
        ),
        SyllableWordModel(
            id = 8,
            word = "مَدْرَسَة",
            wordPhonetic = "مَـ - دْ - رَ - سَ - ة",
            emoji = "🏫",
            category = "أماكن",
            correctSyllables = listOf("مَـ", "دْ", "رَ", "سَـ", "ة"),
            distractorSyllables = listOf("فَـ", "لْ")
        ),
        SyllableWordModel(
            id = 9,
            word = "زَهْرَة",
            wordPhonetic = "زَ - هْ - رَ - ة",
            emoji = "🌸",
            category = "نباتات",
            correctSyllables = listOf("زَ", "هْ", "رَ", "ة"),
            distractorSyllables = listOf("عَـ", "نْ")
        ),
        SyllableWordModel(
            id = 10,
            word = "نَجْمَة",
            wordPhonetic = "نَـ - جْ - مَ - ة",
            emoji = "⭐",
            category = "أشكال",
            correctSyllables = listOf("نَـ", "جْ", "مَ", "ة"),
            distractorSyllables = listOf("دَ", "كْ")
        )
    )

    val quizQuestions = listOf(
        QuizQuestion.MissingLetter(
            id = 101,
            word = "أَ _ نَ ب",
            fullWord = "أَرْنَب",
            options = listOf("رْ", "مْ", "سْ"),
            correctIndex = 0,
            emoji = "🐇"
        ),
        QuizQuestion.MissingLetter(
            id = 102,
            word = "بَ _َ رَ ة",
            fullWord = "بَقَرَة",
            options = listOf("فَ", "قَـ", "سَ"),
            correctIndex = 1,
            emoji = "🐄"
        ),
        QuizQuestion.MissingLetter(
            id = 103,
            word = "سَ مَ _َ ة",
            fullWord = "سَمَكَة",
            options = listOf("لَ", "تَ", "كَـ"),
            correctIndex = 2,
            emoji = "🐟"
        ),
        QuizQuestion.SoundIdentification(
            id = 104,
            targetLetter = "ش",
            targetSound = "شَـ (شَمْس)",
            options = listOf("س", "ش", "ص", "ض"),
            correctIndex = 1
        ),
        QuizQuestion.SoundIdentification(
            id = 105,
            targetLetter = "ق",
            targetSound = "قِـ (قِطَّة)",
            options = listOf("ف", "ق", "ك", "ل"),
            correctIndex = 1
        ),
        QuizQuestion.MissingLetter(
            id = 106,
            word = "كِـ _َ ا ب",
            fullWord = "كِتَاب",
            options = listOf("تَا", "مَا", "سَا"),
            correctIndex = 0,
            emoji = "📚"
        )
    )
}
