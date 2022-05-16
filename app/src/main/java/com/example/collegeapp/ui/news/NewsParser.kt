package com.example.collegeapp.ui.news

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class NewsParser {

    val newsUrl = "https://www.mgkit.ru/assignments"
    var response: String = ""
    lateinit var doc: Document


    fun getResult(): String {
        doc = Jsoup.connect(newsUrl).get()
        response = doc.body().html()

        return Jsoup.parse(response).text()
    }

    fun processResponse() {

    }

}