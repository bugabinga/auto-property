package net.bugabinga.annotation.bean.template.model;

import java.util.Objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

class AutoProperty_TestModel extends TestModel {

  private final StringProperty description;
  private final StringProperty name;
  private final IntegerProperty count;
  private final DoubleProperty state;

  public AutoProperty_TestModel(
      String description, String name, Integer count, Double state) {
    this.description = new SimpleStringProperty(this, "description", description);
    this.name = new SimpleStringProperty(this, "name", name);
    this.count = new SimpleIntegerProperty(this, "count", count);
    this.state = new SimpleDoubleProperty(this, "state", state);
  }

  @Override
  public StringProperty description() {
    return description;
  }

  @Override
  public StringProperty nameProperty() {
    return name;
  }

  @Override
  public IntegerProperty countProperty() {
    return count;
  }

  @Override
  public DoubleProperty stateProperty() {
    return state;
  }

  /**
   * A description of this bean.
   * @return A new description.
   */
  public String getDescription() {
    return description.getValue();
  }

  /**
   *
   * @return
   */
  public String getName() {
    return name.getValue();
  }

  /**
   *
   * @return
   */
  public Integer getCount() {
    return count.getValue();
  }

  /**
   *
   * @return
   */
  public Double getState() {
    return state.getValue();
  }

  /**
   * A description of this bean.
   * @param description The description of this bean.
   */
  public void setDescription(String description) {
    Objects.nonNull(description);
    this.description.setValue(description);
  }

  /**
   *
   * @param name
   */
  public void setName(final String name)
  {
    Objects.nonNull(name);
    this.name.setValue(name);
  }

  /**
   *
   * @param count
   */
  public void setCount(final Integer count) {
    Objects.nonNull(count);
    this.count.setValue(count);
  }

  /**
   *
   * @param state
   */
  public void setState(final Double state){
    Objects.nonNull(state);
    this.state.setValue(state);
  }
}
