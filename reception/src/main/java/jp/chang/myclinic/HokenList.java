package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.ShahokokuhoUtil;
import jp.chang.myclinic.util.KoukikoureiUtil;
import jp.chang.myclinic.util.RoujinUtil;
import jp.chang.myclinic.util.KouhiUtil;

class HokenList extends JList<Object> {

	HokenList(){
		this(6);
	}

	HokenList(int nrow){
		setCellRenderer(new Renderer());
		setVisibleRowCount(nrow);
	}

	static class Renderer extends JLabel implements ListCellRenderer<Object> {

		Renderer(){
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus){
			setText(makeLabel(value));
			if( isSelected ){
				setBackground(list.getSelectionBackground());
			} else {
				setBackground(list.getBackground());
			}
			return this;
		}

		private String makeLabel(Object obj){
			if( obj instanceof ShahokokuhoDTO ){
				return ShahokokuhoUtil.rep((ShahokokuhoDTO)obj);
			} else if( obj instanceof KoukikoureiDTO ){
				return KoukikoureiUtil.rep((KoukikoureiDTO)obj);
			} else if( obj instanceof RoujinDTO ){
				return RoujinUtil.rep((RoujinDTO)obj);
			} else if( obj instanceof KouhiDTO ){
				return KouhiUtil.rep((KouhiDTO)obj);
			} else {
				return "不明の保険";
			}
		}
	}

}