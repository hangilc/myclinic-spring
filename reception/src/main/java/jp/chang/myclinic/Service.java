package jp.chang.myclinic;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import jp.chang.myclinic.dto.*;

class Service {
	public static String serverUrl;
	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	static public List<WqueueFullDTO> listWqueue() throws IOException {
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(serverUrl + "list-wqueue-full");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			//String content = readEntity(entity);
			List<WqueueFullDTO> list = null;
			try(InputStream inputStream = entity.getContent()){
				list = new ObjectMapper().readValue(inputStream, new TypeReference<List<WqueueFullDTO>>(){});
			}
			EntityUtils.consume(entity);
			return list;
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		} finally {
			response.close();
		}
	}

	static public PatientDTO getPatient(int patientId) throws IOException {
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(serverUrl + "get-patient?patient-id=" + patientId);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			PatientDTO patient = null;
			//String content = readEntity(entity);
			try(InputStream inputStream = entity.getContent()){
				patient = new ObjectMapper().readValue(inputStream, PatientDTO.class);
			}
			EntityUtils.consume(entity);
			return patient;
		} catch(IOException ex){
			throw new UncheckedIOException(ex);
		} finally {
			response.close();
		}
	}

	static public List<PatientDTO> searchPatientByName(String lastName, String firstName)
		throws IOException {
		URI uri = null;
		try{
			uri = new URIBuilder(serverUrl + "search-patient-by-name")
				.addParameter("last-name", lastName)
				.addParameter("first-name", firstName)
				.build();
		} catch(URISyntaxException ex){
			ex.printStackTrace();
			throw new RuntimeException("URI error");
		}
		HttpGet httpGet = new HttpGet(uri);
		System.out.println(httpGet);
		try(CloseableHttpResponse response = httpClient.execute(httpGet)){
			HttpEntity entity = response.getEntity();
			String content = readEntity(entity);
			EntityUtils.consume(entity);
			System.out.println(content);
			return null;
			// try(InputStream inputStream = entity.getContent()){
			// 	return new ObjectMapper().readValue(inputStream, 
			// 		new TypeReference<List<PatientDTO>>(){});
			// } finally {
			// 	EntityUtils.consume(entity);
			// }
		}
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