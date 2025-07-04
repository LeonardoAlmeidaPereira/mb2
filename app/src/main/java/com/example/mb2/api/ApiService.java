package com.example.mb2.api;

import com.example.mb2.model.ApiResponse;
import com.example.mb2.model.LoginRequest;
import com.example.mb2.model.LoginResponse;
import com.example.mb2.model.Playlist;
import com.example.mb2.model.RegisterRequest;
import com.example.mb2.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    //METODOS DE AUTENTICAÇÃO

    @POST("users/register")
    Call<User> registerUser(@Body RegisterRequest body);

    @POST("users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest body);

    //METODOS DE PLAYLIST

    @GET("playlists")
    Call<List<Playlist>> getPlaylists(@Header("Authorization") String token);

    @GET("playlists/{id}")
    Call<Playlist> getPlaylistById(@Path("id") String playlistId, @Header("Authorization") String token);

    @POST("playlists")
    Call<Playlist> createPlaylist(@Header("Authorization") String token, @Body Playlist body); // Simplificado para reutilizar a classe Playlist

    @PUT("playlists/{id}")
    Call<Playlist> updatePlaylist(@Path("id") String playlistId, @Header("Authorization") String token, @Body Playlist body); // Simplificado

    @DELETE("playlists/{id}")
    Call<ApiResponse> deletePlaylist(@Path("id") String playlistId, @Header("Authorization") String token);

}