package com.example.mb2.api; // ou .db, ou .dao

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mb2.model.PlaylistEntity;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaylistEntity playlist);

    @Delete
    void delete(PlaylistEntity playlist);

    @Query("SELECT * FROM favorite_playlists")
    List<PlaylistEntity> getAllFavoritePlaylists();

    @Query("SELECT * FROM favorite_playlists WHERE id = :playlistId")
    PlaylistEntity findById(String playlistId);
}