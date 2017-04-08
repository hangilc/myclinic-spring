package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.io.IOException;

class PatientDocPreviewPanel extends JPanel {

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
		updatePreviewImage(null);
		initNavPanel();
		add(previewImage);
		add(controlPanel);
	}

	public void update(Path imagePath, int index, int total){
		updatePreviewImage(imagePath);
		updateControlStatus(index, total);
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
		updateControlStatus(0, 0);
		panel.add(controlStatus);
		panel.add(navPanel);
	}

	private void initNavPanel(){
		BoxLayout layout = new BoxLayout(navPanel, BoxLayout.X_AXIS);
		navPanel.setLayout(layout);
		navPanel.add(navPrev);
		navPanel.add(navNext);
	}

	private void updatePreviewImage(Path path){
		if( path == null ){
			previewImage.setText("（空白）");
		} else {
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
		}
	}

	private void updateControlStatus(int index, int total){
		String label = String.format("%d/%d", index, total);
		controlStatus.setText(label);
	}

}