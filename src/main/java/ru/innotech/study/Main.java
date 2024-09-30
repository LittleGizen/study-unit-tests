package ru.innotech.study;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        TestRunner testRunner = new TestRunner();
        testRunner.runTests(AnnotatedClass.class);
    }
}
