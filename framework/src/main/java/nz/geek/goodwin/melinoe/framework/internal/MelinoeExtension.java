package nz.geek.goodwin.melinoe.framework.internal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

/**
 * @author Goodie
 */
public class MelinoeExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {
    public static String DISPLAY_NAME;
    public static String CLASS_NAME;
    public static String METHOD_NAME;
    public static Throwable THROWABLE;
    public static boolean IN_TEST_METHOD = false;
    public static boolean FIRST_TEST_RUN = false;
    public static Type EXECUTION_TYPE;


    public static boolean inTest() {
        return IN_TEST_METHOD;
    }


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        fetchInformation(context);
        System.out.println("Before all callback");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        fetchInformation(context);
        System.out.println("Before each callback");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        fetchInformation(context);
        System.out.println("After test execution");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        fetchInformation(context);
        IN_TEST_METHOD = true;
        FIRST_TEST_RUN = true;
        System.out.println("Before test execution");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        fetchInformation(context);
        System.out.println("After all callback");
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        fetchInformation(context);
        System.out.println("After each callback");
    }


    private static void fetchInformation(ExtensionContext context) {
        IN_TEST_METHOD = false;
        METHOD_NAME = context.getTestMethod().map(Method::getName).orElse(null);
        CLASS_NAME = context.getTestClass().map(Class::getCanonicalName).orElse(null);
        DISPLAY_NAME = context.getDisplayName();
        context.getExecutionException().ifPresentOrElse(throwable -> THROWABLE = throwable, () -> THROWABLE = null);

        context.getTestMethod().ifPresentOrElse(
                //TODO We will always get the test method, we need to be able to distinguish
                //Somehow After all ends up here???
                method -> {if(method.getAnnotation(BeforeEach.class) != null) {
                        EXECUTION_TYPE = Type.BEFORE_EACH;
                    } else if (method.getAnnotation(AfterEach.class) != null) {
                        EXECUTION_TYPE = Type.AFTER_EACH;
                    } else if (method.getAnnotation(Test.class) != null) {
                        EXECUTION_TYPE = Type.TEST;
                    } else {
                        EXECUTION_TYPE = Type.OTHER;
                    }
                },
                () -> {
                    EXECUTION_TYPE = Type.BEFORE_ALL;
                });
    }

    public enum Type {
        BEFORE_ALL,
        BEFORE_EACH,
        TEST,
        AFTER_EACH,
        AFTER_ALL,
        OTHER,
        ;
    }
}
