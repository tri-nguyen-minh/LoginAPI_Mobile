package dev.source.api;

import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.source.model.Grade;
import dev.source.model.User;

public class WebAPI implements API {

    public static final String BASE_URL_LOGIN = "http://10.0.2.2:5000/login";
    public static final String BASE_URL_GET = "http://10.0.2.2:5000/accounts";

    private RequestQueue requestQueue;

    public WebAPI(Application application) {
        requestQueue = Volley.newRequestQueue(application);
    }

    //    gửi request login (trang main)
    @Override
    public void login(String username, String password, WebAPIListener webAPIListener) {
        String url = BASE_URL_LOGIN;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        lấy được user từ API
                        User user = User.getUserFromJSON(response);
                        webAPIListener.onSuccessfulLogin(user);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Test onErrorResponse");
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, listener, errorListener);
            requestQueue.add(request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    gửi request lấy danh sách điểm (trang Display)
    @Override
    public void getGrade(String token, WebAPIListener webAPIListener) {
        String url = BASE_URL_GET;
        JSONObject jsonObject = new JSONObject();
        try {
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

//                    lấy được danh sách
                    List<Grade> gradeList = new ArrayList<>();
                    JSONObject jsonObject = null;
                    Grade grade = null;
                    String output = response;
                    try {
                        while(output.indexOf("}") > 0) {
                            String strObj = output.substring(output.indexOf("{"), output.indexOf("}") + 1);
                            System.out.println(strObj);
                            jsonObject = new JSONObject(strObj);
                            grade = Grade.getGradeFromJSON(jsonObject);
                            gradeList.add(grade);
                            output = output.substring(response.indexOf("}") + 1);
                        }
                    } catch (Exception e) {
                        System.out.println("cant parse");
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Test onErrorResponse");
                }
            };
//            gắn bearer token vào header
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("Authorization", "Bearer " + token);
                    return header;
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
