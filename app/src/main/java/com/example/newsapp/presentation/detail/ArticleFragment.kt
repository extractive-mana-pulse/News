package com.example.newsapp.presentation.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.domain.model.Articles
import com.example.newsapp.presentation.FullScreenImageFragment
import com.example.newsapp.presentation.bookmarks.vm.ButtonState
import com.example.newsapp.presentation.bookmarks.vm.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private val databaseViewModel : DatabaseViewModel by viewModels()
    private val binding by lazy { FragmentArticleBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val author = arguments?.getString("author")
        val imageUrl = arguments?.getString("image")
        val description = arguments?.getString("post_description")

        binding.apply {

            articleTitle.text = title
            articleAuthor.text = author
            articleDescription.text = description
            Glide.with(requireContext()).load(imageUrl).into(imageArticle)

            articleBackBtn.setOnClickListener { findNavController().popBackStack() }

            shareIcon.setOnClickListener { share() }

            readMoreIcon.setOnClickListener { web() }

            saveIcon.setOnClickListener {
                try {
                    databaseViewModel.saveArticle(
                        Articles(
                            null,
                            title,
                            author,
                            description,
                            webUrl.toString(),
                            imageUrl
                    ))
                    lifecycleScope.launchWhenStarted {
                        databaseViewModel.stateFlow.collectLatest { state ->
                            updateButtonState(state)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Snackbar.make(view, "Error occur! ${e.message}", Snackbar.LENGTH_SHORT).show()
                }
            }

            imageArticle.setOnClickListener {

                val fullScreenImageFragment = FullScreenImageFragment()

                imageArticle.buildDrawingCache()
                val originalBitmap = imageArticle.drawingCache
                val image = originalBitmap.copy(originalBitmap.config, true)

                val extras = Bundle()
                extras.putParcelable("image", image)
                fullScreenImageFragment.arguments = extras

                fullScreenImageFragment.show(requireActivity().supportFragmentManager, "FullScreenImageFragment")
            }
        }
    }

    private fun share() {
        val url = arguments?.getString("url")
        Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, url).type = "text/plain"
        requireContext().startActivity(Intent.createChooser(Intent(),"Choose app:"))
    }

    private fun updateButtonState(state: ButtonState) {
        // Update the UI based on the button state
        when (state) {
            ButtonState.Static -> {
                binding.saveIcon.setImageResource(R.drawable.baseline_bookmark_add_24)
            }
            ButtonState.Added -> {
                binding.saveIcon.setImageResource(R.drawable.baseline_bookmark_added_24)
            }
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun web() {
        val webUrl = arguments?.getString("url")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Snackbar.make(requireView(), "no web browser app is available", Snackbar.LENGTH_SHORT).show()
        }
    }
}