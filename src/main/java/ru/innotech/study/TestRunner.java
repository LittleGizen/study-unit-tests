package ru.innotech.study;

import ru.innotech.study.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {

    /**
     * The map with rules for limiting the number of methods with specific annotations.
     */
    private final Map<String, Integer> methodsLimitMap = Map.of(
            "@ru.innotech.study.annotation.BeforeSuite", 1,
            "@ru.innotech.study.annotation.AfterSuite", 1
    );

    private final List<Method> methodList = new LinkedList<>();

    public void runTests(Class c) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Object instance = c.getConstructor().newInstance();

        checkByRules(c);

        methodList.add(c.getMethod("beforeSuite"));
        methodList.addAll(getSortedTests(c));
        methodList.add(c.getMethod("afterSuite"));

        for (Method method : methodList) {
            method.invoke(instance);
        }
    }

    /**
     * Method for checking the class for compliance with the configured rules
     * There is a check for the limit of methods with the declared annotations and check for priority range in Test annotation
     * @param c - a class with methods to check
     */
    private void checkByRules(Class c) {
        List<Annotation> annotationList = new ArrayList<>();
        Arrays.stream(c.getMethods()).forEach(method -> annotationList.addAll(List.of(method.getDeclaredAnnotations())));

        //Check priority range in Test annotation
        annotationList.forEach(annotation -> {
            if(annotation instanceof Test &&
                    (((Test) annotation).priority() < 1 || ((Test) annotation).priority()>10 )) {
                System.err.printf("Incorrect Test priority - %s. Should be between 1 and 10.\n", ((Test) annotation).priority());
            }
        });

        List<String> annotationsNamesList = new ArrayList<>();
        annotationList.forEach(annotation -> {
            String annotationName = annotation.toString();
            annotationsNamesList.add(annotationName.substring(0, annotationName.indexOf('(')));
        });

        //Check for the limit of methods with the declared annotations
        methodsLimitMap.forEach((annotationName, limit) -> {
            int frequency = Collections.frequency(annotationsNamesList, annotationName);
            if (frequency > limit) {
                System.err.printf("Annotation %s limit (%s) has been exceeded. Found %s methods with this annotation.\n", annotationName, limit, frequency);
            } else {
                System.out.printf("Annotation %s limit (%s) verification passed\n", annotationName, limit);
            }
        });
    }

    /**
     * Method for getting a list of methods with a Test annotation sorted by priority
     * @param c a class with methods annotated by Test
     * @return list of sorted methods
     */
    private List<Method> getSortedTests(Class c) {
        List<Method> testsList = new LinkedList<>();
        Arrays.stream(c.getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(Test.class)) {
                testsList.add(method);
            }
        });

        testsList.sort((o1, o2) -> {
            int o1Priority = o1.getDeclaredAnnotation(Test.class).priority();
            int o2Priority = o2.getDeclaredAnnotation(Test.class).priority();
            return (o1Priority - o2Priority);
        });

        return testsList;
    }
}
