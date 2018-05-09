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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author af.leon
 */
@Entity
@Table(name = "USUARIO")
public class UsuarioEntity implements Serializable {
    
    @Id
    private String id;
    
    private String nombre;
   
    private String correo;
    
    private int telefono;
    
    private String user1;
    
    private List<String> claves;
   
    public UsuarioEntity() {
         this.claves  = new ArrayList();
    }

    public UsuarioEntity(String id, String nombre, String correo, List<String> claves, int telefono, String user ) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.user1=user;
        this.claves = claves;
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   
    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * @return the telefono
     */
    public int getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user1;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user1 = user;
    }

    /**
     * @return the claves
     */
    public List<String> getClaves() {
        return claves;
    }

    /**
     * @param claves the claves to set
     */
    public void setClaves(List<String> claves) {
        this.claves = claves;
    }
    
}
