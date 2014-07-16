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

import java.util.Collection;
import java.util.Collections;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic.Kind;

import org.junit.Test;

/**
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 *
 */
public class ProcessorTest extends AbstractAnnotationProcessorTest {

  /**
   *
   */
  private static final String CASE_PKG = "/net/bugabinga/annotation/bean/model/";

  @Test
  public void test() {

    // A very simple case with one property.
    assertCompilationSuccessful(compileTestCase(CASE_PKG + "SimpleModelCase.java"));

    // A simple case with one property and getter and setter.
    assertCompilationSuccessful(compileTestCase(CASE_PKG + "SimpleBeanModelCase.java"));


    // With Nullable parameters. Both javax.annotation.Nullable and a custom Nullable annotation.
    assertCompilationSuccessful(compileTestCase(CASE_PKG + "NullableModelCase.java"));

    // With custom property implementations.
    assertCompilationSuccessful(compileTestCase(CASE_PKG + "CustomBeanModelCase.java"));

    // If the constructor is not hidden, we throw a warning.
    assertCompilationReturned(Kind.WARNING, 32, compileTestCase(CASE_PKG
        + "WithoutHiddenCtorCase.java"));

    // Error should be thrown if the annotated type is an interface.
    assertCompilationReturned(Kind.ERROR, 27, compileTestCase(CASE_PKG
        + "AnnotatedInterfaceCase.java"));

    // Error should be thrown if the annotated type is not abstract.
    assertCompilationReturned(Kind.ERROR, 27, compileTestCase(CASE_PKG
        + "NonAbstractClassAnnotatedCase.java"));

    // with properties named with the 'Property' suffix.
    assertCompilationSuccessful(compileTestCase(CASE_PKG
        + "FollowingPropertyNamingConventionCase.java"));

    // The most complete possible case.
    assertCompilationSuccessful(compileTestCase(CASE_PKG + "FullBeanModelCase.java"));

    // Stress test. A class with a lot of properties!
    assertCompilationSuccessful(compileTestCase(CASE_PKG + "StressTestCase.java"));
  }

  @Override
  protected Collection<Processor> getProcessors() {
    return Collections.singletonList(new net.bugabinga.annotation.bean.Processor());
  }
}
