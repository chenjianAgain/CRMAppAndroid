package com.zendaimoney.android.athena.hzpytran;

import java.util.ArrayList;

import com.zendaimoney.android.athena.hzpytran.HanziToPinyin.Token;


public class PinYin {
	//汉字返回拼音，字母原样返回，都转换为小写
	public static String getPinYin(String input) {
		ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		StringBuilder sb = new StringBuilder();
		if (tokens != null && tokens.size() > 0) {
			for (Token token : tokens) {
				if (Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}
		return sb.toString().toUpperCase().substring(0, 1);
	}
}
