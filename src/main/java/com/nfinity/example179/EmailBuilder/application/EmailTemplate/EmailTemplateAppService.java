package com.nfinity.example179.EmailBuilder.application.EmailTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nfinity.example179.EmailBuilder.domain.EmailTemplate.IEmailTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nfinity.example179.EmailBuilder.application.EmailTemplate.Dto.*;
import com.nfinity.example179.EmailBuilder.domain.model.EmailTemplateEntity;
import com.nfinity.example179.EmailBuilder.domain.model.QEmailTemplateEntity;
import com.nfinity.example179.EmailBuilder.emailConverter.dto.request.Request;
import com.nfinity.example179.EmailBuilder.emailConverter.dto.response.Response;
import com.nfinity.example179.EmailBuilder.emailConverter.service.MjmlOwnService;
import com.nfinity.example179.CommonModule.Search.*;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

@Service
public class EmailTemplateAppService implements IEmailTemplateAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IEmailTemplateManager _emailTemplateManager;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private EmailTemplateMapper emailTemplateMapper;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private MjmlOwnService mjmlOwnService;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateEmailTemplateOutput Create(CreateEmailTemplateInput email) {

		EmailTemplateEntity re = emailTemplateMapper.CreateEmailTemplateInputToEmailTemplateEntity(email);
		EmailTemplateEntity createdEmail = _emailTemplateManager.Create(re);

		return emailTemplateMapper.EmailTemplateEntityToCreateEmailTemplateOutput(createdEmail);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void Delete(Long eid) {
		EmailTemplateEntity existing = _emailTemplateManager.FindById(eid);
		_emailTemplateManager.Delete(existing);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateEmailTemplateOutput Update(Long eid, UpdateEmailTemplateInput email) {

		EmailTemplateEntity ue = emailTemplateMapper.UpdateEmailTemplateInputToEmailTemplateEntity(email);
		EmailTemplateEntity updatedEmail = _emailTemplateManager.Update(ue);

		return emailTemplateMapper.EmailTemplateEntityToUpdateEmailTemplateOutput(updatedEmail);

	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindEmailTemplateByIdOutput FindById(Long eid) {
		EmailTemplateEntity foundEmail = _emailTemplateManager.FindById(eid);

		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a id=%s", eid);
			return null;
		}

		return emailTemplateMapper.EmailTemplateEntityToFindEmailTemplateByIdOutput(foundEmail);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindEmailTemplateByNameOutput FindByName(String name) {
		EmailTemplateEntity foundEmail = _emailTemplateManager.FindByName(name);
		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a name=%s", name);
			return null;
		}
		return emailTemplateMapper.EmailTemplateEntityToFindEmailTemplateByNameOutput(foundEmail);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindEmailTemplateByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception {
		Page<EmailTemplateEntity> foundEmail = _emailTemplateManager.FindAll(Search(search),pageable);
		List<EmailTemplateEntity> emailList = foundEmail.getContent();

		Iterator<EmailTemplateEntity> emailIterator = emailList.iterator();
		List<FindEmailTemplateByIdOutput> output = new ArrayList<>();

		while (emailIterator.hasNext()) {
			output.add(emailTemplateMapper.EmailTemplateEntityToFindEmailTemplateByIdOutput(emailIterator.next()));    
		}

		return output;
	}

	public BooleanBuilder Search(SearchCriteria search) throws Exception {

		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		if(search != null) {
			if(search.getType()==case1) {
				return searchAllProperties(emailTemplate, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2) {
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields()) {
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(emailTemplate,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3) {
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields()) {
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkProperties(keysList);
				return searchKeyValuePair(emailTemplate, map);
			}

		}
		return null;
	}

	public BooleanBuilder searchAllProperties(QEmailTemplateEntity emailTemplate,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(emailTemplate.templateName.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.category.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.to.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.cc.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.bcc.likeIgnoreCase("%"+ value + "%"));
			builder.or(emailTemplate.subject.likeIgnoreCase("%"+ value + "%"));
		}
		else if(operator.equals("equals")) {
			builder.or(emailTemplate.templateName.eq(value));
			builder.or(emailTemplate.category.eq(value));
			builder.or(emailTemplate.to.eq(value));
			builder.or(emailTemplate.cc.eq(value));
			builder.or(emailTemplate.bcc.eq(value));
			builder.or(emailTemplate.subject.eq(value));
		}

		return builder;
	}

	public void checkProperties(List<String> list) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			if(!((list.get(i).replace("%20","").trim().equals("templateName")) 
					|| (list.get(i).replace("%20","").trim().equals("category"))
					|| (list.get(i).replace("%20","").trim().equals("to"))
					|| (list.get(i).replace("%20","").trim().equals("cc"))
					|| (list.get(i).replace("%20","").trim().equals("bcc"))
					|| (list.get(i).replace("%20","").trim().equals("subject")))) {

				// Throw an exception
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchSpecificProperty(QEmailTemplateEntity emailTemplate,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();

		for (int i = 0; i < list.size(); i++) {

			if(list.get(i).replace("%20","").trim().equals("templateName")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.templateName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.templateName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("category")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.category.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.category.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("to")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.to.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.to.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("cc")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.cc.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.cc.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("bcc")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.bcc.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.bcc.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("subject")) {
				if(operator.equals("contains")) {
					builder.or(emailTemplate.subject.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(emailTemplate.subject.eq(value));
				}
			}
		}
		return builder;
	}

	public BooleanBuilder searchKeyValuePair(QEmailTemplateEntity emailTemplate, Map<String,SearchFields> map) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("templateName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.templateName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.templateName.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.templateName.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("category")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.category.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.category.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.category.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("to")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.to.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.to.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.to.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("cc")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.cc.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.cc.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.cc.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("bcc")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.bcc.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.bcc.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.bcc.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("subject")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(emailTemplate.subject.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(emailTemplate.subject.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(emailTemplate.subject.ne(details.getValue().getSearchValue()));
				}
			}
			
		}
		return builder;
	}

	public ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 5000;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
		= new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		return clientHttpRequestFactory;
	}

	public String convertJsonToHtml(String jsonString) throws IOException{
		String html= " ";
		ObjectMapper mapper = new ObjectMapper();
		Request request = mapper.readValue(jsonString, Request.class);
//		ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
//		RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Content-Type", "application/json");
//		String xapikey = env.getProperty("fastcode.emailconverter.xapikey");
//		String emailServerUrl = env.getProperty("fastcode.emailconverter.url");
//		//String emailServerUrl ="http://localhost:3001/" ;// "https://ngb-api.wlocalhost.org/v1"
//		//headers.set("X-Api-Key", "t7HdQfZjGp6R96fOV4P8v18ggf6LLTQZ1puUI2tz");
//		headers.set("X-Api-Key", xapikey);
//		HttpEntity<JsonNode> request = new HttpEntity<>(root,headers);
//		html = restTemplate.postForObject(emailServerUrl, request, String.class);
		Response response = mjmlOwnService.genrateHtml(request);
		logHelper.getLogger().error("Error",response.getErrors());
		html = response.getHtml();
		
		return html;
	}

}