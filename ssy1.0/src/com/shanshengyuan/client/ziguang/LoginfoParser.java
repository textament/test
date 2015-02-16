package com.shanshengyuan.client.ziguang;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class LoginfoParser {
	public static AccountInfo parserAccount(InputStream inputStream)
	{
		AccountInfo accinfo=null;
		XmlPullParser parser=Xml.newPullParser();
		try {
			parser.setInput(inputStream, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {  
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:  
					accinfo = new AccountInfo();
					break;  
				case XmlPullParser.START_TAG:  
					if (parser.getName().equals("Out")) {  
						accinfo = new AccountInfo();  
					} else if (parser.getName().equals("out_flashnumber")) {  
						eventType = parser.next(); 
						accinfo.setUsrAcc(parser.getText());
					} else if (parser.getName().equals("out_flashnumberpwd")) {  
						eventType = parser.next();  
						accinfo.setUsrPwd(parser.getText());
					}
					break;  
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("Out")) {  
						//  
					}
					break;  
				}
				eventType = parser.next();  
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}

		return accinfo;
	}
	public static AccountInfo parserAccount(String xmlContent)
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(xmlContent.getBytes());
		return parserAccount(stream);
	}
}
