package com.kdapps.githubaccess.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.kdapps.githubaccess.utils.RepoEntity

class DBAsyncTask(val context: Context, val repoEntity: RepoEntity, val mode: Int)
    :AsyncTask<Void, Void, Boolean>(){

    val db = Room.databaseBuilder(context, RepoDatabase::class.java, "repo-db").build()
    override fun doInBackground(vararg p0: Void?): Boolean {
        when(mode){
            1 ->{
                db.repoDao().insertRepo(repoEntity)
                db.close()
                return true
            }
            2 ->{
                db.repoDao().deleteRepo(repoEntity)
                db.close()
                return true
            }

        }
        return false
    }

}