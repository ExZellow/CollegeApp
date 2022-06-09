package com.example.collegeapp.ui.schedule

import android.R.string
import android.graphics.ColorSpace
import android.os.Environment
//import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File

/*
class ScheduleParser {

    val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    val downloadedPath = downloadPath + "/schedule.pdf"


    fun loadSchedule() {//}: String {//List<String> {
        val downloaded = File(downloadedPath)
        val doc = PDDocument.load(downloaded)

        //val text = stripper.getText(doc)
        //return text



        val printer = PrintImageLocations()
        var pageNum = 0
        for (page in doc.pages) {
            pageNum++
            println("Processing page: $pageNum")
            printer.processPage(page)
        }


        /*val stripper: PDFTextStripper = PrintTextLocations()//PDFTableStripper()
        stripper.sortByPosition = true
        stripper.startPage = 0
        stripper.endPage = doc.numberOfPages
        stripper.sortByPosition = true

        val dummy: Writer = OutputStreamWriter(ByteArrayOutputStream())
        //stripper.textLineMatrix
        val text = stripper.getText(doc)

        stripper.writeText(doc, dummy)
*/
        //flatMap
        //val list = text.reader().readLines()

        //return printTextLocations//list
    }

}
*/