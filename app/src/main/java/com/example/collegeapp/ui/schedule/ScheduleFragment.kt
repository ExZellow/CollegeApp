package com.example.collegeapp.ui.schedule

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.text.BoringLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
//import com.example.collegeapp.BuildConfig
//import com.example.collegeapp.ui.schedule.DownloadResult
import com.example.collegeapp.databinding.FragmentScheduleBinding
import com.google.android.material.snackbar.Snackbar
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private lateinit var scheduleViewModel: ScheduleViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSchedule

        var pseudoParsedSchedule = ""

        binding.button.setOnClickListener {

            if (checkReadingPermission()) {
                view?.let { Snackbar.make(it, "Permission granted", Snackbar.LENGTH_LONG).show() }

                CoroutineScope(Dispatchers.IO).launch {

                    val flag = scheduleViewModel.downloader.downloadSchedule()
                    if (flag) {
                        Snackbar.make(textView, "Download finished", Snackbar.LENGTH_LONG).show()

                        scheduleViewModel.parsedURL.loadSchedule()
                    } else Snackbar.make(textView, "Download failed", Snackbar.LENGTH_LONG).show()
                }
            }
            else {
                view?.let { Snackbar.make(it, "Permission denied", Snackbar.LENGTH_LONG).show() }
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE))
            }
        }

        scheduleViewModel.downloading.observe(viewLifecycleOwner) {

            textView.text = pseudoParsedSchedule//it.toString()
        }

        return root

    }



    private fun checkReadingPermission(): Boolean = ContextCompat.checkSelfPermission(requireContext(),
                                                                              Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var permissionGranted = false

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissionGranted = if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true ||
                permissions[Manifest.permission.MANAGE_EXTERNAL_STORAGE] == true) {

                view?.let { Snackbar.make(it, "Permission granted", Snackbar.LENGTH_LONG).show() }
                true

            } else {
                view?.let { Snackbar.make(it, "Permission denied", Snackbar.LENGTH_LONG).show() }
                false
                //requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}


