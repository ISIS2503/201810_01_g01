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
import java.util.List;

/**
 *
 * @author af.leon
 */
public class ResidenciaDTO {
    private String id;
    private String torre;
    private String zona;
    private String piso;
    private String tipo;
    private String apartamento;
    private String cerradura;
    private String propietario;
    private List<AlarmaEntity> alarmas;
    
    public ResidenciaDTO() {
        
    }

    public ResidenciaDTO(String id, String torre, String zona, String piso, String tipo, String apartamento, String cerradura, String propietario, List<AlarmaEntity> alarmas) {
        this.id = id;
        this.torre = torre;
        this.zona = zona;
        this.piso = piso;
        this.tipo = tipo;
        this.apartamento = apartamento;
        this.cerradura = cerradura;
        this.propietario = propietario;
        this.alarmas = alarmas;
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
     * @return the torre
     */
    public String getTorre() {
        return torre;
    }

    /**
     * @param torre the torre to set
     */
    public void setTorre(String torre) {
        this.torre = torre;
    }

    /**
     * @return the zona
     */
    public String getZona() {
        return zona;
    }

    /**
     * @param zona the zona to set
     */
    public void setZona(String zona) {
        this.zona = zona;
    }

    /**
     * @return the piso
     */
    public String getPiso() {
        return piso;
    }

    /**
     * @param piso the piso to set
     */
    public void setPiso(String piso) {
        this.piso = piso;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the apartamento
     */
    public String getApartamento() {
        return apartamento;
    }

    /**
     * @param apartamento the apartamento to set
     */
    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    /**
     * @return the cerradura
     */
    public String getCerradura() {
        return cerradura;
    }

    /**
     * @param cerradura the cerradura to set
     */
    public void setCerradura(String cerradura) {
        this.cerradura = cerradura;
    }

    /**
     * @return the propietario
     */
    public String getPropietario() {
        return propietario;
    }

    /**
     * @param propietario the propietario to set
     */
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    /**
     * @return the alarmas
     */
    public List<AlarmaEntity> getAlarmas() {
        return alarmas;
    }

    /**
     * @param alarmas the alarmas to set
     */
    public void setAlarmas(List<AlarmaEntity> alarmas) {
        this.alarmas = alarmas;
    }
    
    public void addAlarma(AlarmaEntity alarmas) {
        this.alarmas.add(alarmas) ;
    }


    
}
