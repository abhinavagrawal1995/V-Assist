package com.darkarmy.ykl.vassist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.darkarmy.ykl.samaritan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Recommendation extends AppCompatActivity {

    private String TAG = Recommendation.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    private String url;

    ArrayList<HashMap<String, String>> omdbData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recommendation);

        Intent i = getIntent();
        if (i.hasExtra("url")){
            url = i.getStringExtra("url");
        }

        omdbData = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetData().execute();




    }

    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Recommendation.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            OmdbHandler sh = new OmdbHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject c = new JSONObject(jsonStr);

                    String title = c.getString("Title");
                    String year = c.getString("Year");
                    String genre = c.getString("Genre");
                    String plot = c.getString("Plot");
                    String director = c.getString("Director");
                    String runtime = c.getString("Runtime");
                    String releaseDate = c.getString("Released");
                    String imdbRating = c.getString("imdbRating");
                    String metascore = c.getString("Metascore");
                    String poster = c.getString("Poster");



                    HashMap<String, String> movie = new HashMap<>();

                    movie.put("title", title);
                    movie.put("year", "Year"+year);
                    movie.put("genre", genre);
                    movie.put("plot", plot);
                    movie.put("director", director);
                    movie.put("runtime", runtime);
                    movie.put("releaseDate", releaseDate);
                    movie.put("imdbRating", imdbRating);
                    movie.put("metascore", metascore);
                    movie.put("poster", poster);

                    omdbData.add(movie);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    Recommendation.this, omdbData,
                    R.layout.list_item, new String[]{"title", "year", "genre",
                    "plot", "director", "runtime",
                    "releaseDate", "imdbRating", "metascore"},
                    new int[]{R.id.title, R.id.year, R.id.genre,
                    R.id.plot, R.id.director, R.id.runtime,
                    R.id.released, R.id.imdbrating, R.id.metascore});

            ImageView iv=(ImageView)findViewById(R.id.imageView);
            String imgurl=omdbData.get(0).get("poster");
            new ImageLoadTask(imgurl,iv).execute();
            lv.setAdapter(adapter);


        }
    }
}



class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}