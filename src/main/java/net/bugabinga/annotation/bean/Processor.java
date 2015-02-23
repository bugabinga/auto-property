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
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

/**
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 */
@SupportedAnnotationTypes(Processor.AUTO_PROPERTY_ANNOTATION_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Processor extends AbstractProcessor {

  static final String AUTO_PROPERTY_ANNOTATION_NAME = "net.bugabinga.annotation.bean.AutoProperty";

  private static final String
      NON_ABSTRACT_CLASS_WARNING =
      "You have annotated a non-abstract class with @AutoProperty. This will not lead to automatic property generation. Please check {0}.";

  private static final String
      INTERFACE_WARNING =
      "You have annotated an interface with @AutoProperty. This will not lead to automatic property generation. Please check {0}.";

  @Override
  public boolean process(final Set<? extends TypeElement> annotations,
                         final RoundEnvironment roundEnv) {
    final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(AutoProperty.class);
    note("PROCESSING  YOU! " + elements);

    // 1. get annotated classes and mine for the necessary data
    elements.stream()
        .peek(this::warnIfInterface)
        .filter(element -> element.getKind().equals(ElementKind.CLASS))
        .peek(this::warnIfNotAbstract)
        .filter(element -> element.getModifiers()
        .contains(Modifier.ABSTRACT))
        .map(Element::getEnclosedElements);

    // 2. generate AutoProperty_XXX instance from templates and data

    // 3. generate AutoProperty_Bean_XXX if necessary

    return false;
  }

  private Element warnIfInterface(final Element element) {
    if (element.getKind().equals(ElementKind.INTERFACE)) {
      warning(MessageFormat.format(INTERFACE_WARNING, element.getSimpleName()));
    }
    return element;
  }

  private Element warnIfNotAbstract(final Element element) {
    if (!element.getModifiers().contains(Modifier.ABSTRACT)) {
      warning(MessageFormat.format(NON_ABSTRACT_CLASS_WARNING, element.getSimpleName()));
    }
    return element;
  }

  private void note(final String note) {
    processingEnv.getMessager().printMessage(Kind.NOTE, note);
  }

  private void warning(final String note) {
    processingEnv.getMessager().printMessage(Kind.WARNING, note);
  }

  private void error(final String note) {
    processingEnv.getMessager().printMessage(Kind.ERROR, note);
  }

  private void mandatory(final String note) {
    processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, note);
  }

  private void other(final String note) {
    processingEnv.getMessager().printMessage(Kind.OTHER, note);
  }

}
