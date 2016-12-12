package Classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.Tokenizer;
//import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import Classes.Path;
public class RichQuery {
	
	String poorQuery;
	Set<String> categories; // Set containing list of all possible words in field categories
	Set<String> loc_preposition; // Set containing list of prepositions that are used before giving a location/place noun.
	Set<String> attributes; // Set containing list of all possible words in field attributes
	public RichQuery(String query){
		poorQuery=query;
		categories = new HashSet<String>();
		attributes = new HashSet<String>();
		loc_preposition = new HashSet<String>();
		String word;
		Scanner fopen;
		try {
			fopen = new Scanner(new File(Path.Categories)); // Keeping the contents of categories.txt in memory for easy access.
			while (fopen.hasNextLine()==true){
	        	word = fopen.nextLine();
	        	categories.add(word);
	        }
			fopen.close();
			fopen=new Scanner(new File(Path.Prepositions)); // Keeping the contents of location_prepositions.txt in memory for easy access.
	        while (fopen.hasNextLine()==true){
	        	word = fopen.nextLine();
	        	loc_preposition.add(word);
	        }
	        fopen.close();
	        fopen=new Scanner(new File(Path.Attributes)); // Keeping the contents of location_prepositions.txt in memory for easy access.
	        while (fopen.hasNextLine()==true){
	        	word = fopen.nextLine();
	        	attributes.add(word);
	        }
	        fopen.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Map<String,String> getRichQuery(){
		Map<String,String> richQuery = new HashMap<String,String>();
		String token,key;
		Analyzer analyzer = new SimpleAnalyzer();
		TokenStream stream  = analyzer.tokenStream(null, new StringReader(poorQuery));
		try {
			stream.reset();
			while (stream.incrementToken()) {
				token = stream.getAttribute(CharTermAttribute.class).toString();
//				key = getField(token);
				if(token=="cheap"){
					key = "Price Range";
					richQuery.put(key,"1");
					richQuery.put(key,"2");
				}
				if(token=="costly"){
					key = "Price Range";
					richQuery.put(key,"3");
					richQuery.put(key,"4");
					richQuery.put(key,"5");
					
				}
				if(categories.contains(token)){
					key = "Categories";
					richQuery.put(key,token);
				}
				if(attributes.contains(token)){
					key = "Attributes";
					richQuery.put(key,token);
				}
			}
			richQuery.put("All", poorQuery);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		analyzer.close();
		return richQuery;
	}
	public String getField(String token){
//		List<String> fields = new ArrayList<String>();
		if(token=="cheap" || token=="costly")
			return "Price Range";
		if(categories.contains(token))
			return "Categories";
//			fields.add("Categories");
		if(attributes.contains(token))
			return "Attributes";
//			fields.add("Attributes");
//		if(loc_preposition.contains(token))
//			return "Location";
//			fields.add("Location");
//		else
//			return "All";
//			fields.add("All");
		return null;
//		return fields;

	}
	public String[] synonymsList(String token){
		Analyzer analyzer = new SimpleAnalyzer();
		WordnetSynonymParser parser = new WordnetSynonymParser(true,false,analyzer);
//		System.out.println(token);
		Reader in = new StringReader(token);
		try {
			parser.parse(in);
			SynonymMap synonym = parser.build(); 
			String str = synonym.fst.toString();
			System.out.println(str);
//			return synonym;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		String str = "are there any Cheap Restaurants near by";
		str = "cheap";
		RichQuery rq = new RichQuery(str);
		rq.synonymsList(str);
	}
}
