package org.goodiemania.melinoe.framework.drivers.web.page;

import org.goodiemania.melinoe.framework.api.misc.Point;

public class PointImpl implements Point {
    private final int pointX;
    private final int pointY;

    public PointImpl(final int pointX, final int pointY) {
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public static Point of(final int x, final int y) {
        return new PointImpl(x, y);
    }

    @Override
    public int getX() {
        return pointX;
    }

    @Override
    public int getY() {
        return pointY;
    }
}
