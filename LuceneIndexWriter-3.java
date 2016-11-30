import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryCachingPolicy;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by suay on 5/13/14.
 */
public class LuceneIndexWriter {

	String indexPath = "";

	String jsonFilePath = "";

	IndexWriter indexWriter = null;

	public LuceneIndexWriter(String indexPath, String jsonFilePath) {
		this.indexPath = indexPath;
		this.jsonFilePath = jsonFilePath;
	}

	public void createIndex() throws FileNotFoundException, IOException, ParseException{
		JSONArray jsonObjects = parseJSONFile();
		openIndex();
		FieldType type;
		type = new FieldType();
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		type.setStored(true);
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
			Analyzer analyzer = new WhitespaceAnalyzer();
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
				if(classType.equals(JSONObject.class) || classType.equals(JSONArray.class)){
					doc.add(new Field(field, object.get(field).toString(), type));
				}
				else{
					//System.out.println("Field :: "+field+" Value :: "+(String)object.get(field));
					doc.add(new Field(field, object.get(field).toString(), type));
				}
				
			}
			try {
				indexWriter.addDocument(doc);
			} catch (IOException ex) {
				System.err.println("Error adding documents to the index. " +  ex.getMessage());
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

	public static void main(String[] args) throws IOException {
		
		String INDEX_PATH = "C:\\Users\\Venkatesh\\Desktop\\luceneindex";
		String FILE = "C:\\Users\\Venkatesh\\Desktop\\yelpjsonindex\\yelp_academic_dataset_business.json";
		String query = "";
		
		/*LuceneIndexWriter writer = new LuceneIndexWriter(INDEX_PATH, FILE);
		try {
			writer.createIndex();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //Term t = new Term("full_address", "McClure Dravosburg");
        
       // Query query = new TermQuery(t);
       
        //PhraseQuery query = new PhraseQuery("full_address", new String[]{"McClure","Dravosburg"});
        //Builder b = new BooleanQuery.Builder();
        //b.add("McClure", BooleanClause.Occur.MUST);
       // BooleanQuery categoryQuery = new BooleanQuery();
        TermQuery catQuery1 = new TermQuery(new Term("full_address", "McClure"));
        TermQuery catQuery2 = new TermQuery(new Term("full_address", "Dravosburg"));
        //categoryQuery.add(new BooleanClause(catQuery1, BooleanClause.Occur.SHOULD));
        //categoryQuery.add(new BooleanClause(catQuery2, BooleanClause.Occur.SHOULD));
        //categoryQuery.add(new BooleanClause(categoryQuery, BooleanClause.Occur.MUST));

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        
        builder.add(new BooleanClause(catQuery1, BooleanClause.Occur.SHOULD));
        builder.add(new BooleanClause(catQuery2, BooleanClause.Occur.SHOULD));
        TopDocs topDocs = indexSearcher.search(builder.build(), 10);

        //TopDocs topDocs = indexSearcher.search(categoryQuery, 10);
       
         System.out.println(topDocs.scoreDocs[0].doc);
         System.out.println(topDocs.scoreDocs[0]);
        
        IndexReader reader = indexSearcher.getIndexReader();

        List<IndexableField> fieldList =  indexSearcher.getIndexReader().document(topDocs.scoreDocs[0].doc).getFields();
        
        
        
        
        JSONObject json = new JSONObject();
        for(IndexableField field : fieldList){
        	json.put(field.name(), field.stringValue());
        }
         
         System.out.println(json.toString());
      /*   QueryParser parser = new QueryParser(query);
         
         Set<String> categories = new HashSet<>();
         
         while(parser.hasTokens()){
        	 String token = parser.nextToken();
        	 if(categories.contains(token)){
        		 
        	 }
         }*/
         
         
	}

}