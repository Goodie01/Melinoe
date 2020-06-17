package org.goodiemania.melinoe.framework;

public abstract class MelinoeTest {
    //we want to do some funky stuff here...
    public static Context getStatic() {
        return new Context() {
            public void rest() {

            }

            public void web() {

            }
        };
    }

    public Context get() {
        return new Context() {
            public void rest() {

            }

            public void web() {

            }
        };
    }
}
