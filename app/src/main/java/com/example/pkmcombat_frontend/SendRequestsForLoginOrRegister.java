package com.example.pkmcombat_frontend;
import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class SendRequestsForLoginOrRegister {

    public void sendPostRequest(Context context, String url, JSONObject requestBody, String onResponse) {

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                response -> {
                    Toast.makeText(context, onResponse, Toast.LENGTH_SHORT).show();

                    try {

                        String receivedToken = response.getString("token");

                        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("VALID_TOKEN", receivedToken);
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error reading data from the server", Toast.LENGTH_SHORT).show();
                    }

                    Intent myIntent = new Intent(context, MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(myIntent);
                },
                error -> {
                    if (error.networkResponse != null) {
                        String jsonResponse = new String(error.networkResponse.data);
                        try {
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            String errorMsg = jsonObject.optString("error", "Error  in request");
                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error to process the response", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Missing connection with server", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(request);
    }
}