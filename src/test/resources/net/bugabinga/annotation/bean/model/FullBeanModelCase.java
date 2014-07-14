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
package net.bugabinga.annotation.bean.model;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import net.bugabinga.annotation.bean.Processor;

import com.sun.istack.internal.Nullable;

/**
 * This case tests the successful compilation with every possible feature by {@link Processor}.
 *
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 *
 */
public abstract class FullBeanModelCase implements AutoProperty_Bean_FullBeanModelCase {

  public abstract IntegerProperty someState();

  public abstract @Nullable StringProperty descriptionProperty();

  public abstract Property<LocalDateTime> createdOn();

  public abstract Property<String> metaData();

  public abstract ReadOnlyBooleanProperty valid();

  FullBeanModelCase() {
    // hidden c'tor
  }
}
