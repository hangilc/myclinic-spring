package jp.chang.myclinic.rcpt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;

public class HoukatsuKensa {

	public static class Revision {

		private LocalDate validFrom;
		private Map<String, List<Step>> map;

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
		public Map<String, List<Step>> getMap(){
			return map;
		}

		public void setMap(Map<String, List<Step>> map){
			this.map = map;
		}

	}

	public static class GroupEntry {

		private String key;
		private List<Step> steps;

		public GroupEntry(){
			System.out.println("GroupEntry()");
		}

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
		@XmlElement(name = "group")
		public List<GroupEntry> entries;

		public Group(){
			System.out.println("Group()");
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

	public static class GroupMapAdapter extends XmlAdapter<Group, Map<String, List<Step>>> {
		@Override
		public Map<String, List<Step>> unmarshal(Group group){
			Map<String, List<Step>> map = new HashMap<>();
			group.entries.forEach(entry -> {
				String key = entry.getKey();
				List<Step> steps = entry.getSteps();
				map.put(key, steps);
			});
			return map;
		}

		@Override 
		public Group marshal(Map<String, List<Step>> map){
			List<GroupEntry> entries = new ArrayList<>();
			map.forEach((k, v) -> {
				GroupEntry entry = new GroupEntry();
				entry.setKey(k);
				entry.setSteps(v);
				entries.add(entry);
			});
			Group group = new Group();
			group.entries = entries;
			return group;
		}
	}

	@XmlElement(name = "revision")
	public Revision[] revisions;

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
	}
}

/*
public class HoukatsuKensa {
	private String version;
	private Revision[] revisions;

	HoukatsuKensa(Revision[] revisions){
		this.revisions = revisions;
	}

	public static class Revision {
		private LocalDate validFrom;
		private Map<String, Step[]> map;

		public Revision(String validFromRep, Map<String, Step[]> map){
			setValidFrom(validFromRep);
			this.map = map;
		}

		private void setValidFrom(String value){
			validFrom = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
		}
	}

	public static class Step {
		private int threshold;
		private int point;

		public Step(int threshold, int point){
			this.threshold = threshold;
			this.point = point;
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
	}

}
*/
