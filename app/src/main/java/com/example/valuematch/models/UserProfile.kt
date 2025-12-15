package com.example.valuematch.models

import android.hardware.biometrics.BiometricManager

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val values: List<BiometricManager.Strings> = emptyList(),
    val age: Int = 0,
    val bio: String = ""
)