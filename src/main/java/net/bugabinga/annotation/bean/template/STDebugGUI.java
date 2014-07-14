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

import java.util.Arrays;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 *
 */
public class STDebugGUI {

  /**
   * @param args
   */
  public static void main(final String[] args) {
    final STGroup group =
        new STGroupFile("net/bugabinga/annotation/bean/template/autoProperty.stg");
    final ST st = group.getInstanceOf("instance");
    st.add("packageName", "com.test.debug");
    st.add("imports", Arrays.asList("a.b.c.C", "g.b.f.R", "r.t.y.U"));
    st.add("className", "TestModel");
    st.add("isBean", true);
    st.inspect();
  }
}
