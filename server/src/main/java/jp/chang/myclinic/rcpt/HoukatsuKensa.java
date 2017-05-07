package jp.chang.myclinic.rcpt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;

import jp.chang.myclinic.consts.HoukatsuKensaKind;

public class HoukatsuKensa {

	@XmlElementWrapper
	@XmlElement(name = "revision")
	public Revision[] revisions;

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
		private Map<HoukatsuKensaKind, List<Step>> map;

		public Revision(){}

		@XmlJavaTypeAdapter(LocalDateAdapter.class)
		@XmlAttribute(name = "valid-from")
		public LocalDate getValidFrom(){
			return validFrom;
		}

		public void setValidFrom(LocalDate validFrom){
			this.validFrom = validFrom;
		}

		@XmlJavaTypeAdapter(GroupMapAdapter.class)
		@XmlElement(name = "groups")
		public Map<HoukatsuKensaKind, List<Step>> getMap(){
			return map;
		}

		public void setMap(Map<HoukatsuKensaKind, List<Step>> map){
			this.map = map;
		}

		public List<Step> getSteps(HoukatsuKensaKind kind){
			return map.get(kind);
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

	public static class GroupEntry {

		private String key;
		private List<Step> steps;

		public GroupEntry(){ }

		@XmlAttribute
		public String getKey(){
			return key;
		}

		public void setKey(String key){
			this.key = key;
		}

		@XmlElement(name = "step")
		public List<Step> getSteps(){
			return steps;
		}

		public void setSteps(List<Step> steps){
			this.steps = steps;
		}

	}

	public static class Group {

		private List<GroupEntry> entries;

		public Group(){}

		@XmlElement(name = "group")
		public List<GroupEntry> getEntries(){
			return entries;
		}

		public void setEntries(List<GroupEntry> entries){
			this.entries = entries;
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

	public static class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
		@Override
		public LocalDate unmarshal(String v) throws Exception {
			return LocalDate.parse(v, DateTimeFormatter.ISO_LOCAL_DATE);
		}

		@Override
		public String marshal(LocalDate v) throws Exception {
			return v.format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
	}

	public static class GroupMapAdapter extends XmlAdapter<Group, Map<HoukatsuKensaKind, List<Step>>> {
		@Override
		public Map<HoukatsuKensaKind, List<Step>> unmarshal(Group group){
			Map<HoukatsuKensaKind, List<Step>> map = new HashMap<>();
			group.entries.forEach(entry -> {
				String key = entry.getKey();
				List<Step> steps = entry.getSteps();
				map.put(HoukatsuKensaKind.fromCode(key), steps);
			});
			return map;
		}

		@Override 
		public Group marshal(Map<HoukatsuKensaKind, List<Step>> map){
			List<GroupEntry> entries = new ArrayList<>();
			map.forEach((k, v) -> {
				GroupEntry entry = new GroupEntry();
				entry.setKey(k.getCode());
				entry.setSteps(v);
				entries.add(entry);
			});
			Group group = new Group();
			group.entries = entries;
			return group;
		}
	}

	public static HoukatsuKensa load(){
		return JAXB.unmarshal(new File("./houkatsu-kensa.xml"), HoukatsuKensa.class);
	}

	public static void read(){
		HoukatsuKensa houkatsu = JAXB.unmarshal(new File("./houkatsu-kensa.xml"), HoukatsuKensa.class);
		for(Revision r: houkatsu.revisions){
			System.out.println("Revision");
			System.out.println("validFrom:" + r.getValidFrom());
			r.getMap().forEach((key, val) -> {
				System.out.println("key:" + key);
				System.out.println("steps:" + val);
			});
		}
		JAXB.marshal(houkatsu, System.out);
	}
}
