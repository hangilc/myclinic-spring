package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

class PatientDocPreviewPanel extends JPanel {

	private int currentIndex = -1;
	private List<Path> savedPages = new ArrayList<>();
	private JLabel previewImage = new JLabel();
	private JPanel controlPanel = new JPanel();
	private JLabel controlStatus = new JLabel();
	private JPanel navPanel = new JPanel();
	private JButton navPrev = new JButton("前へ");
	private JButton navNext = new JButton("次へ");

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
	}

	public void update(){
		updatePreviewImage();
		updateControlStatus();
		updateButtons();
	}

	public int getNumberOfPages(){
		return savedPages.size();
	}

	private void initPreviewImage(){
    	Dimension dim = new Dimension(210, 297);
    	previewImage.setMinimumSize(dim);
    	previewImage.setPreferredSize(dim);
    	previewImage.setMaximumSize(dim);
	}

	private void initControlPanel(){
		JPanel panel = controlPanel;
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		panel.add(controlStatus);
		panel.add(navPanel);
	}

	private void initNavPanel(){
		BoxLayout layout = new BoxLayout(navPanel, BoxLayout.X_AXIS);
		navPanel.setLayout(layout);
		navPrev.addActionListener(this::doPrev);
		navNext.addActionListener(this::doNext);
		navPanel.add(navPrev);
		navPanel.add(navNext);
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
			previewImage.setText("（空白）");
		}
	}

	private void updateControlStatus(){
		String label = String.format("%d/%d", currentIndex + 1, savedPages.size());
		controlStatus.setText(label);
	}

	private void updateButtons(){
		navPrev.setEnabled(currentIndex - 1 >= 0);
		navNext.setEnabled(currentIndex + 1 < savedPages.size());
	}

}