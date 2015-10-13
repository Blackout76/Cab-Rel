#include <Ethernet.h>
#include <SPI.h>

byte mac[] =   { 0x98, 0x4F, 0xEE, 0x05, 0x35, 0x20 }; 

IPAddress ip(192, 168, 1, 225);
EthernetClient client;
String getHttp= "";
char tmp;
bool adr = false;
String adrServ;
String prtServ;

void setup()
{  
  Serial.begin(9600);
  Ethernet.begin(mac, ip); // si dhcp activé
  Serial.print("Galileo IP address: ");
  Serial.println(Ethernet.localIP());
  
  int erreur = client.connect("192.168.1.50", 800);
  if (client.connected()) 
  {
    client.println("GET /deviceConnect HTTP/1.0");
    client.println();
    int cpt = 1;
     bool cond = false;
    while (client.available()) 
    {
      char c = client.read();
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
        //getHttp+=c;
      }
      if(c == '/' and tmp=='/') adr=true;
      Serial.print(c);
      tmp=c;
    }
    //Serial.print(getHttp);
    //adrServ = getHttp.substring(0,12);
    // prtServ = getHttp.substring(13);
     Serial.println("adr: "+adrServ);
     Serial.print("prt: "+prtServ);

  } 
  else {
    Serial.println("connection failed");
  }
  
}
void loop () {
   /* if(client.available()){
    char c = client.read();
    Serial.print(c);}*/
  }

