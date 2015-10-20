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

IPAddress ip(169, 254, 83, 94);
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
IPAddress httpAdress(169, 254, 83, 93);
int httpPort = 8080;

String adrServ;
int prtServ;
char* idCab = "32";
int i = 0;
String areaNow;
String streetNow;


int screenWidth = 16;
int screenHeight = 2;
int stringStart, stringStop = 0;
int scrollCursor = screenWidth;


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
bool doConnect = false;

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

  //print out the IP address
  Serial.print("IP = ");
  Serial.println(Ethernet.localIP());

}

void loop ()
{
  if(doConnect)
  wsClient.monitor();

  if (wsClient.connected())
  {

    if (stateFreeCab)
    {
      wsClient.send("{\"cmd\": \"cabInfo\"}");
      wsClient.onMessage(onMessage);
 
      displayInfo("FREE  d:0,0km","area :"+areaNow + ", street : " + streetNow);
    }
    else
    {
      lcd.print("BUSY");
    }

    wsClient.onError(onError);
  }
  else
  {
    lcd.setCursor(0, 0);
    lcd.print("Press Select to");
    lcd.setCursor(0, 1);
    lcd.print("    Connect");
    delay(500);
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
        doConnect = false;
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
        doConnect = true;
        connectServer(httpAdress, httpPort );
        delay(1500);
        break;
      }
    case btnNONE:
      {

      }
  }
}

void displayInfo(String l1,String l2)
{
 
  lcd.setCursor(0, 0);
  lcd.print(l1);
  lcd.setCursor(scrollCursor, 1);
  lcd.print(l2.substring(stringStart,stringStop));
  delay(300);
  lcd.clear();
  if(stringStart == 0 && scrollCursor > 0){
    scrollCursor--;
    stringStop++;
  } else if (stringStart == stringStop){
    stringStart = stringStop = 0;
    scrollCursor = screenWidth;
  } else if (stringStop == l2.length() && scrollCursor == 0) {
    stringStart++;
  } else {
    stringStart++;
    stringStop++;
  }
}

void connectServer(IPAddress Adress , int HttpPort )
{

  String getHttp = "";
  adrServ = "";
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
      getHttp += c;

      /*
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

       */
    }

    //disconnect to Http
    closeConnectionHttp();

    Serial.println("gethttp" + getHttp);
    int cc = getHttp.lastIndexOf("{");
    aJsonObject* root = aJson.parse(strToChar(getHttp.substring(cc)));

    if (root != NULL) {
      //Serial.println("Parsed successfully 1 " );
      aJsonObject* prt = aJson.getObjectItem(root, "prt");
      aJsonObject* addr = aJson.getObjectItem(root, "addr");
      if (prt != NULL)
      {
        prtServ = prt->valueint;

      }
      if (addr != NULL)
      {
        adrServ = addr->valuestring;
      }
    }


    Serial.println("                  adr :" + adrServ);
    Serial.print("                   prt: ");
    Serial.println(prtServ);

    //adding the chain end character
    adrServ.concat("\0");

    //connectWebSocketClient(adrServ, prtServ);
    connectWebSocketClient(adrServ, prtServ);
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
  Serial.println(addressServ);
  Serial.println(portServ);
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

void onClose(WebSocketClient client)
{
  lcd.clear();
  Serial.println("Disconnected to server");
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
      aJsonObject* loc_now = aJson.getObjectItem(cabInfo, "loc_now");

      if (loc_now != NULL)
      {
        //Serial.println("Parsed successfully 4 " );
        aJsonObject* area = aJson.getObjectItem(loc_now, "area");
        //Serial.println("Parsed successfully 4 " );
        aJsonObject* location = aJson.getObjectItem(loc_now, "location");

        if (area != NULL)
        {
          //Serial.println("Parsed successfully 5 " );
          Serial.println(areaNow);
          areaNow = area->valuestring;
        }
        if (location != NULL)
        {
          Serial.println(streetNow);
          streetNow = location->valuestring;
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

      if (location != NULL)
      {
        //Serial.println("Parsed successfully 3 " );
        aJsonObject* locationType = aJson.getObjectItem(location, "locationType");
        if (locationType != NULL)
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


