package com.example.newsapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentBookmarksBinding
import com.example.newsapp.presentation.adapters.NewsAdapter

class BookmarksFragment : Fragment() {

    private val newsAdapter by lazy { NewsAdapter() }
    private val binding by lazy { FragmentBookmarksBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        newsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//                putSerializable("article", it)
//            }
//            findNavController().navigate(R.id.action_bookmarksFragment_to_articleFragment, bundle)
//        }
    }
}