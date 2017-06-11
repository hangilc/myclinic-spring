package jp.chang.myclinic;

import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.MeisaiSectionDTO;
import jp.chang.myclinic.dto.SectionItemDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MeisaiDetailDialog extends JDialog {

    public MeisaiDetailDialog(Window owner, MeisaiDTO meisai){
        super(owner, "明細の詳細");
        setLayout(new MigLayout("", "", ""));
        add(new MeisaiDetailPane(meisai), "");
        pack();
    }

    private static class MeisaiDetailPane extends JPanel {
        MeisaiDetailPane(MeisaiDTO meisai) {
            super(new MigLayout("insets 0, gapy 0", "", ""));
            for(int i=0; i < meisai.sections.size(); i++){
                MeisaiSectionDTO section = meisai.sections.get(i);
                add(new JLabel("【" + section.label + "】"), "span 2, wrap");
                for(SectionItemDTO item: section.items){
                    WrappedText wt = new WrappedText(item.label, 200);
                    add(wt, "");
                    add(new JLabel(String.format("%dx%d=%d", item.tanka, item.count, item.tanka * item.count)), "right, wrap");
                }
            }
        }
    }
}

class WrappedText extends JPanel {

    String text;
    int width;
    int height;
    List<String> lines;

    WrappedText(String text, int width){
        setLayout(null);
        this.text = text;
        this.width = width;
        this.lines = breakToLines();
        this.height = getFont().getSize() * lines.size();
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = getFont().getSize();
        int y = fm.getAscent();
        for(String line: lines){
            g.drawString(line, 0, y);
            y += lineHeight;
        }
    }

    private List<String> breakToLines(){
        FontMetrics fm = getFontMetrics(getFont());
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int curWidth = 0;
        for(char ch: text.toCharArray()){
            int cw = fm.charWidth(ch);
            if( curWidth > 0 && curWidth + cw > width ){
                lines.add(sb.toString());
                sb.setLength(0);
                curWidth = 0;
            }
            sb.append(ch);
            curWidth += cw;
        }
        if( sb.length() > 0 ){
            lines.add(sb.toString());
        }
        return lines;
    }

}


