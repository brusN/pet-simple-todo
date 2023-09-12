package ru.nsu.brusn.smpltodo.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nsu.brusn.smpltodo.BuildConfig
import ru.nsu.brusn.smpltodo.R
import ru.nsu.brusn.smpltodo.api.APIHeadersProvider
import ru.nsu.brusn.smpltodo.api.adapter.ZonedDataTimeAdapter
import ru.nsu.brusn.smpltodo.api.model.dto.request.EditTaskRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse
import ru.nsu.brusn.smpltodo.api.model.task.TaskEntity
import ru.nsu.brusn.smpltodo.api.services.UserService
import ru.nsu.brusn.smpltodo.databinding.ActivityEditTaskAttributesBinding
import ru.nsu.brusn.smpltodo.other.DateTimePickerMode
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EditTaskAttributesActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityEditTaskAttributesBinding
    private lateinit var taskAttributes: TaskEntity
    private val calendar = Calendar.getInstance()
    private var timezone = TimeZone.getTimeZone("UTC+07:00")
    private val formatter = SimpleDateFormat("MMMM d, yyyy HH:mm", Locale.US)
    private val zonedDateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm")
    private lateinit var pickerMode: DateTimePickerMode
    private var jwt: String? = null

    private fun sendUpdatedTaskAttributes() {
        val moshi = Moshi.Builder()
            .add(ZonedDataTimeAdapter())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val userService : UserService = retrofit.create(UserService::class.java)
        val requestBody = EditTaskRequest()

        requestBody.name = taskAttributes.name
        requestBody.completed = taskAttributes.completed
        requestBody.startDate = taskAttributes.startDate
        requestBody.deadline = taskAttributes.deadline
        requestBody.important = taskAttributes.important
        requestBody.description = taskAttributes.description

        val headers = mapOf(APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(jwt))
        userService.editTask(headers, requestBody, taskAttributes.id).enqueue(object:
            Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.errorBody() != null) {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    val errMessage = jsonObject.getJSONObject("error").getString("message")
                    Toast.makeText(this@EditTaskAttributesActivity, errMessage, Toast.LENGTH_SHORT).show()
                    return
                }

                if (response.isSuccessful) {
                    finish()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
            }
        })
    }

    private fun deleteTask() {
        val moshi = Moshi.Builder()
            .add(ZonedDataTimeAdapter())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val userService : UserService = retrofit.create(UserService::class.java)

        val headers = mapOf(APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(jwt))
        userService.deleteTask(headers, taskAttributes.id).enqueue(object:
            Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.errorBody() != null) {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    val errMessage = jsonObject.getJSONObject("error").getString("message")
                    Toast.makeText(this@EditTaskAttributesActivity, errMessage, Toast.LENGTH_SHORT).show()
                    return
                }

                if (response.isSuccessful) {
                    finish()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
            }
        })
    }

    private fun showAlertDialogDeleteTask() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_delete_task, null)
        alertDialogBuilder.setView(view)
        val dialog = alertDialogBuilder.create()

        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            deleteTask()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun fillEditTaskForm() {
        with(binding) {
            etTaskName.setText(taskAttributes.name)
            cbTaskCompleted.isChecked = taskAttributes.completed
            binding.tvStartDate.text =
                if (taskAttributes.startDate === null) "none"
                else zonedDateTimeFormatter.format(taskAttributes.startDate)
            binding.tvDeadlineDate.text =
                if (taskAttributes.deadline === null) "none"
                else zonedDateTimeFormatter.format(taskAttributes.deadline)
            binding.cbImportant.isChecked = taskAttributes.important
            binding.etDescription.setText(taskAttributes.description)
        }
    }

    private fun updateStartDate() {
        taskAttributes.startDate = ZonedDateTime.of(
            LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)),
            LocalTime.of(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)),
            timezone.toZoneId()
        )
        binding.tvStartDate.text = formatter.format(calendar)
    }

    private fun updateDeadline() {
        taskAttributes.deadline = ZonedDateTime.of(
            LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)),
            LocalTime.of(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE)),
            timezone.toZoneId()
        )
        binding.tvDeadlineDate.text = formatter.format(calendar)
    }

    private fun updateCompleteStatus() {
        taskAttributes.completed = binding.cbTaskCompleted.isChecked
    }

    private fun updateImportantFlag() {
        taskAttributes.important = binding.cbImportant.isChecked
    }

    private fun saveChanges() {
        taskAttributes.name = binding.etTaskName.text.toString()
        taskAttributes.description = binding.etDescription.text.toString()
        sendUpdatedTaskAttributes()
    }

    private fun bindButtons() {
        binding.btnEscape.setOnClickListener {
            finish()
        }

        binding.btnDeleteTask.setOnClickListener {
            showAlertDialogDeleteTask()
        }

        binding.cbTaskCompleted.setOnClickListener {
            updateCompleteStatus()
        }

        binding.cbImportant.setOnClickListener {
            updateImportantFlag()
        }

        binding.ivStartDate.setOnClickListener {
            pickerMode = DateTimePickerMode.START_DATE
            showDateTimePickers()
        }

        binding.ivDeadlineDate.setOnClickListener {
            pickerMode = DateTimePickerMode.DEADLINE
            showDateTimePickers()
        }

        binding.btnSave.setOnClickListener {
            saveChanges()
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskAttributesBinding.inflate(layoutInflater, null,false)
        setContentView(binding.root)

        jwt = getSharedPreferences("auth", MODE_PRIVATE).getString("jwt", null)
        calendar.timeZone = android.icu.util.TimeZone.getTimeZone(timezone.id.toString())
        taskAttributes = intent.extras?.getSerializable("taskItem") as TaskEntity
        fillEditTaskForm()

        bindButtons()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            this,
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showDateTimePickers() {
        showTimePicker()
        showDatePicker()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    override fun onTimeSet(timePicker: TimePicker?, hours: Int, minutes: Int) {
        calendar.apply {
            set(Calendar.HOUR, hours)
            set(Calendar.MINUTE, minutes)
        }
        when (pickerMode) {
            DateTimePickerMode.START_DATE -> updateStartDate()
            DateTimePickerMode.DEADLINE -> updateDeadline()
        }
    }

}