import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryCachingPolicy;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;

import Classes.FilePath;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by suay on 5/13/14.
 */
public class LuceneIndexWriter {

	String indexPath = "";

	String jsonFilePath = "";

	String tipsjsonFilePath = "";
	IndexWriter indexWriter = null;

	public Set<String> cats = new HashSet<>();
	
	public Map<String,List<String>> tipsMap = new HashMap<>();
	
	private SpatialContext ctx;
	SpatialStrategy strategy;
	
	public LuceneIndexWriter(String indexPath, String jsonFilePath, String tipsjsonFilePath) throws IOException, ParseException {
		this.indexPath = indexPath;
		this.jsonFilePath = jsonFilePath;
		this.tipsjsonFilePath = tipsjsonFilePath;
		
		BufferedReader br = new BufferedReader(new FileReader(new File(tipsjsonFilePath)));
		String line = null;

		while((line = br.readLine()) != null){
			JSONParser parser = new JSONParser();
			JSONObject jsonobj = (JSONObject)parser.parse(line);
			String businessId = jsonobj.get("business_id").toString();
			String tip = jsonobj.get("text").toString();
			List<String> tipsList = tipsMap.get(businessId);
			if(null != tipsList){
				tipsList.add(tip);
				
			}
			else{
				tipsList = new ArrayList<>();
				tipsList.add(tip);
				tipsMap.put(businessId, tipsList);
			}
			
		}
			
		this.ctx = SpatialContext.GEO;

		SpatialPrefixTree grid = new GeohashPrefixTree(ctx, 11);
		this.strategy = new RecursivePrefixTreeStrategy(grid, "location");
	}

	public void createIndex() throws FileNotFoundException, IOException, ParseException{
		JSONArray jsonObjects = parseJSONFile();
		openIndex();
		FieldType type;
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(true);
		type.setTokenized(true);
		type.setStoreTermVectors(true);
		addDocuments(jsonObjects,type);
		finish();
	}

