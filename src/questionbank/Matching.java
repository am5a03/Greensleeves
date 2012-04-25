package questionbank;

import essay.*;
import questionbank.ChoiceGenerator;
import rita.wordnet.RiWordnet;

import java.io.IOException;
import java.util.*;

public class Matching extends Question{
	
	private static ArrayList< Pair<String, String> > QAPairList = new ArrayList< Pair<String, String> >();
	private int numOfMatching = 5;
	private int numOfQs;
	private Essay essay;
	private ArrayList<Pair<Integer, String>> questionList = new ArrayList<Pair<Integer, String>>();
	private ArrayList<String> answerList = new ArrayList<String>();
	
	private Pair<ArrayList<Pair<Integer, String>>, ArrayList<String>> questionAnsPair;
	/*
	Matching(){
	//this.questionType = Question.QuestionType.Matching;
	}
	*/
	
	public Matching(Essay e, int numOfQs){
		this.essay = e;
		this.numOfQs = numOfQs;
	}
	
	//CTC's shuffle
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
	
	private ArrayList<String> getSentence(){
		
		int[] paragraph = new int[essay.getNumOfParas()];
		ArrayList<String> chosenSent = new ArrayList<String>();
		
		for(int i = 0; i < essay.getNumOfParas(); i++){
			paragraph[i] = i;
		}
		
		shuffle(paragraph);
		
		for(int i = 0; i < this.numOfQs; i++){
			Ranker rank = new Ranker();
			Sentence s = rank.getRankedSentences(essay.getParagraph(paragraph[i])).get(0);  // get ranked sentence
			chosenSent.add(s.toString());
		}
		return chosenSent;
		
		
		
//		ArrayList<String> SentenceSource = new ArrayList<String>();
//		
//		Essay e = new Essay("C:\\Users\\NW LEUNG\\git\\Greensleeves\\lib\\test2.txt");
//		
//		int numOfParahraphs = e.getNumOfParas();
//		
//		System.out.println(numOfParahraphs);
//		int[] paragraphs = new int[numOfParahraphs];
//		Ranker r = new Ranker();
//		
//		//for(int i = 0; i < numOfParahraphs; i++) paragraphs[i] = i;
//		//	shuffle(paragraphs);
//		
//		//pick the best sentence from each paragraph
//		for(int i = 0; i < numOfMatching; i++){
//			Sentence s = r.getRankedSentences(e.getParagraph(i)).get(0);  // assume it is OK!!!!!!!!
//			SentenceSource.add(s.toString());
//		}
//		
//		return SentenceSource;
//		
//		/*for ( int i = 0; i < para.size(); i++){
//			System.out.println(para.get(i));
//			System.out.println("next");
//		}*/
//		
//		
//		/*Paragraph p = new Paragraph(para.get(1).getParagraph());
//		ArrayList<Sentence> sentence = p.getSentences();
//		for ( int i = 0; i < sentence.size(); i++){
//			System.out.println(sentence.get(i).getSentenceNum());
//			System.out.println(sentence.get(i).toString());
//			System.out.println("next");
//		}
//		*/
//	
	}
	
