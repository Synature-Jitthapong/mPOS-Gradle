package com.synature.pos;

import java.util.List;

public class Question {
	
	private List<QuestionGroup> QuestionGroup;
	private List<QuestionDetail> QuestionDetail;
	private List<AnswerOption> AnswerOption;
	
	public List<QuestionGroup> getQuestionGroup() {
		return QuestionGroup;
	}

	public void setQuestionGroup(List<QuestionGroup> questionGroup) {
		QuestionGroup = questionGroup;
	}

	public List<QuestionDetail> getQuestionDetail() {
		return QuestionDetail;
	}

	public void setQuestionDetail(List<QuestionDetail> questionDetail) {
		QuestionDetail = questionDetail;
	}

	public List<AnswerOption> getAnswerOption() {
		return AnswerOption;
	}

	public void setAnswerOption(List<AnswerOption> answerOption) {
		AnswerOption = answerOption;
	}

	public static class AnswerOption{
		private int AnswerID;
        private int QuestionID;
        private String AnswerName;
        
        public AnswerOption(int ansId, int quesId, String ansName){
        	this.AnswerID = ansId;
        	this.QuestionID = quesId;
        	this.AnswerName = ansName;
        }
        
		public int getAnswerID() {
			return AnswerID;
		}
		public void setAnswerID(int answerID) {
			AnswerID = answerID;
		}
		public int getQuestionID() {
			return QuestionID;
		}
		public void setQuestionID(int questionID) {
			QuestionID = questionID;
		}
		public String getAnswerName() {
			return AnswerName;
		}
		public void setAnswerName(String answerName) {
			AnswerName = answerName;
		}
	}
	
	public static class QuestionAnswerData
    {
        private int iQuestionID;
        private int iAnsOptionID;
        private double fAnsValue;
        private String szAnsText;

		public int getiQuestionID() {
			return iQuestionID;
		}
		public void setiQuestionID(int iQuestionID) {
			this.iQuestionID = iQuestionID;
		}
		public int getiAnsOptionID() {
			return iAnsOptionID;
		}
		public void setiAnsOptionID(int iAnsOptionID) {
			this.iAnsOptionID = iAnsOptionID;
		}
		public double getfAnsValue() {
			return fAnsValue;
		}
		public void setfAnsValue(double fAnsValue) {
			this.fAnsValue = fAnsValue;
		}
		public String getSzAnsText() {
			return szAnsText;
		}
		public void setSzAnsText(String szAnsText) {
			this.szAnsText = szAnsText;
		}
    }

	public static class QuestionDetail{
		private int QuestionID;
		private String QuestionName;
		private int QuestionGroupID;
		private int QuestionTypeID;
		private int IsRequired;
		private int Ordering;
		private double questionValue;
		private int questionOptId;
		private int optionId;
		private boolean requireSelect;
		
		public boolean isRequireSelect() {
			return requireSelect;
		}
		public void setRequireSelect(boolean requireSelect) {
			this.requireSelect = requireSelect;
		}
		public int getOptionId() {
			return optionId;
		}
		public void setOptionId(int optionId) {
			this.optionId = optionId;
		}
		public double getQuestionValue() {
			return questionValue;
		}
		public void setQuestionValue(double questionValue) {
			this.questionValue = questionValue;
		}
		public int getQuestionOptId() {
			return questionOptId;
		}
		public void setQuestionOptId(int questionOptId) {
			this.questionOptId = questionOptId;
		}
		public int getQuestionID() {
			return QuestionID;
		}
		public void setQuestionID(int questionID) {
			QuestionID = questionID;
		}
		public String getQuestionName() {
			return QuestionName;
		}
		public void setQuestionName(String questionName) {
			QuestionName = questionName;
		}
		public int getQuestionGroupID() {
			return QuestionGroupID;
		}
		public void setQuestionGroupID(int questionGroupID) {
			QuestionGroupID = questionGroupID;
		}
		public int getQuestionTypeID() {
			return QuestionTypeID;
		}
		public void setQuestionTypeID(int questionTypeID) {
			QuestionTypeID = questionTypeID;
		}
		public int getIsRequired() {
			return IsRequired;
		}
		public void setIsRequired(int isRequired) {
			IsRequired = isRequired;
		}
		public int getOrdering() {
			return Ordering;
		}
		public void setOrdering(int ordering) {
			Ordering = ordering;
		}
	}
	
	public static class QuestionGroup{
		private int QuestionGroupID;
		private String QuestionGroupName;
		private int Ordering;
		
		public int getQuestionGroupID() {
			return QuestionGroupID;
		}
		public void setQuestionGroupID(int questionGroupID) {
			QuestionGroupID = questionGroupID;
		}
		public String getQuestionGroupName() {
			return QuestionGroupName;
		}
		public void setQuestionGroupName(String questionGroupName) {
			QuestionGroupName = questionGroupName;
		}
		public int getOrdering() {
			return Ordering;
		}
		public void setOrdering(int ordering) {
			Ordering = ordering;
		}
	}
}
