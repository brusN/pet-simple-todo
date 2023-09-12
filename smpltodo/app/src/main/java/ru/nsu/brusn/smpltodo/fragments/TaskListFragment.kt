package ru.nsu.brusn.smpltodo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.moshi.Moshi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nsu.brusn.smpltodo.BuildConfig
import ru.nsu.brusn.smpltodo.R
import ru.nsu.brusn.smpltodo.adapter.TasksListAdapter
import ru.nsu.brusn.smpltodo.api.APIHeadersProvider
import ru.nsu.brusn.smpltodo.api.adapter.ZonedDataTimeAdapter
import ru.nsu.brusn.smpltodo.api.model.dto.request.CreateNewTaskRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.GetFolderTasksResponse
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse
import ru.nsu.brusn.smpltodo.api.model.task.TaskEntity
import ru.nsu.brusn.smpltodo.api.services.UserService
import ru.nsu.brusn.smpltodo.databinding.FragmentTaskListBinding

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private var folderId: Long = -1
    private val adapter = TasksListAdapter()
    private var jwt: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        folderId = requireArguments().getLong("folderId", -1)
        jwt =requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE).getString("jwt", null)

        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.rvFolderTasks.adapter = adapter
        adapter.setJWT(jwt!!)
        return binding.root
    }

    private fun getAllTasks() {
        val moshi = Moshi.Builder()
            .add(ZonedDataTimeAdapter())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val userService: UserService = retrofit.create(UserService::class.java)

        userService.getAllFolderTasks(
            mapOf(
                APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(
                    jwt.toString()
                )
            ), folderId
        ).enqueue(object : Callback<GetFolderTasksResponse> {
            override fun onResponse(call: Call<GetFolderTasksResponse>, response: Response<GetFolderTasksResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()!!
                    adapter.setItemList(body.data as ArrayList<TaskEntity>)
                }
                return
            }

            override fun onFailure(call: Call<GetFolderTasksResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
                return
            }
        })
    }

    private fun createNewTask(taskName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val userService: UserService = retrofit.create(UserService::class.java)
        val requestBody = CreateNewTaskRequest(folderId, taskName)
        val headers = mapOf(APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(jwt.toString()))

        userService.createNewTask(headers, requestBody).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.errorBody() != null) {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    Log.e("DEBUG", jsonObject.getJSONObject("error").getString("message"))
                    return
                }

                if (response.isSuccessful) {
                    getAllTasks()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
            }
        })
    }

    private fun showCreateNewTaskDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_create_new_task, null)
        alertDialogBuilder.setView(view)
        val dialog = alertDialogBuilder.create()

        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val etTaskName = view.findViewById<EditText>(R.id.etTaskName)

        btnSave.setOnClickListener {
            createNewTask(etTaskName.text.toString())
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun bindAddNewTaskButton() {
        binding.btnAddNewTask.setOnClickListener {
            showCreateNewTaskDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllTasks()
        bindAddNewTaskButton()
    }
}