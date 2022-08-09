package com.sildev.musiclocal.datalocal;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sildev.musiclocal.model.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String LIST_SONG_KEY = "LIST_SONG_KEY";
    private static final String CURRENT_SONG_KEY = "CURRENT_SONG_KEY";
    private static final String IS_SHUFFLE_KEY = "IS_SHUFFLE_KEY";
    private static final String IS_REPEAT_KEY = "IS_REPEAT_KEY";
    private static DataManager instance;
    private InitPreferences initPreferences;

    public static void initManager(Context context) {
        instance = new DataManager();
        instance.initPreferences = new InitPreferences(context);
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static void deleteData() {
        DataManager.getInstance().initPreferences.deleteData(CURRENT_SONG_KEY);
        DataManager.getInstance().initPreferences.deleteData(LIST_SONG_KEY);
    }

    public static void setShuffle(boolean isShuffle) {
        DataManager.getInstance().initPreferences.putBoolean(IS_SHUFFLE_KEY, isShuffle);
    }

    public static boolean getShuffle() {
        return DataManager.getInstance().initPreferences.getBoolean(IS_SHUFFLE_KEY);
    }

    public static void setRepeat(boolean isShuffle) {
        DataManager.getInstance().initPreferences.putBoolean(IS_REPEAT_KEY, isShuffle);
    }

    public static boolean getRepeat() {
        return DataManager.getInstance().initPreferences.getBoolean(IS_REPEAT_KEY);
    }

    public static void setListSong(List<Song> songs) {
        JsonArray jsonArray = new Gson().toJsonTree(songs).getAsJsonArray();
        String strJsonArr = jsonArray.toString();

        DataManager.getInstance().initPreferences.putString(LIST_SONG_KEY, strJsonArr);
    }

    public static void setCurrentSong(Song song) {
        JsonObject jsonObject = new Gson().toJsonTree(song).getAsJsonObject();
        String strJsonArr = jsonObject.toString();
        DataManager.getInstance().initPreferences.putString(CURRENT_SONG_KEY, strJsonArr);
    }


    public static Song getCurrentSong() {
        Song song = null;
        String strJsonObject = DataManager.getInstance().initPreferences.getString(CURRENT_SONG_KEY);
        try {
            JSONObject object = new JSONObject(strJsonObject);
            song = new Gson().fromJson(object.toString(), Song.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return song;
    }

    public static List<Song> getListSong() {
        List<Song> list = new ArrayList<>();
        String strJsonArr = DataManager.getInstance().initPreferences.getString(LIST_SONG_KEY);
        try {
            JSONArray jsonArray = new JSONArray(strJsonArr);
            JSONObject object;
            Song song;
            for (int i = 0; i < jsonArray.length(); i++) {
                object = jsonArray.getJSONObject(i);
                song = new Gson().fromJson(object.toString(), Song.class);
                list.add(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
