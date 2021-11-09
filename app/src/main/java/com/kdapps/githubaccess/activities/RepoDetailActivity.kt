package com.kdapps.githubaccess.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.adapters.myBranchListAdapter
import com.kdapps.githubaccess.adapters.myIssuesListAdapter
import com.kdapps.githubaccess.database.DBAsyncTask
import com.kdapps.githubaccess.utils.BranchEntity
import com.kdapps.githubaccess.utils.IssueEntity
import com.kdapps.githubaccess.utils.RepoEntity
import com.kdapps.githubaccess.utils.connectionManager
import kotlinx.android.synthetic.main.activity_repo_detail.*
import java.util.HashMap

class RepoDetailActivity : AppCompatActivity() {

    lateinit var repoEntity: RepoEntity
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)

        setupToolbar()

        sharedPreferences = this.getSharedPreferences("repo_issues_count", MODE_PRIVATE)


        val id = intent.getStringExtra("id").toString()
        val owner =intent.getStringExtra("owner").toString()
        val repo_name = intent.getStringExtra("repo_name").toString()
        val repo_desc = intent.getStringExtra("repo_desc").toString()
        repoEntity = RepoEntity(id, owner, repo_name, repo_desc)
        val issues_count = sharedPreferences.getString("$owner/$repo_name", "0")


        tv_issue.setText("ISSUES($issues_count)")
        tv_detail_repo_name.setText(owner + "/" + repo_name)
        tv_detail_repo_desc.setText(repo_desc)
        tv_branch.isClickable()

        tv_branch.setOnClickListener(){
            recycler_repo_issues.visibility = View.INVISIBLE
            progressBar_repo_detail.visibility = View.VISIBLE
            tv_issue.setBackgroundColor(resources.getColor(R.color.white))
            tv_branch.background = resources.getDrawable(R.drawable.rounded_gradient_background)
            tv_branch.isSelected
            val branchList = arrayListOf<BranchEntity>()
            val queue = Volley.newRequestQueue(this)
            val url = "https://api.github.com/repos/$owner/$repo_name/branches"
            if(connectionManager().checkConnectivity(this)) {
                val jsonObjectRequest = object : JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener {
                        for( i in 0 until it.length()){
                            val jsonObject = it.getJSONObject(i)
                            val branch_name = jsonObject.getString("name")
                            val sha = jsonObject.getJSONObject("commit").getString("sha")

                            val branchEntity =BranchEntity(branch_name, sha)
                            branchList.add(branchEntity)
                            Log.e("branch", branch_name)
                        }
                        progressBar_repo_detail.visibility = View.INVISIBLE
                        recycler_repo_branches.apply {
                            adapter = myBranchListAdapter(this@RepoDetailActivity,branchList, repoEntity)
                            layoutManager = LinearLayoutManager(this@RepoDetailActivity)
                        }
                    },
                    Response.ErrorListener {
                        println("Error is $it")
                    }
                ){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/vnd.github.v3+json"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }
            else{
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

        tv_issue.setOnClickListener{
            recycler_repo_issues.visibility = View.INVISIBLE
            progressBar_repo_detail.visibility = View.VISIBLE
            tv_branch.setBackgroundColor(resources.getColor(R.color.white))
            tv_issue.background = resources.getDrawable(R.drawable.rounded_gradient_background)
            tv_issue.isSelected
            val issuesList = arrayListOf<IssueEntity>()
            val queue = Volley.newRequestQueue(this)
            val url = "https://api.github.com/repos/$owner/$repo_name/issues?state=open"

            val jsonObjectRequest = object : JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    for( i in 0 until it.length()){
                        val jsonObject = it.getJSONObject(i)
                        val issue_title = jsonObject.getString("title")
                        val creator_name = jsonObject.getJSONObject("user").getString("login")
                        val avatar_url = jsonObject.getJSONObject("user").getString("avatar_url")

                        val issueEntity = IssueEntity(issue_title, creator_name, avatar_url)
                        issuesList.add(issueEntity)
                    }
                    progressBar_repo_detail.visibility = View.INVISIBLE
                    recycler_repo_branches.apply {
                        adapter = myIssuesListAdapter(this@RepoDetailActivity,issuesList)
                        layoutManager = LinearLayoutManager(this@RepoDetailActivity)
                    }
                },
                Response.ErrorListener {
                    println("Error is $it")
                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/vnd.github.v3+json"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        }

    }


    fun setupToolbar(){
        setSupportActionBar(repo_detail_toolbar)
        supportActionBar?.setTitle("Details")
        supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.gradient_background))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.repo_detail_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =item.itemId
        when(id){
            android.R.id.home ->{
                onBackPressed()
            }

            R.id.btn_open_browser ->{
                val url = "https://github.com/${repoEntity.owner}/${repoEntity.repo_name}"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
            R.id.btn_delete_repo ->{
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Confirm")
                dialog.setMessage("Are you sure you want to delete this?")
                dialog.setPositiveButton("Delete"){text, listener ->
                    val delete = DBAsyncTask(this, repoEntity, 2).execute()
                    if(delete.get()){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "Please retry", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.setNegativeButton("Cancel"){text, listener ->

                }
                dialog.create()
                dialog.show()

            }
        }

        return super.onOptionsItemSelected(item)
    }
}