package com.example.lvlconprueba.data.api
import com.example.lvlconprueba.data.dto.CreateProjectRequestDto
import com.example.lvlconprueba.data.dto.CreateProjectResponseDto
import com.example.lvlconprueba.data.dto.FindProjectsRequestDto
import com.example.lvlconprueba.data.dto.FindProjectsResponseDto
import com.example.lvlconprueba.data.dto.ProjectInitFormResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProjectApiService {

    @GET("api/proyecto/initForm")
    suspend fun getInitForm(): Response<ProjectInitFormResponseDto>

    @POST("api/proyecto/saveOrUpdate")
    suspend fun saveOrUpdateProject(@Body request: CreateProjectRequestDto): Response<CreateProjectResponseDto>

    @POST("api/proyecto/find")
    suspend fun findProjects(@Body request: FindProjectsRequestDto): Response<FindProjectsResponseDto>
}
