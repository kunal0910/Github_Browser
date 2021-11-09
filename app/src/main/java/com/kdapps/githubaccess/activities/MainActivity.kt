package com.kdapps.githubaccess.activities

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.adapters.myRepoListAdapter
import com.kdapps.githubaccess.database.DBAsyncTask
import com.kdapps.githubaccess.database.RepoDatabase
import com.kdapps.githubaccess.utils.RepoEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        //Access repo from the database
        val repoList = getAddedRepos(this).execute().get()
        Log.e("list", repoList.toString())
        if(repoList.size != 0 ){
            tv_add_repo.visibility = View.INVISIBLE
            btn_add_repo.visibility = View.INVISIBLE
        }else{
            tv_add_repo.visibility = View.VISIBLE
            btn_add_repo.visibility = View.VISIBLE
        }
        btn_add_repo.setOnClickListener{
            val intent = Intent(this@MainActivity, AddRepoActivity::class.java)
            startActivity(intent)
        }

        recycler_repo_list.apply {
            adapter = myRepoListAdapter(this@MainActivity,repoList)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }
    fun setupToolbar(){
        setSupportActionBar(main_toolbar)
        supportActionBar?.setTitle("Github Access")
        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.gradient_background))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    class getAddedRepos(val context: Context): AsyncTask<Void, Void, List<RepoEntity>>() {

        val db = Room.databaseBuilder(context, RepoDatabase::class.java, "repo-db").build()
        override fun doInBackground(vararg p0: Void?): List<RepoEntity> {
            return db.repoDao().getAllRepos()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.icon_add_repo){
            val intent = Intent(this@MainActivity, AddRepoActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}