/*
* This file is part of auto-property.
*
* auto-property is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* auto-property is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with auto-property.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.bugabinga.annotation.bean.model;

import org.hibernate.mapping.Property;

import net.bugabinga.annotation.bean.AutoProperty;

/**
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 *
 */
@AutoProperty
public abstract class CustomBeanModelCase implements AutoProperty_Bean_CustomBeanModelCase{

/**
 * Some docs.
 * @return A yoyo!
 */
  public abstract BooleanProperty yoyo();

  private final Property<String> customName = new SimpleStringProperty(this,"customName","bob");
  public Property<String> customName() {
    return customName;
  }
  public void setCustomName(String name) {
    customName.set(name);
  }
  public String getCustomName() {
    return customName.get();
  }
  
  CustomBeanModelCase() {
    // hidden c'tor
  }
}
