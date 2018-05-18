#include <Keypad.h>
#include <EEPROM.h>
//-------------------------------------------
#define SIZE_BUFFER_DATA       50
#include "WProgram.h"
void healthChek();
void enviarMensaje(String r);
void setup();
void loop();
void beep();
void teclado();
void confirmacion();
void revisarBateria();
void revisarMovimiento();
void darColor(char a);
boolean correcto();
void registrarDigito(char key);
void receiveData();
void processData();
int StringSplit(String sInput, char cDelim, String sParams[], int iMaxParams);
boolean compareKey(String key);
void addPassword(int val, int index);
void updatePassword(int val, int index);
void deletePassword(int index);
void deleteAllPasswords();
boolean     stringComplete = false;
String      inputString = "";
char        bufferData [SIZE_BUFFER_DATA];
//-------------------------------------------

//pin sonido
int sonido = 24;

// pin led rojo del movimiento
int ledRojo = 12;

// pin pulsador
int pulsador = 11;

// pin led rgb azul
int ledblue = 5;

// pin led rgb rojo
int ledred = 7;

// pin led rgb verde
int ledgreen = 6;

// pin sensor de movimiento
int movimiento = 16;

//valor estado movimiento
int val = 0;  

//iniciamos que estamos sin movimiento
int pirState = LOW; 


//pines del teclado

//Keypad filas
const byte ROWS = 4; 

//Keypad columnas
const byte COLS = 3;

char keys[ROWS][COLS] = {
  { 
    '1','2','3'
  }
  ,
  { 
    '4','5','6'
  }
  ,
  { 
    '7','8','9'
  }
  ,
  { 
    '*','0','#'
  }
};

//Keypad row pins definicion
byte rowPins[ROWS] = {
  17, 18, 19, 20
}; 

//Keypad column pins definicion
byte colPins[COLS] = {
  21, 22, 23
};

//indicador puerta abierta
boolean puertaAbierta = false;

// tiempo de espera
int espera = 5000;

//contador inicial
int contador = 0;

//contador tiempo pasado
int contadorPasado = 0;

// contrase\u00f1as
char contrasenias [4][4]= {
  {
    '1','2','3','4'
  }
  , 
  {
    '0','0','0','0'
  }
  , 
  {
    '1','9','9','6'
  }
  ,
  {
    '1','1','1','1'
  }
}; 

String alarmaSilenciosa = "2399";
// contador errores
int contErrores = 0;

// tamanio contrasenia
int tamanioCont = 4;

// cantidad contrasenias
int cantidadCont = 4;

//contador numero del digito
int contDig = 0;

//numeros ingresados
char ingresados[] = {
  ' ',' ',' ',' '
};

String ingre;

//mensaje enviado
boolean mensaje = false;

//Keypad library initialization
Keypad customKeypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS); 

//Minimum voltage required for an alert
const double MIN_VOLTAGE = 1.2;

//Battery measure pin
const int BATTERY_PIN = 31;

//Battery indicator
const int BATTERY_LED = 15;

//Current battery charge
double batteryCharge;

//mensaje de envio bateria baja
boolean bateriaMensaje = false;

//contador bateria baja
unsigned long contBateria = 0;

//contador health check
unsigned long conthealth = 0;

//estado de silenciado
boolean estadoS = false;

void healthChek()
{
  if( millis()- conthealth>30000)
  {
    conthealth+=30000;
    Serial.println("H");
  }
}

void enviarMensaje(String r)
{
  if(!estadoS)
  {
    Serial.println(r);
  }
}


void setup()
{
  Serial.begin(9600);
  pinMode(ledRojo, OUTPUT);
  pinMode(pulsador, INPUT);
  pinMode(ledblue, OUTPUT);
  pinMode(ledred, OUTPUT);
  pinMode(ledgreen, OUTPUT);
  pinMode(movimiento, INPUT);
  pinMode(sonido, OUTPUT);
  pinMode(BATTERY_LED,OUTPUT);
  pinMode(BATTERY_PIN,INPUT);
  addPassword(1111,1);
  addPassword(2399,2);
  addPassword(1234,3);
  addPassword(8080,4);
  addPassword(8280,5);
  addPassword(8081,6);
  addPassword(1996,7);
  addPassword(1976,8);
  ingre = "";

}

