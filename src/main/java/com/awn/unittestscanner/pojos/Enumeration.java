package com.awn.unittestscanner.pojos;

public enum Enumeration {
  FIELD_ANNOTATION("Field Annotation"),
  OBJECT_OPERATION_READING_VARIABLE("Object Operation Reading Variable"),
  METHOD_CALL ("Method Call"),
  PARAMETER ("Parameter"),


  ALL ("All"),
  EXCEPT ("Except"),
  EQUALS ("Equals"),
  CONTAINS ("Contains"),
  NOT_ONLY ("& NotOnly"),;

  private String type;

  Enumeration(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

}