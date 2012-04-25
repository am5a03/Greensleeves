import questionbank.*;
import essay.*;
/*
 * for testing - Lawrence
 */

public class mainclass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	/*	
		tagparsing tp = new tagparsing("The quick brown fox jumps over the lazy dog.");
		System.out.println(tp.getTypeOfWord("dog"));
		System.out.println(tp.getTypeOfWord("hello"));
		Integer[] nouns = tp.getNouns();
		for (Integer i : nouns)
			System.out.println(i.toString());
	*/	
		
		/*try{
			questionbank.Paraphraser p = new Paraphraser("While emissions from new cars are far less harmful than they used to be, city streets and motorways are becoming more crowded than ever, often with older trucks, buses and taxis which emit excessive levels of smoke and fumes.");
			p.setChanges(false, true, true, false, 0.4);
			System.err.println("paraphrase");
			System.out.println(p.paraphrase());
		
		}catch(Exception e){}
	}
	/*
	public static void main(String[] args){
		LibraryInitializer li = new LibraryInitializer();
		Essay e = new Essay("lib/test1.txt");
		SevenTypes seven = new SevenTypes(e, 4);
		seven.questionGen();
		
	}
	*/
		
		LibraryInitializer li = new LibraryInitializer();
		
		
		Essay e = new Essay("lib/test1.txt");
		Essay[] es = new Essay[1];
		es[0] = e;
		
		SevenTypes st = new SevenTypes(e, 4);
		st.questionGen();
		
		//System.out.println("hi");
		
		//SummaryCloze sc = new SummaryCloze(e, 2);
		//sc.questionGen();
		
		
	}

}
