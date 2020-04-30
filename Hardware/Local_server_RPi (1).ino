[16:56, 09/03/2020] Trisha Robovitics: #define BLYNK_PRINT Serial
#include <WiFi.h>
#include <BlynkSimpleEsp32.h>
char auth[] = "1g_pVuAwDI3E4PvkOEfgo7EYUlfqui9r";          
char ssid[] = "Inspiron";    
char pass[] = "Android1";
char server[] = "192.168.43.205";  
int ledpin = 16; // D1(gpio16)
int button = 4; //D2(gpio4)
int buttonState=0; 
int i;
    
void setup() 
{
  Serial.begin(115200);
  pinMode(ledpin, OUTPUT);
  pinMode(button, INPUT);
  Blynk.begin(auth,ssid, pass,IPAddress(192,168,43,205),8080);
}
void loop()
{  
Blynk.run();
buttonState=digitalRead(button); 
 if (buttonState == 1)
 {
 digitalWrite(ledpin, HIGH); 
 delay(200);
 }
 if (buttonState==0)
 {
 digitalWrite(ledpin, LOW);   
 delay(200);
 }
}
