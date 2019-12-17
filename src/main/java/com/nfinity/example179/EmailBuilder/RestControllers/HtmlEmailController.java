package com.nfinity.example179.EmailBuilder.RestControllers;

import java.io.IOException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nfinity.example179.EmailBuilder.application.EmailTemplate.EmailTemplateAppService;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;

@RestController
@RequestMapping("/htmlEmail")
public class HtmlEmailController {

	 @Autowired
	 private EmailTemplateAppService emailTemplateAppService;

	 @RequestMapping(method = RequestMethod.POST)
	 public ResponseEntity<String> JsonToHtml(@RequestBody @Valid String json) throws IOException
	 {
		 if(json != null)
			 return new ResponseEntity(emailTemplateAppService.convertJsonToHtml(json), HttpStatus.OK);
		 else
			 return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
			 
	 }
}
