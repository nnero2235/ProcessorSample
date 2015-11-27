package com.example;

import com.squareup.javapoet.TypeName;

/**
 * Created by nnero on 15/11/26.
 * 成员信息：包括类型 name
 */
public class FieldInfo {
  private String name;
  private TypeName type;

  public FieldInfo(String name,TypeName type){
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TypeName getType() {
    return type;
  }

  public void setType(TypeName type) {
    this.type = type;
  }
}
