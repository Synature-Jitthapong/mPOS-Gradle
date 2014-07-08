package com.synature.pos;

import java.util.List;

public class SummaryTransaction {

	public List<Order> OrderList;
	public TransactionSummary TransactionSummary;
	
	public int TransactionID;
	public int ComputerID;
	public String SaleDate;
	public int TableID;
	public String TransacionName;
	public String QueueName;
	public String OpenTime;
	public String BeginTime;
	public String EndTime;
	public String PrintWarningTime;
	public int NoCustomer;
	public int NoCustomerWhenOpen;
	public int CallForCheckBill;
	public int NoPrintBillDetail;
	public int BillDetailReferenceNo;
	public boolean HasOrder;
	public boolean IsSplitTransaction;
	public int IsFromOtherTransaction;
	public boolean IsPaymentComplete;
	public boolean IsOtherReceipt;
	public int SplitNo;
	public int SaleMode;
	public int TransactionStatus;
	public String ReferenceNo;
	public int SplitFromTransactionID;
	public int SplitFromComputerID;
	public int FromDepositTransactionID;
	public int FromDepositComputerID;
	
	public static class TransactionSummary{
		public OrderTransactionPrice OrderTransactionPrice; 
		public TransactionProductVAT TransactionProductVAT;
		public ServiceCharge ServiceCharge;
		public List<DisplaySummary> DisplaySummaryList;
		
		public int TransactionID;
        public int ComputerID;
        public double fSubTotalPrice;
        public double fUnSubmitPrice;
        public boolean bHasUnFinishOrder;
        public double fDiscount_OtherPercent;
        public double fDiscount_OtherEachProduct;
        public double fDiscount_OtherAmount;
        public double fDiscount_OtherSummary;
        public double fDiscount_PricePromotion;
        public double fDiscount_PriceNPromotion;
        public double fDiscount_Member;
        public double fDiscount_Staff;
        public double fDiscount_Coupon;
        public double fDiscount_Voucher;
        public double fDiscount_Summary;
        public double fGrandTotalPrice;
        public double fTotalSalePrice;
        public double fTotalProductAmount;
        public double fTotalRetailPrice;
        public double fTransactionVAT;
        public double fTransactionVATAble;
	}
	
	public static class OrderTransactionPrice{
        public double fPriceNoVAT;
        public double fPriceExcludeVAT;
        public double fPriceIncludeVAT;
    }
    
	public static class TransactionProductVAT {
        public double fPriceNoVAT;
        public double fPriceExcludeVAT;
        public double fPriceIncludeVAT;
    }
	
    public static class ServiceCharge {
        public double fServiceChargeWithoutVAT;
        public double fServiceChargeExcludeVAT;
        public double fServiceChargePrice;
    }
    
    public static class DisplaySummary {
    	public String szDisplayName;
    	public double fPriceValue;
    	public boolean bWarningValue;
    }
    
    public static class Order{
    	public int iOrderID;
        public int iSplitNo;
        public int iProductID;
        public String szProductCode;
        public String szProductName;
        public double fAmount;
        public double fTotalPrice;
        public double fPricePerUnit;
        public double fRetailPricePerUnit;
        public String szOrderComment;
        public int iOrderStatus;
        public int iVATType;
        public int iPromotionAmountType;
        public int iProductSetType;
        public boolean bIsProductInSetWithPrice;
        public boolean bHasServiceCharge;
        public int iNoPrintBill;
        public int iPricePromotionID;
        public int iPriceNPromotionID;
        public boolean bIsParentOrder;
        public boolean bIsComment;
        public int iOrderLinkID;
        public int iSaleMode;
        public int totalPriceRoundingType;
        public int iPrintOrder_Status;
        public String dPrintOrder_InsertDate;
    }
    
    public static class TransactionDiscountDetail {
        public int TransactionID;
        public int ComputerID;
        public int SplitNo;
        public String TransactionOpenTime;
        public int MemberDiscountID;
        public int MemberPriceGroupID;
        public double MemberOverPrice;
        public int MemberDiscountAmountType;
        public int MemberAllowOtherPromo;
        public int MemberDiscountFromMinPriceToMax;
        public String MemberCode;
        public String MemberName;
        public int MemberPrintReceiptCopy;
        public int MemberPromotionProperty;
        public int StaffDiscountID;
        public int StaffPriceGroupID;
        public double StaffOverPrice;
        public int StaffDiscountAmountType;
        public int StaffAllowOtherPromo;
        public int StaffDiscountFromMinPriceToMax;
        public String StaffCode;
        public String StaffName;
        public int StaffPrintReceiptCopy;
//        StaffPromotionProperty: ,
//        VoucherDetailList: [],
//        VoucherTotalAmount: 0.0,
//        VoucherTotalUseAmount: 0.0,
//        CouponDetailList: [],
//        OtherDiscountType: 0,
//        OtherAmountDiscount: 0.0000,
//        OtherPercentDiscount: 0.00,
//        HasEachProductDiscount: false,
//        CalculateDiscountFrom: 0,
//        IsCalculateServiceCharge: true,
//        IsSkipCalculateExcludeVAT: false
    }
    
    public static class CustomerDetail {
        public int iMemberID;
        public int iMemberGroupID;
        public String szMemberGroupName;
        public String szMemberCode;
        public String szCustomerFirstName;
        public String szCustomerLastName;
        public String szCustomerFullName;
        public String szQueueName;
        public int iCustomerMainPrice;
    }
}