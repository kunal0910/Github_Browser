package com.kdapps.githubaccess.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.database.DBAsyncTask
import com.kdapps.githubaccess.utils.RepoEntity
import com.kdapps.githubaccess.utils.connectionManager
import kotlinx.android.synthetic.main.activity_add_repo.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.HashMap

class AddRepoActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_repo)

        setupToolbar()
        sharedPreferences = this.getSharedPreferences("repo_issues_count", MODE_PRIVATE)
        val queue = Volley.newRequestQueue(this)


        btn_search_add_repo.setOnClickListener{
            progressBar_add.visibility = View.VISIBLE
            val owner_name = et_owner_name.text
            val repo_name = et_repo_name.text
            val url ="https://api.github.com/repos/$owner_name/$repo_name"
            if (connectionManager().checkConnectivity(this)) {

                val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener {
                        val id = it.getString("id")
                        if(id != null){
                            Toast.makeText(this, "Repository FOUND", Toast.LENGTH_SHORT).show()
                            val repo_name = it.getString("name")
                            val owner_name = it.getJSONObject("owner").getString("login")
                            val repo_desc = it.getString("description")
                            val open_issues_count = it.getString("open_issues_count")
                            sharedPreferences.edit().putString("$owner_name/$repo_name",open_issues_count).apply()

                            val repoEntity = RepoEntity(id,owner_name, repo_name, repo_desc)
                            val checkAdded = DBAsyncTask(this@AddRepoActivity, repoEntity, 1).execute()
                            if(checkAdded.get()){
                                progressBar_add.visibility = View.VISIBLE
                                Toast.makeText(this@AddRepoActivity, "Repo ADDED", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@AddRepoActivity, MainActivity::class.java)
                                startActivity(intent)
                            }else{
                                progressBar_add.visibility = View.VISIBLE
                                Toast.makeText(this@AddRepoActivity, "Repo NOT ADDED", Toast.LENGTH_LONG).show()
                            }

                        }else{
                            progressBar_add.visibility = View.VISIBLE
                            Toast.makeText(this, "Repository NOT FOUND", Toast.LENGTH_SHORT).show()
                        }

                    },
                    Response.ErrorListener {
                        println("Error is $it")
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/vnd.github.v3+json"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } else {
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
    }

    fun setupToolbar(){
        setSupportActionBar(add_repo_toolbar)
        supportActionBar?.setTitle("Add Repo")
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