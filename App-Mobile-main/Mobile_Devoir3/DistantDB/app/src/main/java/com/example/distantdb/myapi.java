package com.example.distantdb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface myapi {

        @FormUrlEncoded // Les paires clé-valeur seront incluses dans le corps de la requête pour envoyer les paramètres.
        @POST("inserttask.php")
        Call<Task> addTask(@Field("task") String task);

        @FormUrlEncoded
        @POST("deletetask.php")
        Call<Task> deleteTask(@Field("id") int id);

        @GET("getalltasks.php")
        Call<List<Task>> getAllTasks();
    }

