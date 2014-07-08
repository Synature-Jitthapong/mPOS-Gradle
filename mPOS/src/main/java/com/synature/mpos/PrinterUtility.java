package com.synature.mpos;

public abstract class PrinterUtility {
	
	public static final int HORIZONTAL_MAX_SPACE = 45;
	public static final int QTY_MAX_SPACE = 12;
	
	protected static String createHorizontalSpace(int usedSpace){
		StringBuilder space = new StringBuilder();
		if(usedSpace > HORIZONTAL_MAX_SPACE){
			usedSpace = usedSpace - 2;
		}
		for(int i = usedSpace; i <= HORIZONTAL_MAX_SPACE; i++){
			space.append(" ");
		}
		return space.toString();
	}
	
	protected static int calculateLength(String text){
		int length = 0;
		for(int i = 0; i < text.length(); i++){
			int code = (int) text.charAt(i);
			if(code != 3633 
					&& code != 3636
					&& code != 3637
					&& code != 3638
					&& code != 3639
					&& code != 3640
					&& code != 3641
					&& code != 3642
					&& code != 3655
					&& code != 3656
					&& code != 3657
					&& code != 3658
					&& code != 3659
					&& code != 3660
					&& code != 3661
					&& code != 3662){
				length ++;
			}
		}
		return length == 0 ? text.length() : length;
	}
	
	protected static String createQtySpace(int usedSpace){
		StringBuilder space = new StringBuilder();
		if(usedSpace > QTY_MAX_SPACE){
			usedSpace = usedSpace - 2;
		}
		for(int i = usedSpace; i <= QTY_MAX_SPACE; i++){
			space.append(" ");
		}
		return space.toString();
	}
	
	protected static String createLine(String sign){
		StringBuilder line = new StringBuilder();
		for(int i = 0; i <= HORIZONTAL_MAX_SPACE; i++){
			line.append(sign);
		}
		return line.toString();
	}
}
