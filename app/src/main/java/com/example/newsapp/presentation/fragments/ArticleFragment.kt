package com.example.newsapp.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private val binding by lazy { FragmentArticleBinding.inflate(layoutInflater)}

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

        val intent = Intent(Intent.ACTION_SEND)
        val url = arguments?.getString("url")
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent.type = "text/plain"

        requireContext().startActivity(Intent.createChooser(intent,"Choose app:"))
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