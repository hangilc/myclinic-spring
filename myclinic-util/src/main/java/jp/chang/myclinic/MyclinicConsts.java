package jp.chang.myclinic;

/**
 * Hello world!
 *
 */
public class MyclinicConsts 
{
	public static final int WqueueStateWaitExam = 0;
	public static final int WqueueStateInExam = 1;
	public static final int WqueueStateWaitCashier = 2;
	public static final int WqueueStateWaitDrug = 3;
	public static final int WqueueStateWaitReExam = 4;

	public enum WqueueState {
		WaitExam(0, "診待"),
		InExam(1, "診中"),
		WaitCashier(2, "会待"),
		WaitDrug(3, "薬待"),
		WaitReExam(4, "再待");

		private int code;
		private String label;

		WqueueState(int code, String label){
			this.code = code;
			this.label = label;
		}

		@Override
		public String toString(){
			return label;
		}
	}
}
