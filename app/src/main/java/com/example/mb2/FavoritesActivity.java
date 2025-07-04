package com.example.mb2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mb2.adapter.PlaylistAdapter; // Vamos reutilizar o mesmo adapter!
import com.example.mb2.api.AppDatabase;
import com.example.mb2.api.PlaylistDao;
import com.example.mb2.model.Playlist;
import com.example.mb2.model.PlaylistEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesActivity extends AppCompatActivity implements PlaylistAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private List<Playlist> favoritePlaylistList;
    private PlaylistDao playlistDao;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        favoritePlaylistList = new ArrayList<>();

        adapter = new PlaylistAdapter(favoritePlaylistList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        playlistDao = AppDatabase.getDatabase(this).playlistDao();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        databaseExecutor.execute(() -> {
            List<PlaylistEntity> entities = playlistDao.getAllFavoritePlaylists();

            List<Playlist> playlists = new ArrayList<>();
            for (PlaylistEntity entity : entities) {
                Playlist p = new Playlist(entity.getName(), entity.getDescription());
                p.setId(entity.getId());
                playlists.add(p);
            }

            runOnUiThread(() -> {
                favoritePlaylistList.clear();
                favoritePlaylistList.addAll(playlists);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onItemClick(int position) {
        // Esta lógica é idêntica à da MainActivity
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Playlist clickedPlaylist = favoritePlaylistList.get(position);
        detailIntent.putExtra("EXTRA_PLAYLIST", clickedPlaylist);
        startActivity(detailIntent);
    }
}