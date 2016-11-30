 package com.Search.SearchEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.wordnet.SynonymMap;
import org.json.simple.JSONObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * Servlet implementation class SearchEngine
 */

public class SearchEngine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	String indexPath = "";


	String jsonFilePath = "";


	IndexWriter indexWriter = null;
	
	static String INDEX_PATH = "/Users/Dimple/Documents/workspace/ISRFinalProject1/luceneindex";
	String FILE = "/Users/Dimple/Documents/workspace/ISRFinalProject1/WebContent/yelp_academic_dataset_business.json";

	public void init() {

	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		String[] words = new String[] { "cheap", "brunch", "costly", "restaurants", "xxxx"};
		SynonymMap map = new SynonymMap(new FileInputStream("/Users/Dimple/Downloads/prolog/wn_s.pl"));
		 for (int i = 0; i < words.length; i++) {
		     String[] synonyms = map.getSynonyms(words[i]);
		     System.out.println(words[i] + ":" + java.util.Arrays.asList(synonyms).toString());
		 }
 
		
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
		
		QueryParser queryParser=new QueryParser(query);
		if(queryParser.hasTokens()){
			String token=queryParser.nextToken();
			System.out.println("Token :: "+token);
			if(querySet.contains(token)){
				String category=token;
				TermQuery catQuery1 = new TermQuery(new Term("categories", "[\"Restaurants\"]"));
				BooleanQuery.Builder builder = new BooleanQuery.Builder();
				builder.add(new BooleanClause(catQuery1, BooleanClause.Occur.SHOULD));
				TopDocs topDocs = indexSearcher.search(builder.build(), 10);			    
				//TopDocs topDocs = indexSearcher.search(categoryQuery, 10);
				JSONObject json=new JSONObject();
				for(int i=0;i<3;i++){
					indexSearcher.doc(topDocs.scoreDocs[i].doc);
					System.out.println(topDocs); 
					System.out.println(topDocs.scoreDocs[i].score); 
					   List<IndexableField> fieldList =  indexSearcher.getIndexReader().document(topDocs.scoreDocs[i].doc).getFields();
				        
				        for(IndexableField field : fieldList){
				        	json.put(field.name(), field.stringValue());
				        }
				         
				         System.out.println(json.toString());
				         request.setAttribute("result",json); 
				}	
				JSONObject returnJson = new JSONObject();
				returnJson.put("result",json.toJSONString());
				
				response.getWriter().print(returnJson.toJSONString());
			}
		}
 
	}

	
	
	
	
	
	
	
}
