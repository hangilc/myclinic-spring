package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.ChangeEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EventObject;

class SettingInfoDialog extends JDialog {

	private static class Setting {
		public Path savingDir;
		public int dip;
		public String defaultDevice;

		Setting(){
			this.savingDir = ScannerSetting.INSTANCE.savingDir;
			this.dip = ScannerSetting.INSTANCE.dip;
			this.defaultDevice = ScannerSetting.INSTANCE.defaultDevice;
		}
	}

	private final Setting setting;

	SettingInfoDialog(Frame owner, String title, boolean modal){
		super(owner, title, modal);
		setting = new Setting();
		setupLabel();
		setupTable();
		setupCommands();
		pack();
	}

	private void setupLabel(){
		JLabel label = new JLabel();
		label.setText(ScannerSetting.INSTANCE.settingFile.toString());
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		add(label, BorderLayout.NORTH);
	}

	private void setupTable(){
		JTable table = new JTable(new Object[][]{
			{"保存フォルダー", new DirValue(setting.savingDir, path -> { setting.savingDir = path; })},
			{"解像度(dip)", new IntValue(setting.dip, ival -> { setting.dip = ival; })},
			{"スキャナー", new DeviceValue(setting.defaultDevice, val -> { setting.defaultDevice = val; })}
		}, new Object[]{"キー", "値"}){
			@Override
			public boolean isCellEditable(int row, int col){
				return col == 1;
			}
		};
		System.out.println(table.getColumnClass(0));
		table.setPreferredSize(new Dimension(400, 80));
		table.setDefaultRenderer(Object.class, new Renderer());
		table.setDefaultEditor(Object.class, new Editor());
		add(table, BorderLayout.CENTER);
	}