void loop()
{ 

  receiveData();
  processData();
  healthChek();
  
  revisarBateria();
  revisarMovimiento();
  teclado();

  int pulsa = digitalRead(pulsador);

  if(contador >= espera) {
    darColor('R');
    if(!mensaje) {
      enviarMensaje("2");
    }//---------------------------------------------------
    mensaje = true; 
    digitalWrite(sonido,HIGH);
  }
  else if(puertaAbierta) {
    darColor('G'); 
    contadorPasado = millis();
    delay(10);
    contador += millis()- contadorPasado ;
    digitalWrite(sonido,LOW);
  }
  else {
    darColor('B');
    mensaje = false;
    digitalWrite(sonido,LOW);
  }
  if(pulsa == 1) {
    puertaAbierta = true;
  }
  else if(!compareKey(ingre)||!puertaAbierta) {
    puertaAbierta = false;
    contador = 0;
  }
}

void beep()
{
  digitalWrite(sonido,HIGH);
  delay(100);
  digitalWrite(sonido,LOW);
  delay(100);
  digitalWrite(sonido,HIGH);
  delay(100);
  digitalWrite(sonido,LOW);
}

void teclado()
{
  char key = customKeypad.getKey();

  if(key) {
    registrarDigito(key);
    if(contDig == tamanioCont) {
      contDig = 0;
      if(!compareKey(ingre)&&contErrores != 3) {
        darColor('R');
        digitalWrite(sonido,HIGH);
        delay(1000);
        contErrores++;
        ingre="";
      }
      else {
        if(alarmaSilenciosa.equals(ingre))
        {
           enviarMensaje("4");//----------------------------------------------------------------------------------------------------
        }
        contDig = 0;
        contErrores = 0;
        puertaAbierta = true;
      }
      if(contErrores == 3) {
        if(!mensaje) {
          enviarMensaje("0");//----------------------------------------------------------------------------------------------------
          mensaje = true;
        }
        delay(espera);
        darColor('R');
        digitalWrite(sonido,HIGH);
        contErrores = 0;
      }
    }
  }
}

void confirmacion()
{
  Serial.println("V;" + ingre);
  while(!stringComplete)
  {
      receiveData();
  }
  processData();
  
}

void revisarBateria()
{
  batteryCharge = (analogRead(BATTERY_PIN)*5.4)/1024;

  if(batteryCharge<=MIN_VOLTAGE) {
    digitalWrite(BATTERY_LED,HIGH);
    if(millis() - contBateria > espera) {
      digitalWrite(sonido,HIGH);
      delay(1000);
      contBateria = millis();
    }

    if(!bateriaMensaje)
    {
      contBateria = millis();
      bateriaMensaje = true;
      enviarMensaje("3");//---------------------------------------------------
    }
  }
  else {
    digitalWrite(BATTERY_LED,LOW);
    bateriaMensaje = false;
    contBateria = 0;
  }
}

void revisarMovimiento() {
  val = digitalRead(movimiento);  // read input value
  if (val == HIGH) {            // check if the input is HIGH
    digitalWrite(ledRojo, HIGH);  // turn LED ON
    if (pirState == LOW) {
      // we have just turned on
      enviarMensaje("1");//-----------------------------------------------
      // We only want to print on the output change, not state
      pirState = HIGH;
    }
  } 
  else {
    digitalWrite(ledRojo, LOW); // turn LED OFF
    if (pirState == HIGH) {
      pirState = LOW;
    }
  }
}

void darColor(char a)
{
  if(a == 'R')
  {
    analogWrite(ledblue,255);
    analogWrite(ledred,0);
    analogWrite(ledgreen,255);
  }
  if(a == 'G')
  {
    analogWrite(ledblue,255);
    analogWrite(ledred,255);
    analogWrite(ledgreen,0);
  }
  if(a == 'B')
  {
    analogWrite(ledblue,0);
    analogWrite(ledred,255);
    analogWrite(ledgreen,255);
  }
}

boolean correcto() {
  boolean resp;
  for(int i=0; i< cantidadCont;i++ ) {
    resp = true;
    for(int j=0; j< tamanioCont;j++ ) {
      if(contrasenias[i][j] != ingresados[j] ) {
        resp = false;
        break;
      }
    }
    if(resp)return resp;
  }
  return false;
}

void registrarDigito(char key) {
  if(key == '#') {
    contDig = 0;
  }
  else if (key == '*')
  {
    puertaAbierta = false;
    ingre="";
  }
  else {
    ingresados[contDig] = key;
    ingre = ingre + key;
    //Serial.println(ingre);
    contDig ++;
    if(ingre.length()>4)
    {
     ingre = ""; 
    }
  }
  //Serial.println(String(ingresados[0])+ " , " + String(ingresados[1]) + " , " + String(ingresados[2])+ " , " + String(ingresados[3]) );
}

