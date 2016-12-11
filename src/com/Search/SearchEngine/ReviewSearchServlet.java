package com.Search.SearchEngine;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Servlet implementation class ReviewSearchServlet
 */
@WebServlet("/ReviewSearchServlet")
public class ReviewSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	String INDEX_PATH = "C:\\Users\\Venkatesh\\Desktop\\lucenereviewindex";
	String FILE = "C:\\Users\\Venkatesh\\Desktop\\review.json";


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewSearchServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init() throws ServletException {
		NLP.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("Review Request Received....");
		response.setContentType("text/html"); 

		String business_id=request.getParameter("business_id");
		System.out.println("business_id :: "+business_id);

		Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		org.apache.lucene.queryparser.classic.QueryParser parser = new org.apache.lucene.queryparser.classic.QueryParser("business_id", new EnglishAnalyzer());
		StringBuilder builder = new StringBuilder();
		TopDocs topDocs;
		try {
			topDocs = indexSearcher.search(parser.parse(business_id), 20);
			JSONArray reviewJSONArray = new JSONArray();
			for(ScoreDoc scoredoc : topDocs.scoreDocs){
				Document doc = indexReader.document(scoredoc.doc);

				String review = doc.get("text");
				reviewJSONArray.add(review);
				StandardTokenizer tockenizer = new StandardTokenizer();

				tockenizer.setReader(new StringReader(review));

				TokenStream tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(tockenizer))), EnglishAnalyzer.getDefaultStopSet());
				tokenStream.reset();
		
				CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
				while (tokenStream.incrementToken()) {
					//System.out.println(token.toString());
					builder.append(token.toString()+" ");
				}
				tokenStream.close();
			}
			 
			System.out.println(builder.toString());
			
			int[] reviewsArray = getSentiment(reviewJSONArray);
			
			JSONObject returnJSON = new JSONObject();
			
			returnJSON.put("wordcloud", builder.toString());
			returnJSON.put("reviews", reviewJSONArray.toString());
			returnJSON.put("pos", reviewsArray[0]);
			returnJSON.put("pos", reviewsArray[1]);
			System.out.println(returnJSON.toString());
			response.getWriter().print(returnJSON.toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public int[] getSentiment(JSONArray reviewsJSONArray){
		 int neg=0;
		 int pos=0;
		 NLP.init();
	        for(String review : (List<String>)reviewsJSONArray) {
	        	
	            System.out.println(review + " : " + NLP.findSentiment(review));
	            if(NLP.findSentiment(review)==1){
	            	neg=neg+1;
	            }
	            if(NLP.findSentiment(review)>1){
	            	pos=pos+1;
	            }
	             
	        }
	        return new int[]{pos,neg};
	}

}
