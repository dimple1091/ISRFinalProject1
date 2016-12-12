package QueryParser;

import org.apache.lucene.analysis.synonym.SynonymFilter; 
import org.apache.lucene.analysis.synonym.SynonymMap; 
import org.apache.lucene.analysis.synonym.SolrSynonymParser; 
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.util.ResourceLoader; 

public class SynonymTransformer {
	
	/**
	 * Load synonyms from the wordnet format, "format=wordnet".
	 */
	private SynonymMap loadWordnetSynonyms(ResourceLoader loader, boolean dedup, Analyzer analyzer) throws IOException, ParseException {
	  final boolean expand = getBoolean("expand", true);
	  String synonyms = args.get("synonyms");
	  if (synonyms == null)
	    throw new IllegalArgumentException("Missing required argument 'synonyms'.");
	  
	  CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder()
	    .onMalformedInput(CodingErrorAction.REPORT)
	    .onUnmappableCharacter(CodingErrorAction.REPORT);
	  
	  WordnetSynonymParser parser = new WordnetSynonymParser(dedup, expand, analyzer);
	  File synonymFile = new File(synonyms);
	  if (synonymFile.exists()) {
	    decoder.reset();
	    parser.add(new InputStreamReader(loader.openResource(synonyms), decoder));
	  } else {
	    List<String> files = splitFileNames(synonyms);
	    for (String file : files) {
	      decoder.reset();
	      parser.add(new InputStreamReader(loader.openResource(file), decoder));
	    }
	  }
	  return parser.build();
	}
}
