package com.nfinity.example179.application.Td1.Dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UpdateTd1Input {

  @NotNull(message = "id Should not be null")
  private Long id;

 
  public Long getId() {
  	return id;
  }

  public void setId(Long id){
  	this.id = id;
  }
}
