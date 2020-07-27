package org.goodiemania.melinoe.framework.drivers.web.page;

import org.goodiemania.melinoe.framework.api.misc.Rectangle;

public class RectangleImpl implements Rectangle {
    private final int width;
    private final int height;
    private final int pointX;
    private final int pointY;

    public RectangleImpl(final int width, final int height, final int pointX, final int pointY) {
        this.width = width;
        this.height = height;
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public static Rectangle of(final int width, final int height, final int x, final int y) {
        return new RectangleImpl(width, height, x, y);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
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
