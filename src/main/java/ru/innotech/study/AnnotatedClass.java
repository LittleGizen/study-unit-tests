package ru.innotech.study;

import ru.innotech.study.annotation.AfterSuite;
import ru.innotech.study.annotation.BeforeSuite;
import ru.innotech.study.annotation.Test;

public class AnnotatedClass {

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("beforeSuite");
    }

    @Test
    public void test() {
        System.out.println("Test with priority by default - 5 ");
    }

    @Test(priority = 1)
    public void test2() {
        System.out.println("Test with priority 1");
    }

    @Test(priority = 10)
    public void test3() {
        System.out.println("Test with priority 10");
    }
    @AfterSuite
    public static void afterSuite() {
        System.out.println("afterSuite");
    }
}
