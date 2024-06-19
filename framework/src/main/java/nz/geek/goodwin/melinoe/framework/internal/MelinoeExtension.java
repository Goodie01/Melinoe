package nz.geek.goodwin.melinoe.framework.internal;



import java.io.StringWriter;
import java.io.PrintWriter;

import nz.geek.goodwin.melinoe.framework.api.Session;
import nz.geek.goodwin.melinoe.framework.api.log.Logger;
import nz.geek.goodwin.melinoe.framework.internal.log.LoggerImpl;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.bidi.log.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Goodie
 */
public class MelinoeExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {
    public static String CLASS_DISPLAY_NAME;
    public static String DISPLAY_NAME;
    public static String CLASS_NAME;
    public static String METHOD_NAME;
    public static Throwable THROWABLE;
    public static TestMethodType EXECUTION_TYPE;

    public static boolean IN_JUNIT_TEST = false;

    private static Map<String, Logger> LOG_STORE = new HashMap<>();


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        populateInformation(context);
        EXECUTION_TYPE = TestMethodType.BEFORE_ALL;
        //populateClassSession(context);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        populateInformation(context);
        EXECUTION_TYPE = TestMethodType.BEFORE_EACH;
        //populateInstanceSession(context);
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        populateInformation(context);
        EXECUTION_TYPE = TestMethodType.TEST;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        populateInformation(context);
        EXECUTION_TYPE = TestMethodType.AFTER_EACH;

        Object logger = context.getStore(ExtensionContext.Namespace.GLOBAL).get("logger"); //TODO store logger here, so I can pull it out and add a failed exception, if said exception exists
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        populateInformation(context);
        EXECUTION_TYPE = TestMethodType.AFTER_ALL;
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        populateInformation(context);
        EXECUTION_TYPE = TestMethodType.OTHER;
        Session.closeAll();
    }

    public static void storeLogger(Logger logger) {
        LOG_STORE.put(createLoggerCustomStoreName(), logger);
    }


    private static void populateInformation(ExtensionContext context) {
        IN_JUNIT_TEST = true;
        METHOD_NAME = context.getTestMethod().map(Method::getName).orElse(null);
        CLASS_NAME = context.getTestClass().map(Class::getCanonicalName).orElse(null);
        DISPLAY_NAME = context.getDisplayName();
        if(METHOD_NAME == null) {
            CLASS_DISPLAY_NAME = DISPLAY_NAME;
        }

        context.getExecutionException().ifPresentOrElse(throwable -> {
            THROWABLE = throwable;
            Logger logger = LOG_STORE.get(createLoggerCustomStoreName());
            if(logger == null) {
                return;
            }

            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();

            logger.add().withSuccess(false).withMessage(stacktrace);

        }, () -> THROWABLE = null);
    }

    private static String createLoggerCustomStoreName() {
        String logMessage = switch (MelinoeExtension.EXECUTION_TYPE) {
            case BEFORE_ALL -> "before_all";
            case BEFORE_EACH -> "before_each";
            case AFTER_EACH -> "after_each";
            case AFTER_ALL -> "after_all";
            default -> "ather???";
        };

        if(isBeforeOrAfterAll()) {
            return CLASS_NAME + "." + logMessage;
        } else {
            if(isBeforeOrAfterEach()) {
                return CLASS_NAME + "." + METHOD_NAME + "."+ logMessage;
            }
            return CLASS_NAME + "." + METHOD_NAME;
        }
    }


    private static boolean isBeforeOrAfterEach() {
        return EXECUTION_TYPE == TestMethodType.BEFORE_EACH || EXECUTION_TYPE == TestMethodType.AFTER_EACH;
    }

    private static boolean isBeforeOrAfterAll() {
        return EXECUTION_TYPE == TestMethodType.BEFORE_ALL || EXECUTION_TYPE == TestMethodType.AFTER_ALL;
    }


    private static void populateInstanceSession(ExtensionContext context) throws IllegalAccessException {
        Object instance = context.getTestInstance().orElseThrow();
        Field[] fields = context.getTestClass().orElseThrow().getDeclaredFields();
        for (Field field : fields) {
            if (!field.getType().equals(Session.class)) {
                break;
            }

            if (Modifier.isStatic(field.getModifiers())) {
                break;
            }

            field.setAccessible(true);
            field.set(instance, MotherSession.getInstance().newSession());
        }
    }

    private static void populateClassSession(ExtensionContext context) throws IllegalAccessException {
        Field[] fields = context.getTestClass().orElseThrow().getDeclaredFields();
        for (Field field : fields) {
            if (!field.getType().equals(Session.class)) {
                break;
            }

            if (!Modifier.isStatic(field.getModifiers())) {
                break;
            }

            field.setAccessible(true);
            field.set(null, MotherSession.getInstance().newSession());
        }
    }
}
