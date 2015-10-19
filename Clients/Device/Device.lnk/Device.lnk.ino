#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>
#include <LiquidCrystal.h>
#include <WebSocketClient.h>
#include <ArduinoJson.h>

//EthernetClient clientHttp;
WebSocketClient wsClient;

//Init of LCd and KeyPad
// select the pins used on the LCD panel
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

// adress mac of the shield
byte mac[] =  { 0x98, 0x4F, 0xEE, 0x05, 0x35, 0x20 };

//IPAddress ip(169,254,83,94);
// the arduino's Ip Adresse
IPAddress ip(192, 168, 1, 225);
// the dns server ip
IPAddress dnServer(192, 168, 1, 1);
// the router's gateway address:
IPAddress gateway(192, 168, 1, 1);
// the subnet:
IPAddress subnet(255, 255, 255, 0);

//define IpAdress and port of Http Server

EthernetClient clientHttp;
IPAddress httpAdress(192, 168, 1, 50);
int httpPort = 800;
/*IPAddress httpAdress(169,254,83,93);
int httpPort = 8080;*/

String adrServ;
String prtServ;
char* idCab = "32";

// define some values used by the panel and buttons
int lcd_key = 0;
int adc_key_in = 0;
#define btnRIGHT 0
#define btnUP 1
#define btnDOWN 2
#define btnLEFT 3
#define btnSELECT 4
#define btnNONE 5

// Cab's State
bool stateFreeCab = true;




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

void setup()
{
  lcd.begin(16, 2); // start the library

  Serial.begin(9600);
  Ethernet.begin(mac, ip);
  //Ethernet.begin(mac, ip, dnServer, gateway,subnet);


  //print out the IP address
  Serial.print("IP = ");
  Serial.println(Ethernet.localIP());

 // connectServer(httpAdress,httpPort );
}

void loop ()
{
  wsClient.monitor();
  if (wsClient.connected())
  {
    //display the cab's state
    lcd.setCursor(0, 0); // move to the begining of the first line
    if (stateFreeCab)
    {
      lcd.print("FREE");
    /*  char* str1 = "{\"cmd\": \"cabInfo\", \"idCab\":";
      char* str2 = "}";
      char * str3 = (char *) malloc(1 + strlen(str1) + strlen(idCab) + strlen(str2) );
      strcpy(str3, str1);
      strcat(str3, idCab);
      strcat(str3, str2);*/
      wsClient.send("{\"cmd\": \"info\"}");
      wsClient.onMessage(onMessage);
    }
    else
    {
      lcd.print("BUSY");
    }

    wsClient.onError(onError);
    // wsClient.send("{\"cmd\": \"info\"}");

    // displayCabInfo("toto");
  }

  //Manage the button
  lcd_key = read_LCD_buttons(); // read the buttons
  switch (lcd_key) // depending on which button was pushed, we perform an action
  {
    case btnRIGHT:
      {
        //
        wsClient.send("");
        break;
      }
    case btnLEFT:
      {
        wsClient.send("");
        break;
      }
    case btnUP:
      {
        closeWebSocketClient();
        if (wsClient.connected()) Serial.println("connected again");
        else  Serial.println(" disconnect");
        break;
        break;
      }
    case btnDOWN:
      {
        break;
      }
    case btnSELECT:
      {
        connectServer(httpAdress, httpPort );
        break;
      }
    case btnNONE:
      {
       
      }
  }
}

void connectServer(IPAddress Adress , int HttpPort )
{

  String getHttp = "";
  adrServ = "";
  prtServ = "";
  char tmp;
  bool adr = false;

  int erreur = clientHttp.connect(httpAdress, httpPort);
  if (clientHttp.connected())
  {
    Serial.print("connect http");
    clientHttp.println("GET /deviceConnect HTTP/1.0");
    clientHttp.println();
    int cpt = 1;
    int i = 0;
    bool cond = false;
    while (clientHttp.available())
    {
      char c = clientHttp.read();
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

    //disconnect to Http
    closeConnectionHttp();

    Serial.println("adr :" + adrServ);
    Serial.print("prt: " + prtServ);

    //adding the chain end character
    adrServ.concat("\0");

    connectWebSocketClient(adrServ, prtServ.toInt());

  }
  else
  {
    Serial.println("connection failed");
  }
}

void closeConnectionHttp()
{
  clientHttp.stop();
}

void closeWebSocketClient( )
{
  wsClient.disconnect();
}

void connectWebSocketClient(String addressServ, int portServ)
{
  Serial.println("connect websocket");
  //Open the WebSocket Client
  wsClient.connect(strToChar(addressServ), portServ);

  wsClient.onOpen(onOpen);

  if (wsClient.connected()) Serial.print("connecter");
  else  Serial.print("non connecter");
}

void onOpen(WebSocketClient client)
{
  lcd.clear();
  Serial.println("Connected to server");
}

void onMessage(WebSocketClient client, char* message)
{
  Serial.print("Received: "); Serial.println(message);
  
  delay(5000);

}

void onError(WebSocketClient client, char* message) {
  Serial.println("onError()");
  Serial.print("ERROR: "); Serial.println(message);
}

char* strToChar(String s)
{
  unsigned int bufSize = s.length() + 1; //String length + null terminator
  char* ret = new char[bufSize];
  s.toCharArray(ret, bufSize);
  return ret;
}


