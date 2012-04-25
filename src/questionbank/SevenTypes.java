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

	private boolean debug = false;
	
	public void toggleDebug(){
		debug = !debug;
	}
	
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
		
		//instruc[0] = "this is an instruction";
		this.setInstruction(instruc);
	}

	
	public boolean isRubbish(){
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
		
		if (maxTypeOccurance < 4){
			return true;
		}else{
			return false;
		}
		
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
		/*System.err.println("potential facts");
		for (String p : potentialFacts){
			System.out.println(p);
		}*/
		
		Random r = new Random();
		ArrayList<String> selectedFacts = new ArrayList<String>();
		//ArrayList<Sentence> selectedQs = new ArrayList<Sentence>();
		if (potentialFacts.size() > qnNum){
			
			for (int i = 0; i < qnNum; i++){
				Integer selected = r.nextInt(potentialFacts.size());
				selectedFacts.add(potentialFacts.get(selected));
				potentialFacts.remove(potentialFacts.get(selected));
				//potentialFacts.
				
				potentialFacts.remove(selected);
				System.out.println("---------------potential facts");
				for (String p : potentialFacts){
					System.out.println(p);
				}
			}
		}else{
			selectedFacts = potentialFacts;
		}
		
		System.out.println("----------------selected facts");
		for (String s : selectedFacts){
			System.out.println(s);
		}
		
		//choices = new ArrayList<String>();
		//choices.addAll(ChoiceGenerator.ChoiceGenerator((String[])selectedFacts.toArray(new String[selectedFacts.size()]), qnNum+2));
		
		try {
			for (String i : (ChoiceGenerator.ChoiceGenerator((String[])selectedFacts.toArray(new String[selectedFacts.size()]), qnNum, 2))){
				choices.add(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String f : selectedFacts){
			System.out.println("====== fact: "+f);
			ArrayList<Sentence> tempSelectedQs = FactEvaluator.getSentenceAbout(f, essay);

			String question = (tempSelectedQs.get(r.nextInt(tempSelectedQs.size())).toString());
			question = paraphrase(question, f, targetType);
			System.out.println(question);
			for (String c : choices){
				System.out.println(">>choice>>" + c);
			}
			
			Integer choiceNum = choices.indexOf(f);
			Pair<String, Integer> pp = new Pair<String, Integer>(question, choiceNum);
			selectedQs.add(pp);
			System.out.println(choiceNum.toString());
			
		}
		
		questionAnsPair = new Pair<ArrayList<Pair<String, Integer>>, ArrayList<String>>(selectedQs, choices);
		
		//questionAnsPair.setLeft(selectedQs);
		//questionAnsPair.setRight(choices);
		


		instruc[0] = "Look at the following information and the list of " + targetType.toString().toLowerCase() + " below.";
		instruc[1] = "Match each information with the correct "+targetType.toString().toLowerCase()+".";
		instruc[2] = "Write the correct letter in boxes on your answer sheet.";
		instruc[3] = "List of " + targetType.toString().toLowerCase();
		
		this.setInstruction(instruc);
		java.util.Collections.shuffle(choices);
		System.out.println(instruc[3]);
		for (String c : choices){
			System.out.println(c);
		}
		
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
			replacement = "this date";
			break;
		case LOCATION:
			replacement = "this location";
			break;
		case MONEY:
			replacement = "this amount";
			break;
		case ORGANIZATION:
			replacement = "this organization";
			break;
		case PERCENT:
			replacement = "this percentage";
			break;
		case PERSON:
			replacement = "this person";
			break;
		case TIME:
			replacement = "this time";
			break;
		default:
			replacement = fact;
			break;
		}
		//result.replaceAll(fact, replacement);
		String inter[] = result.split(fact);
		result = inter[0] + " " + replacement + " " + inter[1];
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
