package jp.chang.myclinic;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import jp.chang.myclinic.dto.*;

class Service {

	static public List<WqueueFullDTO> listWqueue() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/json/list-wqueue-full");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String content = readEntity(entity);
			System.out.println(content);
			EntityUtils.consume(entity);
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		} finally {
			response.close();
		}
		return null;
	}

	static private String readEntity(HttpEntity entity) throws IOException {
		try(InputStream ins = entity.getContent()){
			int bufferSize = 1024;
			char[] buffer = new char[bufferSize];
			StringBuilder out = new StringBuilder();
			Reader in = new InputStreamReader(ins, "UTF-8");
			for(;;){
				int nbytes = in.read(buffer, 0, buffer.length);
				if( nbytes < 0 ){
					break;
				}
				out.append(buffer, 0, nbytes);
			}
			return out.toString();
		}
	}

}