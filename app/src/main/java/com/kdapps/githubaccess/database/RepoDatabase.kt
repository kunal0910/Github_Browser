package com.kdapps.githubaccess.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kdapps.githubaccess.utils.RepoEntity

@Database(entities = arrayOf(RepoEntity::class), version = 1)
abstract class RepoDatabase: RoomDatabase() {
    abstract fun repoDao(): RepoDao

}