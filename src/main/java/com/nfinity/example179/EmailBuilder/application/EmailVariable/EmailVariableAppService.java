package com.nfinity.example179.EmailBuilder.application.EmailVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nfinity.example179.EmailBuilder.domain.EmailVariable.IEmailVariableManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchFields;
import com.nfinity.example179.EmailBuilder.application.EmailVariable.Dto.*;
import com.nfinity.example179.EmailBuilder.domain.model.EmailVariableEntity;
import com.nfinity.example179.EmailBuilder.domain.model.QEmailVariableEntity;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

@Service
@Validated
public class EmailVariableAppService implements IEmailVariableAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IEmailVariableManager _emailVariableManager;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private EmailVariableMapper emailVariableMapper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateEmailVariableOutput Create(CreateEmailVariableInput email) {

		EmailVariableEntity re = emailVariableMapper.CreateEmailVariableInputToEmailVariableEntity(email);
		EmailVariableEntity createdEmail = _emailVariableManager.Create(re);

		return emailVariableMapper.EmailVariableEntityToCreateEmailVariableOutput(createdEmail);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void Delete(Long eid) {
		EmailVariableEntity existing = _emailVariableManager.FindById(eid);
		_emailVariableManager.Delete(existing);
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateEmailVariableOutput Update(Long eid, UpdateEmailVariableInput email) {
		EmailVariableEntity ue = emailVariableMapper.UpdateEmailVariableInputToEmailVariableEntity(email);
		EmailVariableEntity updatedEmail = _emailVariableManager.Update(ue);

		return emailVariableMapper.EmailVariableEntityToUpdateEmailVariableOutput(updatedEmail);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindEmailVariableByIdOutput FindById(Long eid) {
		EmailVariableEntity foundEmail = _emailVariableManager.FindById(eid);

		if (foundEmail == null) {
			logHelper.getLogger().error("There does not exist a email wth a id=%s", eid);
			return null;
		}

		return emailVariableMapper.EmailVariableEntityToFindEmailVariableByIdOutput(foundEmail);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public FindEmailVariableByNameOutput FindByName(String name) {
	    	EmailVariableEntity foundEmail = _emailVariableManager.FindByName(name);
	    	if (foundEmail == null) {
				logHelper.getLogger().error("There does not exist a email wth a name=%s", name);
				return null;
			}
			return emailVariableMapper.EmailVariableEntityToFindEmailVariableByNameOutput(foundEmail);
		}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<FindEmailVariableByIdOutput> Find(SearchCriteria search,Pageable pageable) throws Exception {
		
		Page<EmailVariableEntity> foundEmail = _emailVariableManager.FindAll(Search(search),pageable);
		List<EmailVariableEntity> emailList = foundEmail.getContent();

		Iterator<EmailVariableEntity> emailIterator = emailList.iterator();
		List<FindEmailVariableByIdOutput> output = new ArrayList<>();

		  while (emailIterator.hasNext()) {
	      output.add(emailVariableMapper.EmailVariableEntityToFindEmailVariableByIdOutput(emailIterator.next()));    
		 }

		return output;
	}

	public BooleanBuilder Search(SearchCriteria search) throws Exception {

		QEmailVariableEntity email = QEmailVariableEntity.emailVariableEntity;
		if(search != null) {
			if(search.getType()==case1) {
				return searchAllProperties(email, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2) {
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields()) {
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(email,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3) {
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields()) {
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkProperties(keysList);
				return searchKeyValuePair(email, map);
			}
		}
		return null;
	}
	
	public BooleanBuilder searchAllProperties(QEmailVariableEntity email,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(email.propertyName.likeIgnoreCase("%"+ value + "%"));
			builder.or(email.propertyType.likeIgnoreCase("%"+ value + "%"));
			builder.or(email.defaultValue.likeIgnoreCase("%"+ value + "%"));
		}
		else if(operator.equals("equals")) {
			builder.or(email.propertyName.eq(value));
			builder.or(email.propertyType.eq(value));
			builder.or(email.defaultValue.eq(value));
		}

		return builder;
	}

	public void checkProperties(List<String> list) throws Exception
	{
		for (int i = 0; i < list.size(); i++) {
			if(!((list.get(i).replace("%20","").trim().equals("propertyName"))
					|| (list.get(i).replace("%20","").trim().equals("propertyType"))
					|| (list.get(i).replace("%20","").trim().equals("defaultValue")))) {

				// Throw an exception
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}

	}
	
	public BooleanBuilder searchSpecificProperty(QEmailVariableEntity email,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();

		for (int i = 0; i < list.size(); i++) {

			if(list.get(i).replace("%20","").trim().equals("propertyName")) {
				if(operator.equals("contains")) {
					builder.or(email.propertyName.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(email.propertyName.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("propertyType")) {
				if(operator.equals("contains")) {
					builder.or(email.propertyType.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(email.propertyType.eq(value));
				}
			}
			if(list.get(i).replace("%20","").trim().equals("defaultValue")) {
				if(operator.equals("contains")) {
					builder.or(email.defaultValue.likeIgnoreCase("%"+ value + "%"));
				}
				else if(operator.equals("equals")) {
					builder.or(email.defaultValue.eq(value));
				}
			}
		}
		return builder;
	}

	public BooleanBuilder searchKeyValuePair(QEmailVariableEntity email, Map<String,SearchFields> map) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("propertyName")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(email.propertyName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(email.propertyName.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(email.propertyName.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("propertyType")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(email.propertyType.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(email.propertyType.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(email.propertyType.ne(details.getValue().getSearchValue()));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("defaultValue")) {
				if(details.getValue().getOperator().equals("contains")) {
					builder.and(email.defaultValue.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				}
				else if(details.getValue().getOperator().equals("equals")) {
					builder.and(email.defaultValue.eq(details.getValue().getSearchValue()));
				}
				else if(details.getValue().getOperator().equals("notEqual")) {
					builder.and(email.defaultValue.ne(details.getValue().getSearchValue()));
				}
			}
			
		}
		return builder;
	}
}
