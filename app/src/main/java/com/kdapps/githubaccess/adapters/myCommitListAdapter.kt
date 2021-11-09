package com.kdapps.githubaccess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdapps.githubaccess.R
import com.kdapps.githubaccess.utils.CommitEntity
import kotlinx.android.synthetic.main.commit_item_layout.view.*

class myCommitListAdapter(val context: Context, val commitList: ArrayList<CommitEntity>)
    :RecyclerView.Adapter<myCommitListAdapter.viewHolder>(){

    class viewHolder(view: View):RecyclerView.ViewHolder(view){
        val tv_date = view.tv_date
        val tv_sha_code = view.tv_sha_code
        val tv_message = view.tv_commit_message
        val tv_user_name = view.tv_user_name
        val avatar_img = view.img_avatar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commit_item_layout, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val date = commitList[position].date.substring(0, 9)
        val user_name = commitList[position].user_name
        val avatar_url = commitList[position].avatar_url
        val sha_code = commitList[position].sha.substring(0,6)
        val message = commitList[position].message

        holder.tv_date.setText(date)
        holder.tv_user_name.setText(user_name)
        holder.tv_sha_code.setText(sha_code)
        holder.tv_message.setText(message)

        Glide
            .with(context)
            .load(avatar_url)
            .centerCrop()
            .override(100,100)
            .into(holder.avatar_img)
    }

    override fun getItemCount(): Int {
        return commitList.size
    }
}