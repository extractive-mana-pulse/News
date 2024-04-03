package com.example.newsapp.presentation.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.models.Articles
import com.example.newsapp.databinding.RcViewUiBinding

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.MyViewHolder>(){

    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = RcViewUiBinding.bind(item)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Articles>(){

        override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_view_ui, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("DiscouragedPrivateApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = differ.currentList[position]
        val context = holder.itemView.context

        holder.binding.apply {
            data.apply {

                Glide.with(context)
                    .load(urlToImage)
                    .error(R.drawable.not_found)
                    .centerCrop()
                    .into(loadImage)

                authorTv.text = author

                val updatedList = differ.currentList.toMutableList()
                val itemToRemove = updatedList.firstOrNull { it.title == "[Removed]" }

                if (itemToRemove != null) {
                    updatedList.remove(itemToRemove)
                    differ.submitList(updatedList)
                } else {
                    titleTv.text = title
                }

                if (description.isNullOrBlank()) {
                    descriptionTv.visibility = View.GONE
                } else {
                    descriptionTv.text = description
                }

                holder.itemView.setOnClickListener {

                    val bundle = Bundle().apply {
                        putString("title", title)
                        putString("author", author)
                        putString("url", url)
                        putString("image", urlToImage)
                        putString("post_description", description)
                    }

                    val navController = Navigation.findNavController(holder.itemView)
                    navController.navigate(R.id.articleFragment, bundle)
                }
            }
        }
    }

    private var onItemSelectedListener: ((Articles)-> Unit)? = null

    fun setOnItemClickListener(listener: (Articles) -> Unit) {
        onItemSelectedListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}