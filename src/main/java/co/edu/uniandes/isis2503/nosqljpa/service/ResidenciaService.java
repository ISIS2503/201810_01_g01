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
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IResidenciaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.ResidenciaLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.AlarmaConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ResidenciaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.AlarmaEntity;
import com.sun.istack.logging.Logger;
import java.util.ArrayList;
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

/**
 *
 * @author m.sicard10
 */
@Path("/residencia")
//@Secured({Role.administrador, Role.propietario, Role.yale, Role.seguridadPrivada})
@Produces(MediaType.APPLICATION_JSON)
public class ResidenciaService {
    
    private AlarmaConverter alarma1;
    
    private final IResidenciaLogic residenciaLogic;
    //private final IRoomLogic roomLogic; VA PROPIETARIO Y CERRADURA

    public ResidenciaService() {
        this.residenciaLogic = new ResidenciaLogic();
        //this.roomLogic = new RoomLogic(); VA PROPIETARIO Y CERRADURA
    }

    @POST
    @Secured({Role.administrador, Role.yale})
    public ResidenciaDTO add(ResidenciaDTO dto) {
        return residenciaLogic.add(dto);
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
    @Secured({Role.administrador, Role.yale})
    public ResidenciaDTO update(ResidenciaDTO dto) {
        return residenciaLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    public ResidenciaDTO find(@PathParam("id") String id) {
        return residenciaLogic.find(id);
    }
    
     @GET
    @Path("/{id}/alarmas/{mes}")
    public List<AlarmaDTO> findMes(@PathParam("id") String id, @PathParam("mes") int mes) 
    {
        ResidenciaDTO residencia = residenciaLogic.find(id);
        
        List<AlarmaEntity> alarmas = residencia.getAlarmas();
        List<AlarmaDTO> alarmasN= new ArrayList();
        
        for(int i =0;i<alarmas.size();i++)
        {
            AlarmaEntity alarma = alarmas.get(i);
            if(alarma.getFecha().getMonth()==mes)
            {
                alarma1= new AlarmaConverter();
               
                alarmasN.add( alarma1.entityToDto(alarma));
            }
        }
        
        
        
        
        
        
        return alarmasN;
    }

    @GET
    public List<ResidenciaDTO> all() {
        return residenciaLogic.all();
    }

    @DELETE
    @Secured({Role.administrador, Role.yale})
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            residenciaLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Residencia was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(AdministradorService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }
}
