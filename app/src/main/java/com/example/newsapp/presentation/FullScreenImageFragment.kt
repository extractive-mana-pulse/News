package com.example.newsapp.presentation

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.newsapp.databinding.FragmentFullScreenImageBinding

class FullScreenImageFragment : DialogFragment() {

    private val binding by lazy { FragmentFullScreenImageBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val extras = arguments
            val bmp = extras?.getParcelable<Bitmap>("image")
            imageFullScreen.setImageBitmap(bmp)
        }
    }
}