/*
 * @(#)AbstractAnnotationProcessorTest.java 5 Jun 2009
 *
 * Copyright © 2010 Andrew Phillips. Copyright © 2014 Olivr Jan Krylow (okrylow@gmail.com).
 *
 * ==================================================================== Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. ====================================================================
 */
package net.bugabinga.annotation.bean;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;


/**
 * A base test class for {@link Processor annotation processor} testing that attempts to compile
 * source test cases that can be found on the classpath.
 *
 * @author aphillips
 * @author Oliver Jan Krylow (okrylow@gmail.com)
 * @since 5 Jun 2009
 *
 */
public abstract class AbstractAnnotationProcessorTest {
  private static final String SOURCE_FILE_SUFFIX = ".java";
  private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

  /**
   * @return the processor instances that should be tested
   */
  protected abstract Collection<Processor> getProcessors();

  /**
   * Attempts to compile the given compilation units using the Java Compiler API.
   * <p>
   * The compilation units and all their dependencies are expected to be on the classpath.
   *
   * @param compilationUnits the classes to compile
   * @return the {@link Diagnostic diagnostics} returned by the compilation, as demonstrated in the
   *         documentation for {@link JavaCompiler}
   * @see #compileTestCase(String...)
   */
  protected List<Diagnostic<? extends JavaFileObject>> compileTestCase(
      final Class<?>... compilationUnits) {
    assert compilationUnits != null;

    final String[] compilationUnitPaths = new String[compilationUnits.length];

    for (int i = 0; i < compilationUnitPaths.length; i++) {
      assert compilationUnits[i] != null;
      compilationUnitPaths[i] = toResourcePath(compilationUnits[i]);
    }

    return compileTestCase(compilationUnitPaths);
  }

  private static String toResourcePath(final Class<?> clazz) {
    return clazz.getName().replace('.', '/') + SOURCE_FILE_SUFFIX;
  }

  /**
   * Attempts to compile the given compilation units using the Java Compiler API.
   * <p>
   * The compilation units and all their dependencies are expected to be on the classpath.
   *
   * @param compilationUnitPaths the paths of the source files to compile, as would be expected by
   *        {@link ClassLoader#getResource(String)}
   * @return the {@link Diagnostic diagnostics} returned by the compilation, as demonstrated in the
   *         documentation for {@link JavaCompiler}
   * @see #compileTestCase(Class...)
   *
   */
  protected List<Diagnostic<? extends JavaFileObject>> compileTestCase(
      final String... compilationUnitPaths) {
    assert compilationUnitPaths != null;

    Collection<File> compilationUnits;

    try {
      compilationUnits = findClasspathFiles(compilationUnitPaths);
    } catch (final IOException exception) {
      throw new IllegalArgumentException("Unable to resolve compilation units "
          + Arrays.toString(compilationUnitPaths) + " due to: " + exception.getMessage(), exception);
    }

    final DiagnosticCollector<JavaFileObject> diagnosticCollector =
        new DiagnosticCollector<JavaFileObject>();
    final StandardJavaFileManager fileManager =
        COMPILER.getStandardFileManager(diagnosticCollector, null, null);

    /*
     * Call the compiler with the "-proc:only" option. The "class names" option (which could, in
     * principle, be used instead of compilation units for annotation processing) isn't useful in
     * this case because only annotations on the classes being compiled are accessible.
     *
     * Information about the classes being compiled (such as what they are annotated with) is *not*
     * available via the RoundEnvironment. However, if these classes are annotations, they certainly
     * need to be validated.
     */
    final CompilationTask task =
        COMPILER.getTask(null, fileManager, diagnosticCollector, Arrays.asList("-proc:only"), null,
            fileManager.getJavaFileObjectsFromFiles(compilationUnits));
    task.setProcessors(getProcessors());
    task.call();

    try {
      fileManager.close();
    } catch (final IOException exception) {
    }

    return diagnosticCollector.getDiagnostics();
  }

  private static Collection<File> findClasspathFiles(final String[] filenames) throws IOException {
    final Collection<File> classpathFiles = new ArrayList<File>(filenames.length);

    for (final String filename : filenames) {
      classpathFiles.add(getFile(new URL(filename), "Class file [" + filename + "]"));
    }

    return classpathFiles;
  }

