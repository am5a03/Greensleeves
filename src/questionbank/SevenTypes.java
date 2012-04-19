/**
 * 
 */
package questionbank;

import essay.*;
import questionbank.ChoiceGenerator;

import java.io.IOException;
import java.util.*;


/**
 * @author Lawrence
 *
 */
public class SevenTypes extends Question {
	
	Essay essay;
	int qnNum;
	ArrayList<String> choices = new ArrayList<String>();
	
	ArrayList<Pair<String, Integer>> selectedQs = new ArrayList<Pair<String, Integer>>();
	//Pair <String, Integer> questionFactPair;
	String instruc[] = new String[4];
	
	Pair<ArrayList<Pair<String, Integer>>, ArrayList<String>> questionAnsPair ;
		/* Pair<
		 * 	ArrayList<
		 * 		Pair<Question, FactNumber(index in choices)>
		 * 	>,
		 * 	ArrayList<Choices>
		 * >
		 */

	/**
	 * 
	 */
	public SevenTypes() {
		// TODO Auto-generated constructor stub
		
	}
	
	public SevenTypes(Essay essay, int qnNum){
		this.essay = essay;
		this.qnNum = qnNum;
		this.questionType = Question.QuestionType.SevenTypes;
		
		instruc[0] = "this is an instruction";
		this.setInstruction(instruc);
	}

	/* (non-Javadoc)
	 * @see questionbank.IQuestion#questionGen()
	 */
	@Override
	public void questionGen() {
		// TODO Auto-generated method stub
		Ranker ranker = new Ranker();
		FactEvaluator.type targetType = FactEvaluator.type.PERSON; // type to generate questions, default person
		Integer maxTypeOccurance = 0;
		for (FactEvaluator.type factType : FactEvaluator.type.values()){
			Integer tempTypeCount = 0;
			for (Paragraph p : essay.getParagraphs()){
				for (Sentence s : p.getSentences()){
					tempTypeCount += FactEvaluator.getNumOfFact(factType, s.toString());
				}
			}
			if (tempTypeCount > maxTypeOccurance){
				maxTypeOccurance = tempTypeCount;
				targetType = factType;
			}
		}
		
		//ArrayList<String> factChoices = 
			//FactEvaluator.getFact(targetType, )\
		
		ArrayList<Sentence> potentialQs = new ArrayList<Sentence>();
		
		for (Paragraph p : essay.getParagraphs()){
			for (Sentence s : p.getSentences()){
				if (FactEvaluator.getFact(targetType, s.toString()) != null){
					potentialQs.add(s);
				}
			}	
		}
		
		ArrayList<String> potentialFacts = new ArrayList<String>();
		for (Sentence s : potentialQs){
			potentialFacts.addAll(FactEvaluator.getFact(targetType, s.toString()));
			
		}
		potentialFacts = new ArrayList<String>(new HashSet<String>(potentialFacts));
		
		Random r = new Random();
		ArrayList<String> selectedFacts = new ArrayList<String>();
		//ArrayList<Sentence> selectedQs = new ArrayList<Sentence>();
		if (potentialFacts.size() > qnNum){
			
			for (int i = 0; i < qnNum; i++){
				Integer selected = r.nextInt(potentialFacts.size());
				selectedFacts.add(potentialFacts.get(selected));
				potentialFacts.remove(selected);
			}
		}else{
			selectedFacts = potentialFacts;
		}
		
		//choices = new ArrayList<String>();
		//choices.addAll(ChoiceGenerator.ChoiceGenerator((String[])selectedFacts.toArray(new String[selectedFacts.size()]), qnNum+2));
		
		try {
			for (String i : (ChoiceGenerator.ChoiceGenerator((String[])selectedFacts.toArray(new String[selectedFacts.size()]), qnNum+2))){
				choices.add(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String f : selectedFacts){
			ArrayList<Sentence> tempSelectedQs = FactEvaluator.getSentenceAbout(f, essay);

			String question = (tempSelectedQs.get(r.nextInt(tempSelectedQs.size())).toString());
			question = paraphrase(question, f, targetType);
			Integer choiceNum = choices.indexOf(f);
			Pair<String, Integer> pp = new Pair<String, Integer>(question, choiceNum);
			selectedQs.add(pp);
			
			
		}
		
		questionAnsPair.setLeft(selectedQs);
		questionAnsPair.setRight(choices);

		instruc[0] = "Look at the following information and the list of " + targetType.toString().toLowerCase() + " below.";
		instruc[1] = "Match each information with the correct "+targetType.toString().toLowerCase()+".";
		instruc[2] = "Write the correct letter in boxes on your answer sheet.";
		instruc[3] = "List of " + targetType.toString().toLowerCase();
		
		this.setInstruction(instruc);
		
		/*
		for (int i = 0; i < qnNum; i++){
			Integer selected = r.nextInt(potentialQs.size());
			//selectedQs.add(potentialQs.get(selected));
			potentialQs.remove(selected);
		}
		
		
		
		for (Sentence s : potentialQs){
			selectedFacts.add(FactEvaluator.getFact(targetType, s.toString())
			FactEvaluator.ge
		}
		*/
		
	}
	
	private String paraphrase(String input, String fact, FactEvaluator.type targetType){
		String result = input;
		String replacement;
		switch (targetType){
		case DATE:
			replacement = "when";
			break;
		case LOCATION:
			replacement = "where";
			break;
		case MONEY:
			replacement = "how much";
			break;
		case ORGANIZATION:
			replacement = "who";
			break;
		case PERCENT:
			replacement = "how many";
			break;
		case PERSON:
			replacement = "who";
			break;
		case TIME:
			replacement = "when";
			break;
		default:
			replacement = fact;
			break;
		}
		result.replaceAll(fact, replacement);
		try {
			Paraphraser p = new Paraphraser(result);
			p.setChanges(false, true, true, false, 0.7);
			result = p.paraphrase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}

	public Pair<ArrayList<Pair<String, Integer>>, ArrayList<String>> getQuestionAnsPair(){
		return this.questionAnsPair;
	}
	
	

}