package jp.chang.myclinic.pdfbundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        InputStreamReader streamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(streamReader);
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, System.out);
        document.open();
        reader.lines().forEach(line -> {
            String fname = line.strip();
            if( fname.isEmpty() ){
                return;
            }
            try {
                PdfReader pdfReader = new PdfReader(fname);
                int n = pdfReader.getNumberOfPages();
                for(int i=0;i<n;i++){
                    copy.addPage(copy.getImportedPage(pdfReader, i+1));
                }
            } catch(Exception e){
                throw new RuntimeException(e);
            }
        });
        copy.flush();
        document.close();
    }

}
