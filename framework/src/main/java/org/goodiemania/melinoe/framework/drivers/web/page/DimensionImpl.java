package org.goodiemania.melinoe.framework.drivers.web.page;

import org.goodiemania.melinoe.framework.api.misc.Dimension;

public class DimensionImpl implements Dimension {
    private final int width;
    private final int height;

    private DimensionImpl(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public static Dimension of(final int width, final int height) {
        return new DimensionImpl(width, height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
