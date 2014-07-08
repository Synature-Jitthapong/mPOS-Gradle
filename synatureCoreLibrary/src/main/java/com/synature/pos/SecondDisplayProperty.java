package com.synature.pos;

import java.util.List;

public class SecondDisplayProperty {
	// --- Second Display Class and Property
	public enum eSecDisplayCmdType {
		Initial(0), DisplayItemData(1), ChangePayment(2), ClearScreen(9), ExtraView_WebView(
				21), ExtraView_MemberView(22), ExtraView_CardView(23), QuestionAnswerView(
				51), ResultQuestionAnswer(52);

		private int value;

		eSecDisplayCmdType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	public static class clsSecDisplayInitial{
		public int iCommandTypeID = eSecDisplayCmdType.Initial.getValue(); // 0
		public String szShopName;
		public String szStaffName;
	}

	public static class clsSecDisplayItemData{
		public int iCommandTypeID = eSecDisplayCmdType.DisplayItemData.getValue(); // 1
		public String szGrandTotalPrice;
		public clsSecDisplay_Transaction xTransaction;
		public List<clsSecDisplay_DetailItem> xListDetailItems;
		public List<clsSecDisplay_TransSummary> xListTransSummarys;
	}

	public static class clsSecDisplay_Transaction{
		public String szTransName;
		public int iNoCustomer;
		public String szCustName;
	}

	public static class clsSecDisplay_DetailItem{
		public String szItemName;
		public String szItemQty;
		public String szItemTotalPrice;
		public String szImageUrl;
	}

	public static class clsSecDisplay_TransSummary{
		public String szSumName;
		public String szSumAmount;
	}

	public static class clsSecDisplay_ChangePayment{
		public int iCommandTypeID = eSecDisplayCmdType.ChangePayment.getValue(); // 2
		public String szPayAmount;
		public String szCashChangeAmount;
		// ---------------------------------------
		// --- If szMemberCode has data, then show member point
		// --- If this szMemberCode has not data, then hide member point.
		public String szMemberCode;
		public String szMemberName;
		public String szPrevPoint;
		public String szCurrentPoint;
		public String szBillPoint;
	}

	public static class clsSecDisplay_ClearScreen{
		public int iCommandTypeID = eSecDisplayCmdType.ClearScreen.getValue(); // 9
	}

	public static class clsSecDisplay_Extra_WebView{
		public int iCommandTypeID = eSecDisplayCmdType.ExtraView_WebView.getValue(); // 21
		public int iCloseType; // 0=User Close, 1=Delay Time Close, 2=Both
		public int iCloseSecDelayTime;
		public String szExtraViewTitle;
		public String szWebURL;

	}

	public static class clsSecDisplay_Extra_MemberView{
		public int iCommandTypeID = eSecDisplayCmdType.ExtraView_MemberView.getValue(); // 22
		public int iCloseType; // 0=User Close, 1=Delay Time Close, 2=Both
		public int iCloseSecDelayTime;
		public String szExtraViewTitle;
		public String szMemberCode;
		public String szMemberName;
		public String szMemberPhone;
		public String szMemberMobile;
		public String szMemberExpireDate;
		public String szCurrentPoint;
	}

	public static class clsSecDisplay_Extra_CardView{
		public int iCommandTypeID = eSecDisplayCmdType.ExtraView_CardView.getValue(); // 23
		public int iCloseType; // 0=User Close, 1=Delay Time Close, 2=Both
		public int iCloseSecDelayTime;
		public String szExtraViewTitle;
	}

	public static class clsSecDisplay_QuestionAnswerView{
		public int iCommandTypeID = eSecDisplayCmdType.QuestionAnswerView.getValue(); // 51
		public List<clsSecDisplay_QuestionAnswer> xListQuestionAnswers;
	}

	public static class clsSecDisplay_QuestionAnswer{
		public int iQuestionID;
		public String szQuestionTitle;
		public List<clsSecDisplay_Answer> xListAnswers;
	}

	public static class clsSecDisplay_Answer{
		public int iAnswerID;
		public String szAnswerTitle;
	}

	// --- Send back to POS
	public static class clsSecDisplay_ResponeResultAnswer{
		public eSecDisplayCmdType iCommandTypeID;
		public List<clsSecDisplay_CustomerAnswer> xListCustAnswers;
	}

	public static class clsSecDisplay_CustomerAnswer{
		public int iQuestionID;
		public int iAnswerID;
	}
}
