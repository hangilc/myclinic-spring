package jp.chang.myclinic;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class App 
{
    public static void main( String[] args ) throws IOException 
    {
    	JCommander jc = new JCommander();
    	CommandDownload cDownload = new CommandDownload();
    	jc.addCommand("download", cDownload);
    	jc.parse(args);
    	String command = jc.getParsedCommand();
    	if( "download".equals(command) ){
    		List<String> masters = cDownload.getMasterNames();
    		System.out.println("download: " + masters);
    	}
    	System.out.println(command);
    }

    private static void downloadShinryou() throws IOException {
    	download("医科診療行為マスター", "s.zip");
    }

    private static void downloadIyakuhin() throws IOException {
    	download("医薬品マスター", "y.zip");
    }

    private static void downloadKizai() throws IOException {
    	download("特定器材マスター", "t.zip");
    }

    private static void downloadShoubyoumei() throws IOException {
    	download("傷病名マスター", "b.zip");
    }

    private static void downloadShuushokugo() throws IOException {
    	download("修飾語マスター", "z.zip");
    }

    private static void download(String anchorText, String filepath) throws IOException {
    	WebClient webClient = new WebClient();
    	HtmlPage page = webClient.getPage("http://www.iryohoken.go.jp/shinryohoshu/downloadMenu/");
    	HtmlAnchor anchor = page.getAnchorByText(anchorText);
    	Page downloadPage = anchor.click();
    	WebResponse webResponse = downloadPage.getWebResponse();
    	InputStream in = webResponse.getContentAsStream();
    	String filename = "y.zip";
    	OutputStream out = Files.newOutputStream(Paths.get(filepath));
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

@Parameters(separators="=", commandDescription="Download master file")
class CommandDownload {
	@Parameter(description="Master names")
	private List<String> masterNames;

	public List<String> getMasterNames(){
		return masterNames;
	}
}
