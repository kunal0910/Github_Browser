package com.kdapps.githubaccess.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.activities.CommitsActivity
import com.kdapps.githubaccess.utils.BranchEntity
import com.kdapps.githubaccess.utils.RepoEntity
import kotlinx.android.synthetic.main.branch_list_item_layout.view.*

class myBranchListAdapter(val context: Context, val branchList: ArrayList<BranchEntity>, val repoEntity: RepoEntity):
    RecyclerView.Adapter<myBranchListAdapter.viewHolder>(){

    class viewHolder(view: View): RecyclerView.ViewHolder(view){
        val tv_branch_name = view.tv_branch_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.branch_list_item_layout, parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val branch_name = branchList[position].branch_name
        val sha = branchList[position].sha

        holder.tv_branch_name.setText(branch_name)

        holder.itemView.setOnClickListener(){
            val intent = Intent(context, CommitsActivity::class.java)
            intent.putExtra("branch_name", branch_name)
            intent.putExtra("owner", repoEntity.owner)
            intent.putExtra("repo_name", repoEntity.repo_name)
            intent.putExtra("sha", sha)
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return branchList.size
    }
}