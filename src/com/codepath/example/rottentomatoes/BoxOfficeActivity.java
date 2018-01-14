package com.codepath.example.rottentomatoes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BoxOfficeActivity extends Activity {

	private AsyncHttpClient client;

	private ListView lvMovies;
	private BoxOfficeMoviesAdapter adapterMovies;
	public static final String MOVIE_DETAIL_KEY = "movie";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box_office);
		lvMovies = (ListView) findViewById(R.id.lvMovies);
		ArrayList<BoxOfficeMovie> aMovies = new ArrayList<BoxOfficeMovie>();
		adapterMovies = new BoxOfficeMoviesAdapter(this, aMovies);
		lvMovies.setAdapter(adapterMovies);


		// Fetch the data remotely
		this.client = new AsyncHttpClient();
		String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
		RequestParams params = new RequestParams("apikey", "9htuhtcb4ymusd73d4z6jxcj");
		client.get(url, params,new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int code, JSONObject body) {
				JSONArray items = null;
				try {
					// Get the movies json array
					items = body.getJSONArray("movies");
					// Parse json array into array of model objects
					ArrayList<BoxOfficeMovie> movies = BoxOfficeMovie.fromJson(items);
					// Load model objects into the adapter which displays them
					adapterMovies.addAll(movies);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

        lvMovies.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
                Intent i = new Intent(BoxOfficeActivity.this, BoxOfficeDetailActivity.class);
                i.putExtra(MOVIE_DETAIL_KEY, adapterMovies.getItem(position));
                startActivity(i);
            }
        });
	}
}