	public String paraphrase(String input){
						
		try {
			Paraphraser p = new Paraphraser(input);
			p.setChanges(false, false, true, false, 0.5);
			input = p.paraphrase();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(input);
		
		return input;
	}
	
	
	//function to check Part of speech
	public String checkPos(String word){
		
		RiWordnet wordnet = new RiWordnet();
		String pos = wordnet.getBestPos(word);			
		if(pos == null)                                                             
			pos = "prep";
		
		return pos;
	}
	

	// break  criteria:
	// ( v + null + n or null  ) e.g agree on / something  
	// v e.g. eat / apple
	// (v + r + null + null or n )
	// special case v + r + n  e.g agree on ( because on = adverb in rita)
	private Pair<String, String> breakSent( String input ){
		
		String init = null;
		String init1 = null;
		
		Pair<String, String> QAPair =  new Pair<String, String>(init, init1);
		
		int breakPoint = 0;
		
		String[] word = input.split(" ");
		
		String pos;
	
		/////////////////////////////////////////////////////////////Testing show all pos
		/*
		RiWordnet wordnet = new RiWordnet(null);
		
		for (int i =0; i < word.length; i++){
			
			System.out.println(word[i]);
			pos = wordnet.getBestPos(word[i]);
			if (pos == null)
				System.out.println("null");
			else
				System.out.println(pos);
			
			System.out.println("");
		}
			*/
		////////////////////////////////////////////////////////////////
		
		
		for( int i = 0; i < word.length; i++ ){ 
			
			pos = checkPos(word[i]);			
			
			if( (i == word.length - 1) && (pos.equals("v")) )  //prevent overflow for the verb in the end
					break;
			
			if ( pos.equals("v") ){                                             
				pos = checkPos(word[i + 1]);    
				
				if( pos.equals("prep") ){            //check v + prep + noun or null(the) ( v + null + n or null  ) e.g agree on something                                  
					
					if( i + 2 < word.length){           ////prevent overflow for the verb in the end
						
						pos = checkPos(word[i + 2]); 
					
						if( pos.equals("n") || pos.equals("prep") )  
							breakPoint = i + 2;
						
					}else{
						breakPoint = i + 1;        //if the programme reach this, sentence probably broke not well
					}
						
				//End case
				}else if(pos.equals("r") ){           //check v + adverb + prep + noun or null(the)   (v + r + null + null or n ) e.g. alter drastically with something
					
					if( i + 2 < word.length){           ////prevent overflow for the verb in the end
						
						pos = checkPos(word[i + 2]);     
					
						if( pos.equals("prep") )          
						
							if( i + 3 < word.length){           ////prevent overflow for the verb in the end
								
								pos = checkPos(word[i + 3]);    
						
								if( pos.equals("n") || pos.equals("prep")  )  
									breakPoint = i + 3;
							
							}else{
								breakPoint = i + 1;        //if the programme reach this, sentence probably broke not well
							}
								
							
					}else{
						breakPoint = i + 1;        //if the programme reach this, sentence probably broke not well
					}
						
				//End case
					if( pos.equals("n") )   
						breakPoint = i + 2;
					
					//End case
					
				}else{
					
					breakPoint = i + 1;	// breakPoint = the start of the broken sentence [i] ||  else use the verb as breakPoint
					
				}
				
			}
		
		}
		
		if(breakPoint >= word.length) // prevent overflow, probably no need to run if the above is correct
			breakPoint = 0;
		
		String sentence1 = "" ;
		
		String sentence2 = "";
		
		System.out.println("Final Break point: " + breakPoint);
		
		for (int j = 0; j < breakPoint ; j++){
			sentence1 = sentence1 + word[j] + " ";
		}
		
		for (int k = breakPoint; k < word.length; k++){
			sentence2 = sentence2 + word[k] + " ";
		}
		
		QAPair.setLeft(sentence1);
		QAPair.setRight(sentence2);
		
		System.out.println("1: " + sentence1);
		
		System.out.println("2: " + sentence2);
		
		return QAPair;
		
	}
	
	public ArrayList<Pair<String, String>> AddQAPair( Pair<String, String> QAPair ){
		
		QAPairList.add(QAPair);
		return QAPairList;
		
	}
	
	
//	public static void main(String[] args) throws IOException{
//		Matching match = new Matching();
//		ArrayList<String> input = match.getSentence();
//		
//		String[] sentence;
//		sentence = new String[input.size()];
//		
//		/*
//		String[] input;
//		input = new String[5];
//		
//				// on = "r" =[ but pass now =]
//				input[0] = "Researchers with differing attitudes towards telepathy agree on a more careful selection of subjects"; 
//				
//				//this programme choose the v most closed to the end
//				input[1] = "Reports of experiences during meditation indicated the need to create a suitable environment for telepathy";
//				
//				//pass
//				input[2] = "Attitudes to parapsychology would alter drastically with the discovery of a mechanism for telepathy";
//				
//				//pass
//				input[3] = "Recent autoganzfeld trials suggest that success rates will improve with a more careful selection of subjects";
//				
//				input[4] = "Mary eats apple";
//			*/
//		
//		for( int i = 0; i < input.size(); i++)	{	
//			sentence[i]= match.paraphrase(input.get(i));
//		}
//		
//		String question;
//		String answer;
//		
//		// set all pairs in QAPairList
//		for( int j = 0; j < 5; j++)	{
//			match.AddQAPair(match.sentenceBreak(sentence[j]));
//		}
//		
//		// Testing Display
//		for( int k = 0; k < 5; k++)	{
//			question = QAPairList.get(k).getLeft();
//			answer = QAPairList.get(k).getRight();
//			System.out.println("Question: " + question );
//			System.out.println("Answer: " + answer);
//			System.out.println("");
//		}
//		
//		
//
//	}
	
	
	public void setNumOfQs(int numOfQs){
		this.numOfQs = numOfQs;
	}

//	public void questionGen(Essay e) {
//		// TODO Auto-generated method stub
//		Matching match = new Matching(essay);
//		ArrayList<String> input = match.getSentence();
//		
//		String[] sentence;
//		sentence = new String[input.size()];
//
//		for( int i = 0; i < input.size(); i++)	{	
//			sentence[i]= match.paraphrase(input.get(i));
//		}
//	
//		String question;
//		String answer;
//		
//		// set all pairs in QAPairList
//		for( int j = 0; j < 5; j++)	{
//			match.AddQAPair(match.sentenceBreak(sentence[j]));		}
//		
//		// Testing Display
//		for( int k = 0; k < 5; k++)	{
//			question = QAPairList.get(k).getLeft();
//			answer = QAPairList.get(k).getRight();
//			System.out.println("Question: " + question );
//			System.out.println("Answer: " + answer);
//			System.out.println("");
//		}
//		
//
//		
//	}

	@Override
	public void questionGen() {
		// TODO Auto-generated method stub
		ArrayList<String> sentences = getSentence();
		
		for(int i = 0; i < sentences.size(); i++){
			String s = paraphrase(sentences.get(i));
			Pair<String, String> sentPair = breakSent(s);
			this.questionList.add(new Pair<Integer, String>(i, sentPair.getLeft()));
			this.answerList.add(sentPair.getRight());
		}
		
		Collections.shuffle(questionList);
		
		questionAnsPair = new Pair<ArrayList<Pair<Integer, String>>, ArrayList<String>>(questionList, answerList);
	}
	
	public Pair<ArrayList<Pair<Integer, String>>, ArrayList<String>> getQuestionAnsPair(){
		return this.questionAnsPair;
	}

}
