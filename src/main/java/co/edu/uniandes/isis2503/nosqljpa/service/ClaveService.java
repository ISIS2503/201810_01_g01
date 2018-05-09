/*
 * The MIT License
 *
 * Copyright 2018 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.edu.uniandes.isis2503.nosqljpa.service;

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IClaveConverter;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IClaveLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.ClaveLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.ClaveConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ClaveDTO;
import com.sun.istack.logging.Logger;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.hibernate.ejb.criteria.expression.function.CurrentTimestampFunction;

/**
 *
 * @author m.sicard10
 */
@Path("/clave")
@Produces(MediaType.APPLICATION_JSON)
public class ClaveService 
{
    private final IClaveLogic claveLogic;
    
    private final IClaveConverter converClave;
    
    private final String topic = "home";
    private final String broker = "tcp://172.24.42.95:8083";
    
    MqttClient client;
     
      public ClaveService() {
        this.claveLogic= new ClaveLogic();
        this.converClave= new ClaveConverter();
        //this.roomLogic = new RoomLogic(); IRIA UNIADAD RESIDENCIAL
    }
      
    @GET
    public List<ClaveDTO> all() {
        return claveLogic.all();
    }
    
//    @GET
//    @Path("/validar/{id}")
//    public String validarHorario(@PathParam("id") String mensaje)
//    {
//         
//            String[] msg = mensaje.split(";");
//            String clave = msg[1];
//          ClaveDTO claveDTO= claveLogic.find(clave);
//          Date fechaInicial =   claveDTO.getFechaIngreso();
//          Date fechaFinal =   claveDTO.getFechaFinal();
//          Date fechaActual = new Date();
//          if (fechaActual.getHours()>fechaInicial.getHours()&&fechaActual.getHours()<fechaFinal.getHours())
//          {
//             //MQTT
//            String content = "A";
//            enviarMQTT(content);
//          }
//          else if(fechaActual.getHours()==fechaInicial.getHours())
//          {
//              if(fechaActual.getMinutes()>fechaInicial.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else if(fechaActual.getMinutes()==fechaInicial.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else
//              {
//                   //MQTT
//                String content = "R";
//                enviarMQTT(content);
//              }
//          }
//          else if(fechaActual.getHours()==fechaFinal.getHours())
//          {
//                 if(fechaActual.getMinutes()<fechaFinal.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else if(fechaActual.getMinutes()==fechaFinal.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else
//              {
//                   //MQTT
//                String content = "R";
//                enviarMQTT(content);
//              }
//          }
//          else
//          {
//                //MQTT
//                String content = "R";
//                enviarMQTT(content);
//          }
//         
//        return "se valido el horario";
//    }
    
   
    
    @GET
    @Path("/{id}")
    public ClaveDTO find(@PathParam("id") String id)
    {
        
      
        return claveLogic.find(id);
//        ClaveDTO dto=claveLogic.find(id);
//        String content = "P;"+dto.getId()+";"+dto.getClave();
//        enviarMQTT(content);
//        return claveLogic.find(id);
        
        
    }
    
    
    
    
      
    @POST
    public ClaveDTO add(ClaveDTO dto)
    {    
        //MQTT
        String content = "c;"+dto.getId()+";"+dto.getClave();
        enviarMQTT(content);
        
        return claveLogic.add(dto);
    }
    
    @PUT
    @Path("/{id}")
    public ClaveDTO update(@PathParam("id") String id, ClaveDTO dto) 
    {   
        //MQTT
        String content = "u;"+id+";"+dto.getClave();
        enviarMQTT(content);
       
        return claveLogic.update(dto);
    }
    
     @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        //MQTT
        String content = "d;"+id+"; ";
        enviarMQTT(content);
        
        //BD
        try {
            claveLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Administrador was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(AdministradorService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }
    
    
     @DELETE
  
    public Response deleteAll() {
        
        //MQTT
        String content = "dd; ; ";
        enviarMQTT(content);
        
        //BD
        try {
            List<ClaveDTO> claves = claveLogic.all();
            for(int i=0;i<claves.size();i++)
            {
                ClaveDTO act = claves.get(i);
                claveLogic.delete(act.getId());
            }
            
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Administrador was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(AdministradorService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }

    private void enviarMQTT(String content)
    {
        try
        {
        MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
             
        System.out.println("Connecting to broker: " + broker);
             
        client.connect(connOpts);
             
        System.out.println("Connected");
        System.out.println("Publishing message: "+content);
             
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(2);
        client.publish(topic, message);
             
        System.out.println("Message published");
             
        client.disconnect();
             
        System.out.println("Disconnected");
        //System.exit(0);
        } catch(MqttException me) {
             System.out.println("reason "+me.getReasonCode());
             System.out.println("msg "+me.getMessage());
             System.out.println("loc "+me.getLocalizedMessage());
             System.out.println("cause "+me.getCause());
             System.out.println("excep "+me);
             me.printStackTrace();
        }
    }
    
    

//    @Override
//    public void connectionLost(Throwable thrwbl) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
//    public void correr() 
//{	
//	try 
//	{
//		//CONECION MQTT
//    	client = new MqttClient("tcp://172.24.42.95:8083", MqttClient.generateClientId());
//    	client.setCallback(this);
//        client.connect();
//        client.subscribe("alarma");
//            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//    } 
//    catch (MqttException e) {
//         System.out.println("----------------------------------------------------------------------------------------------------------------");
//        e.printStackTrace();
//       
//    }
//}
//
//    @Override
//    public void messageArrived(String topic , MqttMessage message) throws Exception 
//    {
//            String mensa = message.toString();
//            String[] msg = mensa.split(";");
//            String clave = msg[1];
//          ClaveDTO claveDTO= claveLogic.find(clave);
//          Date fechaInicial =   claveDTO.getFechaIngreso();
//          Date fechaFinal =   claveDTO.getFechaFinal();
//          Date fechaActual = new Date();
//          if (fechaActual.getHours()>fechaInicial.getHours()&&fechaActual.getHours()<fechaFinal.getHours())
//          {
//             //MQTT
//            String content = "A";
//            enviarMQTT(content);
//          }
//          else if(fechaActual.getHours()==fechaInicial.getHours())
//          {
//              if(fechaActual.getMinutes()>fechaInicial.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else if(fechaActual.getMinutes()==fechaInicial.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else
//              {
//                   //MQTT
//                String content = "R";
//                enviarMQTT(content);
//              }
//          }
//          else if(fechaActual.getHours()==fechaFinal.getHours())
//          {
//                 if(fechaActual.getMinutes()<fechaFinal.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else if(fechaActual.getMinutes()==fechaFinal.getMinutes())
//              {
//                   //MQTT
//                String content = "A";
//                enviarMQTT(content);
//              }
//              else
//              {
//                   //MQTT
//                String content = "R";
//                enviarMQTT(content);
//              }
//          }
//          else
//          {
//                //MQTT
//                String content = "R";
//                enviarMQTT(content);
//          }
//         
//          
//           
//            
//            
//        
//        
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//     
//   
//
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken imdt) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
}



