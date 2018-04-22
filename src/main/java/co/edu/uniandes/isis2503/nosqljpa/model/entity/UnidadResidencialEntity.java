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
package co.edu.uniandes.isis2503.nosqljpa.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author af.leon
 */
@Entity
@Table(name = "UNIDADRESIDENCIAL")
public class UnidadResidencialEntity implements Serializable {
    
    @Id
    private String id;
    
    private String nombre;
    
    private String direccion;
    
    private String estrato;
    
    private String barrio;

    @ElementCollection
    private List<String> residencias;
    
    private String administrador;
    
    private String centralYale;
    
    public UnidadResidencialEntity() {
        this.residencias = new ArrayList();
    }

    public UnidadResidencialEntity(String id, String nombre, String direccion, String estrato, String barrio, List<String> residencias, String administrador, String centralYale) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.estrato = estrato;
        this.barrio = barrio;
        this.residencias = residencias;
        this.administrador = administrador;
        this.centralYale = centralYale;
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
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the estrato
     */
    public String getEstrato() {
        return estrato;
    }

    /**
     * @param estrato the estrato to set
     */
    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    /**
     * @return the barrio
     */
    public String getBarrio() {
        return barrio;
    }

    /**
     * @param barrio the barrio to set
     */
    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    /**
     * @return the residencias
     */
    public List<String> getResidencias() {
        return residencias;
    }

    /**
     * @param residencias the residencias to set
     */
    public void setResidencias(List<String> residencias) {
        this.residencias = residencias;
    }

    /**
     * @return the administrador
     */
    public String getAdministrador() {
        return administrador;
    }

    /**
     * @param administrador the administrador to set
     */
    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    /**
     * @return the centralYale
     */
    public String getCentralYale() {
        return centralYale;
    }

    /**
     * @param centralYale the centralYale to set
     */
    public void setCentralYale(String centralYale) {
        this.centralYale = centralYale;
    }
    
    
}
