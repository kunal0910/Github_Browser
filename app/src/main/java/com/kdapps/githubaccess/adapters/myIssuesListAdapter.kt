package com.kdapps.githubaccess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.utils.IssueEntity
import kotlinx.android.synthetic.main.issues_list_layout.view.*

class myIssuesListAdapter(val context: Context ,val issuesList: ArrayList<IssueEntity>):RecyclerView.Adapter<myIssuesListAdapter.viewHolder>() {

    class viewHolder(view: View):RecyclerView.ViewHolder(view){
        val tv_issue_title = view.tv_issue_title
        val tv_issue_creator = view.tv_issue_creator_name
        val img_avatar = view.profile_issue_creator
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.issues_list_layout, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val title = issuesList[position].issue_title
        val creator = issuesList[position].issue_creator_name
        val avatar_url = issuesList[position].avatar_issue_creator_url

        holder.tv_issue_title.setText(title)
        holder.tv_issue_creator.setText(creator)
        Glide
            .with(context)
            .load(avatar_url)
            .centerCrop()
            .override(100,100)
            .into(holder.img_avatar)
    }

    override fun getItemCount(): Int {
        return issuesList.size
    }
}