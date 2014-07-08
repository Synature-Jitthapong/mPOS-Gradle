package com.synature.pos;

import java.util.List;

public class ReasonGroups {
	public List<ReasonGroup> ReasonGroup;
	public List<ReasonDetail> ReasonDetail;
	
	public static class ReasonGroup{
		private int ReasonGroupID;
		private String ReasonGroupName;
		
		public int getReasonGroupID() {
			return ReasonGroupID;
		}
		public void setReasonGroupID(int reasonGroupID) {
			ReasonGroupID = reasonGroupID;
		}
		public String getReasonGroupName() {
			return ReasonGroupName;
		}
		public void setReasonGroupName(String reasonGroupName) {
			ReasonGroupName = reasonGroupName;
		}
		
		@Override
		public String toString() {
			return ReasonGroupName;
		}
	}
	
	public static class ReasonDetail{
		private int ReasonID;
		private int ReasonGroupID;
		private String ReasonText;
		private int Ordering;
		private boolean isChecked;
		
		public boolean isChecked() {
			return isChecked;
		}
		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}
		public int getReasonID() {
			return ReasonID;
		}
		public void setReasonID(int reasonID) {
			ReasonID = reasonID;
		}
		public int getReasonGroupID() {
			return ReasonGroupID;
		}
		public void setReasonGroupID(int reasonGroupID) {
			ReasonGroupID = reasonGroupID;
		}
		public String getReasonText() {
			return ReasonText;
		}
		public void setReasonText(String reasonText) {
			ReasonText = reasonText;
		}
		public int getOrdering() {
			return Ordering;
		}
		public void setOrdering(int ordering) {
			Ordering = ordering;
		}

		@Override
		public String toString() {
			return ReasonText;
		}
	}
}
