package com.menglingpeng.designersshow.mvp.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

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
import com.menglingpeng.designersshow.BaseFragment;
import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mengdroid on 2017/10/13.
 */

public class RecyclerFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private String Type;
    private String token = "498b79c0b032215d0e1e1a2fa487a9f8e5637918fa373c63aa29e48528b2822c";
    private Gson gson;
    public static final String TAB_POPULAR = "Popular";
    public static final String TAB_RECENT = "Recent";
    public static final String TAB_FOLLOWING = "Following";

    public static RecyclerFragment newInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TYPE, type);
        RecyclerFragment fragment = new RecyclerFragment() ;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_recycler;
    }

    @Override
    protected void initView() {
        swipeRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        new GetShotsTask().execute("https://api.dribbble.com/v1/shots");
        initFragments();

    }

    private void initFragments(){
        Type = getArguments().get(Constants.TYPE).toString();
        switch (Type){
            case TAB_FOLLOWING:
                break;
            case TAB_POPULAR:
                break;
            case TAB_RECENT:
                break;
        }

    }

    class GetShotsTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            OkHttpClient client = new OkHttpClient();
            HttpUrl httpUrl = HttpUrl.parse(url).newBuilder()
                    .addQueryParameter("access_token", token)
                    .build();
            Request request = new Request.Builder()
                    .url(httpUrl)
                    .get()
                    .build();
            try {

                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    Log.i("response", response.body().toString());
                    //替换Sting类型null为"".
                    gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
                    JsonParser parser = new JsonParser();
                    JsonArray jsonArray = parser.parse(response.body().toString()).getAsJsonArray();
                    ArrayList<Shots> shotsList = new ArrayList<>();
                    for(JsonElement element : jsonArray){
                        Shots shots = gson.fromJson(element, Shots.class);
                        shotsList.add(shots);
                        Log.i("response", shotsList.get(0).getTitle());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    public class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }

    public class StringNullAdapter extends TypeAdapter<String> {
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

    @Override
    protected void initData() {

    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
