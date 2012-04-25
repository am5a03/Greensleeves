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
		String discoDir = "C:\\Wordbase";
		DISCO disco = new DISCO(discoDir, false);
		
		String[] ansSet;
		ansSet = new String[n + ex];
		
		String[] wrongAns;
		wrongAns = new String[n];
		
		String[] wrongAnsShort;
		wrongAnsShort = new String[n];
		
		String[] wrongAnsLong;
		wrongAnsLong = new String[ex];
		
		int num = 0;  // deal with the numbers
		
		for( int i = 0; i < ex; i++){
			wrongAnsLong[i] = "";
		}
		
		int wrongAnsLongCounter = 0;
		int wrongAnsShortCounter = 0 ;
		
		ReturnDataBN[] simResult;
		simResult = new ReturnDataBN[n];
		
		for(int i = 0; i < n ; i++){
			String[] word = {""};
			if(i < options.length)
				word = options[i].split(" ");
			
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
					if(j < options.length){
						if(options[j] != null){

							try{
								num = Integer.parseInt(options[j]);
								num = num + 5;
								simResult[j] = null;
							}catch(NumberFormatException nf){
								simResult[j] = disco.similarWords( options[j] ); 
							}

							//System.out.println(simResult[j]);      //Pass
						}
					}
					
				}
		        
		        int outputNo = 1; // the number for the similar word chose to use
		        
		        // To eliminate repeat answers with the true answer and the existing answer set
		        for(int k = 0; k < n; k++){
		        	if(simResult[k] != null){
		        		
		        		
		        		for(int j = 0; j < n; j++){
		        			if(j < options.length){
		        				if ( simResult[k].words[outputNo].equals(options[j]) ){
		        					outputNo++;
		        					j = 0;
		        				}
		        			}
		        				
		        		}
		        				
		        		for(int j = 0; j < wrongAnsShortCounter; j++){
		        				if ( simResult[k].words[outputNo].equals(wrongAnsShort[j]) ){
		        					outputNo++;
		        					j = 0;
		        				}				
		        		}	
					
		        		
		        		wrongAnsShort[k] = simResult[k].words[outputNo];
		        		
		        	}else{
		        		wrongAnsShort[k] = "" + num;
		        	}
		        	
		        	outputNo = 1; 
		        		
		        }
		        
		        wrongAnsShortCounter++;
		        
			}
			
		}
		//Testing
		//for( int j = 0; j < 4; j++)
		//	System.out.println(wrongAnsShort[j]);//Above problem
		//System.out.println(wrongAnsShortCounter);
		
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
        	if(j < options.length){
        		ansSet[j] =  options[j];
        		ansSet[j + ex] =  wrongAns[j];
        	}
        	
        	
        }
        //Testing
        //for( int j = 0; j < n + n; j++){
        //	System.out.println(ansSet[j]);
        //}
        
        ArrayList<String> ansSetOutput = new ArrayList<String>();
        for( int i = 0; i < n + ex; i++){
        	ansSetOutput.add(ansSet[i]);
        }
        
        //java.util.Collections.shuffle(ansSetOutput);
		
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
        
       // checkNull(options,n,ex);
        
        
        
        
        return ansSet; 
		

	}
	
	/*private static void checkNull(String[] input,int n,int ex) throws IOException{
		for( int i = 0; i < input.length; i++){
			if(input[i] == null)
				ChoiceGenerator.ChoiceGenerator(input, n, ex);
		}
		
	}*/
	
	public static void main(String[] args) throws IOException{
		
		String[] options;
		options = new String[4];
		
		String[] show;
		show = new String[10];
		
		options[0] = "Mediterranean";
		options[1] = "Europe";
		options[2] = "England";
		options[3] = "1998";
		
		
		show = ChoiceGenerator(options, 4, 4);
		
		for( int i = 0; i < 8; i++){
        	System.out.println(show[i]); 
        }
		
		
	}

}