package com.nfinity.example179.domain.model;

import java.io.Serializable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
@Entity
@Table(name = "td1", schema = "test8")
public class Td1Entity implements Serializable {

  private Long id;
 
  public Td1Entity() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  public Long getId() {
  return id;
  }

  public void setId(Long id){
  this.id = id;
  }
  


//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//      if (!(o instanceof Td1Entity)) return false;
//        Td1Entity td1 = (Td1Entity) o;
//        return id != null && id.equals(td1.id);
//  }

}

  
      


