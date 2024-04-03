package com.example.newsapp.presentation.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.domain.models.Articles
import com.example.newsapp.presentation.vm.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

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
                    Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
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

    private fun web() {
        val webUrl = arguments?.getString("url")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "no web browser app is available", Toast.LENGTH_SHORT).show()
        }
    }
}