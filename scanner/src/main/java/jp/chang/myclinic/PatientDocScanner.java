package jp.chang.myclinic;

import jp.chang.wia.Wia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientDocScanner extends JDialog {

    private int patientId;
    private int lastPageIndex;
    private boolean scanningHokensho = false;
    private String timeStamp;
    private Path saveDir;
    //private String deviceId;
    private static DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
    private PatientDocInfoPanel patientDocInfoPanel;
    private PatientDocPreviewPanel patientDocPreviewPanel;
    private static Logger logger = LoggerFactory.getLogger(PatientDocScanner.class);

    public PatientDocScanner(Frame owner, int patientId) {
        this(owner, patientId, false);
    }

    public PatientDocScanner(Frame owner, int patientId, boolean scanningHokensho){
        super(owner, true);
        this.scanningHokensho = scanningHokensho;
        if( scanningHokensho ){
            setTitle(String.format("保険証のスキャン (%d)", patientId));
        } else {
            setTitle(String.format("患者書類のスキャン (%d)", patientId));
        }
        this.patientId = patientId;
        this.lastPageIndex = 0;
        this.timeStamp = makeTimeStamp();
        this.saveDir = getSaveDir();
        this.patientDocInfoPanel = new PatientDocInfoPanel(patientId);
        this.patientDocPreviewPanel = new PatientDocPreviewPanel(){
            @Override
            public void onRescan(Path path){
                doRescan(path);
            }

            @Override
            public void onNumberOfPagesChanged(int pages){
                patientDocInfoPanel.updateTotalPages(pages);
            }
        };
        add(makeCenterPanel(), BorderLayout.CENTER);
        add(makeCommandPanel(), BorderLayout.SOUTH);
        pack();
    }

    private JComponent makeCenterPanel(){
        JPanel panel = new JPanel();
        panel.add(makeCenterPanelContent());
        return panel;
    }

    private JComponent makeCenterPanelContent(){
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(boxLayout);
        panel.add(patientDocInfoPanel);
        panel.add(patientDocPreviewPanel);
        return panel;
    }

    private JComponent makeCommandPanel(){
        JPanel panel = new JPanel();
        JButton start = new JButton("スタート");
        JButton finish = new JButton("終了");
        int maxWidth = 0;
        for(JButton btn: new JButton[]{start, finish}){
            Dimension dim = btn.getPreferredSize();
            if( dim.width > maxWidth ){
                maxWidth = dim.width;
            }
        }
        for(JButton btn: new JButton[]{start, finish}){
            Dimension dim = btn.getPreferredSize();
            dim.width = maxWidth;
            btn.setPreferredSize(dim);
        }
        start.addActionListener(this::doStart);
        finish.addActionListener(event -> {
            dispose();
        });
        panel.add(start);
        panel.add(finish);
        return panel;
    }

    private Path getSaveDir(){
        return ScannerSetting.INSTANCE.savingDir;
    }

    private void addPage(Path path){
        patientDocPreviewPanel.addPage(path);
        patientDocPreviewPanel.update();
    }

    private int getNextPageIndex(){
        return patientDocPreviewPanel.getNumberOfPages() + 1;
    }

    private void doRescan(Path path){
        String deviceId = resolveDeviceId();
        if( deviceId == null ){
        	return;
        }
        Path savePath = null;
        try{
            savePath = File.createTempFile("rescan", ".bmp").toPath();
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this, "failed to printersetting temporary file");
            return;
        }
        ScannerDialog dialog = new ScannerDialog(this, deviceId, savePath);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        if( !dialog.isCanceled() ){
            logger.debug("dialog ended");
            try{
                convertImage(savePath, "jpg", path);
                logger.info("re-scanned file {}", path);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "画像コンバージョンに失敗しました。");
                logger.error("convertImage failed", ex);
            } catch(OutOfMemoryError e){
                JOptionPane.showMessageDialog(this, "メモリー不足エラー");
                logger.error("Out Of Memory");
            }
        }
    }

    private void doStart(ActionEvent event){
        String deviceId = resolveDeviceId();
        if( deviceId == null ){
        	return;
        }
        String saveFileName = composeSaveFileName();
        Path savePath = saveDir.resolve(saveFileName);
        ScannerDialog dialog = new ScannerDialog(this, deviceId, savePath);
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        if( !dialog.isCanceled() ){
            logger.debug("dialog ended");
            try{
                Path outPath = convertImage(savePath, "jpg");
                addPage(outPath);
                logger.info("scanned file {}", savePath);
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "画像コンバージョンに失敗しました。");
                logger.error("convertImage failed", ex);
            } catch(OutOfMemoryError e){
                JOptionPane.showMessageDialog(this, "メモリー不足エラー");
                logger.error("Out Of Memory");
            }
        }
    }

    private String composeSaveFileName(){
        if( scanningHokensho ){
            return String.format("%d-hokensho-%s-%02d.bmp", patientId, timeStamp, getNextPageIndex());
        } else {
            return String.format("%d-%s-%02d.bmp", patientId, timeStamp, getNextPageIndex());
        }
    }

    private static Path convertImage(Path source, String format, Path output) throws IOException {
        BufferedImage src = ImageIO.read(source.toFile());
        System.out.println(output.toString());
        boolean ok = ImageIO.write(src, format, output.toFile());
        if( !ok ){
            throw new RuntimeException("image conversion failed");
        }
        Files.delete(source);
        return output;
    }

    private static Path convertImage(Path source, String format) throws IOException {
        String srcFileName = source.getFileName().toString();
        String dstFileName = srcFileName.replaceFirst("\\.bmp$", "." + format);
        Path output = source.resolveSibling(dstFileName);
        return convertImage(source, format, output);
    }

    private String resolveDeviceId(){
        {
            String deviceId = ScannerSetting.INSTANCE.defaultDevice;
            if( !"".equals(deviceId) ){
                return deviceId;
            }
        }
        List<Wia.Device> devices = Wia.listDevices();
        if( devices.size() == 0 ){
            JOptionPane.showMessageDialog(this, "接続された。スキャナーがみつかりません。");
            return null;
        } else if( devices.size() == 1 ){
            return devices.get(0).deviceId;
        } else {
        	for(Wia.Device dev: devices){
        		System.out.println(dev.name);
        	}
            return ScannerUtil.pickDevice();
        }
    }

    private String makeTimeStamp(){
        LocalDateTime dt = LocalDateTime.now();
        return dt.format(timeStampFormatter);
    }
}