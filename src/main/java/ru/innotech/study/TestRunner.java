package ru.innotech.study;

import ru.innotech.study.annotation.AfterSuite;
import ru.innotech.study.annotation.BeforeSuite;
import ru.innotech.study.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestRunner {

    /**
     * The map with rules for limiting the number of methods with specific annotations.
     */
    private final Map<Class, Integer> methodsLimitMap = Map.of(
            BeforeSuite.class, 1,
            AfterSuite.class, 1
    );

    public void runTests(Class c) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Object instance = c.getConstructor().newInstance();
        Method[] methods = c.getDeclaredMethods();

        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> testList = new LinkedList<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                checkStaticModifier(method, BeforeSuite.class);
                if (beforeSuiteMethod == null) {
                    beforeSuiteMethod = method;
                } else {
                    System.err.printf("Annotation %s limit has been exceeded. Found another method (%s) with this annotation.\n", BeforeSuite.class, method);
                }
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                checkStaticModifier(method, AfterSuite.class);
                if (afterSuiteMethod == null) {
                    afterSuiteMethod = method;
                } else {
                    System.err.printf("Annotation %s limit has been exceeded. Found another method (%s) with this annotation.\n", AfterSuite.class, method);
                }
            } else if (method.isAnnotationPresent(Test.class)) {
                checkPriorityRange(method);
                testList.add(method);
            }
        }

        testList.sort((Method o1, Method o2) -> {
            int o1Priority = o1.getDeclaredAnnotation(Test.class).priority();
            int o2Priority = o2.getDeclaredAnnotation(Test.class).priority();
            return (o1Priority - o2Priority);
        });

        if (beforeSuiteMethod != null) {
            beforeSuiteMethod.invoke(instance);
        }
        for (Method method : testList) {
            method.invoke(instance);
        }
        if (afterSuiteMethod != null) {
            afterSuiteMethod.invoke(instance);
        }
    }

    private void checkPriorityRange(Method method) {
        int priority = method.getDeclaredAnnotation(Test.class).priority();
        if (priority < 1 || priority > 10) {
            System.err.printf("Annotation %s priority out of range (1-10) in method %s.\n", Test.class, method);
        }
    }

    private void checkStaticModifier(Method method, Class annotationClass) {
        if (!Modifier.isStatic(method.getModifiers())) {
            System.err.printf("Method %s marked with %s but not static.\n", method, annotationClass);
        }
    }
}
