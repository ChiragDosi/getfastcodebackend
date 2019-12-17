package com.nfinity.example179.EmailBuilder.application.EmailTemplate;

import org.mapstruct.Mapper;

import com.nfinity.example179.EmailBuilder.application.EmailTemplate.Dto.*;
import com.nfinity.example179.EmailBuilder.domain.model.EmailTemplateEntity;

@Mapper(componentModel = "spring")
public interface EmailTemplateMapper {

    EmailTemplateEntity CreateEmailTemplateInputToEmailTemplateEntity(CreateEmailTemplateInput emailDto);

    CreateEmailTemplateOutput EmailTemplateEntityToCreateEmailTemplateOutput(EmailTemplateEntity entity);

    EmailTemplateEntity UpdateEmailTemplateInputToEmailTemplateEntity(UpdateEmailTemplateInput emailDto);

    UpdateEmailTemplateOutput EmailTemplateEntityToUpdateEmailTemplateOutput(EmailTemplateEntity entity);

    FindEmailTemplateByIdOutput EmailTemplateEntityToFindEmailTemplateByIdOutput(EmailTemplateEntity entity);

    FindEmailTemplateByNameOutput EmailTemplateEntityToFindEmailTemplateByNameOutput(EmailTemplateEntity entity);
}