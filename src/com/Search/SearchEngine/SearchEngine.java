 package com.Search.SearchEngine;

//import java.io.FileInputStream;
import java.io.IOException;
//import java.io.StringReader;
import java.nio.file.Paths;
//import java.nio.file.Path;
import java.util.Arrays;
//import java.util.HashMap;
import java.util.HashSet;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
//import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
//import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Attribute;
//import org.apache.lucene.util.CharsRef;
//import org.apache.lucene.wordnet.SynonymMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Point;

import Classes.FilePath;
import Classes.RichQuery;

//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
//import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
//import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
//import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
//import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
//import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
//import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
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

	static String INDEX_PATH = FilePath.LuceneIndex;
	String FILE = FilePath.BusinessFile;
	 SpatialContext ctx;
	 SpatialStrategy strategy;
	public void init() {

			this.ctx = SpatialContext.GEO;

			SpatialPrefixTree grid = new GeohashPrefixTree(ctx, 11);
			this.strategy = new RecursivePrefixTreeStrategy(grid, "location");
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
		String curdir = System.getProperty("user.dir");
		System.out.println(curdir);
		String query=request.getParameter("query");
		String latitudestr=request.getParameter("latitude");
		String longitudestr=request.getParameter("longitude");
		System.out.println("query :: "+query);
		System.out.println("latitude ::: "+latitudestr);
		System.out.println("longitude ::: "+longitudestr);
		double latitude = Double.parseDouble(latitudestr);
		double longitude = Double.parseDouble(longitudestr);
		System.out.println(latitude);
		System.out.println(longitude);
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

		
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		 MultiFieldQueryParser multiFieldqueryParser = new MultiFieldQueryParser(fieldSet.toArray(new String[fieldSet.size()]),new EnglishAnalyzer());
		 
		 RichQuery richQuery = new RichQuery(query);
		 
		 Map<String,String> queryMap = richQuery.getRichQuery();
//		  Map<String,String> queryMap = new HashMap<>();
//		  queryMap.put("Price Range", "1,2");
//		  queryMap.put("all", "parking");
//		  queryMap.put("categories", "restaurants");
		  //queryMap.put("address", "pittsburgh");
		 for(String queryField : queryMap.keySet()){
			 String queryValue = queryMap.get(queryField);
			 System.out.println("Query Field ::: "+queryField+"  Query Value ::: "+queryValue);
			 try {
				 	if(queryField.equalsIgnoreCase("Price Range")){
				 		System.out.println("Rangeeeeeeeeeeee");
				 		 BooleanQuery.Builder innerBooleanQueryBuilder = new BooleanQuery.Builder();
				 		String[] priceRangeValues = queryValue.split(Pattern.quote(","));
				 		for(String priceRangeValue : priceRangeValues){
				 			QueryParser parser = new QueryParser("Price Range", new KeywordAnalyzer());
				 			System.out.println("Value ::: "+priceRangeValue);
				 			innerBooleanQueryBuilder.add(new BooleanClause(parser.parse(priceRangeValue), BooleanClause.Occur.SHOULD));
				 		}
				 		builder.add(new BooleanClause(innerBooleanQueryBuilder.build(), BooleanClause.Occur.FILTER));
				 	}
				 	else if(queryField.equalsIgnoreCase("all")){
				 		System.out.println("ALlllllllllllll");
				 		BooleanQuery.Builder allQueryBuilder = new BooleanQuery.Builder();
				 		String[] allQueryValues = queryValue.split(Pattern.quote(" "));
				 		for(String allQueryValue : allQueryValues){
				 			System.out.println("allQueryValue ::: "+allQueryValue);
				 			//QueryParser allQueryparser = new QueryParser("Price Range", new EnglishAnalyzer());
				 			allQueryBuilder.add(new BooleanClause(multiFieldqueryParser.parse(allQueryValue), BooleanClause.Occur.SHOULD));
				 			//builder.add(new BooleanClause(multiFieldqueryParser.parse(allQueryValue), BooleanClause.Occur.FILTER));
				 		}
				 		builder.add(new BooleanClause(allQueryBuilder.build(), BooleanClause.Occur.MUST));
			 		
				 	}
				 	else if(queryField.equalsIgnoreCase("address")){
			
				 						 	
				 			QueryParser parseraddress = new QueryParser("full_address", new EnglishAnalyzer());
				 		
				 			//innerBooleanQueryBuilder.add(new BooleanClause(parser.parse(priceRangeValue), BooleanClause.Occur.SHOULD));
				 		
				 		builder.add(new BooleanClause(parseraddress.parse(queryValue), BooleanClause.Occur.FILTER));
				 	} 
				 	else if(queryField.equalsIgnoreCase("categories")){
				 		BooleanQuery.Builder innerBooleanCatBuilder = new BooleanQuery.Builder();
				 		String[] categoryValues = queryValue.split(Pattern.quote(" "));
				 		QueryParser parserCategories = new QueryParser("categories", new StandardAnalyzer());
				 		for(String categoryValue : categoryValues){
				 			innerBooleanCatBuilder.add(new BooleanClause(parserCategories.parse(categoryValue), BooleanClause.Occur.SHOULD));
				 		}
				 		builder.add(new BooleanClause(innerBooleanCatBuilder.build(), BooleanClause.Occur.FILTER));
				 	}
					else if(queryField.equalsIgnoreCase("neighborhoods")){
						
						 	
			 			QueryParser parseraddress = new QueryParser("neighborhoods", new EnglishAnalyzer());
			 		
			 			//innerBooleanQueryBuilder.add(new BooleanClause(parser.parse(priceRangeValue), BooleanClause.Occur.SHOULD));
			 		
			 		builder.add(new BooleanClause(parseraddress.parse(queryValue), BooleanClause.Occur.SHOULD));
			 	} 
					else if(queryField.equalsIgnoreCase("attributes")){
				 		BooleanQuery.Builder innerBooleanattrBuilder = new BooleanQuery.Builder();
				 		String[] attrValues = queryValue.split(Pattern.quote(" "));
				 		QueryParser parserCategories = new QueryParser("attributes", new EnglishAnalyzer());
				 		for(String attrValue : attrValues){
				 			innerBooleanattrBuilder.add(new BooleanClause(parserCategories.parse(attrValue), BooleanClause.Occur.SHOULD));
				 		}
				 		builder.add(new BooleanClause(innerBooleanattrBuilder.build(), BooleanClause.Occur.FILTER));
				 	}
				 	
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		 
		/*while(queryParser.hasTokens()){
			String token=queryParser.nextToken();
			System.out.println("Token :: "+token);
			try {
				builder.add(new BooleanClause(multiFieldqueryParser.parse(token), BooleanClause.Occur.FILTER));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		
		
		
/*		TopDocs topDocs = indexSearcher.search(builder.build(), 30, new Sort(new SortField[] {
				SortField.FIELD_SCORE,
				new SortField("stars", SortField.Type.DOUBLE, true)}));*/
		 
		 TopDocs topDocs = null;
		try {
			topDocs = search( latitude,longitude, 7, indexSearcher,builder);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			//	new SortField("field_2", SortField.STRING) }));			    
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

	
	public TopDocs search(Double lat, Double lng, int distance,IndexSearcher searcher,BooleanQuery.Builder builder) throws IOException, org.apache.lucene.queryparser.classic.ParseException{

		Point p = ctx.makePoint(lng, lat);
		SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects,
				ctx.makeCircle(lng, lat, DistanceUtils.dist2Degrees(distance, DistanceUtils.EARTH_EQUATORIAL_RADIUS_MI)));
		Filter filter = strategy.makeFilter(args);
		
		ValueSource valueSource = strategy.makeDistanceValueSource(p);
		//Sort distSort = new Sort(valueSource.getSortField(false)).rewrite(searcher);
		Sort distSort = new Sort(new SortField[] {
				SortField.FIELD_SCORE,
				new SortField("stars", SortField.Type.DOUBLE, true),
				valueSource.getSortField(false)
				}).rewrite(searcher);
		int limit = 25;
		
		// org.apache.lucene.queryparser.classic.QueryParser parser = new org.apache.lucene.queryparser.classic.QueryParser("categories", new EnglishAnalyzer());
		
		 // BooleanQuery.Builder builder = new BooleanQuery.Builder();
		 // builder.add(new BooleanClause(parser.parse("restaurants"), BooleanClause.Occur.FILTER));
		  builder.add(new BooleanClause(new MatchAllDocsQuery(), BooleanClause.Occur.FILTER));
		  
		//TopDocs topDocs = searcher.search(new MatchAllDocsQuery(), filter, limit, distSort);
		  TopDocs topDocs = searcher.search(builder.build(), filter, limit, distSort);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;

		for(ScoreDoc s: scoreDocs) {

			Document doc = searcher.doc(s.doc);
			Point docPoint = (Point) ctx.readShape(doc.get(strategy.getFieldName()));
			double docDistDEG = ctx.getDistCalc().distance(args.getShape().getCenter(), docPoint);
			double docDistInKM = DistanceUtils.degrees2Dist(docDistDEG, DistanceUtils.EARTH_EQUATORIAL_RADIUS_MI);
			System.out.println(doc.get("business_id") + "\t" + doc.get("name") + "\t" + docDistInKM + " miles ");

		}
	return topDocs;
	
	}
	
	
}
