package questionbank;
import java.util.ArrayList;
import java.util.Random;

import essay.*;

public class SummaryClozes extends Question {

	
	SummaryCloze chicken;
	Essay essay;
	Integer qnNum;
	
	ArrayList<Pair<String, String>> questionAnsPair = new ArrayList<Pair<String, String>>();
	
	public SummaryClozes(Essay e, Integer qnNum) {
		// TODO Auto-generated constructor stub
		this.questionType = Question.QuestionType.cloze;
		essay = e;
		this.qnNum = qnNum;
	}

	@Override
	public void questionGen() {
		// TODO Auto-generated method stub
		for (int i = 0; i < qnNum; i++){
			Random r = new Random();
			chicken = new SummaryCloze(essay, r.nextInt(essay.getParagraphs().size()));
			chicken.questionGen();
			questionAnsPair.add(chicken.questionAnsPair);
		}

	}
	
	public ArrayList<Pair<String, String>> getQuestionAnsPair(){
		return this.questionAnsPair;
	}

}
