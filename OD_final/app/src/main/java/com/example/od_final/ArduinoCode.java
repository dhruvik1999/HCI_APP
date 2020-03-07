package com.example.od_final;

public class ArduinoCode {
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
int cnt = 1;

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
    Serial.println("listenning ...");
    client = server.accept();
    if(client){
      Serial.println("Client connected successfully");
      lock=0;
    }
  }

  if(cnt%5==0) {
    sendMessage("do");
  }else{
    sendMessage("don't");
  }
  cnt+=1;






}




















 */
