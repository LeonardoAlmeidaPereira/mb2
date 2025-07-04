package com.example.mb2.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_playlists") // Define o nome da tabela
public class PlaylistEntity {

    @PrimaryKey // Marca o 'id' como a chave primária da tabela
    @NonNull // Garante que o ID nunca será nulo
    private String id;

    private String name;
    private String description;

    // Getters e Setters (pressione Alt+Insert para gerar)

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}