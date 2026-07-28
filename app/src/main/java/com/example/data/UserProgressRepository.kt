package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserProgress(
    val totalStars: Int = 12,
    val wordsMastered: Int = 5,
    val quizzesCompleted: Int = 3,
    val streakDays: Int = 1,
    val unlockedBadges: List<String> = listOf("بَطَل الحُرُوف", "مُكْتَشِف الكَلِمَات")
)

object UserProgressRepository {

    private val _progress = MutableStateFlow(UserProgress())
    val progress: StateFlow<UserProgress> = _progress.asStateFlow()

    fun addStars(amount: Int) {
        val current = _progress.value
        _progress.value = current.copy(
            totalStars = current.totalStars + amount
        )
    }

    fun incrementWordsMastered() {
        val current = _progress.value
        _progress.value = current.copy(
            wordsMastered = current.wordsMastered + 1,
            totalStars = current.totalStars + 3
        )
    }

    fun incrementQuizzesCompleted() {
        val current = _progress.value
        _progress.value = current.copy(
            quizzesCompleted = current.quizzesCompleted + 1,
            totalStars = current.totalStars + 5
        )
    }
}
