package com.example.collegeapp.ui.schedule

/*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

import android.os.Environment


import android.animation.LayoutTransition
import android.net.Uri

import android.webkit.MimeTypeMap

import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
*/
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
//import com.example.collegeapp.BuildConfig
//import com.example.collegeapp.ui.schedule.DownloadResult
import com.example.collegeapp.databinding.FragmentScheduleBinding
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

/*import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
*/

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private lateinit var scheduleViewModel: ScheduleViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    /*
    private val PERMISSIONS = listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_REQUEST_CODE = 1
    private val DOWNLOAD_FILE_CODE = 2
    */
    private val base = "https://drive.google.com/uc?export=download&id="
    private val fileUrl = "https://drive.google.com/file/d/1JViBKaRX2tA9DnFX294_OOkNgvEKOOc2/view"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSchedule

        setDownloadButtonClickListener()
        /*if (hasPermissions(context, PERMISSIONS)) {
            setDownloadButtonClickListener()
        } else {
            requestPermissions(PERMISSIONS.toTypedArray(), PERMISSION_REQUEST_CODE)
        }*/

        /*scheduleViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root

    }
    private fun downloadFile(url: URL, outputFileName: String) {
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(outputFileName).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
    }

    private fun buildDirectUrl(url: String): String
    {
        val splitted = url.split("/")
        return base + splitted[5]
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setDownloadButtonClickListener() {
        /*val folder = context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "kitten_in_a_cup.png"
        print("called")
        val file = File(folder, fileName)
        val uri = context?.let {
            FileProvider.getUriForFile(it, "${BuildConfig.APPLICATION_ID}.provider", file)
        }
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri?.path)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)*/

        binding.button.setOnClickListener {
            /*val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.setDataAndType(uri, mimeType)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(Intent.EXTRA_TITLE, fileName)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, DOWNLOAD_FILE_CODE)*/
            val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            val downloadedPath = downloadPath + "/kek.pdf"
            val url = URL(buildDirectUrl(fileUrl))

            GlobalScope.launch(Dispatchers.IO)
            {
                downloadFile(url, downloadedPath)
            }
            val downloaded = File(downloadedPath)
            val doc = PDDocument.load(downloaded)
            val stripper = PDFTextStripper()
            val text = stripper.getText(doc)
            //etc..
        }
    }

    /*private fun downloadFile(context: Context, url: String, file: Uri) {
        val ktor = HttpClient(Android)

        scheduleViewModel.setDownloading(true)
        context.contentResolver.openOutputStream(file)?.let { outputStream ->
            CoroutineScope(Dispatchers.IO).launch {
                HttpClient(Android).downloadFile(outputStream, url).collect {
                    withContext(Dispatchers.Main) {
                        when (it) {
                            is DownloadResult.Success -> {
                                scheduleViewModel.setDownloading(false)
                                //binding.progressBar.progress = 0
                                viewFile(file)
                            }

                            is DownloadResult.Error -> {
                                scheduleViewModel.setDownloading(false)
                                Toast.makeText(
                                    context,
                                    "Error while downloading file",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            /*is DownloadResult.Progress -> {
                                binding.progressBar.progress = it.progress
                            }*/
                        }
                    }
                }
            }
        }
    }

    private fun viewFile(uri: Uri) {
        context?.let { context ->
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val chooser = Intent.createChooser(intent, "Open with")

            if (intent.resolveActivity(context.packageManager) != null) {
                startActivity(chooser)
            } else {
                Toast.makeText(context, "No suitable application to open file", Toast.LENGTH_LONG).show()
            }
        }
    }*/

    /*private fun hasPermissions(context: Context?, permissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            return permissions.all { permission ->
                ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && hasPermissions(context, PERMISSIONS)) {
            setDownloadButtonClickListener()
        }
    }
    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DOWNLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                context?.let { context ->
                    downloadFile(context, fileUrl, uri)
                }
            }
        }
    }*/*/
}


