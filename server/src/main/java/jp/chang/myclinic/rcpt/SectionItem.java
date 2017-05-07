package jp.chang.myclinic.rcpt;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SectionItem {
	private int count;

	SectionItem(){
		count = 1;
	}

	public int getCount(){
		return count;
	}

	protected void incCount(int n){
		count += n;
	}

	public abstract int getTanka();
	public abstract String getLabel();

	public boolean canExtend(Object arg){
		return false;
	}

	public void extend(Object arg){
		throw new RuntimeException("cannot happen");
	}

	public static int sum(List<SectionItem> items){
		return items.stream()
		.collect(Collectors.summingInt(item -> item.getTanka() * item.getCount()));
	}

	@Override
	public String toString(){
		return "SectionItem[" +
			"tanka=" + getTanka() + "," +
			"count=" + getCount() + "," +
			"label=" + getLabel() + //"," +
		"]";
	}

}

/*
import java.util.List;
import java.util.stream.Collectors;

public abstract class SectionItem {

	private final Object key;
	private int count;

	SectionItem(Object key){
		this.key = key;
		this.count = 1;
	}

	public Object getKey(){
		return key;
	}

	public boolean canMerge(SectionItem item){
		return getKey().equals(item.getKey());
	}

	public void merge(SectionItem item){
		if( !canMerge(item) ){
			throw new RuntimeException("inconsistent key");
		}
		setCount(getCount() + item.getCount());
	}

	abstract int getTanka();
	abstract String getLabel();

	public int getCount(){
		return count;
	}

	public void setCount(int count){
		this.count = count;
	}

	public static int sum(List<SectionItem> items){
		return items.stream()
		.collect(Collectors.summingInt(item -> item.getTanka() * item.getCount()));
	}

	@Override
	public String toString(){
		return "SectionItem[" +
			"tanka=" + getTanka() + "," +
			"count=" + getCount() + "," +
			"label=" + getLabel() + //"," +
		"]";
	}

}
*/