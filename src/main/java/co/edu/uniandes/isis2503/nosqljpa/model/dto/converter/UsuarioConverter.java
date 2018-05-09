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
package co.edu.uniandes.isis2503.nosqljpa.model.dto.converter;

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IAdministradorConverter;
import co.edu.uniandes.isis2503.nosqljpa.interfaces.IUsuarioConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.AdministradorDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.UsuarioDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.AdministradorEntity;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.UsuarioEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author af.leon
 */
public class UsuarioConverter implements IUsuarioConverter {

    public static IUsuarioConverter CONVERTER = new UsuarioConverter();

    public UsuarioConverter() {
    }

    @Override
    public UsuarioDTO entityToDto(UsuarioEntity entity) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCorreo(entity.getCorreo());
        dto.setTelefono(entity.getTelefono());
        dto.setUser1(entity.getUser());
        dto.setClaves(entity.getClaves());
        return dto;
    }

    @Override
    public UsuarioEntity dtoToEntity(UsuarioDTO dto) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());
        entity.setUser(dto.getUser1());
        entity.setClaves(dto.getClaves());
        

        return entity;
    }

    @Override
    public List<UsuarioDTO> listEntitiesToListDTOs(List<UsuarioEntity> entities) {
        ArrayList<UsuarioDTO> dtos = new ArrayList<>();
        for (UsuarioEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

    @Override
    public List<UsuarioEntity> listDTOsToListEntities(List<UsuarioDTO> dtos) {
        ArrayList<UsuarioEntity> entities = new ArrayList<>();
        for (UsuarioDTO dto : dtos) {
            entities.add(dtoToEntity(dto));
        }
        return entities;
    }
    
}
