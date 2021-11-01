package com.uc.aflmoviedb.model;

import com.google.gson.Gson;

import java.util.List;

public class Genre {

    private List<Genres> genres;

    public static Genre objectFromData(String str) {

        return new Gson().fromJson(str, Genre.class);
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    public static class Genres {
        private int id;
        private String name;

        public static Genres objectFromData(String str) {

            return new Gson().fromJson(str, Genres.class);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
