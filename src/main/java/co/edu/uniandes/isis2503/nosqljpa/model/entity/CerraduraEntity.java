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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author af.leon
 */
@Entity
@Table(name = "CERRADURA")
public class CerraduraEntity implements Serializable {
    
     @Id
    private String id;
    
    private String tipo;
    
    private String residencia;
    
    private String fechaingreso;
   
    public CerraduraEntity() {
 
    }

    public CerraduraEntity(String id, String tipo, String residencia, String fechaingreso) {
        this.id = id;
        this.tipo = tipo;
        this.residencia = residencia;
        this.fechaingreso = fechaingreso;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the residencia
     */
    public String getResidencia() {
        return residencia;
    }

    /**
     * @param residencia the residencia to set
     */
    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    /**
     * @return the fechaingreso
     */
    public String getFechaingreso() {
        return fechaingreso;
    }

    /**
     * @param fechaingreso the fechaingreso to set
     */
    public void setFechaingreso(String fechaingreso) {
        this.fechaingreso = fechaingreso;
    }
    
}
