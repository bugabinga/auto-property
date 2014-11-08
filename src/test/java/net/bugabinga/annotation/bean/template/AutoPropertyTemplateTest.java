package net.bugabinga.annotation.bean.template;

import net.bugabinga.annotation.bean.template.model.TestModel;

import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import javafx.util.Pair;

import static net.java.quickcheck.generator.PrimitiveGeneratorsIterables.someIntegers;
import static net.java.quickcheck.generator.PrimitiveGeneratorsIterables.someDoubles;
import static net.java.quickcheck.generator.PrimitiveGeneratorsIterables.somePrintableStrings;
import static net.java.quickcheck.generator.PrimitiveGeneratorsIterables.someStrings;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by bugabinga on 26.07.14.
 */
public class AutoPropertyTemplateTest {

  public static <A, B, C> Stream<C> zip(Stream<? extends A> a,
                                        Stream<? extends B> b,
                                        BiFunction<? super A, ? super B, ? extends C> zipper) {
    Objects.requireNonNull(zipper);
    @SuppressWarnings("unchecked")
    Spliterator<A> aSpliterator = (Spliterator<A>) Objects.requireNonNull(a).spliterator();
    @SuppressWarnings("unchecked")
    Spliterator<B> bSpliterator = (Spliterator<B>) Objects.requireNonNull(b).spliterator();

    // Zipping looses DISTINCT and SORTED characteristics
    int characteristics = aSpliterator.characteristics() & bSpliterator.characteristics() &
                          ~(Spliterator.DISTINCT | Spliterator.SORTED);

    long zipSize = ((characteristics & Spliterator.SIZED) != 0)
                   ? Math
                       .min(aSpliterator.getExactSizeIfKnown(), bSpliterator.getExactSizeIfKnown())
                   : -1;

    Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
    Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
    Iterator<C> cIterator = new Iterator<C>() {
      @Override
      public boolean hasNext() {
        return aIterator.hasNext() && bIterator.hasNext();
      }

      @Override
      public C next() {
        return zipper.apply(aIterator.next(), bIterator.next());
      }
    };

    Spliterator<C> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
    return (a.isParallel() || b.isParallel())
           ? StreamSupport.stream(split, true)
           : StreamSupport.stream(split, false);
  }

  @Test
  public void testAutoPropertyTemplate() {
    //To test the ST template for correctness, we generate a test java class, compile it and run unit tests against it
    final STGroup group =
        new STGroupFile("net/bugabinga/annotation/bean/template/autoProperty.stg");

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
        new Property("DoubleProperty", "stateProperty", "SimpleDoubleProperty", "state", "Double",
                     "",
                     "", ""));

    st.add("packageName", "com.test.debug")
        .add("imports", Arrays
            .asList("javafx.beans.property.StringProperty",
                    "javafx.beans.property.SimpleStringProperty",
                    "javafx.beans.property.IntegerProperty",
                    "javafx.beans.property.SimpleIntegerProperty",
                    "javafx.beans.property.DoubleProperty",
                    "javafx.beans.property.SimpleDoubleProperty", "java.util.Objects",
                    "net.bugabinga.annotation.bean.template.model.TestModel"))
        .add("className", "TestModel")
        .add("isBean", true)
        .add("params", params);

    final String rendered = st.render(80);

    assertFalse(rendered.isEmpty());

    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    try (StandardJavaFileManager fileManager = compiler
        .getStandardFileManager(diagnostics, null, null)) {

      JavaCompiler.CompilationTask task = compiler.getTask(
          null,
          fileManager,
          diagnostics,
          null,
          null,
          fileManager.getJavaFileObjectsFromStrings(Arrays.asList(rendered)));

      if (task.call()) {
    	  try(
        final URLClassLoader
            classLoader =
            new URLClassLoader(new URL[]{new File("./").toURI().toURL()})) {
    		  
        final Class<?> loadedClass = classLoader.loadClass("com.test.debug.AutoProperty_TestModel");

        //this monster zips 4 random value generator streams into one
        zip(
            zip(stream(someStrings()), stream(somePrintableStrings()), Pair<String, String>::new),
            zip(stream(someIntegers()), stream(someDoubles()), Pair<Integer, Double>::new),
            (Pair<String, String> descName, Pair<Integer, Double> countState) -> Arrays
                .asList(descName.getKey(), descName.getValue(), countState.getKey(),
                        countState.getValue()))

            .forEach(props -> {
              Object obj = null;
              try {
                obj = loadedClass
                    .getMethod("create", String.class, String.class, Integer.class,
                               Double.class)
                    .invoke(null, props.get(0), props.get(1), props.get(2),
                            props.get(3));
              } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                fail(e.getMessage());
              }

              assertTrue(obj instanceof TestModel);
              testCompiledTemplate((TestModel) obj);
            });
    	  }

      } else {
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
          fail(String.format("Error on line %d in %s%n",
                             diagnostic.getLineNumber(),
                             diagnostic.getSource().toUri()));
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      fail(e.getMessage());
    }
  }

  private void testCompiledTemplate(TestModel model) {
    // model is an instance of the compiled java class from the ST template
    // now we assert some things about that instance

  }

  private <T> Stream<T> stream(Iterable<T> iteratable) {
    return StreamSupport.stream(iteratable.spliterator(), false);
  }
}
