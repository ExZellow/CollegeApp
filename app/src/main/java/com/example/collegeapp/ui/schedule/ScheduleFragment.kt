package com.example.collegeapp.ui.schedule

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room.databaseBuilder
import com.example.collegeapp.databinding.FragmentScheduleBinding
import com.example.collegeapp.ui.ScheduleAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.scheduleRecyclerView
        val inputGroup: TextView = binding.inputGroupNumber
        val emulatorBaseUrl = "http://10.0.2.2:80/"
        val phoneBaseUrl = "http://192.168.1.5:80/"

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)


        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(phoneBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()

        val scheduleJson = retrofit.create(ParserResponse::class.java)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        val db = databaseBuilder(requireContext(), ScheduleDB.DB::class.java, "Schedule").build()

        val dao = db.getDao()

        val gson = Gson()

        binding.button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                scheduleJson.getScheduleJson().enqueue(object :
                    Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        val res = response.body()
                        //val obj = JsonParser.parseString(res.toString())
                        // JsonObject(res)
                        if (res != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                //dao.deleteData()
                                val jsonResponse: Map<String, Map<String, String?>> =
                                    gson.fromJson(
                                        res,
                                        object :
                                            TypeToken<Map<String, Map<String, String?>>>() {}.type
                                    )
                                val scheduleRoom =
                                    ScheduleMapper().mapFromGsonResultToSchedule(
                                        jsonResponse
                                    )
                                CoroutineScope(Dispatchers.Main).launch {
                                    binding.scheduleRecyclerView.adapter =
                                        ScheduleAdapter().apply {
                                            setGroupSchedule(inputGroup.text.toString(), scheduleRoom)
                                        }
                                }

                                //dao.insertData(scheduleRoom) //TODO: get json from a server
                                println(scheduleRoom)

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
                        call: Call<String>,
                        t: Throwable
                    ) {
                        Snackbar.make(
                            recyclerView,
                            t.localizedMessage!!,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
        return root
    }
}


