#include "WebSocketsServer.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266WebServer.h>
#include <ArduinoJson.h>
#include <WS2812FX.h>

#define STRIP_PIN 2             // PIN D4 on ESP-8266
#define STRIP_LED_NUMBER 57

WiFiClientSecure wifiClient;
ESP8266WiFiMulti wifiMulti;
DynamicJsonDocument doc(512);
StaticJsonDocument<200> staticDoc;
WS2812FX strip = WS2812FX(STRIP_LED_NUMBER, STRIP_PIN, NEO_GRB + NEO_KHZ800);
HTTPClient sender;
ESP8266WebServer server(80);
WebSocketsServer webSocket = WebSocketsServer(81);

void setup() {
  Serial.begin(115200);
  wifiMulti.addAP("", "");
  wifiMulti.addAP("", "");

  while (wifiMulti.run() != WL_CONNECTED) {
    delay(1000);
  }
  WiFi.mode(WIFI_STA);
  Serial.println();
  Serial.print("Local IP: ");
  Serial.println(WiFi.localIP());

  webSocket.begin();
  webSocket.onEvent(webSocketEvent);
  server.begin();
  sendStatus();
  
  strip.init();
  strip.setBrightness(255);
  strip.setColor(0, 255, 0);
  strip.start();
  Serial.println("Setup done!");
}

void loop() {
  webSocket.loop();
  server.handleClient();
  strip.service();
}

void webSocketEvent(uint8_t num, WStype_t type, uint8_t *payload, size_t welength) {
  switch(type) {
    case WStype_CONNECTED: {
      IPAddress ip = webSocket.remoteIP(num);
      Serial.printf("[%u] Connection from ", num);
      Serial.println(ip.toString());
    }
    break;

    case WStype_TEXT:
      Serial.printf("[%u] Text: %s\n", num, payload);
      webSocket.sendTXT(num, payload);
      String value = (const char *)payload;
      handleRequest(value);
      break;
  }
}

void handleRequest(String value) {
  stopStrip();
  DeserializationError error = deserializeJson(doc, value);
  uint8_t currentMode = doc["mode"];
  uint8_t redColor = doc["red"];
  uint8_t greenColor = doc["green"];
  uint8_t blueColor = doc["blue"];
  if(currentMode != 0) {
    strip.setMode(currentMode);
  }
  if(redColor != 0 || greenColor != 0 || blueColor != 0) {
    strip.setColor(redColor, greenColor, blueColor);
  }
  strip.start();
}

void stopStrip() {
  strip.stop();
  strip.strip_off();
  strip.setMode(NO_OPTIONS);
}

void sendStatus() {
  wifiClient.setInsecure();
  wifiClient.connect("https://led-rest.holfelder.cloud", 443);
  if (sender.begin(wifiClient, "")) {
    sender.addHeader("Content-Type", "application/json");

    const size_t CAPACITY = JSON_OBJECT_SIZE(10);
    StaticJsonDocument<CAPACITY> doc;

    String jsonOutput;
    String ip = WiFi.localIP().toString();
    String mac = WiFi.macAddress();

    JsonObject object = doc.to<JsonObject>();
    object["address"] = ip;
    object["mac"] = mac;
    serializeJson(doc, jsonOutput);

    int httpCode = sender.PATCH(jsonOutput);

    if (httpCode > 0) {
      if (httpCode == HTTP_CODE_OK) {
        String payload = sender.getString();
        Serial.println(payload);
      }
    } else {
      String payload = sender.getString();
      Serial.println(payload);
      Serial.printf("HTTP-Error: ", sender.errorToString(httpCode).c_str());
    }
    sender.end();
  } else {
    Serial.printf("HTTP-Verbindung konnte nicht hergestellt werden!");
  }
}