	/**
	 * Parse a Json file. The file path should be included in the constructor
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public JSONArray parseJSONFile() throws FileNotFoundException, IOException, ParseException{

		JSONParser parser = new JSONParser();

		BufferedReader br = new BufferedReader(new FileReader(new File(jsonFilePath)));

		String line = null;

		JSONArray arrayObjects = new JSONArray();

		while((line = br.readLine()) != null){
			JSONObject jsonObject = (JSONObject) parser.parse(line);
			/*	for (Object o : arrayObjects) {
				JSONObject person = (JSONObject) o;

				String name = (String) person.get("name");
				System.out.println(name);

				Double city = (Double) person.get("lat");
				System.out.println(city);

			}*/
			arrayObjects.add(jsonObject);
		}


		return arrayObjects;
	}

	public boolean openIndex() {
		try {
			Directory dir = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new EnglishAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			iwc.setMaxBufferedDocs(10000);

			//Always overwrite the directory
			iwc.setOpenMode(OpenMode.CREATE);
			indexWriter = new IndexWriter(dir, iwc);

			return true;
		} catch (Exception e) {
			System.err.println("Error opening the index. " + e.getMessage());

		}
		return false;

	}

	/**
	 * Add documents to the index
	 */
	public void addDocuments(JSONArray jsonObjects, FieldType type){
		for(JSONObject object : (List<JSONObject>) jsonObjects){
			Document doc = new Document();
			for(String field : (Set<String>) object.keySet()){
				/*			Class type = object.get(field).getClass();
				if(type.equals(String.class)){
					doc.add(new StringField(field, (String)object.get(field), Field.Store.YES));
				}else if(type.equals(Long.class)){
					doc.add(new LongField(field, (long)object.get(field), Field.Store.YES));
				}else if(type.equals(Double.class)){
					doc.add(new DoubleField(field, (double)object.get(field), Field.Store.YES));
				}else if(type.equals(Boolean.class)){
					doc.add(new StringField(field, object.get(field).toString(), Field.Store.YES));
				}*/
				//doc.add(new StoredField("DOCNO", docno));	
				Class classType = object.get(field).getClass();
				//System.out.println(classType.getName());
				if(classType.equals(JSONObject.class) ){
					//	System.out.println("Field :"+field);
					//doc.add(new Field(field, object.get(field).toString(), type));
					parseJSONToText(object, field, type, doc);
				}
				else if (classType.equals(JSONArray.class)){
					JSONArray arr = (JSONArray) object.get(field);
					for(String str : (List<String>) arr){
					//	System.out.println(str);
						if(field.equals("categories")){
							cats.add(str);
						}
						
						doc.add(new Field(field, str, type));
					}

				}
				else if(field.equals("stars")){
					FieldType DOUBLE_FIELD_TYPE_STORED_SORTED = new FieldType();
					  DOUBLE_FIELD_TYPE_STORED_SORTED.setTokenized(true);
					    DOUBLE_FIELD_TYPE_STORED_SORTED.setOmitNorms(true);
					    DOUBLE_FIELD_TYPE_STORED_SORTED.setIndexOptions(IndexOptions.DOCS);
					    DOUBLE_FIELD_TYPE_STORED_SORTED
					        .setNumericType(FieldType.NumericType.DOUBLE);
					    DOUBLE_FIELD_TYPE_STORED_SORTED.setStored(true);
					    DOUBLE_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
					    DOUBLE_FIELD_TYPE_STORED_SORTED.freeze();
					//doc.add(new Field ("stars", object.get(field).toString(), Field.Store.NO, Field.Index.NOT_ANALYZED));
					    doc.add(new DoubleField ("stars",Double.parseDouble(object.get(field).toString())  , DOUBLE_FIELD_TYPE_STORED_SORTED));
					//doc.add(new SortedDocValuesField("stars", new BytesRef(object.get(field).toString())));
				}
				else{
					//System.out.println("Field :"+field);
					//System.out.println("Field :: "+field+" Value :: "+(String)object.get(field));
					doc.add(new Field(field, object.get(field).toString(), type));
				}

			}
			
			
			String business_id = doc.get("business_id");
			List<String> tips = this.tipsMap.get(business_id);
			if(null != tips){
				for(String tip: tips){
					doc.add(new Field("tips", tip, type));
				}
			}
			 newGeoDocument(doc, ctx.makePoint((double)object.get("longitude"),(double)object.get("latitude")), type);
			try {
				indexWriter.addDocument(doc);
			} catch (IOException ex) {
				System.err.println("Error adding documents to the index. " +  ex.getMessage());
			}
	
		}
		
	}
	
	private Document newGeoDocument(Document doc, Shape shape, FieldType type) {

		for(IndexableField f : strategy.createIndexableFields(shape)) {
			doc.add(f);
		}

		doc.add(new StoredField(strategy.getFieldName(), ctx.toString(shape)));
		return doc;
	}

	private void parseJSONToText(JSONObject object, String field, FieldType type, Document doc) {
		
		JSONObject jsonObj = (JSONObject)object.get(field);
		for(String key : (Set<String>)jsonObj.keySet()){
			if(jsonObj.get(key).getClass().equals(JSONObject.class)){
				parseJSONToText(jsonObj, key, type, doc);
			}
			else{
				String value = jsonObj.get(key).toString();
				doc.add(new Field(key, value, type));
			}	
		}
		
	}

	/**
	 * Write the document to the index and close it
	 */
	public void finish(){
		try {
			indexWriter.commit();
			indexWriter.close();
		} catch (IOException ex) {
			System.err.println("We had a problem closing the index: " + ex.getMessage());
		}
	}

	public static void main(String[] args) throws IOException, org.apache.lucene.queryparser.classic.ParseException, ParseException {
		
		String INDEX_PATH = FilePath.LuceneIndex;
		String FILE = FilePath.BusinessFile;
		String TIPS_FILE = FilePath.TipFile;
		String query = "";
		
		LuceneIndexWriter writer = new LuceneIndexWriter(INDEX_PATH, FILE, TIPS_FILE);
//		try {
//			writer.createIndex();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	/*	Iterator<String> itr = writer.cats.iterator();
		
		while(itr.hasNext()){
			System.out.println(itr.next());
		}
		*/
		
		
		
        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        
        Document doc = indexSearcher.getIndexReader().document(7);
        List<IndexableField> fieldList = doc.getFields();
        System.out.println(Arrays.toString(doc.getValues("categories")));
        JSONObject json = new JSONObject();
        Set<String> fieldSet = new HashSet<>();
        for(IndexableField field : fieldList){
        	System.out.println(field.name()+"  "+field.fieldType());
        	String[] values = doc.getValues(field.name());
        	if(values.length > 1){
        		JSONArray jsonArray =  new JSONArray();
        		jsonArray.add(Arrays.asList(values));
        		json.put(field.name(),jsonArray);
        	}
        	else{
        		json.put(field.name(),values[0]);
        	}
        	
        	fieldSet.add(field.name());
        }
        
        //MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fieldSet.toArray(new String[fieldSet.size()]),new WhitespaceAnalyzer());
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[]{"Price Range"},new WhitespaceAnalyzer());
        //Term t = new Term("full_address", "McClure Dravosburg");
        MultiFieldQueryParser queryParser2 = new MultiFieldQueryParser(fieldSet.toArray(new String[fieldSet.size()]),new WhitespaceAnalyzer());
       // Query query = new TermQuery(t);
       
        //PhraseQuery query = new PhraseQuery("full_address", new String[]{"McClure","Dravosburg"});
        //Builder b = new BooleanQuery.Builder();
        //b.add("McClure", BooleanClause.Occur.MUST);
       // BooleanQuery categoryQuery = new BooleanQuery();
       // TermQuery catQuery1 = new TermQuery(new Term("full_address", "McClure"));
      //  TermQuery catQuery2 = new TermQuery(new Term("full_address", "Dravosburg"));
        TermQuery catQuery2 = new TermQuery(new Term("categories", "Fast Food"));
       // PhraseQuery catQuery1 = new PhraseQuery("categories", new String[]{"Restaurants"});
        //categoryQuery.add(new BooleanClause(catQuery1, BooleanClause.Occur.SHOULD));
        //categoryQuery.add(new BooleanClause(catQuery2, BooleanClause.Occur.SHOULD));
        //categoryQuery.add(new BooleanClause(categoryQuery, BooleanClause.Occur.MUST));
       
   
        
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        
      // builder.add(new BooleanClause(catQuery1, BooleanClause.Occur.FILTER));
        //builder.add(new BooleanClause(catQuery2, BooleanClause.Occur.FILTER));
        //builder.add(new BooleanClause(queryParser.parse("Dravosburg"), BooleanClause.Occur.FILTER));
        builder.add(new BooleanClause(queryParser.parse("1"), BooleanClause.Occur.FILTER));
        builder.add(new BooleanClause(queryParser2.parse("restaur"), BooleanClause.Occur.FILTER));
    //    builder.add(new BooleanClause(queryParser.parse("nearby"), BooleanClause.Occur.SHOULD));
  //      builder.add(new BooleanClause(queryParser.parse("Food"), BooleanClause.Occur.FILTER));
        TopDocs topDocs = indexSearcher.search(builder.build(), 20);

        //TopDocs topDocs = indexSearcher.search(categoryQuery, 10);
       for(ScoreDoc scoredoc : topDocs.scoreDocs){
    	   System.out.print(scoredoc.doc+" ");
       }
        // System.out.println(topDocs.scoreDocs[0].doc);
        // System.out.println(topDocs.scoreDocs[0]);
        
        IndexReader reader = indexSearcher.getIndexReader();

        //List<IndexableField> fieldList =  indexSearcher.getIndexReader().document(topDocs.scoreDocs[0].doc).getFields();
        
   
        
         System.out.println(json.toString());
      /*   QueryParser parser = new QueryParser(query);
         
         Set<String> categories = new HashSet<>();

         while(parser.hasTokens()){
        	 String token = parser.nextToken();
        	 if(categories.contains(token)){

        	 }
         }*/
 
         //writer.search(40.4422268, -79.9552743, 1, indexSearcher);
         writer.filterSearch(indexSearcher);
	}

	public void search(Double lat, Double lng, int distance,IndexSearcher searcher ) throws IOException, org.apache.lucene.queryparser.classic.ParseException{

		Point p = ctx.makePoint(lng, lat);
		SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects,
				ctx.makeCircle(lng, lat, DistanceUtils.dist2Degrees(distance, DistanceUtils.EARTH_EQUATORIAL_RADIUS_MI)));
		Filter filter = strategy.makeFilter(args);
		
		ValueSource valueSource = strategy.makeDistanceValueSource(p);
		Sort distSort = new Sort(valueSource.getSortField(false)).rewrite(searcher);

		int limit = 25;
		
		 org.apache.lucene.queryparser.classic.QueryParser parser = new org.apache.lucene.queryparser.classic.QueryParser("categories", new EnglishAnalyzer());
		
		  BooleanQuery.Builder builder = new BooleanQuery.Builder();
		  builder.add(new BooleanClause(parser.parse("restaurants"), BooleanClause.Occur.FILTER));
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

	
	}
	public void filterSearch(IndexSearcher indexSearcher) throws org.apache.lucene.queryparser.classic.ParseException, IOException{
		
		QueryParser parser = new QueryParser("Price Range", new KeywordAnalyzer());
		QueryParser parser3 = new QueryParser("Price Range", new StandardAnalyzer());
		QueryParser parser2 = new QueryParser("categories", new EnglishAnalyzer());

		 BooleanQuery.Builder innerBooleanQueryBuilder = new BooleanQuery.Builder();
		 innerBooleanQueryBuilder.add(new BooleanClause(parser.parse(String.valueOf(1)), BooleanClause.Occur.SHOULD));
		innerBooleanQueryBuilder.add(new BooleanClause(parser3.parse("3"), BooleanClause.Occur.SHOULD));
		 
		 
		 BooleanQuery.Builder builder = new BooleanQuery.Builder();
		// builder.add(new BooleanClause(new MatchAllDocsQuery(), BooleanClause.Occur.SHOULD));
		
		 builder.add(new TermQuery(new Term("categories", "restaur")), BooleanClause.Occur.FILTER);
		// builder.add(new BooleanClause(parser.parse("1 OR 2"), BooleanClause.Occur.MUST));
		 builder.add(new BooleanClause(innerBooleanQueryBuilder.build(), BooleanClause.Occur.FILTER));
	        

	        
	        TopDocs topDocs = indexSearcher.search(builder.build(), 20);
	        
	        for(ScoreDoc scoredoc : topDocs.scoreDocs) {
	        	System.out.print(scoredoc.doc+" ");
	        }
	}
	
}