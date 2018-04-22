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


import co.edu.uniandes.isis2503.nosqljpa.interfaces.IClaveConverter;
import co.edu.uniandes.isis2503.nosqljpa.model.dto.model.ClaveDTO;
import co.edu.uniandes.isis2503.nosqljpa.model.entity.ClaveEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author m.sicard10
 */
public class ClaveConverter implements IClaveConverter
{
    
   public static IClaveConverter CONVERTER = new ClaveConverter();
    
    

    public ClaveConverter() {
    }

    @Override
    public ClaveDTO entityToDto(ClaveEntity entity) {
        ClaveDTO dto = new ClaveDTO();
        dto.setId(entity.getId());
        dto.setPeticion(entity.getPeticion());
        dto.setClave(entity.getClave());
        
        return dto;
    }

    @Override
    public ClaveEntity dtoToEntity(ClaveDTO dto) {
        ClaveEntity entity = new ClaveEntity();
        entity.setId(dto.getId());
        entity.setPeticion(dto.getPeticion());
        entity.setClave(dto.getClave());
        
        return entity;
    }

    @Override
    public List<ClaveDTO> listEntitiesToListDTOs(List<ClaveEntity> entities) {
        ArrayList<ClaveDTO> dtos = new ArrayList<>();
        for (ClaveEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

    @Override
    public List<ClaveEntity> listDTOsToListEntities(List<ClaveDTO> dtos) {
        ArrayList<ClaveEntity> entities = new ArrayList<>();
        for (ClaveDTO dto : dtos) {
            entities.add(dtoToEntity(dto));
        }
        return entities;
    }
    
    
}
