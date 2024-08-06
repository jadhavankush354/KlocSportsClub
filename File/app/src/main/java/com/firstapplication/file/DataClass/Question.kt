package com.firstapplication.file.DataClass
data class Question(
    val id:String,
    val userName:String,
    val comment: String,
    val replies: List<Replies>
)