package com.example.mb2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mb2.adapter.PlaylistAdapter;
import com.example.mb2.api.ApiService;
import com.example.mb2.api.RetrofitClient;
import com.example.mb2.model.Playlist;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PlaylistAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private List<Playlist> playlistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewPlaylists);
        playlistList = new ArrayList<>();

        adapter = new PlaylistAdapter(playlistList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        Button buttonGoToFavorites = findViewById(R.id.buttonGoToFavorites);
        buttonGoToFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> {
            logout();
        });

        fetchPlaylists();
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        Playlist clickedPlaylist = playlistList.get(position);
        detailIntent.putExtra("EXTRA_PLAYLIST", clickedPlaylist);
        startActivity(detailIntent);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void fetchPlaylists() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(this, "Sessão expirada. Por favor, faça login novamente.", Toast.LENGTH_LONG).show();
            logout();
            return;
        }

        String authToken = "Bearer " + token;
        ApiService apiService = RetrofitClient.getClient();
        Call<List<Playlist>> call = apiService.getPlaylists(authToken);

        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    playlistList.clear();
                    playlistList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Playlists reais carregadas: " + playlistList.size());
                } else {
                    Log.e(TAG, "Erro na resposta ao buscar playlists: " + response.code());
                    Toast.makeText(MainActivity.this, "Não foi possível carregar as playlists.", Toast.LENGTH_SHORT).show();

                    if (response.code() == 401 || response.code() == 403) {
                        logout();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.e(TAG, "Falha de conexão: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Falha de conexão.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}