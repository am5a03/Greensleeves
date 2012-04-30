package questionbank;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Random;

import de.linguatools.disco.DISCO;
import de.linguatools.disco.ReturnDataBN;
import de.linguatools.disco.ReturnDataCol;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;  
import rita.wordnet.RiWordnet;


public class Paraphraser {
	DISCO disco;
	String workingSent;
	boolean changes[] = {false, false, false, false};
	double prob = 1;
	RiWordnet ri;
	
	public Paraphraser(String workingSent) throws IOException{
		//disco = new DISCO(Parameters.disco.discoDir, false);
		this.workingSent = workingSent;
		ri = new RiWordnet();
		
	}
	
	public void setChanges(boolean noun, boolean verb, boolean adj, boolean voice, double prob){
		changes[0] = noun;
		changes[1] = verb;
		changes[2] = adj;
		changes[3] = voice;
		this.prob = prob;
	}
	
	public String paraphrase() throws IOException{
		Random r = new Random();
		//System.err.println(disco.similarWords("dog").words[1]);
		//String inter[] = workingSent.split(" ");
		//String sent2 = "This is another sentence.";
	    TokenizerFactory<CoreLabel> tokenizerFactory = 
	      PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
	    List<CoreLabel> rawWords2 = 
	      tokenizerFactory.getTokenizer(new StringReader(workingSent)).tokenize();
		String inter[] = new String[rawWords2.size()];
		for (int i = 0; i < inter.length; i++){
			inter[i] = rawWords2.get(i).originalText();
		}
		Tagparsing tp = new Tagparsing(workingSent);
		for (int i = 0; i < 4; i++){
			String posStr;
			if ( (i == 0 || i == 1 || i == 2) && changes[i] ){ // nouns, verbs and adj
				Integer n[];
				switch(i){
					case 0:		n = tp.getNouns();
								posStr = "n";
								break;
					case 1:		n = tp.getVerbs();
								posStr = "v";	
								break;
					default:	n = tp.getAdj();
								posStr = "a";
								break;
				}
				//Integer n[] = tp.getNouns();
				//System.err.println("done parse nouns");
				//for (Integer j : n)
					//System.err.println(j.toString());
				//for (Integer ni : n){
				//Integer ni = 0;
				for (Integer ni : n){
					if (r.nextDouble() < this.prob){
						//System.out.println(ni.toString());
						//System.err.println(inter[ni]);
						String sw = "";
						
						try{
							//sw = disco.similarWords(inter[ni]).words[1];
							sw = ri.getAllSynonyms(inter[ni], posStr)[0];
						}catch (NullPointerException e){
							sw = inter[ni];
						}
						//System.err.println(sw);
						inter[ni] = sw;
					}
				}
				//System.out.println(ni.toString());
			
			}else if ( i == 3 && changes[i] ){ // voice
				
			}
		}
		
	
		String result = "";
		for (int i = 0; i < inter.length; i++)
			if (i != inter.length - 1){
				if (tp.isStickLeftPunc(i+1)){
					result += inter[i];
					System.out.println("punc");
				}else
					result += inter[i] + " ";
			}else
				result += inter[i];
		return result;
	}
	
	
	
}