//comunicacnon con wifi---------
void receiveData() {
  while (Serial.available()>0) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    if (inChar == '\n') {
      inputString.toCharArray(bufferData, SIZE_BUFFER_DATA);
      stringComplete = true;
    }
  }
}

void processData() {
  if (stringComplete) {
    // Implementaci\u00f3n...
    //Serial.println(inputString);
      inputString.trim();
      if(inputString.startsWith("S"))
    {
        estadoS = !estadoS;
        beep();
    }
    if(inputString.equals("R"))
    {
        darColor('R');
        digitalWrite(sonido,HIGH);
        delay(1000);
        contErrores++;
        ingre=""; 
    }
    if(inputString.equals("A"))
    {
        //Serial.println(ingre);
        puertaAbierta = true;
        //Serial.println("abrio");
        //beep();
    }else{
    
    String s[3];
    StringSplit(inputString,';',s,3);
    String ress = s[0];
    String ress2 = s[1];
    String ress3 = s[2];
    //Serial.println(ress);
    //Serial.println(ress2);
    //Serial.println(ress3);
    
    if(ress=="C")
        {
          //Serial.println("cree");
          int index = ress2.toInt();
          int val = ress3.toInt();
          addPassword(val, index);
          beep();
        }
        if(ress=="U")
        {
          int index = ress2.toInt();
          int val = ress3.toInt();
          updatePassword(val,index);
          beep();
        }
        if(ress=="D"&&compareKey(ress3))
        {
          int index = ress2.toInt();
          int val = ress3.toInt();
          deletePassword(index);
          beep();
        }
        if(ress=="DD"&&compareKey(ress3))
        {
          deleteAllPasswords();
          beep();
        }
    }
    inputString = "";
    stringComplete = false;
  }
}

//-------------------------------------------------------------

int StringSplit(String sInput, char cDelim, String sParams[], int iMaxParams)
{
    int iParamCount = 0;
    int iPosDelim = 0;
    int iPosStart = 0;

    do {
      //Serial.println(iPosDelim);
      //Serial.println(iPosStart);
        // Searching the delimiter using indexOf()
        iPosDelim = sInput.indexOf(cDelim,iPosStart);
        if (iPosDelim > 0) {
            // Adding a new parameter using substring() 
            sParams[iParamCount] = sInput.substring(iPosStart,iPosDelim);
            iParamCount++;
            // Checking the number of parameters
            if (iParamCount >= iMaxParams) {
                return (iParamCount);
            }
            iPosStart = iPosDelim + 1;
        }
    } while (iPosDelim >= 0);
    if (iParamCount < iMaxParams) {
        // Adding the last parameter as the end of the line
        sParams[iParamCount] = sInput.substring(iPosStart);
        iParamCount++;
    }

    return (iParamCount);
}

// Method that compares a key with stored key
boolean compareKey(String key) {
  int acc = 3;
  int codif, arg0, arg1; 
  for(int i=0; i<3; i++) {
    codif = EEPROM.read(i);
    while(codif!=0) {
      if(codif%2==1) {
        arg0 = EEPROM.read(acc);
        arg1 = EEPROM.read(acc+1)*256;
        arg1+= arg0;
        if(String(arg1)==key) {
          return true;
        }
      }
      acc+=2;
      codif>>=1;
    }
    acc=(i+1)*16+3;
  }
  return false;
}
//Method that adds a password in the specified index
void addPassword(int val, int index) {
  byte arg0 = val%256;
  byte arg1 = val/256;
  EEPROM.write((index*2)+3,arg0);
  EEPROM.write((index*2)+4,arg1);
  byte i = 1;
  byte location = index/8;
  byte position = index%8;
  i<<=position;
  byte j = EEPROM.read(location);
  j |= i;
  EEPROM.write(location,j);
}

//Method that updates a password in the specified index
void updatePassword(int val, int index) {
  byte arg0 = val%256;
  byte arg1 = val/256;
  EEPROM.write((index*2)+3,arg0);
  EEPROM.write((index*2)+4,arg1);
}

//Method that deletes a password in the specified index
void deletePassword(int index) {
  byte i = 1;
  byte location = index/8;
  byte position = index%8;
  i<<=position;
  byte j = EEPROM.read(location);
  j ^= i;
  EEPROM.write(location,j);
}

//Method that deletes all passwords
void deleteAllPasswords() {
  //Password reference to inactive
  EEPROM.write(0,0);
  EEPROM.write(1,0);
  EEPROM.write(2,0);
}


