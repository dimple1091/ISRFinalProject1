 package com.Search.SearchEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.wordnet.SynonymMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.document.Document;

/**
 * Servlet implementation class SearchEngine
 */

public class SearchEngine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	String indexPath = "";


	String jsonFilePath = "";


	IndexWriter indexWriter = null;
	
	//static String INDEX_PATH = "/Users/Dimple/Documents/workspace/ISRFinalProject1/luceneindex";
	//String FILE = "/Users/Dimple/Documents/workspace/ISRFinalProject1/WebContent/yelp_academic_dataset_business.json";

	static String INDEX_PATH = "C:\\Users\\Venkatesh\\Desktop\\luceneindex";
	String FILE = "C:\\Users\\Venkatesh\\Desktop\\yelpjsonindex\\yelp_academic_dataset_business.json";
	
	public void init() {

	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
	/*	String[] words = new String[] { "cheap", "brunch", "costly", "restaurants", "xxxx"};
		SynonymMap map = new SynonymMap(new FileInputStream("/Users/Dimple/Downloads/prolog/wn_s.pl"));
		 for (int i = 0; i < words.length; i++) {
		     String[] synonyms = map.getSynonyms(words[i]);
		     System.out.println(words[i] + ":" + java.util.Arrays.asList(synonyms).toString());
		 }*/
 
		
		System.out.println("Request Received....");
		response.setContentType("text/html"); 

		String query=request.getParameter("query");
		System.out.println("query :: "+query);
		Set<String> querySet=new HashSet<String>();
		querySet.add("Restaurants");
		//String INDEX_PATH = "C:\\Users\\Venkatesh\\Desktop\\luceneindex";
		//String FILE = "yelp_academic_dataset_business.json";

		Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	
		
	    List<IndexableField> fieldList =  indexSearcher.getIndexReader().document(0).getFields();
        System.out.println(Arrays.toString(indexSearcher.getIndexReader().document(0).getValues("categories")));
        Set<String> fieldSet = new HashSet<>();
        for(IndexableField field : fieldList){
        	fieldSet.add(field.name());
        }

		
		QueryParser queryParser=new QueryParser(query);
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		 MultiFieldQueryParser multiFieldqueryParser = new MultiFieldQueryParser(fieldSet.toArray(new String[fieldSet.size()]),new EnglishAnalyzer());
		while(queryParser.hasTokens()){
			String token=queryParser.nextToken();
			System.out.println("Token :: "+token);
			try {
				builder.add(new BooleanClause(multiFieldqueryParser.parse(token), BooleanClause.Occur.FILTER));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		TopDocs topDocs = indexSearcher.search(builder.build(), 30);			    
		//TopDocs topDocs = indexSearcher.search(categoryQuery, 10);
		
		JSONArray jsonArray = new JSONArray();
		for(ScoreDoc scoreDoc : topDocs.scoreDocs){
			//indexSearcher.doc(scoreDoc.doc);
			//System.out.println(topDocs); 
			System.out.println(scoreDoc.score); 
			Document doc = indexSearcher.getIndexReader().document(scoreDoc.doc);
			   List<IndexableField> fieldList2 =  doc.getFields();
			   JSONObject json=new JSONObject();
		        for(IndexableField field : fieldList2){
		        	String[] values = doc.getValues(field.name());
		        	if(values.length > 1){
		        		JSONArray jsonArray2 =  new JSONArray();
		        		for(String value : values){
		        			jsonArray2.add(value);
		        		}
		        		
		        		json.put(field.name(),jsonArray2);
		        	}
		        	else{
		        		json.put(field.name(),values[0]);
		        	}
		        }
		         jsonArray.add(json);
		         System.out.println(json.toString());
		         request.setAttribute("result",json); 
		}	
		JSONObject returnJson = new JSONObject();
		returnJson.put("result",jsonArray);
		
		response.getWriter().print(returnJson.toString());
 	}

	
	
	
	
	
	
	
}
