package com.nfinity.example179.application.Td1;

import org.mapstruct.Mapper;
import com.nfinity.example179.application.Td1.Dto.*;
import com.nfinity.example179.domain.model.Td1Entity;

@Mapper(componentModel = "spring")
public interface Td1Mapper {

   Td1Entity CreateTd1InputToTd1Entity(CreateTd1Input td1Dto);
   
   CreateTd1Output Td1EntityToCreateTd1Output(Td1Entity entity);

   Td1Entity UpdateTd1InputToTd1Entity(UpdateTd1Input td1Dto);

   UpdateTd1Output Td1EntityToUpdateTd1Output(Td1Entity entity);

   FindTd1ByIdOutput Td1EntityToFindTd1ByIdOutput(Td1Entity entity);


}
