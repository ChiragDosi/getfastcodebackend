package com.nfinity.example179.application.Td1;

import com.nfinity.example179.application.Td1.Dto.*;
import com.nfinity.example179.domain.Td1.ITd1Manager;
import com.nfinity.example179.domain.model.QTd1Entity;
import com.nfinity.example179.domain.model.Td1Entity;
import com.nfinity.example179.CommonModule.Search.*;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.cache.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class Td1AppService implements ITd1AppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private ITd1Manager _td1Manager;
	
	@Autowired
	private Td1Mapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateTd1Output Create(CreateTd1Input input) {

		Td1Entity td1 = mapper.CreateTd1InputToTd1Entity(input);
		
		Td1Entity createdTd1 = _td1Manager.Create(td1);
		return mapper.Td1EntityToCreateTd1Output(createdTd1);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Td1", key = "#td1Id")
	public UpdateTd1Output Update(Long  td1Id, UpdateTd1Input input) {

		Td1Entity td1 = mapper.UpdateTd1InputToTd1Entity(input);
		
		Td1Entity updatedTd1 = _td1Manager.Update(td1);
		
		return mapper.Td1EntityToUpdateTd1Output(updatedTd1);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Td1", key = "#td1Id")
	public void Delete(Long td1Id) {

		Td1Entity existing = _td1Manager.FindById(td1Id) ; 
		_td1Manager.Delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Td1", key = "#td1Id")
	public FindTd1ByIdOutput FindById(Long td1Id) {

		Td1Entity foundTd1 = _td1Manager.FindById(td1Id);
		if (foundTd1 == null)  
			return null ; 
 	   
 	    FindTd1ByIdOutput output=mapper.Td1EntityToFindTd1ByIdOutput(foundTd1); 
		return output;
	}
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Td1")
	public List<FindTd1ByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<Td1Entity> foundTd1 = _td1Manager.FindAll(Search(search), pageable);
		List<Td1Entity> td1List = foundTd1.getContent();
		Iterator<Td1Entity> td1Iterator = td1List.iterator(); 
		List<FindTd1ByIdOutput> output = new ArrayList<>();

		while (td1Iterator.hasNext()) {
			output.add(mapper.Td1EntityToFindTd1ByIdOutput(td1Iterator.next()));
		}
		return output;
	}
	
	BooleanBuilder Search(SearchCriteria search) throws Exception {

		QTd1Entity td1= QTd1Entity.td1Entity;
		if(search != null) {
			if(search.getType()==case1)
			{
				return searchAllProperties(td1, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2)
			{
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields())
				{
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(td1,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3)
			{
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields())
				{
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkProperties(keysList);
				return searchKeyValuePair(td1, map,search.getJoinColumns());
			}

		}
		return null;
	}
	
	BooleanBuilder searchAllProperties(QTd1Entity td1,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
		}
		else if(operator.equals("equals"))
		{
        	if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
       	 	}
			else if(StringUtils.isNumeric(value)){
        	}
        	else if(SearchUtils.stringToDate(value)!=null) {
			}
		}

		return builder;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
		list.get(i).replace("%20","").trim().equals("id")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	BooleanBuilder searchSpecificProperty(QTd1Entity td1,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();
		
		for (int i = 0; i < list.size(); i++) {
		
		}
		return builder;
	}
	
	BooleanBuilder searchKeyValuePair(QTd1Entity td1, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		return builder;
	}
	
	
    
	
}


