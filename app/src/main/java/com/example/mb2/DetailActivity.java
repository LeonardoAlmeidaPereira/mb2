package com.example.mb2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mb2.api.AppDatabase;
import com.example.mb2.api.PlaylistDao;
import com.example.mb2.model.Playlist;
import com.example.mb2.model.PlaylistEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {

    private TextView detailName, detailDescription, detailId;
    private FloatingActionButton fabFavorite;

    private PlaylistDao playlistDao;
    private Playlist currentPlaylist;
    private boolean isFavorite = false;

    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        playlistDao = AppDatabase.getDatabase(this).playlistDao();

        detailName = findViewById(R.id.detailTextViewName);
        detailDescription = findViewById(R.id.detailTextViewDescription);
        detailId = findViewById(R.id.detailTextViewId);
        fabFavorite = findViewById(R.id.fab_favorite);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("EXTRA_PLAYLIST")) {
            currentPlaylist = (Playlist) intent.getSerializableExtra("EXTRA_PLAYLIST");

            if (currentPlaylist != null) {
                detailName.setText(currentPlaylist.getName());
                detailDescription.setText(currentPlaylist.getDescription());
                detailId.setText("ID: " + currentPlaylist.getId());

                checkIfFavorite();
            }
        }

        fabFavorite.setOnClickListener(view -> {
            toggleFavoriteStatus();
        });
    }

    private void checkIfFavorite() {
        databaseExecutor.execute(() -> {
            PlaylistEntity entity = playlistDao.findById(currentPlaylist.getId());
            isFavorite = (entity != null);

            runOnUiThread(() -> {
                updateFabIcon();
            });
        });
    }

    private void toggleFavoriteStatus() {
        databaseExecutor.execute(() -> {
            if (isFavorite) {
                PlaylistEntity entityToDelete = new PlaylistEntity();
                entityToDelete.setId(currentPlaylist.getId());
                playlistDao.delete(entityToDelete);

                runOnUiThread(() -> Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show());
            } else {
                PlaylistEntity entityToInsert = new PlaylistEntity();
                entityToInsert.setId(currentPlaylist.getId());
                entityToInsert.setName(currentPlaylist.getName());
                entityToInsert.setDescription(currentPlaylist.getDescription());
                playlistDao.insert(entityToInsert);

                runOnUiThread(() -> Toast.makeText(this, "Adicionado aos favoritos!", Toast.LENGTH_SHORT).show());
            }
            isFavorite = !isFavorite;
            runOnUiThread(this::updateFabIcon);
        });
    }

    private void updateFabIcon() {
        if (isFavorite) {
            fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_filled));
        } else {
            fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border));
        }
    }
}