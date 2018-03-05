      #include <Keypad.h>
      
      // pin led rojo
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
        int espera = 1000;
        
        //contador inicial
        int contador = 0;
        
        //contador tiempo pasado
        int contadorPasado = 0;
        
        // contraseñas
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
    
      
      void setup()
      {
       Serial.begin(9600);
       pinMode(ledRojo, OUTPUT);
       pinMode(pulsador, INPUT);
       pinMode(ledblue, OUTPUT);
       pinMode(ledred, OUTPUT);
       pinMode(ledgreen, OUTPUT);
       pinMode(movimiento, INPUT);
      }
      
      void loop()
      {
       int seMovio = digitalRead(movimiento);
       int pulsa = digitalRead(pulsador);
       char key = customKeypad.getKey();
       
       if(key){registrarDigito(key);
         if(contDig == tamanioCont){
           contDig = 0;
           if(!correcto()&&contErrores != 3){
             darColor('R');
             delay(1000);
             contErrores++;
           }else{
             contDig = 0;
             contErrores = 0;
             puertaAbierta = true;
           }
           if(contErrores == 3){
             if(!mensaje){
               Serial.println("0");//----------------------------------------------------------------------------------------------------
               mensaje = true;
             }
               delay(espera);
               darColor('R');
               contErrores = 0;
             }
           //Serial.println(String(contErrores));
           //Serial.println(String(correcto()));
         }
       }
       
       if(seMovio == 1){
        digitalWrite(ledRojo,HIGH);
        if(!mensaje){Serial.println("1");} //---------------------------------------------------------------------------------------------
        mensaje = true;
      }
        digitalWrite(ledRojo,LOW);
       if(contador >= espera){darColor('R');if(!mensaje){Serial.println("2");}mensaje = true;}//---------------------------------------------------
                       else if(puertaAbierta){
                           darColor('G'); 
                           contadorPasado = millis();
                           delay(10);
                           contador += millis()- contadorPasado ;
                           }
                         else{darColor('B');mensaje = false;}
       if(pulsa == 1){puertaAbierta = true;}
                       else if(!correcto()||!puertaAbierta){puertaAbierta = false;contador = 0;}
       
       //Serial.println(String(millis())+"  " + contadorPasado );
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
        if(key == '#'){contDig = 0;}else if (key == '*')
        {
          puertaAbierta = false;          
        }
        else{
        ingresados[contDig] = key;
        contDig ++;
        }
        //Serial.println(String(ingresados[0])+ " , " + String(ingresados[1]) + " , " + String(ingresados[2])+ " , " + String(ingresados[3]) );
      }
