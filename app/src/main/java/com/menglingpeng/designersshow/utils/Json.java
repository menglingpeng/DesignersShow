package com.menglingpeng.designersshow.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.menglingpeng.designersshow.mvp.model.AuthToken;
import com.menglingpeng.designersshow.mvp.model.Comments;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.mvp.model.User;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mengdroid on 2017/10/17.
 */

public class Json {

    public static Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();

    public static ArrayList<Shots> parseShots(String shotsJson){

        //替换Sting值null为"".
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(shotsJson).getAsJsonArray();
        ArrayList<Shots> shotsList = new ArrayList<>();
        for(JsonElement element : jsonArray){
            Shots shots = gson.fromJson(element, Shots.class);
            shotsList.add(shots);
        }
        return shotsList;
    }

    public static ArrayList<Comments> parseComments(String shotsJson){

        //替换Sting值null为"".
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(shotsJson).getAsJsonArray();
        ArrayList<Comments> commentsList = new ArrayList<>();
        for(JsonElement element : jsonArray){
            Comments comments = gson.fromJson(element, Comments.class);
            commentsList.add(comments);
        }
        return commentsList;
    }

    public static AuthToken parseAuthToken(String json){
        return gson.fromJson(json, AuthToken.class);
    }

    public static User parseUser(String json){
        return gson.fromJson(json, User.class);
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }

    public static class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            // TODO Auto-generated method stub
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            // TODO Auto-generated method stub
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

}
