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

        List<Prop> paramsMap = Arrays.asList(
                new Prop("StringProperty", "descriptionProperty"),
                new Prop("StringProperty", "nameProperty"),
                new Prop("IntegerProperty", "countProperty"),
                new Prop("DoubleProperty", "stateProperty"));


/*
                TODO Syntax highlighter for ST + little tooling
        TODO Tutorial for ST mapping funtions with seperators
        TODO Tutorial for ST desctructuring
        TODO Tutorial for custom functions
*/

        st.add("packageName", "com.test.debug")
                .add("imports", Arrays.asList("a.b.c.C", "g.b.f.R", "r.t.y.U"))
                .add("className", "TestModel")
                .add("isBean", true)
                .add("params", paramsMap);

        st.inspect(80);
    }

    private static class Prop {
        private String type;
        private String name;

        public Prop(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
