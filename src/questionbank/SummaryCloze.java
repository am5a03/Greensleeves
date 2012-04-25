package questionbank;

import java.io.StringReader;
import java.util.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import essay.*;

public class SummaryCloze extends Question {
	
	Essay essay;
	//enum clozeType {MC, fillin};
	//clozeType type;
	Integer targetPara;
	
	Pair<String, String> questionAnsPair;
	

	public SummaryCloze(Essay e, Integer targetPara) {
		// TODO Auto-generated constructor stub
		this.essay = e;
		//this.type = type;
		//this.qnNum = qnNum;
		this.targetPara = targetPara;
		
	}

	@Override
	public void questionGen() {
		// TODO Auto-generated method stub
		//essay.getNumOfParas();
		
		 Random r = new Random();
		//Integer targetPara = r.nextInt(essay.getNumOfParas());
		Ranker ranker = new Ranker();
		ArrayList<Sentence> potentialQs = ranker.getRankedSentences(essay.getParagraphs().get(targetPara));
		Sentence targetSent = potentialQs.get(r.nextInt(potentialQs.size()));
		
		try{
			Paraphraser paraphraser = new Paraphraser(targetSent.toString());
			paraphraser.setChanges(false, true, true, false, 0.4);
			String paraphrased = paraphraser.paraphrase();
			Tagparsing tp = new Tagparsing(paraphrased);
			Integer nounsPos[] = tp.getNouns();
			Integer targetBlankPos = r.nextInt(nounsPos.length);
		    TokenizerFactory<CoreLabel> tokenizerFactory = 
		      PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		    List<CoreLabel> rawWords2 = 
		      tokenizerFactory.getTokenizer(new StringReader(paraphrased)).tokenize();
			String inter[] = new String[rawWords2.size()];
			for (int i = 0; i < inter.length; i++){
				inter[i] = rawWords2.get(i).originalText();
			}
			String answer = inter[targetBlankPos];
			inter[targetBlankPos] = "________________";
			String result = "";
			for (int i = 0; i < inter.length; i++)
				if (i != inter.length - 1)
					result += inter[i] + " ";
				else
					result += inter[i];
			questionAnsPair = new Pair<String, String>(result, answer);
			System.out.println(result);
			System.out.println(answer);
		}catch(Exception e){e.printStackTrace();}
		
	}

}
