package com.kdapps.githubaccess.utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Repositories")
data class RepoEntity(
    @PrimaryKey val id: String,
    @ColumnInfo (name = "owner") val owner: String,
    @ColumnInfo (name = "repo_name") val repo_name: String,
    @ColumnInfo (name = "repo_desc") val repo_desc: String
)
