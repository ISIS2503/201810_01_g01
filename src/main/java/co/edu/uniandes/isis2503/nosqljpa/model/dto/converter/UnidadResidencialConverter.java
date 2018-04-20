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

import co.edu.uniandes.isis2503.nosqljpa.interfaces.IUnidadResidencialConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.UnidadResidencialDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.UnidadResidencialEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author m.sicard10
 */
public class UnidadResidencialConverter implements IUnidadResidencialConverter {

    public static IUnidadResidencialConverter CONVERTER = new UnidadResidencialConverter();

    public UnidadResidencialConverter() {
    }

    @Override
    public UnidadResidencialDTO entityToDto(UnidadResidencialEntity entity) {
        UnidadResidencialDTO dto = new UnidadResidencialDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDireccion(entity.getDireccion());
        dto.setEstrato(entity.getEstrato());
        dto.setBarrio(entity.getBarrio());
        dto.setResidencias(entity.getResidencias());
        dto.setAdministrador(entity.getAdministrador());
        dto.setCentralYale(entity.getCentralYale());
        return dto;
    }

    @Override
    public UnidadResidencialEntity dtoToEntity(UnidadResidencialDTO dto) {
        UnidadResidencialEntity entity = new UnidadResidencialEntity();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
        entity.setEstrato(dto.getEstrato());
        entity.setBarrio(dto.getBarrio());
        entity.setResidencias(dto.getResidencias());
        entity.setAdministrador(dto.getAdministrador());
        entity.setCentralYale(dto.getCentralYale());
        return entity;
    }

    @Override
    public List<UnidadResidencialDTO> listEntitiesToListDTOs(List<UnidadResidencialEntity> entities) {
        ArrayList<UnidadResidencialDTO> dtos = new ArrayList<>();
        for (UnidadResidencialEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

    @Override
    public List<UnidadResidencialEntity> listDTOsToListEntities(List<UnidadResidencialDTO> dtos) {
        ArrayList<UnidadResidencialEntity> entities = new ArrayList<>();
        for (UnidadResidencialDTO dto : dtos) {
            entities.add(dtoToEntity(dto));
        }
        return entities;
    }
    
}
