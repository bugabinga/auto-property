package net.bugabinga.annotation.bean.template;

/**
* Created by bugabinga on 26.07.14.
*/
class Property {

  public final String type;
  public final String name;
  public final String impl;
  public final String simpleName;
  public final String simpleType;
  public final String commentText;
  public final String commentParam;
  public final String commentReturn;

  public Property(String type, String name, String impl, String simpleName, String simpleType,
                  String commentText,
                  String commentParam, String commentReturn) {
    this.type = type;
    this.name = name;
    this.impl = impl;
    this.simpleName = simpleName;
    this.simpleType = simpleType;
    this.commentText = commentText;
    this.commentParam = commentParam;
    this.commentReturn = commentReturn;
  }
}
