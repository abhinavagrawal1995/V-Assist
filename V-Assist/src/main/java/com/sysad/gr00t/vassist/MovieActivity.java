package com.darkarmy.ykl.vassist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.sysad.gr00t.vassist.R;

import java.util.Random;

public class MovieActivity extends AppCompatActivity {

    Button generateRecButton;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movie);

        generateRecButton = (Button) findViewById(R.id.movieRec);


        generateRecButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                generateRecommendation();
            }
        });
    }

    public void generateRecommendation ()   {
        String[] movieTitles = {"Goldfinger",
                "godfather",
                "Harry Potter",
                "Sholay",
                "don"};

        Random r = new Random();
        int index = r.nextInt(16 - 0) + 0;

        String param = movieTitles[index];
        param = param.replace(" ", "+");

        url = "http://www.omdbapi.com/?t=" + param + "&y=&plot=short&r=json";

        Intent intent = new Intent(this, Recommendation.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public void tellme(View v){
        Intent intent = new Intent(this, Recommendation.class);
        EditText temme=(EditText) findViewById(R.id.editText);
        String param =temme.getText().toString();
        param = param.replace(" ", "+");
        url = "http://www.omdbapi.com/?t=" + param + "&y=&plot=short&r=json";
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
