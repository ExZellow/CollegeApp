package com.example.collegeapp.ui.news

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.collegeapp.ui.news.Utilities.setUrl
import kotlinx.coroutines.Dispatchers


object Utilities {

    @JvmStatic
    fun WebView.setUrl(url: String) {
        this.settings.userAgentString = this.settings.userAgentString + "app/mgkit"
        this.loadUrl(url)
    }
}

class NewsViewModel : ViewModel() {

    //private val newsParser = NewsParser()

    private val _text: LiveData<String> = liveData(Dispatchers.IO) {
        //val data = newsParser.getResult()
        //emit(data)
    }

    private val webViewUrl = MutableLiveData<String>().apply{ value = "https://www.mgkit.ru/assignments" }
    val url: LiveData<String> = webViewUrl
    val text: LiveData<String> = _text

}