package com.kdapps.githubaccess.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.adapters.myCommitListAdapter
import com.kdapps.githubaccess.utils.CommitEntity
import com.kdapps.githubaccess.utils.connectionManager
import kotlinx.android.synthetic.main.activity_add_repo.*
import kotlinx.android.synthetic.main.activity_commits.*
import java.util.HashMap

class CommitsActivity : AppCompatActivity() {

    lateinit var branch_name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commits)

        setupToolbar()
        branch_name = intent.getStringExtra("branch_name").toString()
        val sha = intent.getStringExtra("sha")
        val owner = intent.getStringExtra("owner")
        val repo_name = intent.getStringExtra("repo_name")

        if(connectionManager().checkConnectivity(this)){

            val commitList = ArrayList<CommitEntity>()
            val queue = Volley.newRequestQueue(this)
            val url = "https://api.github.com/repos/$owner/$repo_name/commits?sha=$sha"

            val jsonArrayRequest = object: JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    for(i in 0 until it.length()){
                        val jsonObject = it.getJSONObject(i)
                        val date = jsonObject.getJSONObject("commit").getJSONObject("committer").getString("date")
                        val user_name = jsonObject.getJSONObject("commit").getJSONObject("committer").getString("name")
                        val message = jsonObject.getJSONObject("commit").getString("message")
                        val avatar_url = jsonObject.getJSONObject("committer").getString("avatar_url")
                        val commitEntity = CommitEntity(sha.toString(), date, message, avatar_url, user_name)
                        commitList.add(commitEntity)

                    }
                    progressBar_commit.visibility = View.INVISIBLE
                    recycler_commit_list.apply {
                        adapter = myCommitListAdapter(this@CommitsActivity, commitList)
                        layoutManager = LinearLayoutManager(this@CommitsActivity)
                    }

                },
                Response.ErrorListener {
                    progressBar_commit.visibility = View.INVISIBLE
                    println("Error is $it")
                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/vnd.github.v3+json"
                    return headers
                }
            }
            queue.add(jsonArrayRequest)

        }else{
            progressBar_commit.visibility = View.INVISIBLE
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                this.finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }


    }

    fun setupToolbar(){
        setSupportActionBar(commit_toolbar)
        supportActionBar?.setTitle("Commits")
        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.gradient_background))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        if(id == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}