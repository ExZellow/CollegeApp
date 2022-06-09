package com.example.collegeapp.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collegeapp.databinding.FragmentNewsBinding
import com.example.collegeapp.ui.news.Utilities.setUrl

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        val newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        val root: View = binding.root

        val webView = binding.newsWebView

        newsViewModel.url.observe(viewLifecycleOwner) {
            webView.setUrl(it)
        }

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}