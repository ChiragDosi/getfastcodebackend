package com.nfinity.example179.application.Td1;

import java.util.List;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.application.Td1.Dto.*;

@Service
public interface ITd1AppService {

	CreateTd1Output Create(CreateTd1Input td1);

    void Delete(Long id);

    UpdateTd1Output Update(Long id, UpdateTd1Input input);

    FindTd1ByIdOutput FindById(Long id);

    List<FindTd1ByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception;

}
