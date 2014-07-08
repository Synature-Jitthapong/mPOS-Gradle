package com.synature.util;

import java.security.MessageDigest;
import java.util.Locale;

public class EncryptSHA1 {
	
	public String sha1(String text) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(text.getBytes("UTF-8"));
			byte[] digestBytes = messageDigest.digest();

			String hex = null;
			for (int i = 0; i < digestBytes.length; i++) {
				hex = Integer.toHexString(0xFF & digestBytes[i]);
				if (hex.length() < 2)
					sb.append("0");
				sb.append(hex);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String pass = sb.toString();
		return pass.toUpperCase(Locale.US);
	}
}
