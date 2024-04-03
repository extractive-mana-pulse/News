package com.example.newsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.presentation.adapters.NewsAdapter
import com.example.newsapp.presentation.sealed.Resource
import com.example.newsapp.presentation.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val newsAdapter by lazy { NewsAdapter() }
    private val viewModel : MainViewModel by viewModels()
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            rcView.adapter = newsAdapter
            rcView.layoutManager = LinearLayoutManager(requireContext())
//            rcView.addItemDecoration(DividerItemDecoration(rcView.context, DividerItemDecoration.VERTICAL))

            refreshLayout.setOnRefreshListener {
                // TODO when user swipe down update page with new data. new data can be regarding when it's published(fresh one!)
                //  or depends on other conditions!
                refreshLayout.isRefreshing = false
            }

            viewModel.tryResponse.observe(viewLifecycleOwner) { response ->
                when(response) {

                    is Resource.Success -> {
                        response.data?.let { newsResponse ->
                            newsAdapter.differ.submitList(newsResponse.articles)
                        }
                    }
                    is  Resource.Loading -> {

                    }

                    is Resource.Error -> {
                        response.message?.let {
                            Toast.makeText(requireContext(), "error: $it", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

//            newsAdapter.setOnItemClickListener {
//                val bundle = Bundle().apply {
//                    putSerializable("article", it)
//                }
//                findNavController().navigate(R.id.action_homeFragment_to_articleFragment, bundle)
//            }
        }
    }
}
