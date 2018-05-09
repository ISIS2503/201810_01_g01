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

import co.edu.uniandes.isis2503.nosqljpa.auth.AuthorizationFilter.Role;
import co.edu.uniandes.isis2503.nosqljpa.auth.Secured;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import com.sun.istack.logging.Logger;
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
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author m.sicard10
 */
@Path("/alarma")
//@Secured({Role.administrador, Role.propietario, Role.seguridadPrivada, Role.yale})
@Produces(MediaType.APPLICATION_JSON)
public class AlarmaService{
    
    private final String topic = "home";
    private final String broker = "tcp://172.24.42.95:8083";
    
    MqttClient client;
    
 private final IAlarmaLogic alarmaLogic;
    //private final IRoomLogic roomLogic; IRIA UNIADAD RESIDENCIAL

    public AlarmaService() {
        this.alarmaLogic = new AlarmaLogic();
        //this.roomLogic = new RoomLogic(); IRIA UNIADAD RESIDENCIAL
    }

    @POST
    public AlarmaDTO add(AlarmaDTO dto) {
        return alarmaLogic.add(dto);
    }
    
    
    @POST
    @Path("/{id}/silenciar")
    public String slienciarAlarma(@PathParam("id") String id) {
        String mensajeAPublicar = "S;"+id;
        enviarMQTT(mensajeAPublicar);
        return "se silencio la alarma con ID"+" "+id;
    }

    //@POST
    //@Path("{code}/rooms")
    //public RoomDTO addRoom(@PathParam("code") String code, RoomDTO dto) {
        //FloorDTO floor = floorLogic.findCode(code);
        //RoomDTO result = roomLogic.add(dto);
        //floor.addRoom(dto.getId());
        //floorLogic.update(floor);
        //return result;
    //}

    @PUT
    public AlarmaDTO update(AlarmaDTO dto) {
        return alarmaLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    public AlarmaDTO find(@PathParam("id") String id) {
        return alarmaLogic.find(id);
    }
    

    @GET
    public List<AlarmaDTO> all() {
        return alarmaLogic.all();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            alarmaLogic.delete(id);
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
    
}
