#include <Keypad.h>
#include <EEPROM.h>
//-------------------------------------------
#define SIZE_BUFFER_DATA       50
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

// contraseñas
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
  updatePassword(2222,1);

}

void loop()
{
  ingre = "";
  ingre.concat(((String)ingresados[0]).toInt());
  ingre.concat(((String)ingresados[1]).toInt());
  ingre.concat(((String)ingresados[2]).toInt());
  ingre.concat(((String)ingresados[3]).toInt());
  

  receiveData();
  processData();

  revisarBateria();
  revisarMovimiento();
  teclado();

  int pulsa = digitalRead(pulsador);

  if(contador >= espera) {
    darColor('R');
    if(!mensaje) {
      Serial.println("2");
      Serial1.println("2");
    }//---------------------------------------------------
    mensaje = true; 
    digitalWrite(sonido,HIGH);
  }
  else if(puertaAbierta) {
    darColor('G'); 
    contadorPasado = millis();
    delay(10);
    contador += millis()- contadorPasado ;
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
      }
      else {
        contDig = 0;
        contErrores = 0;
        puertaAbierta = true;
      }
      if(contErrores == 3) {
        if(!mensaje) {
          Serial.println("0");
          Serial1.println("0");//----------------------------------------------------------------------------------------------------
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
      Serial.println("3");//---------------------------------------------------
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
      Serial.println("1");//-----------------------------------------------
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
  }
  else {
    ingresados[contDig] = key;
    contDig ++;
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
    // Implementación...
    //Serial.println(inputString);

    //String ress = pp(inputString);


    String s[3];
    
    StringSplit(inputString,';',s,3);
    String ress = s[0];
    String ress2 = s[1];
    String ress3 = s[2];
    
    if(ress=="C")
        {
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
        if(ress=="D")
        {
          int index = ress2.toInt();
          int val = ress3.toInt();
          deletePassword(index);
          beep();
        }
        if(ress=="DD")
        {
          deleteAllPasswords();
          beep();
        }
    
    stringComplete = false;
  }
}

//-------------------------------------------------------------

int StringSplit(String sInput, char cDelim, String sParams[], int iMaxParams)
{
    int iParamCount = 0;
    int iPosDelim, iPosStart = 0;

    do {
        // Searching the delimiter using indexOf()
        iPosDelim = sInput.indexOf(cDelim,iPosStart);
        if (iPosDelim > (iPosStart+1)) {
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

