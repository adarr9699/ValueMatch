package com.example.valuematch.models

import android.hardware.biometrics.BiometricManager

data class MatchResult (
    val userId: String = "",
    val matchScore: Int = 0,
    val sharedValues: List<BiometricManager.Strings> = emptyList()
)