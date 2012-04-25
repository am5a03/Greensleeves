package questionbank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import questionbank.Question.QuestionType;
import GUI.*;

import essay.*;

public class ExamGenerator implements Runnable{
	private Essay[] essays;
	private ArrayList<Question> questionList;
	
	private int[] questionQuota;
	
	public ExamGenerator(Essay[] essays){
		this.essays = essays;
		this.questionList = new ArrayList<Question>();
		this.questionQuota = new int[3];
		
		//The standard question quota for IELTS, 13 question on passage 1 and 2, 14 questions on passage 3.
		this.questionQuota[0] = 13;
		this.questionQuota[1] = 13;
		this.questionQuota[2] = 14;
	}
	
	/**
	 * Generate question for the whole exam paper
	 */
	public synchronized void genQuestion(){
//		if(GUI.qts != null)
		for(int i = 0; i < this.essays.length; i++){
			if(i == 0){
				ArrayList<QuestionType> qt = GUI.qts.get(i);
				
				chooseQuestionType(i, qt);
//				genInfo(this.essays[i], i);
//				genParaHeading(essays[i], i);
//				this.questionQuota[i] -= essays[i].getNumOfParas();
//				//For the remaining quota, generate MC
//				genMCQ(essays[i], this.questionQuota[i]);				
				
			}else if(i == 1){
//				genParaHeading(essays[i], i);
//				this.questionQuota[i] -= essays[i].getNumOfParas();
				ArrayList<QuestionType> qt = GUI.qts.get(i);
				chooseQuestionType(i, qt);

			}else if(i == 2){
				//genMCQ(essays[i], this.questionQuota[i]-10);
				ArrayList<QuestionType> qt = GUI.qts.get(i);
				chooseQuestionType(i, qt);
			}
		}
	}
	
	private void chooseQuestionType(int i, ArrayList<QuestionType> qt){
		if(qt.get(0) == QuestionType.InfoIdentification){
			genInfo(this.essays[i], i);
			this.questionQuota[i] -= essays[i].getNumOfParas();
		}else if(qt.get(0) == QuestionType.ParagraphHeading){
			genParaHeading(essays[i], i);
			this.questionQuota[i] -= essays[i].getNumOfParas();
		}else if(qt.get(0) == QuestionType.MCQ){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i]/2;
			int min = Math.min(numOfParas, quota);
			
			genMCQ(essays[i], min);
			this.questionQuota[i] -= min;
		}else if(qt.get(0) == QuestionType.TFNG){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i]/2;
			int min = Math.min(numOfParas, quota);
			
