package jp.chang.myclinic;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App 
{
	static class MasterDownloadInfo {
		public String text;
		public String name;

		MasterDownloadInfo(String text, String name){
			this.text = text;
			this.name = name;
		}
	}

	static Map<String, MasterDownloadInfo> downloadInfoMap = new LinkedHashMap<>();

	static {
		downloadInfoMap.put("shinryou", new MasterDownloadInfo("医科診療行為マスター", "s"));
		downloadInfoMap.put("iyakuhin", new MasterDownloadInfo("医薬品マスター", "y"));
		downloadInfoMap.put("kizai", new MasterDownloadInfo("特定器材マスター", "t"));
		downloadInfoMap.put("shoubyoumei", new MasterDownloadInfo("傷病名マスター", "b"));
		downloadInfoMap.put("shuushokugo", new MasterDownloadInfo("修飾語マスター", "z"));
	}

    public static void main(String[] args)
    {
    	if( args.length == 0 ){
    		usage();
    		System.exit(1);
    	}
    	confirmArgs(args);
    	Arrays.stream(args).forEach(master -> {
    		MasterDownloadInfo info = downloadInfoMap.get(master);
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");
    		String stamp = LocalDateTime.now().format(formatter);
    		String file = info.name + "-" + stamp + ".zip";
    		download(info.text, file);
    	});
    }

    private static void usage(){
    	Options options = new Options();
    	HelpFormatter formatter = new HelpFormatter();
    	String available = String.join(",", downloadInfoMap.keySet().stream().collect(Collectors.toList()));
    	String usage = "master-download [options] master...";
    	String header = "available masters are: " + available;
    	String footer = "example: master-download iyakuhin";
    	formatter.printHelp(usage, header, options, footer);
    }

    private static void confirmArgs(String[] args){
    	Set<String> keys = downloadInfoMap.keySet();
    	List<String> invalids = Arrays.stream(args)
    		.filter(name -> !keys.contains(name))
    		.collect(Collectors.toList());
    	if( invalids.size() > 0 ){
    		System.out.println("Error: invalid args to master-download");
    		System.out.println(invalids);
    	}
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

    private static void download(String anchorText, String filepath) {
    	try{
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
	    } catch(IOException ex){
	    	throw new UncheckedIOException(ex);
	    }
    }
}
