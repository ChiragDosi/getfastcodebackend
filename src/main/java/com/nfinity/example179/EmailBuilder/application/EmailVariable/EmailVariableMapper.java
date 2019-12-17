package com.nfinity.example179.EmailBuilder.application.EmailVariable;

import org.mapstruct.Mapper;

import com.nfinity.example179.EmailBuilder.application.EmailVariable.Dto.*;
import com.nfinity.example179.EmailBuilder.domain.model.EmailVariableEntity;


@Mapper(componentModel = "spring")
public interface EmailVariableMapper {

    EmailVariableEntity CreateEmailVariableInputToEmailVariableEntity(CreateEmailVariableInput emailDto);

    CreateEmailVariableOutput EmailVariableEntityToCreateEmailVariableOutput(EmailVariableEntity entity);

    EmailVariableEntity UpdateEmailVariableInputToEmailVariableEntity(UpdateEmailVariableInput emailDto);

    UpdateEmailVariableOutput EmailVariableEntityToUpdateEmailVariableOutput(EmailVariableEntity entity);

    FindEmailVariableByIdOutput EmailVariableEntityToFindEmailVariableByIdOutput(EmailVariableEntity entity);

    FindEmailVariableByNameOutput EmailVariableEntityToFindEmailVariableByNameOutput(EmailVariableEntity entity);
}