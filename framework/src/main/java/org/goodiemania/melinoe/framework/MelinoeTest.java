package org.goodiemania.melinoe.framework;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class MelinoeTest {

    private static Context classContext;

    @RegisterExtension
    static BeforeAllCallback beforeAllCallback = extensionContext -> {
        System.out.println("Before all callback; " + extensionContext.getDisplayName());
    };
    @RegisterExtension
    static AfterAllCallback afterAllCallback = extensionContext -> {
        System.out.println("After all callback");
    };

    private Context context;

    //we want to do some funky stuff here...
    public static Context getClassContext() {
        System.out.println("getClassContext()");
        return new Context() {
            public void rest() {

            }

            public void web() {

            }
        };
    }

    public Context getContext() {
        System.out.println("getContext()");
        return new Context() {
            public void rest() {

            }

            public void web() {

            }
        };
    }
}
