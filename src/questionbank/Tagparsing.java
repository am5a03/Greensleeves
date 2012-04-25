package questionbank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;  
import edu.stanford.nlp.ling.HasWord;  
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;


public class Tagparsing {
	
	String workingSent; 
	String parsed;
	
	public Tagparsing(String sent){
		this.workingSent = sent;
		this.parsed = parse();
	}
	
	private String parse(){
		LexicalizedParser lp = LibraryInitializer.LP;
		
	    TokenizerFactory<CoreLabel> tokenizerFactory = 
	      PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    List<CoreLabel> rawWords2 = 
	      tokenizerFactory.getTokenizer(new StringReader(workingSent)).tokenize();
	    Tree parse = lp.apply(rawWords2);
	
	    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    //GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	    //List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	    
	    TreePrint tp = new TreePrint("wordsAndTags");
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PrintWriter pw = new PrintWriter(baos);
	    tp.printTree(parse, pw);
	    String result = baos.toString();

	    return result;
	}
	
	public String getParsedString(){
		return parsed;
	}
	
	public String getTypeOfWord(String word){
		String splitted[] = this.parsed.split(" ");
		String type = "XX";
		for (String i : splitted){
			if (i.split("/")[0].equals(word)){
				type = i.split("/")[1];
				break;
			}	
		}
		return type;
	}
	
	public Integer[] getPartsOfSpeech(String tag){
		ArrayList<Integer> ar = new ArrayList<Integer>(); 
		//int rcount = 0;
		String splitted[] = this.parsed.split(" ");
		int i = 0;
		for (i = 0; i < splitted.length; i++ ){
			if (splitted[i].split("/")[1].startsWith(tag)){
				ar.add(i);
				//rcount++;
			}
		}
		//Integer result[] = null;
		return (Integer[])ar.toArray(new Integer[ar.size()]);
	}
	
	public Integer[] getNouns(){
		return this.getPartsOfSpeech("NN");
	}
	
	public Integer[] getVerbs(){
		return this.getPartsOfSpeech("VB");
	}
	
	public Integer[] getAdj(){
		return this.getPartsOfSpeech("JJ");
	}
	
	public boolean isStickLeftPunc(Integer pos){
		String splitted[] = this.parsed.split(" ");
		//System.out.println(splitted[pos]);
		String p = splitted[pos].split("/")[1];
		if ((p.equals(".")) || (p.equals(",")) || (p.equals(":")))
			return true;
		else 
			return false;
	}
	
	/*
	public boolean isStickRightPunc(Integer pos){
		String splitted[] = this.parsed.split(" ");
		String p = splitted[pos].split("/")[1];
		if ((p == "(") || (p == "$") || (p == "#"))
			return true;
		else 
			return false;
	}
	*/
}
