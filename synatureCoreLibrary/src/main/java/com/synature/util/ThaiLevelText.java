package com.synature.util;

import android.text.TextUtils;

/**
 * @author j1tth4
 * This utility class use for manage level of thai text 
 */
public class ThaiLevelText {
	/**
	 * @param unicode prntTxt
	 * @return OPOSThaiText objec
	 */
	public static OPOSThaiText parsingThaiLevel(String prntTxt) {
		String strLine1 = "";
		String strLine2 = "";
		String strLine3 = "";
		int i = 0;
		int code = 0;
		int nextCode = 0;
		String strChar = "";
		OPOSThaiText resultText = new OPOSThaiText();
		for (; i < prntTxt.length(); i++) {
			code = prntTxt.charAt(i);
			strChar = Character.toString(prntTxt.charAt(i));
			switch (code) {
			case 3633: //ไม้หันอากาศ
			case 3636: //สระอิ
			case 3637: //สระอี
			case 3638: //สระอึ
			case 3639: //สระอื
				//ไม้หันอากาศ และ สระด้านบน เช่น อิ อี
				//Check if letter next to it is วรรณยุกต์
				if (i == prntTxt.length() - 1) {
					// This is the last letter
					strChar = Character.toString(prntTxt.charAt(i));
				} else {
					nextCode = (int) prntTxt.charAt(i + 1);
					switch (nextCode) {// Comboine current and the next character into new one
					case 3656: //ไม้เอก
					case 3657: //ไม้โท
					case 3658: //ไม้ตรี
					case 3659: //ไม้จัตวา
					case 3660: //การันต์
					//case 3661: //นิคหิต
						//วรรณยุกต์ และการันต์
						switch (nextCode) {
						case 3656:
							switch (code) {
							case 3633: //ไม้หันอากาศ
								nextCode = 3633;
								break;
							case 3636: //อิ
								nextCode = 3636;
								break;
							case 3637: //อี
								nextCode = 3637;
								break;
							case 3638: //อึ
								nextCode = 3638;
								break;
							case 3639: //อื
								nextCode = 3639;
								break;
							}
							break;
						case 3657: //ไม้โท
							switch (code) {
							case 3633: //ไม้หันอากาศ
								nextCode = 3633;
								break;
							case 3636: //อิ
								nextCode = 3636;
								break;
							case 3637: //อี
								nextCode = 3637;
								break;
							case 3638: //อึ
								nextCode = 3638;
								break;
							case 3639: //อื
								nextCode = 3639;
								break;
							}
							break;
						case 3658: //ไม้ตรี
							switch (code) {
							case 3633: //ไม้หันอากาศ
								nextCode = 3633;
								break;
							case 3636: //อิ
								nextCode = 3636;
								break;
							case 3637: //อี
								nextCode = 3637;//0x8b;
								break;
							case 3638: //อึ
								nextCode = 3638;
								break;
							case 3639: //อื
								nextCode = 3639;
								break;
							}
							break;
						case 3659: //ไมจัตวา
							switch (code) {
							case 3633: //ไม้หันอากาศ
								nextCode = 3633;
								break;
							case 3636: //อิ
								nextCode = 3636;
								break;
							case 3637: //อี
								nextCode = 3637;
								break;
							case 3638: //อึ
								nextCode = 3638;
								break;
							case 3639: //อื
								nextCode = 3639;
								break;
							}
							break;
						case 3660: //การันต์
							switch (code) {
							case 3636: //อิ
								nextCode = 3636;
								break;
							}
							break;
						}
						strChar = Character.toString((char) nextCode);
						i += 1;
						break;
					default:
						strChar = Character.toString(prntTxt.charAt(i));
						break;
					}
				}
				strLine1 = strLine1.substring(0, strLine1.length() - 1) + strChar;
				break;
			case 3655: //ไม้ไต่คู้
			case 3656:
			case 3657:
			case 3658:
			case 3659:
			case 3660:
			case 3661:
				//วรรณยุกต์
				// Check if letter next to it is วรรณยุกต์
				if (i == prntTxt.length() - 1) { // This is the last letter
					strLine1 = strLine1.substring(0, strLine1.length() - 1) + Character.toString(prntTxt.charAt(i));
				} else {
					nextCode = (int) prntTxt.charAt(i + 1);
					switch (nextCode) {
					case 3635: //สระอำ
						switch (code) {
						case 3656: //ไม้เอก
							strLine1 = strLine1.substring(0, strLine1.length() - 1) + Character.toString((char) 3656) + " ";
							break;
						case 3657: //ไม้โท
							strLine1 = strLine1.substring(0, strLine1.length() - 1) + Character.toString((char) 3657) + " ";
							break;
						case 3658: //ไม้ตรี
							strLine1 = strLine1.substring(0, strLine1.length() - 1) + Character.toString((char) 3658) + " ";
							break;
						case 3659: //ไม้จัตวา
							strLine1 = strLine1.substring(0, strLine1.length() - 1) + Character.toString((char) 3659) + " ";
							break;
						}
						strLine2 += Character.toString((char) 3635);
						strLine3 += " ";
						i += 1;
						break;
					default:
						strLine1 = strLine1.substring(0, strLine1.length() - 1) + Character.toString(prntTxt.charAt(i));
						break;
					}
				}
				break;
			case 3640: //สระอุ
			case 3641: //สระอู
				strLine3 = strLine3.substring(0, strLine3.length() - 1) + Character.toString(prntTxt.charAt(i));
				break;
			default:
				strLine1 += " ";
				strLine2 += Character.toString(prntTxt.charAt(i));
				strLine3 += " ";
				break;
			}
		}
		resultText.TextLine1 = TextUtils.isEmpty(strLine1.trim()) ? "" : strLine1;
		resultText.TextLine2 = TextUtils.isEmpty(strLine2.trim()) ? "" : strLine2;
		resultText.TextLine3 = TextUtils.isEmpty(strLine3.trim()) ? "" : strLine3;
		return resultText;
	}

	public static class OPOSThaiText
	{
	    public String TextLine1;
	    public String TextLine2;
	    public String TextLine3;
	}
}
