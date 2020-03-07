package com.example.od_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

public class PassivePhase extends FragmentActivity implements OnMapReadyCallback {

    Double[] latitude,longitude;
    int size = 0;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passive_phase);
        getData();




    }

    private void getData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.thingspeak.com/channels/1007907/feeds.json?api_key=SCC53U4QXQQW2D9P";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Toast.makeText( getApplicationContext(), "Response is: "+ response.substring(0,500) , Toast.LENGTH_SHORT ).show();
                        Log.v("RES",response);

                        Gson g = new Gson();
                        Example example = g.fromJson(response, Example.class);
                        size = example.getFeeds().size();
                        latitude = new Double[size];
                        longitude = new Double[size];

                        for( int i=0;i<size;i++ ){
                            latitude[i] = Double.valueOf(example.getFeeds().get(i).getField3());
                            longitude[i] = Double.valueOf(example.getFeeds().get(i).getField4());
                            Log.v("latLong ",latitude[i] +" "+ longitude[i]);
                        }

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(PassivePhase.this);

                        //example.getFeeds().get(0).getField1()

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( getApplicationContext(), "volly error : read" , Toast.LENGTH_SHORT ).show();                    }
        });
        queue.add(stringRequest);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        LatLng pnt = new LatLng( 13.0069243,74.7956938 );
//        map.addMarker(new MarkerOptions().position(pnt).title("Obs"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(pnt));

        for(int i=0;i<size;i++){
            Log.v("Shown  ", "Success");
            LatLng pnt = new LatLng( latitude[i], longitude[i] );
            map.addMarker(new MarkerOptions().position(pnt).title("Obs"+i));
            map.moveCamera(CameraUpdateFactory.newLatLng(pnt));
        }

    }
}
