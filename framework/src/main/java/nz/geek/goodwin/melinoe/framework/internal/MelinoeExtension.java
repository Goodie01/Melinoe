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
    public static Type EXECUTION_TYPE;


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        fetchInformation(context);
        EXECUTION_TYPE = Type.BEFORE_ALL;
        System.out.println("Before all callback");
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        fetchInformation(context);
        EXECUTION_TYPE = Type.BEFORE_EACH;
        System.out.println("Before each callback");
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        fetchInformation(context);
        EXECUTION_TYPE = Type.TEST;
        System.out.println("Before test execution");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        fetchInformation(context);
        EXECUTION_TYPE = Type.AFTER_EACH;
        System.out.println("After test execution");
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        fetchInformation(context);
        EXECUTION_TYPE = Type.AFTER_ALL;
        System.out.println("After each callback");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        fetchInformation(context);
        EXECUTION_TYPE = Type.OTHER;
        System.out.println("After all callback");
    }


    private static void fetchInformation(ExtensionContext context) {
        METHOD_NAME = context.getTestMethod().map(Method::getName).orElse(null);
        CLASS_NAME = context.getTestClass().map(Class::getCanonicalName).orElse(null);
        DISPLAY_NAME = context.getDisplayName();
        context.getExecutionException().ifPresentOrElse(throwable -> THROWABLE = throwable, () -> THROWABLE = null);
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
