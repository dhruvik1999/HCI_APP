package com.example.od_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(getApplicationContext() , Home.class));
//            }
//        } , 3000);

       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Socket socket = new Socket("192.168.43.178", 5555);
                   InputStreamReader streamReader= new InputStreamReader(socket.getInputStream());
                   BufferedReader reader= new BufferedReader(streamReader);

                   while(true){
                       String value= reader.readLine();
                       Log.v("MSG : ", value);
                   }

               }catch(Exception E){
                   E.printStackTrace();
               }
           }
       }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

/*

#include <Bridge.h>
#include <Console.h>
#include <Wire.h>
#include <YunServer.h>
#include <YunClient.h>

const int ledPin = 13;
YunServer server(5555);
YunClient client;
int lock = 1;

void setup() {
  Bridge.begin();

  server.noListenOnLocalhost();
  server.begin();

  Serial.begin(9600);
  Serial.println("Project has started");

}

void sendMessage(String a){
   if(client){
    Serial.println("connected");
    client.println(a);
    delay(2000);
  }
}

void loop() {

  if(lock){
    client = server.accept();
    if(client){
      Serial.println("Client connected successfully");
      lock=0;
    }
  }


  sendMessage("This is dhruvik");





}

 */
