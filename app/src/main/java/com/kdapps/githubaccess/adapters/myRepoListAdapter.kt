package com.kdapps.githubaccess.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.recyclerview.widget.RecyclerView
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.activities.RepoDetailActivity
import com.kdapps.githubaccess.utils.RepoEntity
import kotlinx.android.synthetic.main.repo_list_item_layout.view.*

class myRepoListAdapter(val context: Context,val repoList:List<RepoEntity>):RecyclerView.Adapter<myRepoListAdapter.viewHolder>() {

    class viewHolder(view: View):RecyclerView.ViewHolder(view){
        val tv_repo_name = view.tv_repo_name
        val tv_repo_desc = view.tv_repo_description
        val btn_share = view.btn_share
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.repo_list_item_layout, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val id = repoList[position].id
        val owner = repoList[position].owner
        val repo_name = repoList[position].repo_name
        val repo_desc = repoList[position].repo_desc

        holder.tv_repo_name.setText(owner + "/" + repo_name)
        if(repo_desc == "null"){
            holder.tv_repo_desc.setText("No description")
        }
        else{
            holder.tv_repo_desc.setText(repo_desc)
        }

        holder.itemView.setOnClickListener(){
            val intent = Intent(context, RepoDetailActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("owner", owner)
            intent.putExtra("repo_name", repo_name)
            intent.putExtra("repo_desc", repo_desc)
            startActivity(context, intent, null)
        }

        holder.btn_share.setOnClickListener(){
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "A github repository is shared to you from Github Access. Repo name: $owner/$repo_name. Description: $repo_desc. Link: https://github.com/$owner/$repo_name" )
                type = "text/plain"
            }
            startActivity(context,Intent.createChooser(sendIntent, "Send Repo"), null)
            //startActivity(context, shareIntent, null)
        }

    }

    override fun getItemCount(): Int {
        return repoList.size
    }

}