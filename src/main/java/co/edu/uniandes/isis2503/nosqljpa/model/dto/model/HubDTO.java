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

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jd.carrillor
 */
@XmlRootElement
public class HubDTO
{
    private String id;
    
     private String cerradura;

    private int healthChecksPermitidos;
    
    public HubDTO()
    {
        
    }
    
    public HubDTO(String id, int healthChecksPermitidos, String cerradura)
    {
        this.id = id;
        this.healthChecksPermitidos = healthChecksPermitidos;
        this.cerradura=cerradura;
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
     * @return the healthChecksPermitidos
     */
    public int getHealthChecksPermitidos() {
        return healthChecksPermitidos;
    }

    /**
     * @param healthChecksPermitidos the healthChecksPermitidos to set
     */
    public void setHealthChecksPermitidos(int healthChecksPermitidos) {
        this.healthChecksPermitidos = healthChecksPermitidos;
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
    
}
