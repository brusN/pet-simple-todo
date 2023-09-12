package ru.nsu.brusn.smpltodo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.drawerlayout.widget.DrawerLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nsu.brusn.smpltodo.BuildConfig
import ru.nsu.brusn.smpltodo.R
import ru.nsu.brusn.smpltodo.api.APIHeadersProvider
import ru.nsu.brusn.smpltodo.api.model.dto.request.EditFolderRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse
import ru.nsu.brusn.smpltodo.api.services.UserService
import ru.nsu.brusn.smpltodo.databinding.FragmentTaskListTopbarBinding
import ru.nsu.brusn.smpltodo.interfaces.MainActivityInterface

class TaskListTopBarFragment : Fragment() {
    private lateinit var binding: FragmentTaskListTopbarBinding
    private var folderId: Long = -1
    private var folderName: String? = null
    private var jwt: String? = null
    private lateinit var mainActivityInterface: MainActivityInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListTopbarBinding.inflate(inflater, container, false)
        folderId = requireArguments().getLong("folderId")
        folderName = requireArguments().getString("folderName")
        jwt = requireActivity()
            .getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
            .getString("jwt", null)

        binding.tvFolderName.text = folderName
        bindOpenNavigationMenuButton()
        bindDeleteFolderButton()
        bindEditFolderButton()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainActivityInterface = context as MainActivityInterface
        } catch (castException: ClassCastException) {
            Log.e("DEBUG", "Can't cast to main activity interface")
        }
    }

    private fun deleteFolder() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val userService: UserService = retrofit.create(UserService::class.java)
        val headers = mapOf(APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(jwt.toString()))

        userService.deleteFolder(headers, folderId).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.errorBody() != null) {
                    Log.e("DEBUG", "Error while deleting folder")
                }

                if (response.isSuccessful) {
                    Log.e("DEBUG", "Deleted success")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
            }
        })
    }

     private fun showDeleteFolderDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_delete_folder, null)
        alertDialogBuilder.setView(view)
        val dialog = alertDialogBuilder.create()

        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            deleteFolder()
            mainActivityInterface.loadUpdatedFolders(false)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun bindDeleteFolderButton() {
        binding.btnDeleteFolder.setOnClickListener {
            showDeleteFolderDialog()
        }
    }

    private fun showEditFolderDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_edit_folder, null)
        alertDialogBuilder.setView(view)
        val dialog = alertDialogBuilder.create()

        val btnConfirm = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val etNewFolderName = view.findViewById<EditText>(R.id.etNewFolderName)
        etNewFolderName.setText(folderName)

        btnConfirm.setOnClickListener {
            editFolder(etNewFolderName.text.toString())
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun editFolder(newFolderName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val userService: UserService = retrofit.create(UserService::class.java)
        val headers = mapOf(APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(jwt.toString()))
        val request = EditFolderRequest(newFolderName)

        userService.editFolder(headers, request, folderId).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.errorBody() != null) {
                    Log.e("DEBUG", "Error while editing folder")
                }

                if (response.isSuccessful) {
                    Log.e("DEBUG", "Edited success")
                    binding.tvFolderName.text = newFolderName
                    mainActivityInterface.loadUpdatedFolders(true)
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
            }
        })
    }

    private fun bindEditFolderButton() {
        binding.btnEditFolder.setOnClickListener {
            showEditFolderDialog()
        }
    }

    private fun bindOpenNavigationMenuButton() {
        binding.btnOpenNavigationMenu.setOnClickListener {
            requireActivity().findViewById<DrawerLayout>(R.id.drawer).openDrawer(Gravity.LEFT)
        }
    }
}