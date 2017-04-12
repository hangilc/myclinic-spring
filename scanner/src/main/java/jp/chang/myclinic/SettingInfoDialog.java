package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.ChangeEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class SettingInfoDialog extends JDialog {

	SettingInfoDialog(Frame owner, String title, boolean modal){
		super(owner, title, modal);
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
			{"myclinic.scanner.save.dir", new DirValue(ScannerSetting.INSTANCE.savingDir)}
		}, new Object[]{"キー", "値"}){
			@Override
			public Class<?> getColumnClass(int column){
				if( column == 1 ){
					return Value.class;
				} else {
					return Object.class;
				}
			}

			@Override
			public void editingStopped(ChangeEvent event){
				super.editingStopped(event);
				System.out.println("change event");
				System.out.println(event.getSource());
			}
		};
		table.setPreferredSize(new Dimension(400, 40));
		table.setDefaultRenderer(Value.class, new ValueRenderer());
		table.setDefaultEditor(Value.class, new ValueEditor());
		add(table, BorderLayout.CENTER);
	}

	private void setupCommands(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton closeButton = new JButton("閉じる");
		closeButton.addActionListener(event -> {
			dispose();
		});
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

	private static class DirectoryPath {
		public Path path;

		DirectoryPath(Path path){
			this.path = path;
		}
	}

	private static class Value { }

	private static class DirValue extends Value {
		public Path path;

		DirValue(Path path){
			this.path = path;
		}
	}

	private static class ValueRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column){
			if( value instanceof DirValue ){
				DirValue dirValue = (DirValue)value;
				return super.getTableCellRendererComponent(table, dirValue.path, isSelected, hasFocus, row, column);
			} else {
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		}
	}

	private static class ValueEditor extends AbstractCellEditor implements TableCellEditor {
		private Value currentValue;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
			int row, int column){
			currentValue = (Value)value;
			if( value instanceof DirValue ){
				DirValue dirValue = (DirValue)value;
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
				JTextField textField = new JTextField(dirValue.path.toString());
				textField.addActionListener(event -> {
					String text = textField.getText();
					Path newPath = Paths.get(text);
					if( Files.exists(newPath) && Files.isDirectory(newPath) ){
						currentValue = new DirValue(newPath);
						fireEditingStopped();
					} else {
						Toolkit.getDefaultToolkit().beep();
					}
				});
				JButton button = new JButton("参照");
				button.addActionListener(event -> {
					JFileChooser fc = new JFileChooser(dirValue.path.toFile());
					fc.setDialogTitle("保存フォルダーの選択");
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int ret = fc.showOpenDialog(table);
					if( ret == JFileChooser.APPROVE_OPTION ){
						File file = fc.getSelectedFile();
						currentValue = new DirValue(file.toPath());
						fireEditingStopped();
					}
				});
				panel.add(textField);
				panel.add(Box.createHorizontalStrut(5));
				panel.add(button);
				return panel;
			} else {
				return new JLabel("editing");
			}
		}

		@Override
		public Object getCellEditorValue(){
			return currentValue;
		}
	}

}