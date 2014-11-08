package net.bugabinga.annotation.bean.template.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * A TestModel for testing purposes. It will be used in {@link net.bugabinga.annotation.bean.template.AutoPropertyTemplateTest}
 * to check the validity of our StringTemplate templates. Created by bugabinga on 26.07.14.
 */
public abstract class TestModel {

  protected TestModel() {
    //hidden ctor
  }

  /**
   * Creates a fresh TestModel.
   *
   * @param description The description.
   * @param name        The name.
   * @param count       The count of something.
   * @param state       The state of something.
   * @return A fresh TestModel.
   */
  public static TestModel create(String description, String name, Integer count, Double state) {
    return new AutoProperty_TestModel(description, name, count, state);
  }

  /**
   * A description of this bean.
   *
   * @param description The description of this bean.
   * @return A new description.
   */
  public abstract StringProperty description();

  public abstract StringProperty nameProperty();

  public abstract IntegerProperty countProperty();

  public abstract DoubleProperty stateProperty();

}

