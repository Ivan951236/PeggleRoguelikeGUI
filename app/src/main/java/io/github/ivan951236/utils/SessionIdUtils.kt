package io.github.ivan951236.utils

import kotlin.random.Random

object SessionIdUtils {
    
    private const val CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?"
    
    fun generateSessionId(): String {
        return buildString {
            repeat(128) {
                append(CHARACTERS[Random.nextInt(CHARACTERS.length)])
            }
        }
    }
}