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
package net.bugabinga.annotation.bean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 *
 */
public class StressTestGenerator {
  public static void main(final String[] args) {
    final String propertyTemplate = "Property<{0}>";
    final String primitiveTemplate = "{0}Property";

    final List<String> possibles = new ArrayList<>();
    possibles.add("Integer");
    possibles.add("Double");
    possibles.add("Boolean");
    possibles.add("String");
    possibles.add("Long");
    possibles.add("Float");
    possibles.add("Object");
    possibles.add("Duration");
    possibles.add("LocalDateTime");
    possibles.add("Throwable");

    final Random randomizer = new Random();

    final List<String> types = new LinkedList<>();
    for (int i = 0; i < 255; ++i) {
      types.add(possibles.get(randomizer.nextInt(possibles.size())));
    }

    for (final String type : types) {
      String prop;
      if (isPrimitive(type)) {
        if (fiftyFifty()) {
          prop = MessageFormat.format(primitiveTemplate, type);
        } else {
          prop = MessageFormat.format(primitiveTemplate, type);
        }
      } else {
        prop = MessageFormat.format(propertyTemplate, type);
      }
      System.out.println(MessageFormat.format("public abstract {0} {1}(); ", prop,
          "p" + Integer.toHexString(prop.hashCode()) + Math.abs(randomizer.nextInt())));
    }
  }

  private static boolean fiftyFifty() {
    return new Random().nextBoolean();
  }

  private static boolean isPrimitive(final String type) {
    return type.equals("Integer") || type.equals("Double") || type.equals("Boolean")
        || type.equals("String") || type.equals("Long") || type.equals("Float");
  }
}
