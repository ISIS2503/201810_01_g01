      #include <Keypad.h>
      //-------------------------------------------
      #define SIZE_BUFFER_DATA       50
      #include "WProgram.h"
void setup();
void loop();
void teclado();
void revisarBateria();
void revisarMovimiento();
void darColor(char a);
boolean correcto();
void registrarDigito(char key);
void receiveData();
void processData();
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
       { '1','2','3' },
       { '4','5','6' },
       { '7','8','9' },
       { '*','0','#' }
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
        char contrasenias [4][4]={{'1','2','3','4'}, 
                                  {'0','0','0','0'}, 
                                  {'1','9','9','6'},
                                  {'1','1','1','1'}}; 
        
        
        // contador errores
        int contErrores = 0;
        
        // tamanio contrasenia
        int tamanioCont = 4;
        
        //contador numero del digito
        int contDig = 0;
        
        //numeros ingresados
        char ingresados[] = {' ',' ',' ',' '};
        
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
      }
      
      void loop()
      {
       receiveData();
       processData();
       
       revisarBateria();
       revisarMovimiento();
       teclado();
      
       int pulsa = digitalRead(pulsador);
       
       if(contador >= espera){
           darColor('R');
           if(!mensaje){Serial.println("2");Serial1.println("2");}//---------------------------------------------------
           mensaje = true; 
           digitalWrite(sonido,HIGH);
          }
                       else if(puertaAbierta){
                           darColor('G'); 
                           contadorPasado = millis();
                           delay(10);
                           contador += millis()- contadorPasado ;
                           }
                         else{darColor('B');
                               mensaje = false;
                               digitalWrite(sonido,LOW);
                             }
       if(pulsa == 1){puertaAbierta = true;}
                       else if(!correcto()||!puertaAbierta){puertaAbierta = false;contador = 0;}
       
      }

      void teclado()
      {
               char key = customKeypad.getKey();
       
       if(key){registrarDigito(key);
         if(contDig == tamanioCont){
           contDig = 0;
           if(!correcto()&&contErrores != 3){
             darColor('R');
             digitalWrite(sonido,HIGH);
             delay(1000);
             contErrores++;
           }else{
             contDig = 0;
             contErrores = 0;
             puertaAbierta = true;
           }
           if(contErrores == 3){
             if(!mensaje){
               Serial.println("0");Serial1.println("0");//----------------------------------------------------------------------------------------------------
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
              if(millis() - contBateria > espera){
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
            
      void revisarMovimiento(){
              val = digitalRead(movimiento);  // read input value
        if (val == HIGH) {            // check if the input is HIGH
          digitalWrite(ledRojo, HIGH);  // turn LED ON
          if (pirState == LOW) {
            // we have just turned on
            Serial.println("1");//-----------------------------------------------
            // We only want to print on the output change, not state
            pirState = HIGH;
          }
        } else {
          digitalWrite(ledRojo, LOW); // turn LED OFF
          if (pirState == HIGH){
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
      
      boolean correcto(){
        boolean resp;
        for(int i=0; i< tamanioCont;i++ ){
          resp = true;
          for(int j=0; j< tamanioCont;j++ ){
            if(contrasenias[i][j] != ingresados[j] ){resp = false;break;}
          }
          if(resp)return resp;
        }
        return false;
      }
      
      void registrarDigito(char key){
        if(key == '#'){contDig = 0;}
        else if (key == '*')
        {
          puertaAbierta = false;          
        }
        else{
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
        // Implementaci\u00f3n...
        //Serial.println(inputString);
      }
    }

