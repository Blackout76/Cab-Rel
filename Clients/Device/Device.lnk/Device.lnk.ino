#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>


// adress mac of the shield
byte mac[] =  { 0x98, 0x4F, 0xEE, 0x05, 0x35, 0x20 };
// the arduino's Ip Adresse
IPAddress ip(192, 168, 1, 225);


//define IpAdress and port of Http Server
EthernetClient clientHttp;
IPAddress httpAdress(192, 168, 1, 50);
int httpPort = 800;
String adrServ;
String prtServ;

// define some values used by the panel and buttons
int lcd_key = 0;
int adc_key_in = 0;
#define btnRIGHT 0
#define btnUP 1
#define btnDOWN 2
#define btnLEFT 3
#define btnSELECT 4
#define btnNONE 5


void setup()
{
  Serial.begin(9600);
  //Configure 
  Ethernet.begin(mac, ip);

  //print out the IP address
  Serial.print("IP = ");
  Serial.println(Ethernet.localIP());
}


void loop()
{
  //Check if connect to server Http
  if (clientHttp.connected())
  {
     delay(1000);
     Serial.print("connecté");
  }
  else
  {
    delay(1000);
     Serial.print("Non connecté");
  }
  //Manage the button
  lcd_key = read_LCD_buttons(); // read the buttons
  switch (lcd_key) // depending on which button was pushed, we perform an action
  {
    case btnRIGHT:
      {
        break;
      }
    case btnLEFT:
      {
        break;
      }
    case btnUP:
      {
        break;
      }
    case btnDOWN:
      {
        break;
      }
    case btnSELECT:
      {
        Serial.print("Clique select");
        delay(1000);
         connectServerHttp(httpAdress, httpPort);
         closeConnectionHttp();
        break;
      }
    case btnNONE:
      {
        break;
      }
  }
}

//Function  Connect to Http server and get Ip and port of WebServer
void connectServerHttp(IPAddress ipServer, int portServer)
{

  String getHttp = "";
  adrServ = "";
  prtServ = "";
  char tmp;
  bool adr = false;

  int erreur = clientHttp.connect(httpAdress, httpPort);
  if (clientHttp.connected())
  {
    clientHttp.println("GET /deviceConnect HTTP/1.0");
    clientHttp.println();
    int cpt = 1;
    int i = 0;
    bool cond = false;
    while (clientHttp.available())
    {
      char c = clientHttp.read();
      Serial.print(c);
      if (adr)
      {
        if (c == ':')
        {
          cond = true;
        }
        else
        {
          if (cond == true) prtServ += c;

          else  adrServ += c;
        }
      }
      if (c == '/' and tmp == '/') adr = true;
      tmp = c;

    }

    Serial.println("adr :" + adrServ);
    Serial.print("prt: " + prtServ);

  }
  else
  {
    Serial.println("connection failed");
  }
}

// Function for Close Http Client
void closeConnectionHttp()
{
   //disconnect to Http
    clientHttp.stop();
    Serial.print("Connection to Http server is close");
}

// read the buttons
int read_LCD_buttons()
{
  adc_key_in = analogRead(0); // read the value from the sensor
  //Serial.print(adc_key_in);
  // my buttons when read are centered at these valies: 0, 144, 329, 504, 741
  // we add approx 50 to those values and check to see if we are close
  if (adc_key_in > 1000) return btnNONE; // We make this the 1st option for speed reasons since it will be the
  if (adc_key_in < 50) return btnRIGHT;
  if (adc_key_in < 195) return btnUP;
  if (adc_key_in < 380) return btnDOWN;
  if (adc_key_in < 555) return btnLEFT;
  if (adc_key_in < 790) return btnSELECT;
  return btnNONE; // when all others fail, return this...
}



