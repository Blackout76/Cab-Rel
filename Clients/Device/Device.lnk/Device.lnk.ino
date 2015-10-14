#include "Arduino.h"
#include <Ethernet.h>
#include <SPI.h>
#include <LiquidCrystal.h>


EthernetClient clientHttp;

//Init of LCd and KeyPad
// select the pins used on the LCD panel
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);
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

// adress mac of the shield
byte mac[] =   { 0x98, 0x4F, 0xEE, 0x05, 0x35, 0x20 }; 
// the arduino's Ip Adresse  
IPAddress ip(192, 168, 1, 225);
// the dns server ip
IPAddress dnServer(192, 168, 1, 1);
// the router's gateway address:
IPAddress gateway(192, 168, 1, 1);
// the subnet:
IPAddress subnet(255, 255, 255, 0);




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
  String getHttp= "";
  char tmp;
  bool adr = false;
  String adrServ;
  String prtServ;

  lcd.begin(16, 2); // start the library
  
  Serial.begin(9600);
  Ethernet.begin(mac, ip, dnServer, gateway, subnet);
  //print out the IP address
  Serial.print("IP = ");
  Serial.println(Ethernet.localIP());
  
  int erreur = clientHttp.connect("192.168.1.50", 800);
  if (clientHttp.connected()) 
  {
    clientHttp.println("GET /deviceConnect HTTP/1.0");
    clientHttp.println();
    int cpt = 1;
    int i=0;
     bool cond = false;
    while (clientHttp.available()) 
    {
      char c = clientHttp.read();
      if(adr)
      {
        if(c == ':')
        {
          cond = true;
        }
        else
        {
          if(cond == true)
          {
            prtServ+= c;
          }
          else
          {
            adrServ+=c;
          }
        }
      }
      if(c == '/' and tmp=='/') adr=true;
      Serial.print(c);
      tmp=c;
    }
    //disconnect to Http
    clientHttp.stop();

    Serial.println("adr :"+adrServ);
    Serial.print("prt: "+prtServ);
    

  } 
  else 
  {
    Serial.println("connection failed");
  }
  
}
void loop () {

   manageCabState();

  lcd.setCursor(0,1); // move to the begining of the second line
  //Manage the button
  lcd_key = read_LCD_buttons(); // read the buttons
  switch (lcd_key) // depending on which button was pushed, we perform an action
  {
    case btnRIGHT:
    {
      lcd.print("Refused   ");
      break;
    }
    case btnLEFT:
    {
      lcd.print("Accept ");
      break;
    }
    case btnUP:
    {
      lcd.print("UP      ");
      break;
    }
    case btnDOWN:
    {
      lcd.print("DOWN ");
      break;
    }
  
  }
}

void manageCabState()
{
   //display the cab's state
  lcd.setCursor(0,0); // move to the begining of the first line
  if(stateFreeCab) lcd.print("FREE");
  else lcd.print("BUSY");
}

  