			genTFNG(essays[i], min);
			this.questionQuota[i] -= min;
		}else if(qt.get(0) == QuestionType.Matching){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i]/2;
			int min = Math.min(numOfParas, quota);
			
			genMatching(essays[i], min);
			this.questionQuota[i] -= min;
		}else if(qt.get(0) == QuestionType.SevenTypes){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i];
			int min = Math.min(numOfParas, quota);
			
			genSevenTypes(essays[i], min);
			this.questionQuota[i] -= min;
		}else if(qt.get(0) == QuestionType.cloze){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i];
			int min = Math.min(numOfParas, quota);
			//for(int j = 0; j<min; j++){
			//	getSummaryCloze(essays[j], (new Random()).nextInt(essays[j].getParagraphs().size()));
			//}
			getSummaryCloze(essays[i], min);
			this.questionQuota[i] -= min;
		}
		
		if(qt.get(1) == QuestionType.ParagraphHeading){
			genParaHeading(essays[i], i);
		}else if(qt.get(1) == QuestionType.MCQ){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i];
			int min = Math.min(numOfParas, quota);
			
			genMCQ(essays[i], min);
		}else if(qt.get(1) == QuestionType.TFNG){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i];
			int min = Math.min(numOfParas, quota);
			
			genTFNG(essays[i], min);
		}else if(qt.get(1) == QuestionType.SevenTypes){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i];
			int min = Math.min(numOfParas, quota);
			
			genSevenTypes(essays[i], min);
		}else if(qt.get(1) == QuestionType.Matching){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i];
			int min = Math.min(numOfParas, quota);
			
			genMatching(essays[i], min);
			this.questionQuota[i] -= min;
		}else if(qt.get(1) == QuestionType.cloze){
			int numOfParas = essays[i].getNumOfParas();
			int quota = this.questionQuota[i];
			int min = Math.min(numOfParas, quota);

			//for(int j = 0; j<min; j++){
			//	getSummaryCloze(essays[j], (new Random()).nextInt(essays[j].getParagraphs().size()));
			//}
			getSummaryCloze(essays[i], min);
			this.questionQuota[i] -= min;
		}
	}
	
	private synchronized void genInfo(Essay e, int essayNum){
		InfoIdentification ii = new InfoIdentification(e, essayNum);
		ii.questionGen();
		questionList.add(ii);
	}
	
	private synchronized void genParaHeading(Essay e, int essayNum){
		ParagraphHeading ph = new ParagraphHeading(e, essayNum); 
		ph.questionGen();
		questionList.add(ph);
	}
	
	private synchronized void genMCQ(Essay e, int numOfMCQs){
		int numOfParahraphs = e.getNumOfParas();
		int[] paragraphs = new int[numOfParahraphs];
		
		for(int i = 0; i < numOfParahraphs; i++) paragraphs[i] = i;
		shuffle(paragraphs);
		
		MCQs mcqs = new MCQs();
		
		for(int i = 0; i < numOfMCQs; i++){
			MCQ mcq = new MCQ(e.getParagraph(paragraphs[i]), paragraphs[i]);
			mcq.questionGen();
			mcqs.addQuestionAnsPair(mcq.getQuestionAnsPair());
			mcqs.addInstruction(mcq.getInstructions()[0]);
		}
		
		questionList.add(mcqs);
	}
	
	private synchronized void genTFNG(Essay e, int numOfTFNGs){
		int numOfParahraphs = e.getNumOfParas();
		int[] paragraphs = new int[numOfParahraphs];
		Ranker r = new Ranker();
		
		for(int i = 0; i < numOfParahraphs; i++) paragraphs[i] = i;
		shuffle(paragraphs);
		
		TFNGs tfngs = new TFNGs();
		
		for(int i = 0; i < numOfTFNGs; i++){
			Sentence s = r.getRankedSentences(e.getParagraph(i)).get(0); //Get the highest ranked sentence
			TFNG tfng = new TFNG(s);
			tfng.questionGen();
			tfngs.addQuestionAnsPair(tfng.getQuestionAnsPair());
		}
		
		questionList.add(tfngs);
		
	}
	
	private synchronized void genSevenTypes(Essay e, int numOf7Types){
		SevenTypes seven = new SevenTypes(e, numOf7Types);
		seven.questionGen();
		questionList.add(seven);
	}
	
	private synchronized void genMatching(Essay e, int numOfQs){
		Matching matching = new Matching(e, numOfQs);
		matching.questionGen();
		questionList.add(matching);
	}
	
	private synchronized void getSummaryCloze(Essay e, Integer para){
		SummaryClozes summaryClozes = new SummaryClozes(e, para);
		summaryClozes.questionGen();
		questionList.add(summaryClozes);
	}
	
	/**
	 * 
	 * @return The question list containing all the 40 questions
	 */
	public synchronized ArrayList<Question> getQuestionList(){
		return this.questionList;
	}
	
	private void shuffle(int[] arr){
		int tmp;
		Random r = new Random();
		
		for(int i = 0; i < arr.length; i++){
			int chosen = r.nextInt(arr.length);
			tmp = arr[chosen];
			arr[chosen] = arr[i];
			arr[i] = tmp;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized(this){
			genQuestion();
		}
		
	}
	
	
}
