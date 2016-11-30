package com.Search.SearchEngine;

import java.util.StringTokenizer;


public class QueryParser {
	
	private String query;
	
	private StringTokenizer tokenizer = null;
	public QueryParser(String query) {
		super();
		this.query = query;
		tokenizer = new StringTokenizer(this.query, " ");
	}


	public String nextToken(){
		//while(tokenizer.hasMoreTokens()){
			return tokenizer.nextToken();
		//}
		//return null;
	}
	
	public boolean hasTokens(){
		return tokenizer.hasMoreTokens();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub


	}


}




