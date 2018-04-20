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
package co.edu.uniandes.isis2503.nosqljpa.model.dto.model;

import co.edu.uniandes.isis2503.nosqljpa.model.entity.AlarmaEntity;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jd.carrillor
 */
@XmlRootElement
public class CentralYaleDTO {
    
    private String id;
    
    private String nombre;
   
    private List<String> unidadesResidenciales;
    
     private List<String> alarmas;
    
    public CentralYaleDTO() {
        this.unidadesResidenciales = new ArrayList();
        this.alarmas = new ArrayList();
    }

    public CentralYaleDTO(String id, String nombre, String code, List<String> unidadesResidenciales, List<String> alarmas) {
        this.id = id;
        this.nombre = nombre;
        this.unidadesResidenciales = unidadesResidenciales;
        this.alarmas=alarmas;
      
    }
    
     public void addUnidadResidencial(String id) {
         this.getUnidadesResidenciales().add(id);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the unidadesResidenciales
     */
    public List<String> getUnidadesResidenciales() {
        return unidadesResidenciales;
    }

    /**
     * @param unidadesResidenciales the unidadesResidenciales to set
     */
    public void setUnidadesResidenciales(List<String> unidadesResidenciales) {
        this.unidadesResidenciales = unidadesResidenciales;
    }

    /**
     * @return the alarmas
     */
    public List<String> getAlarmas() {
        return alarmas;
    }

    /**
     * @param alarmas the alarmas to set
     */
    public void setAlarmas(List<String> alarmas) {
        this.alarmas = alarmas;
    }
    public void addtAlarmas(String alarma) {
        this.alarmas.add(alarma);
    }
    
}
