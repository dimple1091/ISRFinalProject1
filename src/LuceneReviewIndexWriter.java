import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Search.SearchEngine.QueryParser;

public class LuceneReviewIndexWriter {

	
	IndexWriter indexWriter = null;
	String indexPath = "";

	String jsonFilePath = "";
	
	Map<String, List<String>> reviewsMap = new HashMap<>();
	
	public LuceneReviewIndexWriter(String indexPath, String jsonFilePath) throws IOException, ParseException{
		this.indexPath = indexPath;
		this.jsonFilePath = jsonFilePath;
		BufferedReader br = new BufferedReader(new FileReader(new File(jsonFilePath)));
		String line = null;
		
		while((line = br.readLine()) != null){
			JSONParser parser = new JSONParser();
			JSONObject jsonobj = (JSONObject)parser.parse(line);
			String businessId = jsonobj.get("business_id").toString();
			String review = jsonobj.get("text").toString();
			List<String> reviewsList = reviewsMap.get(businessId);
			if(null != reviewsList){
				reviewsList.add(review);
				
			}
			else{
				reviewsList = new ArrayList<>();
				reviewsList.add(review);
				reviewsMap.put(businessId, reviewsList);
			}


		}
	}
	

	public void createIndex() throws FileNotFoundException, IOException, ParseException{

		openIndex();
		FieldType type;
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(true);
		type.setTokenized(true);
		type.setStoreTermVectors(true);
		addDocuments(type);
		finish();
	}
	
	public void finish(){
		try {
			indexWriter.commit();
			indexWriter.close();
		} catch (IOException ex) {
			System.err.println("We had a problem closing the index: " + ex.getMessage());
		}
	}

	
	
	public static void main(String[] args) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
		
		String INDEX_PATH = "C:\\Users\\Venkatesh\\Desktop\\lucenereviewindex";
		String FILE = "C:\\Users\\Venkatesh\\Desktop\\review.json";
		

        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        
        Document doc = indexSearcher.getIndexReader().document(1);
        List<IndexableField> fieldList = doc.getFields();
      //  System.out.println(Arrays.toString(doc.getValues("text")));
        for(IndexableField field : fieldList){
        //	System.out.println(field.name()+"  "+field.stringValue());
        }
        
        org.apache.lucene.queryparser.classic.QueryParser parser = new org.apache.lucene.queryparser.classic.QueryParser("business_id", new EnglishAnalyzer());

        TopDocs topDocs = indexSearcher.search(parser.parse("UsFtqoBl7naz8AVUBZMjQQ"), 20);
        
        for(ScoreDoc scoredoc : topDocs.scoreDocs){
     	   System.out.print(scoredoc.doc+" ");
        }
        
        
        String review = doc.get("text");
        
       System.out.println(review);
        
        
        StandardTokenizer tockenizer = new StandardTokenizer();
        
        tockenizer.setReader(new StringReader(review));
               
        TokenStream tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(tockenizer))), EnglishAnalyzer.getDefaultStopSet());
        tokenStream.reset();
        //And ends here

        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.println(token.toString());
        }
        tokenStream.close();
       // JSONObject json = new JSONObject();
       // Set<String> fieldSet = new HashSet<>();
/*        for(IndexableField field : fieldList){
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
        }*/
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
	
	public void addDocuments(FieldType type) throws IOException, ParseException{
		System.out.println("Started adding documents");
		BufferedReader br = new BufferedReader(new FileReader(new File(jsonFilePath)));
		String line = null;

		
		for(String business_id : reviewsMap.keySet()){
			List<String> reviewsList = reviewsMap.get(business_id);
			Document doc = new Document();
			
			doc.add(new Field("business_id", business_id, type));
			for(String review : reviewsList){
				doc.add(new Field("review", review, type));
			}
			try {
				indexWriter.addDocument(doc);
			} catch (IOException ex) {
				System.err.println("Error adding documents to the index. " +  ex.getMessage());
			}
		}
		
		
		/*while((line = br.readLine()) != null){
			Document doc = new Document();
			JSONParser parser = new JSONParser();
			JSONObject jsonobj = (JSONObject)parser.parse(line);
			
			//for(String field : (Set<String>) jsonobj.keySet()){
			//}
			doc.add(new Field("business_id", jsonobj.get("business_id").toString(), type));
			doc.add(new Field("text", jsonobj.get("text").toString(), type));
			try {
				indexWriter.addDocument(doc);
			} catch (IOException ex) {
				System.err.println("Error adding documents to the index. " +  ex.getMessage());
			}

		}
*/	}
}
