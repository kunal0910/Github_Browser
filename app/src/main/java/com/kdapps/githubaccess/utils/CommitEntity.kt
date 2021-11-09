package com.kdapps.githubaccess.utils

data class CommitEntity(
    val sha:String,
    val date:String,
    val message: String,
    val avatar_url:String,
    val user_name: String
)
