package jp.chang.myclinic.server.rcpt;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jp.chang.myclinic.consts.HoukatsuKensaKind;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

//import javax.xml.bind.JAXB;

//import javax.xml.bind.JAXB;

public class HoukatsuKensa {

	public Revision[] revisions;

	public static class LocalDateDeserializer extends StdDeserializer<LocalDate> {

		protected LocalDateDeserializer(){
			super(LocalDate.class);
		}

		@Override
		public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
			return LocalDate.parse(jsonParser.readValueAs(String.class));
		}
	}

	public static class KensaMapDeserializer extends StdDeserializer<KensaMapWrapper>{

		protected KensaMapDeserializer(){
			super(KensaMapWrapper.class);
		}

		@Override
		public KensaMapWrapper deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
			KensaMapWrapper kensaMapWrapper = new KensaMapWrapper();
			List<Group> groups = jsonParser.readValueAs(new TypeReference<List<Group>>(){});
			for(Group group: groups){
				HoukatsuKensaKind kind = HoukatsuKensaKind.fromCode(group.getKey());
				kensaMapWrapper.put(kind, group.getSteps());
			}
			return kensaMapWrapper;
		}
	}

	public Optional<Integer> calcTen(HoukatsuKensaKind kind, int n, LocalDate at){
		Revision r = findRevision(at);
		if( r == null ){
			return Optional.empty();
		} else {
			return r.calcTen(kind, n);
		}
	}

	public Revision findRevision(LocalDate at){
		for(Revision r: revisions){
			if( r.validFrom.compareTo(at) <= 0 ){
				return r;
			}
		}
		return null;
	}

	public static class Revision {

		private LocalDate validFrom;
		private KensaMapWrapper mapWrapper;

		public Revision(){}

		@JsonDeserialize(using = LocalDateDeserializer.class)
		@JacksonXmlProperty(localName="valid-from")
		public LocalDate getValidFrom(){
			return validFrom;
		}

		public void setValidFrom(LocalDate validFrom){
			this.validFrom = validFrom;
		}

		@JacksonXmlProperty(localName="groups")
		@JsonDeserialize(using = KensaMapDeserializer.class)
		public KensaMapWrapper getMapWrapper(){
			return mapWrapper;
		}

		public void setMapWrapper(KensaMapWrapper map){
			this.mapWrapper = map;
		}

		public Map<HoukatsuKensaKind, List<Step>> getMap(){
			return mapWrapper.getMap();
		}

		public List<Step> getSteps(HoukatsuKensaKind kind){
			List<Step> steps = mapWrapper.get(kind);
			if( steps == null ){
				return Collections.emptyList();
			} else {
				return steps;
			}
		}

		public Optional<Integer> calcTen(HoukatsuKensaKind kind, int n){
			for(Step step: getSteps(kind)){
				if( step.getThreshold() <= n ){
					return Optional.of(step.getPoint());
				}
			}
			return Optional.empty();
		}

	}

	public static class KensaMapWrapper {
		private  Map<HoukatsuKensaKind, List<Step>> map = new HashMap<>();

		public void put(HoukatsuKensaKind kind, List<Step> steps){
			map.put(kind, steps);
		}

		public List<Step> get(HoukatsuKensaKind kind){
			return map.get(kind);
		}

		public Map<HoukatsuKensaKind, List<Step>> getMap(){
			return map;
		}
	}

	public static class Group {

		private String key;
		private List<Step> steps;

		public Group(){ }

		@JacksonXmlProperty(isAttribute = true)
		public String getKey(){
			return key;
		}

		public void setKey(String key){
			this.key = key;
		}

		@JacksonXmlElementWrapper(useWrapping = false)
		@JacksonXmlProperty(localName = "step")
		public List<Step> getSteps(){
			return steps;
		}

		public void setSteps(List<Step> steps){
			this.steps = steps;
		}

		@Override
		public String toString() {
			return "Group{" +
					"key='" + key + '\'' +
					", steps=" + steps +
					'}';
		}
	}

	public static class Step {

		private int threshold;
		private int point;

		public Step(){

		}

		public int getThreshold(){
			return threshold;
		}

		public void setThreshold(int threshold){
			this.threshold = threshold;
		}

		public int getPoint(){
			return point;
		}

		public void setPoint(int point){
			this.point = point;
		}

		@Override
		public String toString(){
			return "Step[" +
				"threshold=" + threshold + "," + 
				"point=" + point + 
			"]";
		}
	}

	public static HoukatsuKensa load() throws IOException {
		XmlMapper xmlMapper = new XmlMapper();
		HoukatsuKensa houkatsuKensa = xmlMapper.readValue(new File("./config/houkatsu-kensa.xml"), HoukatsuKensa.class);
//		for(Revision r: houkatsuKensa.revisions){
//			System.out.println("Revision");
//			System.out.println("validFrom:" + r.getValidFrom());
//			r.getMap().forEach((key, val) -> {
//				System.out.println("key:" + key);
//				System.out.println("steps:" + val);
//			});
//		}
		return houkatsuKensa;
	}

	public static void read(){
//		HoukatsuKensa houkatsu = JAXB.unmarshal(new File("./houkatsu-kensa.xml"), HoukatsuKensa.class);
//		for(Revision r: houkatsu.revisions){
//			System.out.println("Revision");
//			System.out.println("validFrom:" + r.getValidFrom());
//			r.getMap().forEach((key, val) -> {
//				System.out.println("key:" + key);
//				System.out.println("steps:" + val);
//			});
//		}
//		JAXB.marshal(houkatsu, System.out);
	}
}
