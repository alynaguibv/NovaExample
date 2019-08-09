package com.aly.nova.example.model

data class PasteBin(
    var categories: List<Category>,
    var color: String,
    var created_at: String,
    var current_user_collections: List<Any>,
    var height: Int,
    var id: String,
    var liked_by_user: Boolean,
    var likes: Int,
    var links: LinksXX,
    var urls: Urls,
    var user: User,
    var width: Int
)