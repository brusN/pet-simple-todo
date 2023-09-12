package ru.nsu.brusn.smpltodo.adapter

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nsu.brusn.smpltodo.BuildConfig
import ru.nsu.brusn.smpltodo.R
import ru.nsu.brusn.smpltodo.activity.EditTaskAttributesActivity
import ru.nsu.brusn.smpltodo.api.APIHeadersProvider
import ru.nsu.brusn.smpltodo.api.adapter.ZonedDataTimeAdapter
import ru.nsu.brusn.smpltodo.api.model.dto.request.EditTaskRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse
import ru.nsu.brusn.smpltodo.api.model.task.TaskEntity
import ru.nsu.brusn.smpltodo.api.services.UserService
import ru.nsu.brusn.smpltodo.databinding.TaskItemBinding

class TasksListAdapter : RecyclerView.Adapter<TasksListAdapter.TasksHolder>() {
    private var taskList = ArrayList<TaskEntity>()
    private var jwt: String? = null

    inner class TasksHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = TaskItemBinding.bind(item)

        private fun updateTaskStatus(task: TaskEntity, taskCompleteStatus: Boolean) {
            val moshi = Moshi.Builder()
                .add(ZonedDataTimeAdapter())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
            val userService : UserService = retrofit.create(UserService::class.java)
            val requestBody = EditTaskRequest()
            requestBody.completed = taskCompleteStatus

            val headers = mapOf(APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(jwt))
            userService.editTask(headers, requestBody, task.id).enqueue(object: Callback<MessageResponse> {
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
                        task.completed = binding.cbTaskDone.isChecked
                        if (task.completed) binding.tvTaskName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        else binding.tvTaskName.paintFlags = 0
                    }
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e("DEBUG", t.toString())
                }
            })
        }

        fun bind(task: TaskEntity) = with(binding) {
            tvTaskName.text = task.name
            if (task.completed) tvTaskName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            cbTaskDone.isChecked = task.completed
            cbTaskDone.setOnClickListener {
                updateTaskStatus(task, cbTaskDone.isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TasksHolder(view)
    }

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        holder.bind(taskList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditTaskAttributesActivity::class.java)
            intent.putExtra("taskItem", taskList[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun setItemList(items: ArrayList<TaskEntity>) {
        taskList = items
        notifyDataSetChanged()
    }

    fun setJWT(jwt: String) {
        this.jwt = jwt
    }
}