package ru.nsu.brusn.smpltodo.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.nsu.brusn.smpltodo.BuildConfig
import ru.nsu.brusn.smpltodo.R
import ru.nsu.brusn.smpltodo.api.APIHeadersProvider
import ru.nsu.brusn.smpltodo.api.model.dto.request.CreateNewFolderRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.GetAllFoldersResponse
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse
import ru.nsu.brusn.smpltodo.api.services.UserService
import ru.nsu.brusn.smpltodo.databinding.ActivityMainBinding
import ru.nsu.brusn.smpltodo.fragments.TaskListFragment
import ru.nsu.brusn.smpltodo.fragments.TaskListTopBarFragment
import ru.nsu.brusn.smpltodo.interfaces.MainActivityInterface

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainActivityInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var jwt: String? = null
    private val mainMenuGroupId = 0
    private val subMenuGroupId = 1
    private val folderGroupItemIdMap = HashMap<Int, Long>()
    private var lastMenuItem: MenuItem? = null

    private fun navigateToSignInActivity() {
        Intent(this, SignInActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .also {
                startActivity(it)
            }
    }

    private fun logout() {
        sharedPreferences.edit().remove("jwt").apply()
        navigateToSignInActivity()
    }

    private fun verifyUserAuthorization() {
        jwt = sharedPreferences.getString("jwt", null)
        if (jwt == null) {
            navigateToSignInActivity()
        }
    }

    private fun loadUserFolders(skipCheckItem: Boolean) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val userService: UserService = retrofit.create(UserService::class.java)

        userService.getAllUserFolders(
            mapOf(
                APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(
                    jwt.toString()
                )
            )
        ).enqueue(object : Callback<GetAllFoldersResponse> {
            override fun onResponse(
                call: Call<GetAllFoldersResponse>,
                response: Response<GetAllFoldersResponse>
            ) {
                if (!response.isSuccessful) {
                    return
                }

                // Fetching data
                val body = response.body()!!

                // Filling menu
                val menu = binding.navigationMenu.menu
                menu.clear()
                var folderMapId = 0
                body.data.forEach { folder ->
                    run {
                        folderGroupItemIdMap[folderMapId] = folder.id
                        menu.add(mainMenuGroupId, folderMapId, Menu.NONE, folder.name).setIcon(R.drawable.folder_icon)
                        folderMapId += 1
                    }
                }
                lastMenuItem = menu.getItem(0)

                if (!skipCheckItem)
                    onNavigationItemSelected(menu.getItem(0))
            }

            override fun onFailure(call: Call<GetAllFoldersResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
                showShortToast("Error while getting folders!")
                return
            }
        })
    }

    private fun showShortToast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun createNewFolder(newFolderName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val userService: UserService = retrofit.create(UserService::class.java)
        val requestBody = CreateNewFolderRequest(newFolderName)
        val headers = mapOf(APIHeadersProvider.AUTHORIZATION to APIHeadersProvider.getBearer(jwt.toString()))

        userService.createNewFolder(headers, requestBody).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.errorBody() != null) {
                    val jsonObject = JSONObject(response.errorBody()?.string())
                    showShortToast(jsonObject.getJSONObject("error").getString("message"))
                    return
                }

                if (response.isSuccessful) {
                    loadUserFolders(true)
                    showShortToast("Folder created successfully")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("DEBUG", t.toString())
                showShortToast("Error while creating new folder")
            }
        })
    }

    private fun showAlertDialogCreateNewFolder() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_create_new_folder, null)
        alertDialogBuilder.setView(view)
        val dialog = alertDialogBuilder.create()

        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val etFolderName = view.findViewById<EditText>(R.id.etFolderName)

        btnSave.setOnClickListener {
            createNewFolder(etFolderName.text.toString())
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        // Verifying existing user auth
        verifyUserAuthorization()

        // Configuring navigation menu
        val navigationView = binding.navigationMenu
        loadUserFolders(false)
        navigationView.setNavigationItemSelectedListener(this)

        // Binding
        bindHeaderButtons()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val bundleTaskList = Bundle()
        bundleTaskList.putLong("folderId", folderGroupItemIdMap.getValue(item.itemId))
        val taskListFragment = TaskListFragment()
        taskListFragment.arguments = bundleTaskList


        val taskListTopbarFragment = TaskListTopBarFragment()
        val bundleTopbar = Bundle()
        bundleTopbar.putLong("folderId", folderGroupItemIdMap.getValue(item.itemId))
        bundleTopbar.putString("folderName", item.title.toString())
        taskListTopbarFragment.arguments = bundleTopbar

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainTopBar, taskListTopbarFragment)
            replace(R.id.mainFrameLayout, taskListFragment)
            commit()
        }

        lastMenuItem = item

        binding.drawer.closeDrawer(Gravity.LEFT)
        return true
    }

    private fun loadLastTaskList() {
        lastMenuItem?.let { onNavigationItemSelected(it) }
    }


    private fun bindHeaderButtons() {
        val navMenuHeader = binding.navigationMenu.getHeaderView(0)

        navMenuHeader.findViewById<android.widget.Button>(R.id.btnLogout).setOnClickListener {
            logout()
        }

        navMenuHeader.findViewById<android.widget.Button>(R.id.btnCreateFolder).setOnClickListener {
            showAlertDialogCreateNewFolder()
        }
    }

    override fun loadUpdatedFolders(skipCheckItem: Boolean) {
        loadUserFolders(skipCheckItem)
    }

    override fun onResume() {
        super.onResume()
        loadLastTaskList()
    }
}