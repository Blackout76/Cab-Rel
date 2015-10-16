#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>


// adress mac of the shield
byte mac[] =  { 0x98, 0x4F, 0xEE, 0x05, 0x35, 0x20 };
// the arduino's Ip Adresse
IPAddress ip(192, 168, 1, 225);


void setup()
{
  Serial.begin(9600);
  Ethernet.begin(mac,ip);
  
  //print out the IP address
  Serial.print("IP = ");
  Serial.println(Ethernet.localIP());

}


void loop()
{

}

