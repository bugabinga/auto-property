/*
 * This file is part of auto-property.
 * 
 * auto-property is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * auto-property is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with auto-property. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package net.bugabinga.annotation.bean.template;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 */
public class STDebugGUI {

  /**
   * @param args arguments
   */
  public static void main(final String[] args) {
    final STGroup group =
        new STGroupFile("net/bugabinga/annotation/bean/template/autoProperty.stg");
    final ST st = group.getInstanceOf("auto_property_instance");

    List<Prop> params = Arrays.asList(
        new Prop("StringProperty", "descriptionProperty", "SimpleStringProperty", "description",
                 "String", "A description of this bean.", "A new description.",
                 "The description of this bean."),
        new Prop("StringProperty", "nameProperty", "SimpleStringProperty", "name", "String", "",
                 "", ""),
        new Prop("IntegerProperty", "countProperty", "SimpleIntegerProperty", "count",
                 "Integer", "", "", ""),
        new Prop("DoubleProperty", "stateProperty", "SimpleDoubleProperty", "state", "Double", "",
                 "", ""));


/*
        TODO Syntax highlighter for ST + little tooling
        TODO Tutorial for ST mapping funtions with seperators
        TODO Tutorial for ST desctructuring
        TODO Tutorial for custom functions
*/



    st.add("packageName", "com.test.debug")
        .add("imports", Arrays.asList("a.b.c.C", "g.b.f.R", "r.t.y.U"))//FIXME imports need to be own value type
        .add("className", "TestModel")
        .add("isBean", true)
        .add("params", params);

    st.inspect(80);
  }

  /*TODO(bugabinga): Auto-Value to generate this:*/
  private static class Prop {

    public final String type;
    public final String name;
    public final String impl;
    public final String simpleName;
    public final String simpleType;
    public final String commentText;
    public final String commentParam;
    public final String commentReturn;

    public Prop(String type, String name, String impl, String simpleName, String simpleType,
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
}
