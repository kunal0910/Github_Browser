package com.kdapps.githubaccess.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kdapps.githubaccess.utils.RepoEntity

@Dao
interface RepoDao {

    @Insert
    fun insertRepo(repoEntity: RepoEntity)

    @Delete
    fun deleteRepo(repoEntity: RepoEntity)

    @Query(" SELECT * FROM Repositories")
    fun getAllRepos(): List<RepoEntity>
}