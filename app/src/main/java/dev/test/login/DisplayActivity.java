package dev.test.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dev.source.api.API;
import dev.source.api.WebAPI;
import dev.source.api.WebAPIListener;
import dev.source.model.Grade;
import dev.source.model.User;

public class DisplayActivity extends AppCompatActivity {

    private static final String PREFERENCE = "PREFERENCE";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("USER");

        Button btnTestToken = findViewById(R.id.btnTestToken);
        Button btnTest = findViewById(R.id.btnTest);
        Button btnLogout = findViewById(R.id.btnLogout);

//        Button SHOW GRADE, dùng AsyncTask mở Http Connection
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection;
                        try {
                            URL url = new URL("http://10.0.2.2:5000/accounts");
                            connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestProperty("Authorization", "Bearer " + user.getToken());
                            if(connection.getResponseCode() == 200) {

                                List<Grade> gradeList = new ArrayList<>();
                                Grade grade = null;

                                InputStream responseBody = connection.getInputStream();

                                BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                JSONObject jsonObject = null;
                                try {
                                    while ((line = reader.readLine()) != null) {
                                        sb.append(line + "\n");
                                        String output = sb.toString();
                                        while(output.indexOf("}") > 0) {
                                            String strObj = output.substring(output.indexOf("{"), output.indexOf("}") + 1);
                                            System.out.println("V.2: "+strObj);
                                            jsonObject = new JSONObject(strObj);
                                            grade = Grade.getGradeFromJSON(jsonObject);
                                            gradeList.add(grade);
                                            output = output.substring(sb.toString().indexOf("}") + 1);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        responseBody.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                System.out.println(connection.getResponseCode());
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }
        });

//      Button SHOW GRADE V.2, gọi API qua class WebAPI như login
        btnTestToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API webAPI = new WebAPI(DisplayActivity.this.getApplication());
                webAPI.getGrade(user.getToken(), new WebAPIListener());
            }
        });

//        logout, xóa session
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.clear().commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}