package com.example.valuematch.models

data class UserProfile(
val uid: String = "",
val name: String = "",
val age: Int = 0,
val bio: String = "",
val politicalValues: String = "",
val religiousValues: String = "",
val lifestyleValues: String = "",
val photoUrl: String = ""
)