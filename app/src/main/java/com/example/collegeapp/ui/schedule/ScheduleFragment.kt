package com.example.collegeapp.ui.schedule

//import com.example.collegeapp.BuildConfig
//import com.example.collegeapp.ui.schedule.DownloadResult
/*import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
 */

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room.databaseBuilder
import com.example.collegeapp.databinding.FragmentScheduleBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory


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

        val recyclerView = binding.scheduleRecyclerView

        val scheduleTextView: TextView = binding.textSchedule
        /*val groupTextView: TextView = binding.textGroup
        val lessonNumberTextView: TextView = binding.textLessonNumber
        val subjectTextView: TextView = binding.textSubject
        val teacherTextView: TextView = binding.textTeacher
        val classroomTextView: TextView = binding.textClassroom*/
        val emulatorBaseUrl = "http://10.0.2.2:80/"
        val phoneBaseUrl = "http://192.168.1.5:80/"

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)


        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(phoneBaseUrl)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()

        val scheduleJson = retrofit.create(Retrofit.ParserResponse::class.java)

        lateinit var adapter: ScheduleJsonAdapter

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        val db = databaseBuilder(requireContext(), ScheduleDB.DB::class.java, "Schedule").build()

        val dao = db.getDao()

        CoroutineScope(Dispatchers.IO).launch {
            val scheduleMap: Map<String, LessonsJson> = mapOf()
            //adapter = ScheduleJsonAdapter(dao.getData().map {
            //    Retrofit.ScheduleJson(it.group, it.lessons)
            //}
                /*Retrofit.Schedule().apply {
                    group = it.group
                    lessons.apply {
                        lessons[id].lessonNumber = it.lessons[id].lessonNumber
                        lessons[id].subject = it.lessons[id].subject
                        lessons[id].teacher = it.lessons[id].teacher
                        lessons[id].classroom = it.lessons[id].classroom

                    }
                }*/
            //)

            launch(Dispatchers.Main) {
                recyclerView.adapter = adapter
            }
        }

        var pseudoParsedSchedule = ""
        val gson = Gson()

        binding.button.setOnClickListener {

            if (checkReadingPermission()) {
                view?.let { Snackbar.make(it, "Permission granted", Snackbar.LENGTH_LONG).show() }

                CoroutineScope(Dispatchers.IO).launch {



                    val flag = true//scheduleViewModel.downloader.downloadSchedule()
                    if (flag) {
                        Snackbar.make(scheduleTextView, "Download finished", Snackbar.LENGTH_LONG).show()

                        scheduleJson.getScheduleJson().enqueue(object:
                            Callback<Retrofit.ScheduleResponse> {
                            override fun onResponse(
                                call: Call<Retrofit.ScheduleResponse>,
                                response: Response<Retrofit.ScheduleResponse>
                            ) {
                                val res = response.body()
                                //val obj = JsonParser.parseString(res.toString())
                                // JsonObject(res)
                                if (res != null) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        dao.deleteData()
                                        val scheduleRoom = gson.fromJson(res.getSchedule().toString(), Retrofit.ScheduleResponse::class.java)
                                        dao.insertData(scheduleRoom)
                                        /*dao.insertData(res.map {
                                            ScheduleDB.ScheduleRoom(
                                                it.group,
                                                listOf(it.lessons!!)
                                            )
                                        })*/
                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<Retrofit.ScheduleResponse>,
                                t: Throwable
                            ) {
                                Snackbar.make(scheduleTextView, t.localizedMessage!!, Snackbar.LENGTH_LONG).show()

                            }
                            })
                        //scheduleViewModel.parsedURL.loadSchedule()
                    } else Snackbar.make(scheduleTextView, "Download failed", Snackbar.LENGTH_LONG).show()
                }
            }
            else {
                view?.let { Snackbar.make(it, "Permission denied", Snackbar.LENGTH_LONG).show() }
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE))
            }
        }

        //ViewModelProvider(this)[scheduleViewModel::class.java].getResult()
        //ViewModelProvider(this)[scheduleViewModel::class.java].a.observe(viewLifecycleOwner) {

            //textView.text = pseudoParsedSchedule//it.toString()
            //val textExtract = TextExtract()
            //val result = textExtract.getJson()
            //binding.apply {
                /*groupTextView.text = result?.group?.first().toString()
                lessonNumberTextView.text = result?.lessons?.first()?.lessonNumber.toString()
                subjectTextView.text = result?.lessons?.first()?.subject
                teacherTextView.text = result?.lessons?.first()?.teacher
                classroomTextView.text = result?.lessons?.first()?.classroom*/
            //}
            //lessonNumberTextView =
        //}

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


