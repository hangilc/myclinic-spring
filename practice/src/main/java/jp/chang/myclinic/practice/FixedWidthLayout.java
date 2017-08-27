package jp.chang.myclinic.practice;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FixedWidthLayout implements LayoutManager2 {

    public static class Replace {
        private Component origComponent;

        public Replace(Component origComponent){
            this.origComponent = origComponent;
        }
    }

    public static class Before {
        private Component anchorComponent;

        public Before(Component anchorComponent){
            this.anchorComponent = anchorComponent;
        }
    }

    public static class After {
        private  Component anchorComponent;

        public After(Component anchorComponent){
            this.anchorComponent = anchorComponent;
        }
    }

    private int width;
    private List<Component> series = new ArrayList<>();

    public FixedWidthLayout(int width){
        this.width = width;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        series.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int height = 0;
        for(Component c: series){
            height += c.getPreferredSize().height;
        }
        Insets insets = parent.getInsets();
        height += insets.top + insets.bottom;
        return new Dimension(width, height);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int x = insets.left;
        int height = insets.top;
        for (Component component : series) {
            Dimension size = component.getPreferredSize();
            component.setBounds(x, height, size.width, size.height);
            height += size.height;
        }
        height += insets.bottom;
        parent.setSize(width, height);
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if( constraints instanceof Replace ) {
            Replace repl = (Replace) constraints;
            int n = series.size();
            for (int i = 0; i < n; i++) {
                Component c = series.get(i);
                if (c == repl.origComponent) {
                    series.set(i, comp);
                    c.getParent().remove(c);
                }
            }
        } else if( constraints instanceof Before ) {
            Before before = (Before) constraints;
            Component anchor = before.anchorComponent;
            int n = series.size();
            for (int i = 0; i < n; i++) {
                Component c = series.get(i);
                if (c == anchor) {
                    series.add(i, comp);
                    break;
                }
            }
        } else if( constraints instanceof  After ){
            After after = (After) constraints;
            Component anchor = after.anchorComponent;
            int n = series.size();
            for (int i = 0; i < n; i++) {
                Component c = series.get(i);
                if (c == anchor) {
                    series.add(i+1, comp);
                    break;
                }
            }
        } else {
            series.add(comp);
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }
}
