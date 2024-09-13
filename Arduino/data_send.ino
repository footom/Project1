#include <DHT.h>
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <SPI.h>

#define   MQ_PIN     (A0)//設定 感測器接在A0
#define   RL_VALUE   (5) //定義 RL 電阻值 5K
#define   RO_CLEAN_AIR_FACTOR    (9.83) //RO在清潔空氣中電阻值
#define   CALIBARAION_SAMPLE_TIMES   (5) //定義較準時採樣時間 ms
#define   CALIBRATION_SAMPLE_INTERVAL (50)  //定義較準時採樣次數
#define   READ_SAMPLE_INTERVAL         (50)   //定義採樣次數
#define   READ_SAMPLE_TIMES            (5)    //定義採樣間隔時間
#define DHTPIN D2
#define DHTTYPE DHT11
#define isFlamePin D1

#define   GAS_LPG         (0)
#define   GAS_CO          (1)
#define   GAS_SMOKE       (2)
float       LPGCurve[3]   = {2.3,0.21,-0.47};   //LPG 的斜率                                               
float       COCurve[3]    = {2.3,0.72,-0.34};    //CO的斜率                                              
float       SmokeCurve[3] = {2.3,0.53,-0.44};    //Smoke的斜率                              
float       Ro            = 10;                 //Ro 初始化為10K
int isFlame = HIGH; //預設高電位

DHT dht(DHTPIN,DHTTYPE);

float humidityData; //濕度
float temperatureData; //溫度
float lpg, co, smoke;
const char* ssid = "your network";
const char* password = "your password";
char server[] = "your ips";

WiFiClient client; //建立ESP8266客戶端物件

void setup() {
  Serial.begin(9600);
  Ro = MQCalibration(MQ_PIN);   //校正感測器,開機時請確保空氣是乾淨的
  delay(10);
  dht.begin();
  //connect to wifi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while(WiFi.status() != WL_CONNECTED){ //嘗試連接
    delay(500);
    Serial.print(".");
  }
    Serial.println("");
    Serial.println("WiFi connected");
    
    //start the server
    //server.begin();
    Serial.println("Server started");
    Serial.print("IP Address:");
    Serial.print(WiFi.localIP());
    delay(1000);
    Serial.println("connecting...");
    pinMode(isFlamePin, INPUT);
}
//------------------------------------------------------------------------------//

void loop(){
  humidityData = dht.readHumidity();
  temperatureData = dht.readTemperature();
  lpg = MQGetGasPercentage(MQRead(MQ_PIN)/Ro,GAS_LPG);
  co = MQGetGasPercentage(MQRead(MQ_PIN)/Ro,GAS_CO);
  smoke = MQGetGasPercentage(MQRead(MQ_PIN)/Ro,GAS_SMOKE);
  Sending_To_phpmyadmindatabase();
  delay(5000); // interval
}


void Sending_To_phpmyadmindatabase(){   //連接mysql

    if (client.connect(server, 80)) {
      Serial.println("connected");
      // Make a HTTP request:
      isFlame = digitalRead(isFlamePin);
      Serial.print("hunidity=");
      Serial.print(humidityData);
      Serial.print("% ");
      
      Serial.print("&temperature=");
      Serial.print(temperatureData);
      Serial.print("度 ");
      
      Serial.print("&LPG: ");
      Serial.print(lpg);
      Serial.print("ppm   ");
  
      Serial.print("&CO: ");
      Serial.print(co);
      Serial.print("&ppm   ");
  
      Serial.print("&Smoke: ");
      Serial.print(smoke);
      Serial.print("ppm   ");
      
      Serial.print("&fire=");
      if (isFlame== LOW){
        Serial.print("1");
      }else{
        Serial.print("0");
      }
      Serial.println("");
      
      client.print("GET /test/connectDatabase.php?humidity=");     //YOUR URL 
      client.print(humidityData);
      client.print("&temperature=");  
      client.print(temperatureData);
      client.print("&lpg=");
      client.print(lpg);
      client.print("&co=");
      client.print(co);
      client.print("&smoke=");
      client.print(smoke);
      client.print("&fire=");
      if (isFlame== LOW){
        client.print(1);
      }else{
        client.println(0);
      }
      
      client.print(" ");      //SPACE BEFORE HTTP/1.1
      client.print("HTTP/1.1");
      client.println();
      client.println("Host: 192.168.43.198");
      client.println("Connection: close");
      client.println();
  }else{
    // if you didn't get a connection to the server:
    Serial.println("connection failed");
  }
}
//------------------------------------MQ2計算----------------------------------//
float MQResistanceCalculation(int raw_adc){
  return ( ((float)RL_VALUE*(1023-raw_adc)/raw_adc));
}

float MQCalibration(int mq_pin){
  int i;
  float val=0;

  for (i=0;i<CALIBARAION_SAMPLE_TIMES;i++) {            //take multiple samples
    val += MQResistanceCalculation(analogRead(mq_pin));
    delay(CALIBRATION_SAMPLE_INTERVAL);
  }
  val = val/CALIBARAION_SAMPLE_TIMES;                   //calculate the average value

  val = val/RO_CLEAN_AIR_FACTOR;                        //divided by RO_CLEAN_AIR_FACTOR yields the Ro
                                                        //according to the chart in the datasheet

  return val;
}           

float MQRead(int mq_pin){
  int i;
  float rs=0;

  for (i=0;i<READ_SAMPLE_TIMES;i++) {
    rs += MQResistanceCalculation(analogRead(mq_pin));
    delay(READ_SAMPLE_INTERVAL);
  }

  rs = rs/READ_SAMPLE_TIMES;

  return rs;
}

int MQGetGasPercentage(float rs_ro_ratio, int gas_id){
  if ( gas_id == GAS_LPG ) {
     return MQGetPercentage(rs_ro_ratio,LPGCurve);
  } else if ( gas_id == GAS_CO ) {
     return MQGetPercentage(rs_ro_ratio,COCurve);
  } else if ( gas_id == GAS_SMOKE ) {
     return MQGetPercentage(rs_ro_ratio,SmokeCurve);
  }  

  return 0;
}

int  MQGetPercentage(float rs_ro_ratio, float *pcurve){
  return (pow(10,( ((log(rs_ro_ratio)-pcurve[1])/pcurve[2]) + pcurve[0])));
}
