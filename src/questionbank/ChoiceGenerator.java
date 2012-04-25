package questionbank;


import java.io.IOException;
import java.util.ArrayList;

import de.linguatools.disco.DISCO;
import de.linguatools.disco.ReturnDataBN;

public class ChoiceGenerator {
	
	//Need a set of correct answer (in array format) and the number of these answers as input
	public static String[]  ChoiceGenerator(String[] options, int n, int ex ) throws IOException{
		
		// discoDir is the path to get the DISCO wordbase
		//String discoDir = "Z:\\AdvancedBase";
		//String discoDir = "Z:\\Wordbase";
		String discoDir = Parameters.disco.discoDir;
		DISCO disco = new DISCO(discoDir, false);
		
		String[] ansSet;
		ansSet = new String[n + ex];
		
		String[] wrongAns;
		wrongAns = new String[n];
		
		String[] wrongAnsShort;
		wrongAnsShort = new String[n];
		
		String[] wrongAnsLong;
		wrongAnsLong = new String[ex];
		for( int i = 0; i < ex; i++){
			wrongAnsLong[i] = "";
		}
		
		int wrongAnsLongCounter = 0;
		int wrongAnsShortCounter = 0 ;
		
		ReturnDataBN[] simResult;
		simResult = new ReturnDataBN[n];
		
		for(int i = 0; i < n ; i++){
			String[] word = options[i].split(" ");
			
		// Do it for long options, no check repeating option
			if( word.length > 1){
					
				ReturnDataBN tempResult;
				
				for( int j = 0; j< word.length; j++){
					tempResult = disco.similarWords(word[j]);
					wrongAnsLong[wrongAnsLongCounter] =  wrongAnsLong[wrongAnsLongCounter] + tempResult.words[1] + " ";
				}		
				
				wrongAnsLongCounter ++;
				
				//keep all wrongAnsLong in wrongAnsLong[wrongAnsLongCounter]
				
				
				
			// Do it for Short options with repeating options checking
			}else{
			
				//Get similar words from DISCO
				for(int j = 0; j < n; j++){
					if(options[j] != null){
						simResult[j] = disco.similarWords( options[j] ); 
					}
				}
		        
		        int outputNo = 1; // the number for the similar word chose to use
		        
		        // To eliminate repeat answers with the true answer and the existing answer set
		        for(int k = 0; k < ex; k++){
		        	if(simResult[k] != null){
		        		
		        		
		        		for(int j = 0; j < n; j++){
		        				if ( simResult[k].words[outputNo].equals(options[j]) ){
		        					outputNo++;
		        					j = 0;
		        				}
		        		}
		        				
		        		for(int j = 0; j < wrongAnsShortCounter; j++){
		        				if ( simResult[k].words[outputNo].equals(wrongAnsShort[j]) ){
		        					outputNo++;
		        					j = 0;
		        				}				
		        		}		
					
		        	wrongAnsShort[k] = simResult[k].words[outputNo];
		        	
		        	}
		        	
		        	outputNo = 1; 
		        		
		        }
		        
		        wrongAnsShortCounter++;
		        
			}
			
		}
        
		//Put long and short wrongAns to wrongAns;
		for( int j = 0; j < wrongAnsShortCounter; j++){
			//System.out.println("Testing Short : " + wrongAnsShort[j]);
			wrongAns[j] = wrongAnsShort[j];
		}
		for( int j = 0; j < wrongAnsLongCounter; j++){
			//System.out.println("Testing Long : " + wrongAnsLong[j]);
			wrongAns[j + wrongAnsShortCounter ] = wrongAnsLong[j];
		}
			
			
        //Generate AnsSet with true and false answers
        for( int j = 0; j < n; j++){
        	ansSet[j] =  options[j];
        	ansSet[j + ex] =  wrongAns[j];
        	
        }
        
        ArrayList<String> ansSetOutput = new ArrayList<String>();
        for( int i = 0; i < n + ex; i++){
        	ansSetOutput.add(ansSet[i]);
        }
        
        java.util.Collections.shuffle(ansSetOutput);
		
        //add A - Z index to each answer
        /*int A = 65;
        for( int j = 0; j < n*2; j++){
        	ansSet[j] = (char)A + " " + ansSet[j];
        	A++;
        }*/
        
        //return the answer set
        
        for( int i = 0; i < n + ex; i++){
        	ansSet[i] = ansSetOutput.get(i);
        }
        
        return ansSet; 
		

	}
	
	public static void main(String[] args) throws IOException{
		
		String[] options;
		options = new String[5];
		
		String[] show;
		show = new String[10];
		
		options[0] = "Germany";
		options[1] = "America";
		options[2] = "Semantic similarity";
		options[3] = "Taiwan";
		options[4] = "Steve Jobs";
		
		show = ChoiceGenerator(options, 5, 3);
		
		for( int i = 0; i < 8; i++){
        	System.out.println(show[i]); 
        }
		
		
	}

}