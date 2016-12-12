package QueryParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
//import org.apache.lucene.analysis.LowerCaseFilter;
//import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


import Classes.QueryTransformer;
import Classes.RichQuery;


public class QueryProcessing{
	
//	private final List<QueryTransformer> queryTransformers;
//
//    public RichQuery extract(String poorQuery) {
//        // Step 1: Build the initial rich query.
//        RichQuery richQuery = new RichQuery(poorQuery);
//        
//
//        // Step 2: Apply query transformations.
//        for (QueryTransformer qf : this.queryTransformers) {
//            richQuery = qf.transform(richQuery);
//        }
//
//        // Step 3: Profit.
//        return richQuery;
//    }
//	
//	String query;
////	int index=0;
//	public QueryProcessing(String query) {
//		this.query=query;
//		
//	}
//	
//	@Override
//	protected TokenStreamComponents createComponents(String fieldName) {
//	    Tokenizer source = new LetterTokenizer();
//	    TokenStream filter = new LowerCaseFilter(source);
//	    filter = new StopFilter(filter,StopAnalyzer.ENGLISH_STOP_WORDS_SET);
//	    filter = new PorterStemFilter(filter);
//	    return new TokenStreamComponents(source, filter);
//	}
//	public List<String> getTokenStream(){
//		Analyzer analyzer = new StandardAnalyzer();
//		List<String> result = new ArrayList<>();
//	    try {
//	      TokenStream stream  = analyzer.tokenStream(null, new StringReader(query));
//	      stream.reset();
//	      
//	      while (stream.incrementToken()) {
//	        result.add(stream.getAttribute(CharTermAttribute.class).toString());
//	      }
//	    } catch (IOException e) {
//	      // not thrown b/c we're using a string reader...
//	      throw new RuntimeException(e);
//	    }
//	    
//	    analyzer.close();
//	    return result;
//	}
//	public static void main(String[] args) {
//		String str = "are there any Cheap Restaurants near by";
//		QueryProcessing q = new QueryProcessing(str);
//		List<String> list = q.getTokenStream();
//		for(String s:list){
//			System.out.println(s);
//		}
//	}
}
