package com.uzitech.currencyconverter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    String base;
    JSONObject data;

    ArrayList<String> Curr;

    Spinner curr_in, curr_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String url = "https://api.ratesapi.io/api/latest";

        final JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    base = response.getString("base");
                    data = response.getJSONObject("rates");
                    setData();
                } catch (Exception e) {
                    Log.d("ERROR", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue q = Volley.newRequestQueue(this);
        q.add(request);
    }

    private void setData() throws JSONException {
        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();

        Curr = new ArrayList<>();

        Iterator<String> iter = data.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            Curr.add(key);
        }
        data.put(base, 1);
        Curr.add(base);
        Collections.sort(Curr);

        curr_in = findViewById(R.id.curr_in);
        curr_out = findViewById(R.id.curr_out);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Curr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        curr_in.setAdapter(adapter);
        curr_out.setAdapter(adapter);
    }

    public void calculateCurr(View view) throws JSONException {
        EditText input_val = findViewById(R.id.input_val);

        if (!input_val.getText().toString().isEmpty()) {
            Double input_value = Double.parseDouble(input_val.getText().toString());
            Double exchange_val = data.getDouble(curr_out.getSelectedItem().toString()) / data.getDouble(curr_in.getSelectedItem().toString());

            Double fin = exchange_val * input_value;

            TextView conv_value = findViewById(R.id.conv_value);
            conv_value.setText(String.format("%.2f", fin));
        } else {
            Toast.makeText(getApplicationContext(), "Enter a value", Toast.LENGTH_SHORT).show();
        }
    }
}