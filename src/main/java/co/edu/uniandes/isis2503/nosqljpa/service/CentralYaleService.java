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
import co.edu.uniandes.isis2503.nosqljpa.interfaces.ICentralYaleLogic;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IUnidadResidencialLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.AlarmaLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.CentralYaleLogic;
import co.edu.uniandes.isis2503.nosqljpa.logic.UnidadResidencialLogic;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AlarmaDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.CentralYaleDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.UnidadResidencialDTO;
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
@Path("/centralYale")
@Produces(MediaType.APPLICATION_JSON)
public class CentralYaleService {
   
    private final ICentralYaleLogic centralYaleLogic;
    private final IUnidadResidencialLogic unidadesLogic;
    private final IAlarmaLogic alarmaLogic;

    public CentralYaleService() {
        this.centralYaleLogic = new CentralYaleLogic();
        this.unidadesLogic = new UnidadResidencialLogic();
        this.alarmaLogic= new AlarmaLogic();
    }

    @POST
    public CentralYaleDTO add(CentralYaleDTO dto) {
        return centralYaleLogic.add(dto);
    }
    
    @POST
    @Path("{id}/alarmas")
    public AlarmaDTO addRoom(@PathParam("id") String id, AlarmaDTO dto) {
        CentralYaleDTO floor = centralYaleLogic.find(id);
        floor.addtAlarmas(dto.getNombre());
        AlarmaDTO resul= alarmaLogic.add(dto);
        resul.setCentral(floor.getId());
        centralYaleLogic.update(floor);
       
        return resul;
    }
    
    @GET
    @Path("{id}/alarmas")
    public List<String> addRoom(@PathParam("id") String id) {
        CentralYaleDTO admin = centralYaleLogic.find(id);
        
        return (admin.getAlarmas());
    }

    @POST
    @Path("{id}/unidadResidencial")
    public UnidadResidencialDTO addUnidadResidencial(@PathParam("id") String id, UnidadResidencialDTO dto) {
        CentralYaleDTO central = centralYaleLogic.find(id);
        UnidadResidencialDTO result = unidadesLogic.add(dto);
        central.addUnidadResidencial(dto.getId());
        centralYaleLogic.update(central);
        return result;
    }

    @PUT
    public CentralYaleDTO update(CentralYaleDTO dto) {
        return centralYaleLogic.update(dto);
    }

    @GET
    @Path("/{id}")
    public CentralYaleDTO find(@PathParam("id") String id) {
        return centralYaleLogic.find(id);
    }

    @GET
    public List<CentralYaleDTO> all() {
        return centralYaleLogic.all();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            centralYaleLogic.delete(id);
            return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: CentralYale was deleted").build();
        } catch (Exception e) {
            Logger.getLogger(AdministradorService.class).log(Level.WARNING, e.getMessage());
            return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
        }
    }
}
