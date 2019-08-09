package com.aly.nova.example.model

data class User(
    val id: String,
    val links: Links,
    val name: String,
    val profile_image: ProfileImage,
    val username: String
)