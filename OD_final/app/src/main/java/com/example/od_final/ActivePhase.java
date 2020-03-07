package com.example.od_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ActivePhase extends AppCompatActivity {

    private Button start;
    DataHelper dataHelper;
    Socket socket;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_phase);

        initiallization();
        requestPermission();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataHelper.isAlive()==false) {
                    dataHelper.start();
                } else {
                    dataHelper.interrupt();
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initiallization() {
        start = this.findViewById(R.id.b_start);
        dataHelper = new DataHelper();
        client =  LocationServices.getFusedLocationProviderClient(this);
    }

    public class DataHelper extends Thread {

        @Override
        public void run() {
            super.run();

            try {
                socket = new Socket("192.168.43.178", 5555);
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);

                while (true) {
                        String value = reader.readLine();
                    Log.v("MSG : ", value);
                    if(value.equals("do")){
                        processData();
                    }
                }

            } catch (Exception E) {
                E.printStackTrace();
            }
        }


    }

    private void processData(){
        client.getLastLocation().addOnSuccessListener(ActivePhase.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    //Toast.makeText(getApplicationContext() , location.toString(), Toast.LENGTH_SHORT).show();
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    //Toast.makeText( getApplicationContext() , latitude + " " + longitude + " : ", Toast.LENGTH_SHORT ).show();
                    updateData( latitude , longitude );
                    //getData();
                }
            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions( this , new String[]{ACCESS_FINE_LOCATION},1  );
    }


    private void updateData( double latitude, double longitude ){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.thingspeak.com/update?api_key=OAPXZ73I07XJUHH3&field3="+ latitude +"&field4="+longitude;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Toast.makeText( getApplicationContext(), "Response is: "+ response.substring(0,500) , Toast.LENGTH_SHORT ).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( getApplicationContext(), "volly error : write" , Toast.LENGTH_SHORT ).show();                    }
        });
        queue.add(stringRequest);
    }

}
