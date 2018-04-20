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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IPropietarioLogic;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IResidenciaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.PropietarioLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.ResidenciaLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.PropietarioDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ResidenciaDTO;
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
@Path("/propietario")
@Produces(MediaType.APPLICATION_JSON)
public class PropietarioService {

    private final IPropietarioLogic propietarioLogic;
    private final IResidenciaLogic residenciaLogic; 
     private final IAlarmaLogic alarmaLogic;

    public PropietarioService() {
        this.propietarioLogic = new PropietarioLogic();
        this.residenciaLogic = new ResidenciaLogic(); 
        this.alarmaLogic = new AlarmaLogic();
    }
    
     @POST
    @Path("{id}/alarmas")
    public AlarmaDTO addRoom(@PathParam("id") String id, AlarmaDTO dto) {
        PropietarioDTO floor = propietarioLogic.find(id);
        floor.addtAlarmas(dto.getNombre());
        AlarmaDTO resul= alarmaLogic.add(dto);
        resul.setPropietario(floor.getId());
        propietarioLogic.update(floor);
       
        return resul;
    }
    
    @GET
    @Path("{id}/alarmas")
    public List<String> addRoom(@PathParam("id") String id) {
        PropietarioDTO admin = propietarioLogic.find(id);
        
        return (admin.getAlarmas());
    }

    @POST
    public PropietarioDTO add(PropietarioDTO dto) {
        return propietarioLogic.add(dto);
    }

    @POST
    @Path("{id}/residencia")
    public ResidenciaDTO addResidencia(@PathParam("id") String id, ResidenciaDTO dto) {
        PropietarioDTO propie = propietarioLogic.find(id);
        ResidenciaDTO result = residenciaLogic.add(dto);
        propie.addResidencia(dto.getId());
        propietarioLogic.update(propie);
        return result;
    }

    @PUT
    public PropietarioDTO update(PropietarioDTO dto) {
        return propietarioLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    public PropietarioDTO find(@PathParam("id") String id) {
        return propietarioLogic.find(id);
    }

    @GET
    public List<PropietarioDTO> all() {
        return propietarioLogic.all();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            propietarioLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Propietario was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(AdministradorService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }

}
