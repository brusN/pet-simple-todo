package ru.nsu.brusn.smpltodo.api.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import ru.nsu.brusn.smpltodo.api.model.dto.request.CreateNewFolderRequest
import ru.nsu.brusn.smpltodo.api.model.dto.request.CreateNewTaskRequest
import ru.nsu.brusn.smpltodo.api.model.dto.request.EditFolderRequest
import ru.nsu.brusn.smpltodo.api.model.dto.request.EditTaskRequest
import ru.nsu.brusn.smpltodo.api.model.dto.response.GetAllFoldersResponse
import ru.nsu.brusn.smpltodo.api.model.dto.response.GetFolderTasksResponse
import ru.nsu.brusn.smpltodo.api.model.dto.response.MessageResponse

interface UserService {
    @GET("/api/folder/all")
    fun getAllUserFolders(@HeaderMap headers: Map<String, String>): Call<GetAllFoldersResponse>

    @GET("/api/folder/all/{folderId}")
    fun getAllFolderTasks(@HeaderMap headers: Map<String, String>, @Path("folderId") folderId: Long): Call<GetFolderTasksResponse>

    @POST("/api/folder/create")
    fun createNewFolder(@HeaderMap headers: Map<String, String>, @Body request: CreateNewFolderRequest): Call<MessageResponse>

    @DELETE("/api/folder/delete/{folderId}")
    fun deleteFolder(@HeaderMap headers: Map<String, String>, @Path("folderId") folderId: Long): Call<MessageResponse>

    @POST("/api/folder/modify/{folderId}")
    fun editFolder(@HeaderMap headers: Map<String, String>, @Body request: EditFolderRequest, @Path("folderId") folderId: Long): Call<MessageResponse>

    @POST("/api/task/create")
    fun createNewTask(@HeaderMap header: Map<String, String>, @Body request: CreateNewTaskRequest): Call<MessageResponse>

    @POST("/api/task/modify/{taskId}")
    fun editTask(@HeaderMap headers: Map<String, String>, @Body request: EditTaskRequest, @Path("taskId") taskId: Long): Call<MessageResponse>

    @DELETE("/api/task/delete/{taskId}")
    fun deleteTask(@HeaderMap headers: Map<String, String>, @Path("taskId") taskId: Long): Call<MessageResponse>
}