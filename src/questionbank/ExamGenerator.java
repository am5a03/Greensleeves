package questionbank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
		for(int i = 0; i < this.essays.length; i++){
			if(i == 0){
				genInfo(this.essays[i], i);
				//genParaHeading(essays[i], i);
				this.questionQuota[i] -= essays[i].getNumOfParas();
				//For the remaining quota, generate MC
				genMCQ(essays[i], this.questionQuota[i]);
			}else if(i == 1){
				genParaHeading(essays[i], i);
				this.questionQuota[i] -= essays[i].getNumOfParas();


			}else if(i == 2){

			}
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
		
		for(int i = 0; i < numOfMCQs; i++){
			MCQ mcq = new MCQ(e.getParagraph(i));
			mcq.questionGen();
			questionList.add(mcq);
		}
	}
	
	private synchronized void genTFNG(Essay e, int numOfTFNGs){
		int numOfParahraphs = e.getNumOfParas();
		int[] paragraphs = new int[numOfParahraphs];
		Ranker r = new Ranker();
		
		for(int i = 0; i < numOfParahraphs; i++) paragraphs[i] = i;
		shuffle(paragraphs);
		
		for(int i = 0; i < numOfTFNGs; i++){
			Sentence s = r.getRankedSentences(e.getParagraph(i)).get(0); //Get the highest ranked sentence
			TFNG tfng = new TFNG(s);
			tfng.questionGen();
			questionList.add(tfng);
		}
		
	}
	
	private synchronized void genSevenTypes(Essay e, int numOf7Types){
		SevenTypes seven = new SevenTypes(e, numOf7Types);
		seven.questionGen();
		questionList.add(seven);
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
