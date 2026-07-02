package com.example.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AchievementManager {
    private var sharedPrefs: android.content.SharedPreferences? = null

    // Reactive states backed by SharedPreferences
    var exploredShapes by mutableStateOf(emptySet<String>())
        private set

    var flippedCards by mutableStateOf(emptySet<Int>())
        private set

    var quizHighScore by mutableStateOf(0)
        private set

    fun init(context: Context) {
        val prefs = context.applicationContext.getSharedPreferences("geokids_rewards", Context.MODE_PRIVATE)
        sharedPrefs = prefs
        exploredShapes = prefs.getStringSet("explored_shapes", emptySet()) ?: emptySet()
        flippedCards = (prefs.getStringSet("flipped_cards", emptySet()) ?: emptySet())
            .mapNotNull { it.toIntOrNull() }.toSet()
        quizHighScore = prefs.getInt("quiz_high_score", 0)
    }

    fun exploreShape(shapeId: String) {
        if (shapeId !in exploredShapes) {
            val updated = exploredShapes + shapeId
            exploredShapes = updated
            sharedPrefs?.edit()?.putStringSet("explored_shapes", updated)?.apply()
        }
    }

    fun flipCard(cardId: Int) {
        if (cardId !in flippedCards) {
            val updated = flippedCards + cardId
            flippedCards = updated
            sharedPrefs?.edit()?.putStringSet("flipped_cards", updated.map { it.toString() }.toSet())?.apply()
        }
    }

    fun submitQuizScore(score: Int) {
        if (score > quizHighScore) {
            quizHighScore = score
            sharedPrefs?.edit()?.putInt("quiz_high_score", score)?.apply()
        }
    }

    fun resetAchievements() {
        exploredShapes = emptySet()
        flippedCards = emptySet()
        quizHighScore = 0
        sharedPrefs?.edit()?.clear()?.apply()
    }

    // Achievement checklist helpers
    fun hasExploredAll(): Boolean = exploredShapes.size >= 3 // Cube, Rectangular Prism, Pyramid
    fun hasFlippedAll(): Boolean = flippedCards.size >= 5 // All 5 flashcards
    fun hasHighQuizScore(): Boolean = quizHighScore >= 4
    fun hasPerfectQuizScore(): Boolean = quizHighScore == 5
}
