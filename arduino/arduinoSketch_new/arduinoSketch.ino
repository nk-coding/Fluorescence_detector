String lastMessage = "";
String separator = "%";
int delayBetweenValues = 50;
int ammountPointsPerMeasurement = 50;
unsigned long delayOn = 5000;
unsigned long delayOff = 500;

int startPin = A6; //pin measuring the inital resistance
int inputPin = A7;
int lightPin = 2;

boolean handShakeDone = false;

void setup() {
  // put your setup code here, to run once:
  pinMode(lightPin, OUTPUT);
  Serial.begin(57600);
  mySendString("RDY");
}


/**
 * Halts the programm until two bytes (that should represent the number of
 * and the delay between measurents) are recieved.
 */
void configuremeasurements(){
  boolean isRecieved = false;
  while(!isRecieved){
    if(Serial.available() > 6){
      String msg = "";
      isRecieved = true;
      delayBetweenValues = Serial.read();
      ammountPointsPerMeasurement = Serial.read();
      delayOn = ((long) Serial.read()) << 16; 
      delayOn = delayOn + (((long) Serial.read()) << 8);
      delayOn = delayOn + Serial.read();
      delayOff = Serial.read() << 7;
      delayOff = delayOff + Serial.read();
      //Debugging
      /*sendValue(delayBetweenValues);
      sendValue(ammountPointsPerMeasurement);
      sendValue(delayOn);
      sendValue(delayOff);*/
    }
  }
}

void sendValue(long value){
  String snd = separator;
  snd = value + snd;
  Serial.print(snd);
}

void mySendString(String string){
  String snd = separator;
  snd = string + snd;
  Serial.print(snd);
  }

void measure(int pin){
  digitalWrite(lightPin,HIGH);
  analogRead(pin); //Dummy measurement
  delay(delayOn);
  //measure
  for(int i = 0; i < ammountPointsPerMeasurement; i++){
      int analogValue = analogRead(pin);
      sendValue(analogValue);
      delay(delayBetweenValues);
    }
  delay(delayOff);
  digitalWrite(lightPin,LOW);
  mySendString("RDY");
  }

void measureDummy(int pin){
    analogRead(pin);
  }

void measureBase(int pin){
  measureDummy(pin);
  digitalWrite(lightPin,HIGH);
  delay(delayOn);
  for(int i = 0; i < ammountPointsPerMeasurement; i++){
      int analogValue = analogRead(pin);
      delay(delayBetweenValues);
      sendValue(analogValue);
    }
  delay(delayOff);
  digitalWrite(lightPin,LOW);  
  mySendString("RDY");
  }

void disconnectLightbringer(){
  handShakeDone = false;
  digitalWrite(lightPin,LOW);
  }

/*If there seems to be a bug with the microprocessor
 * delete the if statement when lastMessage == "BYE"
 */
void loop() {
  if(Serial.available() > 0){
    lastMessage = Serial.readStringUntil('%');
    if(lastMessage == "BYE"){
      disconnectLightbringer();
      }
    else if(lastMessage == "ACK"){
        handShakeDone = true;
      }
    else if(lastMessage == "CON"){
        configuremeasurements();
      }
    else if(lastMessage == "ME1"){
          measureBase(startPin);
      }
    else if (lastMessage == "MES"){
          measure(inputPin);
      }
    }
    if(!handShakeDone){
      mySendString("RDY");
      delay(100);
      }
}
