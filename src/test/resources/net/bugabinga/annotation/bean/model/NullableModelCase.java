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

import javax.annotation.Nullable;

import net.bugabinga.annotation.bean.AutoProperty;

/**
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 *
 */
@AutoProperty
public abstract class NullableModelCase {

public abstract @Nullable Property<Object> meSoDirty();
public abstract @Nullable Property<Object> meSoDoge();

NullableModelCase() {
  // hidden c'tor
}

public @interface Nullable {}
}
