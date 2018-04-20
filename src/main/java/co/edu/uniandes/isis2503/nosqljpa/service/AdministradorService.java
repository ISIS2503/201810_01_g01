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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAdministradorConverter;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAdministradorLogic;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaConverter;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.AdministradorLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.AdministradorConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.converter.AlarmaConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AdministradorDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.AlarmaEntity;
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

/**
 *
 * @author m.sicard10
 */
@Path("/administrador")
@Produces(MediaType.APPLICATION_JSON)
public class AdministradorService {
    
    private final IAdministradorLogic administradorLogic;
    //private final IRoomLogic roomLogic; IRIA UNIADAD RESIDENCIAL
    private final IAlarmaLogic alarmaLogic;
    
    private final IAlarmaConverter conver;
    
    private final IAdministradorConverter converAdm;

    public AdministradorService() {
        this.administradorLogic = new AdministradorLogic();
        this.alarmaLogic = new AlarmaLogic();
        this.conver= new AlarmaConverter();
        this.converAdm= new AdministradorConverter();
        //this.roomLogic = new RoomLogic(); IRIA UNIADAD RESIDENCIAL
    }

    @POST
    public AdministradorDTO add(AdministradorDTO dto) {
        return administradorLogic.add(dto);
    }

    @POST
    @Path("{id}/alarmas")
    public AlarmaDTO addRoom(@PathParam("id") String id, AlarmaDTO dto) {
        AdministradorDTO floor = administradorLogic.find(id);
        floor.addtAlarmas(dto);
        AlarmaDTO resul= alarmaLogic.add(dto);
        resul.setAdmin(floor.getId());
        administradorLogic.update(floor);
       
        return resul;
    }

    @PUT
    public AdministradorDTO update(AdministradorDTO dto) {
        return administradorLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    public AdministradorDTO find(@PathParam("id") String id) {
        return administradorLogic.find(id);
    }
    
    

    @GET
    @Path("{id}/alarmas")
    public List<AlarmaDTO> addRoom(@PathParam("id") String id) {
        AdministradorDTO admin = administradorLogic.find(id);
        
        return (admin.getAlarmas());
    }

    //@GET
    //@Path("{id}/alarmas")
    //public List<String> addRoom(@PathParam("id") String id) {
      //  AdministradorDTO admin = administradorLogic.find(id);
        
        //return conver.listEntitiesToListDTOs(admin.getAlarmas());
    //}


    @GET
    public List<AdministradorDTO> all() {
        return administradorLogic.all();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            administradorLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Administrador was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(AdministradorService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }
}
