
package view.components;

import java.awt.*;

public class WrapLayout extends FlowLayout {

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap); // ignore the warning
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getSize().width;

            if (targetWidth == 0) {
                targetWidth = Integer.MAX_VALUE;
            }

            Insets insets = target.getInsets();
            int horizontalInsets = insets.left + insets.right + getHgap() * 2;
            int maxWidth = targetWidth - horizontalInsets;

            Dimension dimension = new Dimension(0, 0);
            int rowWidth = 0;
            int rowHeight = 0;

            for (Component component : target.getComponents()) {
                if (!component.isVisible()) {
                    continue;
                }

                Dimension size = preferred
                        ? component.getPreferredSize()
                        : component.getMinimumSize();

                if (rowWidth + size.width > maxWidth) {
                    dimension.width = Math.max(dimension.width, rowWidth);
                    dimension.height += rowHeight + getVgap();

                    rowWidth = 0;
                    rowHeight = 0;
                }

                if (rowWidth > 0) {
                    rowWidth += getHgap();
                }

                rowWidth += size.width;
                rowHeight = Math.max(rowHeight, size.height);
            }

            dimension.width = Math.max(dimension.width, rowWidth);
            dimension.height += rowHeight;

            dimension.width += horizontalInsets;
            dimension.height += insets.top + insets.bottom + getVgap() * 2;

            return dimension;
        }
    }
}
