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
import org.stringtemplate.v4.StringRenderer;

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
// TODO(bugabinga): we could write a modeladaptor for javafx properties, not clear if necessary though
//    group.registerModelAdaptor();

    group.registerRenderer(String.class, new StringRenderer());
    final ST st = group.getInstanceOf("auto_property_instance");

    List<Property> params = Arrays.asList(
        new Property("StringProperty", "descriptionProperty", "SimpleStringProperty", "description",
                 "String", "A description of this bean.", "A new description.",
                 "The description of this bean."),
        new Property("StringProperty", "nameProperty", "SimpleStringProperty", "name", "String", "",
                 "", ""),
        new Property("IntegerProperty", "countProperty", "SimpleIntegerProperty", "count",
                 "Integer", "", "", ""),
        new Property("DoubleProperty", "stateProperty", "SimpleDoubleProperty", "state", "Double", "",
                 "", ""));

/*
        TODO Syntax highlighter for ST + little tooling
        TODO Tutorial for ST mapping funtions with seperators
        TODO Tutorial for ST desctructuring
        TODO Tutorial ModelAdaptors
        TODO Tutorial Renderers + Bonus: Abusing Renderer
        TODO simpleName could be replaced by renderer , should it?
        TODO equals ,toString and hashCode
*/

    st.add("packageName", "com.test.debug")
        .add("imports", Arrays
            .asList("a.b.c.C", "g.b.f.R", "r.t.y.U"))
        .add("className", "TestModel")
        .add("isBean", true)
        .add("params", params);

    st.inspect(80);
  }

}
