package jp.chang.myclinic;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App 
{
    public static void main( String[] args ) throws IOException 
    {
    	download();
        System.out.println( "Hello World!" );
    }

    private static void download() throws IOException {
    	WebClient webClient = new WebClient();
    	HtmlPage page = webClient.getPage("http://www.iryohoken.go.jp/shinryohoshu/downloadMenu/");
    	System.out.println("Loaded page");
    	HtmlAnchor anchor = page.getAnchorByText("医薬品マスター");
    	Page downloadPage = anchor.click();
    	WebResponse webResponse = downloadPage.getWebResponse();
    	InputStream in = webResponse.getContentAsStream();
    	System.out.println(anchor);
    	String filename = "y.zip";
    	OutputStream out = Files.newOutputStream(Paths.get(filename));
    	byte[] buffer = new byte[1024];
    	int len = in.read(buffer);
    	while( len >= 0 ){
    		out.write(buffer, 0, len);
    		len = in.read(buffer);
    	}
    	in.close();
    	out.close();
    	webClient.close();
    }
}