  /**
   * Resolve the given resource URL to a <code>java.io.File</code>, i.e. to a file in the file
   * system.
   *
   * @param resourceUrl the resource URL to resolve
   * @param description a description of the original resource that the URL was created for (for
   *        example, a class path location)
   * @return a corresponding File object
   * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
   */
  private static File getFile(final URL resourceUrl, final String description)
      throws FileNotFoundException {
    requireNonNull(resourceUrl, "Resource URL must not be null");
    if (!"file".equals(resourceUrl.getProtocol())) {
      throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
          + "because it does not reside in the file system: " + resourceUrl);
    }
    try {
      return new File(toURI(resourceUrl).getSchemeSpecificPart());
    } catch (final URISyntaxException ex) {
      // Fallback for URLs that are not valid URIs (should hardly ever happen).
      return new File(resourceUrl.getFile());
    }
  }

  /**
   * Create a URI instance for the given URL, replacing spaces with "%20" quotes first.
   * <p>
   * Furthermore, this method works on JDK 1.4 as well, in contrast to the <code>URL.toURI()</code>
   * method.
   *
   * @param url the URL to convert into a URI instance
   * @return the URI instance
   * @throws URISyntaxException if the URL wasn't a valid URI
   * @see java.net.URL#toURI()
   */
  private static URI toURI(final URL url) throws URISyntaxException {
    return toURI(url.toString());
  }

  /**
   * Create a URI instance for the given location String, replacing spaces with "%20" quotes first.
   *
   * @param location the location String to convert into a URI instance
   * @return the URI instance
   * @throws URISyntaxException if the location wasn't a valid URI
   */
  private static URI toURI(final String location) throws URISyntaxException {
    return new URI(location.replaceAll(" ", "%20"));
  }

  /**
   * Asserts that the compilation produced no errors, i.e. no diagnostics of type {@link Kind#ERROR}
   * .
   *
   * @param diagnostics the result of the compilation
   * @see #assertCompilationReturned(Kind, long, List)
   * @see #assertCompilationReturned(Kind[], long[], List)
   */
  protected static void assertCompilationSuccessful(
      final List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    assert diagnostics != null;

    for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
      assertFalse("Expected no errors", diagnostic.getKind().equals(Kind.ERROR));
    }

  }

  /**
   * Asserts that the compilation produced results of the following {@link Kind Kinds} at the given
   * line numbers, where the <em>n</em>th kind is expected at the <em>n</em>th line number.
   * <p>
   * Does not check that these is the <em>only</em> diagnostic kinds returned!
   *
   * @param expectedDiagnosticKinds the kinds of diagnostic expected
   * @param diagnostics the result of the compilation
   * @see #assertCompilationSuccessful(List)
   * @see #assertCompilationReturned(Kind, long, List)
   */
  protected static void assertCompilationReturned(final Kind[] expectedDiagnosticKinds,
      final long[] expectedLineNumbers, final List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    assert expectedDiagnosticKinds != null && expectedLineNumbers != null
        && expectedDiagnosticKinds.length == expectedLineNumbers.length;

    for (int i = 0; i < expectedDiagnosticKinds.length; i++) {
      assertCompilationReturned(expectedDiagnosticKinds[i], expectedLineNumbers[i], diagnostics);
    }

  }

  /**
   * Asserts that the compilation produced a result of the following {@link Kind} at the given line
   * number.
   * <p>
   * Does not check that this is the <em>only</em> diagnostic kind returned!
   *
   * @param expectedDiagnosticKind the kind of diagnostic expected
   * @param expectedLineNumber the line number at which the diagnostic is expected
   * @param diagnostics the result of the compilation
   * @see #assertCompilationSuccessful(List)
   * @see #assertCompilationReturned(Kind[], long[], List)
   */
  protected static void assertCompilationReturned(final Kind expectedDiagnosticKind,
      final long expectedLineNumber, final List<Diagnostic<? extends JavaFileObject>> diagnostics) {
    assert expectedDiagnosticKind != null && diagnostics != null;
    boolean expectedDiagnosticFound = false;

    for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {

      if (diagnostic.getKind().equals(expectedDiagnosticKind)
          && diagnostic.getLineNumber() == expectedLineNumber) {
        expectedDiagnosticFound = true;
      }

    }

    assertTrue("Expected a result of kind " + expectedDiagnosticKind + " at line "
        + expectedLineNumber, expectedDiagnosticFound);
  }

}
