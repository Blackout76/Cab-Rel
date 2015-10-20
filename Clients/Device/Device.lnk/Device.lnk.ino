#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>
#include <LiquidCrystal.h>
#include <WebSocketClient.h>
#include <aJSON.h>

//EthernetClient clientHttp;
WebSocketClient wsClient;

//Init of LCd and KeyPad
// select the pins used on the LCD panel
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

// adress mac of the shield
byte mac[] =  { 0x98, 0x4F, 0xEE, 0x05, 0x35, 0x20 };

IPAddress ip(169,254,83,94);
// the arduino's Ip Adresse
//IPAddress ip(192, 168, 1, 225);
// the dns server ip
/*IPAddress dnServer(192, 168, 1, 1);
// the router's gateway address:
IPAddress gateway(192, 168, 1, 1);
// the subnet:
IPAddress subnet(255, 255, 255, 0);*/

//define IpAdress and port of Http Server

EthernetClient clientHttp;
/*IPAddress httpAdress(192, 168, 1, 50);
int httpPort = 800;*/
IPAddress httpAdress(169,254,83,93);
int httpPort = 8080;

String adrServ;
String prtServ;
char* idCab = "32";

String areaNow;
String streetNow;

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


// function definitions
void parseJson(char *jsonString) ;
int read_LCD_buttons();
void connectServer(IPAddress Adress , int HttpPort );
void closeConnectionHttp();

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
  connectServer(httpAdress,8080);

  //Open the WebSocket Client
  wsClient.connect("169.254.83.93", 2589);
  wsClient.onOpen(onOpen);
}

void loop ()
{
  wsClient.monitor();

  if (wsClient.connected())
  {

    if (stateFreeCab)
    {
      lcd.setCursor(0, 0); // move to the begining of the first line
      lcd.print("FREE");
      /*  char* str1 = "{\"cmd\": \"cabInfo\", \"idCab\":";
        char* str2 = "}";
        char * str3 = (char *) malloc(1 + strlen(str1) + strlen(idCab) + strlen(str2) );
        strcpy(str3, str1);
        strcat(str3, idCab);
        strcat(str3, str2);*/
      wsClient.send("{\"cmd\": \"cabInfo\"}");
      wsClient.onMessage(onMessage);
      lcd.setCursor(0, 1);
      //lcd.print("Salut");
      Serial.println(areaNow);
      lcd.print(areaNow + ", " + streetNow);
      lcd.scrollDisplayLeft();
      //display the cab's state
      lcd.setCursor(0, 0); // move to the begining of the first line
      lcd.print("FREE");
      delay(1000);
    }
    else
    {
      lcd.print("BUSY");
    }

    wsClient.onError(onError);
  }
  else
  {
    Serial.print("deconnecter du serveur du con");
  }
  

  //Manage the button
  lcd_key = read_LCD_buttons(); // read the buttons
  switch (lcd_key) // depending on which button was pushed, we perform an action
  {
    case btnRIGHT:
      {

        wsClient.send("{\"cmd\":\"requestAnswer\",\"idRequest\":\"D32\",\"answer\":\"no\"}");
        break;
      }
    case btnLEFT:
      {
        wsClient.send("{\"cmd\":\"requestAnswer\" ,\"idRequest\": \"D32\",\"answer\":\"yes\"}");
        break;
      }
    case btnUP:
      {
        closeWebSocketClient();
        if (wsClient.connected()) Serial.println("connected again");
        else  Serial.println(" disconnect");
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
  Serial.println(HttpPort);
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

    //disconnect to Http
    
    closeConnectionHttp();
    Serial.println("adr :" + adrServ);
    Serial.print("prt: " + prtServ);

    //adding the chain end character
    adrServ.concat("\0");

   // connectWebSocketClient(adrServ, prtServ.toInt());

  }
  else
  {
    Serial.println("connection failed");
  }
}

void closeConnectionHttp()
{
  Serial.println("Deconecter de http");
  clientHttp.stop();
}

void closeWebSocketClient( )
{
  Serial.println("Deconecter de Web");
  wsClient.disconnect();
}

void connectWebSocketClient(String addressServ, int portServ)
{
  Serial.println("tentative de connect websocket");
  //Open the WebSocket Client
  wsClient.connect(strToChar(addressServ), portServ);
  wsClient.connect(strToChar(addressServ), 2589);
  wsClient.onOpen(onOpen);

  if (wsClient.connected()) Serial.print("connecter");
  else  Serial.print("non connecter");
}

void onOpen(WebSocketClient client)
{
  lcd.clear();
  Serial.println("Connected to server");
}

void onClose(WebSocketClient client)
{
  lcd.clear();
  Serial.println("Connected to server");
}

void onMessage(WebSocketClient client, char* message)
{
  Serial.println("Received: "); Serial.println(message);

  parseJson(message);

}

void parseJson(char *jsonString)
{
  aJsonObject* root = aJson.parse(jsonString);
  if (root != NULL)
  {
    //Serial.println("Parsed successfully 2 " );
    aJsonObject* cabInfo = aJson.getObjectItem(root, "cabInfo");

    if (cabInfo != NULL)
    {
      //Serial.println("Parsed successfully 3 " );
      aJsonObject* locnow = aJson.getObjectItem(cabInfo, "locnow");

      if (locnow != NULL)
      {
        //Serial.println("Parsed successfully 4 " );
        aJsonObject* area = aJson.getObjectItem(locnow, "area");
        //Serial.println("Parsed successfully 4 " );
        aJsonObject* street = aJson.getObjectItem(locnow, "street");

        if (area != NULL)
        {
          //Serial.println("Parsed successfully 5 " );
          areaNow = area->valuestring;
        }
        if (street != NULL)
        {
          //Serial.println("Parsed successfully 5 " );
          streetNow = street->valuestring;
        }
      }

      //Serial.println("Parsed successfully 3 " );
      aJsonObject* request = aJson.getObjectItem(cabInfo, "request");

      if (request != NULL)
      {
        aJsonObject* area = aJson.getObjectItem(request, "area");
        aJsonObject* street = aJson.getObjectItem(request, "street");

        if (area != NULL)
        {
          Serial.println( area->valuestring);
        }
        if (street != NULL)
        {
          areaNow = street->valuestring;
        }
        else
        {
          Serial.println("street est null");
          Serial.print(request->valuestring);
        }
      } else {
        Serial.println("request est null");
      }
    }
    
    //Serial.println("Parsed successfully 2 " );
    aJsonObject* cabQueue = aJson.getObjectItem(root, "cabQueue");
 if (cabQueue != NULL)
    {
      //Serial.println("Parsed successfully 3 " );
      aJsonObject* area = aJson.getObjectItem(cabQueue, "area");

       //Serial.println("Parsed successfully 3 " );
      aJsonObject* location = aJson.getObjectItem(cabQueue, "location");

      if(location!= NULL)
      {
         //Serial.println("Parsed successfully 3 " );
          aJsonObject* locationType = aJson.getObjectItem(location, "locationType");
          if(locationType != NULL)
          {
        /*    if(locationType->valuestring.equals("street"))
            {
              
            }
            else
            {
              
            }*/
          }
      }
    }

  }
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


