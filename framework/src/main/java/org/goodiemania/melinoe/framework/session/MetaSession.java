package org.goodiemania.melinoe.framework.session;

import java.util.ArrayList;
import java.util.List;
import org.goodiemania.melinoe.framework.drivers.ClosableDriver;

public class MetaSession {
    private List<ClosableDriver> drivers = new ArrayList<>();

    public MetaSession() {
        System.out.println("creating a new meta session");
    }

    public void addDriver(final ClosableDriver driver) {
        drivers.add(driver);
    }
}