	private void setupCommands(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton storeButton = new JButton("設定ファイルに保存");
		storeButton.addActionListener(event -> {
			storeButton.setEnabled(false);
			writeToGlobal();
			try{
				ScannerSetting.INSTANCE.saveToFile();
			} catch(IOException ex){
				JOptionPane.showMessageDialog(null, "ファイルの保存に失敗しました。");
			}
			storeButton.setEnabled(true);
		});
		JButton closeButton = new JButton("閉じる");
		closeButton.addActionListener(event -> {
			writeToGlobal();
			dispose();
		});
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		panel.add(storeButton);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

	private void writeToGlobal(){
		ScannerSetting global = ScannerSetting.INSTANCE;
		global.savingDir = setting.savingDir;
		global.dip = setting.dip;
		global.defaultDevice = setting.defaultDevice;
	}

	private static abstract class Value {
		private Object origValue;
		private Object value;

		Value(Object value){
			this.origValue = value;
			this.value = value;
		}

		public Object getValue(){
			return value;
		}

		public void setValue(Object value){
			this.value = value;
		}

		abstract public Component getTableCellEditorComponent(AbstractCellEditor editor, JTable table, 
			Object object, boolean isSelected, int row, int column);

		public Component getTableCellEditorComponent(JTable table, Object object, boolean isSelected, int row, int column){
			return new JLabel("Editor");
		}

	}

	private static class DirValue extends Value {
		public static interface ChangeHandler {
			void handle(Path path);
		}

		private ChangeHandler handler;

		DirValue(Path path, ChangeHandler handler){
			super(path);
			this.handler = handler;
		}

		public Path getPath(){
			return (Path)getValue();
		}

		public void setPath(Path path){
			setValue(path);
		}

		@Override
		public Component getTableCellEditorComponent(AbstractCellEditor editor, JTable table, 
			Object object, boolean isSelected, int row, int column){
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			JTextField textField = new JTextField(getPath().toString());
			textField.addActionListener(event -> {
				String text = textField.getText();
				Path newPath = Paths.get(text);
				changeCurrentPath(editor, newPath);
			});
			JButton browseButton = new JButton("参照");
			browseButton.addActionListener(event -> {
				JFileChooser jfc = new JFileChooser(getPath().toFile());
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret = jfc.showDialog(table, "決定");
				if( ret == JFileChooser.APPROVE_OPTION ){
					changeCurrentPath(editor, jfc.getSelectedFile().toPath());
				}
			});
			panel.add(textField);
			panel.add(Box.createHorizontalStrut(2));
			panel.add(browseButton);
			return panel;
		}

		private void changeCurrentPath(AbstractCellEditor editor, Path newPath){
			if( Files.exists(newPath) && Files.isDirectory(newPath) ){
				setPath(newPath);
				editor.stopCellEditing();
				handler.handle(newPath);
			} else {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	private static class IntValue extends Value {
		public interface ChangeHandler {
			void handle(int ival);
		}

		private ChangeHandler handler;

		IntValue(Integer ival, ChangeHandler handler){
			super(ival);
			this.handler = handler;
		}

		public int getInt(){
			return (Integer)getValue();
		}

		public void setInt(Integer ival){
			setValue(ival);
		}

		@Override
		public Component getTableCellEditorComponent(AbstractCellEditor editor, JTable table, 
			Object object, boolean isSelected, int row, int column){
			JTextField textField = new JTextField(String.valueOf(getInt()));
			textField.addActionListener(event -> {
				String text = textField.getText();
				try{
					int ival = Integer.parseInt(text);
					setInt(ival);
					editor.stopCellEditing();
					handler.handle(ival);
				} catch(NumberFormatException ex){
					Toolkit.getDefaultToolkit().beep();
				}
			});
			return textField;
		}
	}

	private static class DeviceValue extends Value {
		public interface ChangeHandler {
			void handle(String val);
		}

		private ChangeHandler handler;

		DeviceValue(String sval, ChangeHandler handler){
			super(sval);
			this.handler = handler;
		}

		public String getString(){
			return (String)getValue();
		}

		public void setString(String sval){
			setValue(sval);
		}

		@Override
		public Component getTableCellEditorComponent(AbstractCellEditor editor, JTable table, 
			Object object, boolean isSelected, int row, int column){
			JPanel panel = new JPanel();
			if( isSelected ){
				panel.setBackground(table.getSelectionBackground());
			} else {
				panel.setBackground(table.getBackground());
			}
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			JButton pickButton = new JButton("参照");
			pickButton.addActionListener(event -> {
				String device = ScannerUtil.pickDevice();
				System.out.println(device);
			});
			panel.add(pickButton);
			return panel;
		}

		@Override 
		public Component getTableCellEditorComponent(JTable table, Object object, boolean isSelected, int row, int column){
			JPanel panel = new JPanel();
			if( isSelected ){
				panel.setBackground(table.getSelectionBackground());
			} else {
				panel.setBackground(table.getBackground());
			}
			panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
			JButton pickButton = new JButton("参照");
			// pickButton.addActionListener(event -> {
			// 	String device = ScannerUtil.pickDevice();
			// 	System.out.println(device);
			// });
			panel.add(pickButton);
			return panel;
		}
	}

	private static class Renderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected,
			boolean hasFocus, int row, int column){
			if( object instanceof Value ){
				Value value = (Value)object;
				if( isSelected ){
					return value.getTableCellEditorComponent(table, value, isSelected, row, column);
				}
				return super.getTableCellRendererComponent(table, value.getValue(), isSelected, hasFocus, row, column);
			} else  {
				return super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);
			}
		}
	}

	private static class Editor extends AbstractCellEditor implements TableCellEditor {
		private Value current;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object object, boolean isSelected,
			int row, int column){
			if( object instanceof Value ){
				current = (Value)object;
				return current.getTableCellEditorComponent(this, table, object, isSelected, row, column);
			} else {
				return new JLabel("Not supported");
			}
		}

		@Override
		public Object getCellEditorValue(){
			return current;
		}
	}

}