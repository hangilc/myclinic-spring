package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

import java.nio.file.Path;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.UncheckedIOException;

import java.util.ArrayList;
import java.util.List;

class PatientDocPreviewPanel extends JPanel {

	private int currentIndex = -1;
	private List<Path> savedPages = new ArrayList<>();
	private JLabel previewImage = new JLabel("（空白）");
	private JPanel controlPanel = new JPanel();
	private JLabel controlStatus = new JLabel();
	private JPanel navPanel = new JPanel();
	private JButton navPrev = new JButton("前へ");
	private JButton navNext = new JButton("次へ");
	private JLabel rescanLink = new JLabel("<html><font color='blue'><a><u>再スキャン</u></a></font></html>");
	private JLabel deleteLink = new JLabel("<html><font color='blue'><a><u>削除</u></a></font></html>");

	public PatientDocPreviewPanel(){
		super();
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(layout);
		initPreviewImage();
		initControlPanel();
		initNavPanel();
		add(previewImage);
		add(controlPanel);
		update();
	}

	public void addPage(Path path){
		savedPages.add(path);
		currentIndex = savedPages.size() - 1;
		onNumberOfPagesChanged(savedPages.size());
	}

	public void update(){
		updatePreviewImage();
		updateControlStatus();
		updateButtons();
	}

	public int getNumberOfPages(){
		return savedPages.size();
	}

	public void onNumberOfPagesChanged(int pages){

	}

	public void onRescan(Path path){

	}

	public void reloadImage(){
		updatePreviewImage();
	}

	public void rewriteSerials() throws IOException {
		for(int i=0;i<savedPages.size();i++){
			Path path = savedPages.get(i);
			ScannedFileName sfn = ScannedFileName.parse(path.getFileName().toString());
			if( sfn.getSerial() != (i+1) ){
				sfn.setSerial(i+1);
				Path newPath = path.resolveSibling(sfn.toString());
				System.out.println("rewrite");
				System.out.println(path.toString());
				System.out.println(newPath.toString());
				Files.move(path, newPath);
				savedPages.set(i, newPath);
			}
		}
	}

	private void initPreviewImage(){
    	Dimension dim = new Dimension(210, 297);
    	previewImage.setMinimumSize(dim);
    	previewImage.setPreferredSize(dim);
    	previewImage.setMaximumSize(dim);
	}

	private void initControlPanel(){
		JPanel panel = controlPanel;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		controlStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(Box.createVerticalGlue());
		panel.add(controlStatus);
		panel.add(navPanel);
		panel.add(Box.createVerticalGlue());
	}

	private void initNavPanel(){
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.setAlignmentX(Component.CENTER_ALIGNMENT);
			navPrev.addActionListener(this::doPrev);
			navNext.addActionListener(this::doNext);
			panel.add(navPrev);
			panel.add(navNext);
			navPanel.add(panel);
		}
		{
			rescanLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
			rescanLink.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent ev){
					if( currentIndex >= 0 && currentIndex < savedPages.size() ){
						int result = JOptionPane.showConfirmDialog(null, "本当にこのページを再スキャンしますか？",
							"再スキャンの確認", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
						if( result != JOptionPane.OK_OPTION ){
							return;
						}
						onRescan(savedPages.get(currentIndex));
					}
				}
			});
			rescanLink.setHorizontalAlignment(SwingConstants.CENTER);
			rescanLink.setAlignmentX(Component.CENTER_ALIGNMENT);
			navPanel.add(rescanLink);
		}
		{
			deleteLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
			deleteLink.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent ev){
					if( currentIndex >= 0 && currentIndex < savedPages.size() ){
						int result = JOptionPane.showConfirmDialog(null, "本当にこのページを削除しますか？",
							"削除の確認", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
						if( result != JOptionPane.OK_OPTION ){
							return;
						}
						try{
							Files.delete(savedPages.get(currentIndex));
							savedPages.remove(currentIndex);
							if( currentIndex >= savedPages.size() ){
								currentIndex -= 1;
							}
							rewriteSerials();
							update();
							onNumberOfPagesChanged(savedPages.size());
						} catch(IOException ex){
							throw new UncheckedIOException(ex);
						}
					}
				}
			});
			deleteLink.setHorizontalAlignment(SwingConstants.CENTER);
			deleteLink.setAlignmentX(Component.CENTER_ALIGNMENT);
			navPanel.add(deleteLink);
		}
	}

	private void doPrev(ActionEvent event){
		int index = currentIndex - 1;
		if( index >= 0 && index < savedPages.size() ){
			currentIndex = index;
			update();
		}
	}

	private void doNext(ActionEvent event){
		int index = currentIndex + 1;
		if( index >= 0 && index < savedPages.size() ){
			currentIndex = index;
			update();
		}
	}

	private void updatePreviewImage(){
		if( currentIndex >= 0 && currentIndex < savedPages.size() ){
			Path path = savedPages.get(currentIndex);
	    	BufferedImage origImg;
	    	try{
		    	origImg = ImageIO.read(path.toFile());
		    } catch(IOException ex){
	            JOptionPane.showMessageDialog(this, "画像を読み込めませんでした。");
	            return;
		    }
	    	int type = origImg.getType();
	    	if( type == 0 ){
	    		type = BufferedImage.TYPE_INT_ARGB;
	    	}
	    	Dimension dim = previewImage.getSize(null);
	    	BufferedImage resizedImg = new BufferedImage(dim.width, dim.height, type);
	    	Graphics2D g = resizedImg.createGraphics();
	    	g.drawImage(origImg, 0, 0, dim.width, dim.height, null);
	    	g.dispose();
	    	previewImage.setIcon(new ImageIcon(resizedImg));
		} else {
			previewImage.setIcon(null);
		}
	}

	private void updateControlStatus(){
		String label = String.format("%d/%d", currentIndex + 1, savedPages.size());
		controlStatus.setText(label);
	}

	private void updateButtons(){
		navPrev.setEnabled(currentIndex - 1 >= 0);
		navNext.setEnabled(currentIndex + 1 < savedPages.size());
		boolean enabled = currentIndex >= 0 && currentIndex < savedPages.size();
		rescanLink.setEnabled(enabled);
		deleteLink.setEnabled(enabled);
	}

}