package Classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.Reader;
import java.io.StringReader;
//import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.Tokenizer;
//import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
//import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
//import org.apache.lucene.analysis.synonym.SynonymFilter;
//import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.wordnet.SynonymMap;
//import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import Classes.FilePath;
public class RichQuery {
	
	String poorQuery;
	Set<String> categories; // Set containing list of all possible words in field categories
	Set<String> neighborhood; // Set containing list of prepositions that are used before giving a location/place noun.
	Set<String> attributes; // Set containing list of all possible words in field attributes
	SynonymMap map;
	public RichQuery(String query){
		poorQuery=query;
//		categories = new HashSet<String>();
//		attributes = new HashSet<String>();
//		loc_preposition = new HashSet<String>();
//		String word;
//		Scanner fopen;
		try {
			categories = loadVariables(FilePath.Categories);
			attributes = loadVariables(FilePath.Attributes);
			neighborhood = loadVariables(FilePath.Neighborhood);
//			fopen = new Scanner(new File(FilePath.Categories)); // Keeping the contents of categories.txt in memory for easy access.
//			while (fopen.hasNextLine()==true){
//	        	word = fopen.nextLine();
//	        	categories.add(word);
//	        }
//			fopen.close();
//			fopen=new Scanner(new File(FilePath.Prepositions)); // Keeping the contents of location_prepositions.txt in memory for easy access.
//	        while (fopen.hasNextLine()==true){
//	        	word = fopen.nextLine();
//	        	loc_preposition.add(word);
//	        }
//	        fopen.close();
//	        fopen=new Scanner(new File(FilePath.Attributes)); // Keeping the contents of location_prepositions.txt in memory for easy access.
//	        while (fopen.hasNextLine()==true){
//	        	word = fopen.nextLine();
//	        	attributes.add(word);
//	        }
//	        fopen.close();
			map = new SynonymMap(new FileInputStream(FilePath.PrologFile)); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Set<String> loadVariables(String fp){
		Set<String> st = new HashSet<String>();
		String word;
		Scanner fopen;
		try {
			fopen = new Scanner(new File(fp));
			while (fopen.hasNextLine()==true){
	        	word = fopen.nextLine();
	        	st.add(word);
	        }
	        fopen.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Keeping the contents of location_prepositions.txt in memory for easy access.
        
        return st;
	}
	public Map<String,String> getRichQuery(){
		Map<String,String> richQuery = new HashMap<String,String>();
		String allquery,token,key,cat="",attr="";
		Set<String> categoryPresent,attrPresent;
		Analyzer analyzer = new ClassicAnalyzer();
		allquery="";
		try {
			TokenStream stream  = analyzer.tokenStream(null, new StringReader(poorQuery));
			stream.reset();
			while (stream.incrementToken()) {
				token = stream.getAttribute(CharTermAttribute.class).toString();
//				key = getField(token);
//				System.out.println(token);
				categoryPresent = synonymsList(token);
				categoryPresent.add(token);
//				System.out.println(setToString(categoryPresent));
				categoryPresent.retainAll(categories);
//				System.out.println(setToString(categoryPresent));
				attrPresent = synonymsList(token);
				attrPresent.add(token);
				attrPresent.retainAll(attributes);
				if(synonymsList("cheap").contains(token)){
					key = "Price Range";
					richQuery.put(key,"1 2");
				}
				else if(synonymsList("costly").contains(token)){
					key = "Price Range";
					richQuery.put(key,"3 4 5");
					
				}
				else if(!categoryPresent.isEmpty()){
//					key = "Categories";
					cat+=setToString(categoryPresent);
//					if(!attrPresent.isEmpty()){
//						attr+=setToString(attrPresent);
//					}
				}
				else if(!attrPresent.isEmpty()){
//					key = "Attributes";
					attr+=setToString(attrPresent);
//					richQuery.put(key,token);
				}
				else if(neighborhood.contains(token)){
					richQuery.put("Neighborhood",token);
				}
				else{
					allquery+=token+" ";
				}
				
			}
			if(cat!="")
				richQuery.put("Categories",cat);
			if(attr!="")
				richQuery.put("Attributes",attr);
			if(allquery!="")
				richQuery.put("All", allquery);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		analyzer.close();
		return richQuery;
	}
//	public String getField(String token){
////		List<String> fields = new ArrayList<String>();
//		if(token=="cheap" || token=="costly")
//			return "Price Range";
//		if(categories.contains(token))
//			return "Categories";
////			fields.add("Categories");
//		if(attributes.contains(token))
//			return "Attributes";
////			fields.add("Attributes");
////		if(loc_preposition.contains(token))
////			return "Location";
////			fields.add("Location");
////		else
////			return "All";
////			fields.add("All");
//		return null;
////		return fields;
//
//	}
	public String setToString(Set<String> st){
		String result="";
		for(String str:st){
			result+=str+" ";
		}
		return result;
	}
	public Set<String> synonymsList(String token){
		Set<String> synonyms = new HashSet<>();
		for(String str:java.util.Arrays.asList(map.getSynonyms(token))){
			synonyms.add(str);
		}
		return synonyms;
//		try {
//			
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//		Analyzer analyzer = new SimpleAnalyzer();
//		WordnetSynonymParser parser = new WordnetSynonymParser(true,false,analyzer);
////		System.out.println(token);
//		Reader in = new StringReader(token);
//		try {
//			parser.parse(in);
//			SynonymMap synonym = parser.build(); 
//			String str = synonym.fst.toString();
//			System.out.println(str);
////			return synonym;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
	}
//	public static void main(String[] args) {
//		String str = "thai";
////		str = "cheap";
//		RichQuery rq = new RichQuery(str);
//		Map<String,String> mp = rq.getRichQuery();
//		for(String key:mp.keySet()){
//			System.out.println(key + ":" + mp.get(key));
//		}
//		
//	}
}
