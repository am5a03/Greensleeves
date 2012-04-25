package questionbank;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.Element;

import simplenlg.features.Feature;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import com.alchemyapi.api.AlchemyAPI;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;

public class SentenceProcessor {
	private static Stack<String> subj = new Stack<String>();
	private static Stack<String> verb = new Stack<String>();
	private static Stack<String> obj = new Stack<String>();
	
	public static ArrayList<String> sents = new ArrayList<String>();
	
	/**
	 * 
	 * @param iw IndexedWord, root of the tree
	 * @param dependency Stanford dependency SemanticGraph
	 * @param depth Depth of the tree
	 * @param tdl TypedDependency iterator
	 */
	public static void traverse(IndexedWord iw, SemanticGraph dependency, int depth, Iterator<TypedDependency> tdl )
	{
			if(depth == 0){
				subj.clear();
				verb.clear();
				obj.clear();
				sents.clear();
				//System.out.println(iw);
				//System.out.println(dependency);
				
			}
			if(!tdl.hasNext()){
				return;
			}
			
			String s = "->";
			
			
			for(int i = 0; i<depth; i++){
				s += "->";
			}
			
			List<IndexedWord> cl = dependency.getChildList(iw);
			//System.out.println(s+ cl.toString());
			IndexedWord tmp = new IndexedWord();
			
			for(int i = 0; i < cl.size(); i++){
				tmp = cl.get(i);
				String str = tmp.toString();
				s += str + " ";
				
				
				
				//int nodeIdx = tmp.index();
				//IndexedWord tmpNode = dependency.getNodeByIndex(nodeIdx);
					
				/*if(dependency.hasChildWithReln(tmpNode, EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER)){
					System.out.println(dependency.getChildrenWithReln(tmpNode, EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER));
				}*/
				if(tdl.hasNext()){				
					TypedDependency td = (TypedDependency) tdl.next();
					//Skip root
					if(td.reln().toString().equals("root")){
						if(tdl.hasNext()){
							td = tdl.next();
						}
						
					}
					//System.out.println(s+" "+td);
					GrammaticalRelation gr = td.reln();
					int depIdx = td.dep().index();
					int govIdx = td.gov().index();
					
					Lexicon lexicon = Lexicon.getDefaultLexicon();
			        NLGFactory nlgFactory = new NLGFactory(lexicon);
			        Realiser realiser = new Realiser(lexicon);

					SPhraseSpec p = nlgFactory.createClause();
					NPPhraseSpec np1, np2;
					PPPhraseSpec pp;
					
					//DIRECT OBJECT
					if(gr.equals(EnglishGrammaticalRelations.DIRECT_OBJECT)){
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						IndexedWord govIw = dependency.getNodeByIndex(govIdx);
						Collection<GrammaticalRelation> grc = new LinkedList<GrammaticalRelation>();
						
						grc.add(EnglishGrammaticalRelations.POSSESSION_MODIFIER);
						grc.add(EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER);
						grc.add(EnglishGrammaticalRelations.NOUN_COMPOUND_MODIFIER);
						
						//Negation
						IndexedWord neg = dependency.getChildWithReln(govIw, EnglishGrammaticalRelations.NEGATION_MODIFIER);
						
						List<IndexedWord> dep = dependency.getChildrenWithRelns(depIw, grc);
						List<IndexedWord> prepOf = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("of"));
						List<IndexedWord> prepFrom = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("from"));
						List<IndexedWord> prepIn = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("in"));
						List<IndexedWord> prepOn = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("on"));
						String adjStr = "";
						String prepStr= "";
						String npAfter = "";
						//System.out.println(depIw);
						
						if(dep.size()>0){
							for(int j = 0; j < dep.size(); j++){
								adjStr += dep.get(j).value() + " ";
							}
						}
						
						if(prepOf.size() > 0){
							prepStr = "of";
							npAfter = prepOf.get(0).value();
						}else if(prepFrom.size() > 0){
							prepStr = "from";
							npAfter = prepFrom.get(0).value();
						}
						
						if(subj.size() != 0){
							
							//System.out.println(depIw.tag());
							
							String test = adjStr + depIw.value() + " " + prepStr + " " + npAfter;
							
							SPhraseSpec sent = nlgFactory.createClause(subj.pop(), govIw.value(), test);
							//NPPhraseSpec fumes = nlgFactory.createNounPhrase("fumes");
							
							//PPPhraseSpec pp1 = nlgFactory.createPrepositionPhrase();
							//pp1.addComplement(fumes);
							//pp1.setPreposition("of");
							//sent.addComplement(pp1);
							if(neg != null){
								sent.setFeature(Feature.NEGATED, true);
							}
							String out  = realiser.realiseSentence(sent);
							String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
							
							
						}else{
							verb.push(govIw.value());
							obj.push(depIw.value());
						}
						
						//p.setSubject("Bell");
						
						
					}else if(gr.equals(EnglishGrammaticalRelations.NOMINAL_SUBJECT)){					
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						Collection<GrammaticalRelation> grc = new LinkedList<GrammaticalRelation>();
						grc.add(EnglishGrammaticalRelations.DETERMINER);
						grc.add(EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER);
						grc.add(EnglishGrammaticalRelations.NOUN_COMPOUND_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSION_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSIVE_MODIFIER);
						List<IndexedWord> dep = dependency.getChildrenWithRelns(depIw, grc);
						String modifier = "";
						if(dep.size()!=0){
							for(int j = 0; j<dep.size(); j++){
								modifier += dep.get(j).value() + " ";
							}
							
						}
						
						//System.out.println(depIw);
						if(verb.size() != 0 && obj.size() != 0){
							
							p.setSubject(modifier+depIw.value());
						    p.setVerb(verb.pop());
						    p.setObject(obj.pop());
						    
						    String out = realiser.realiseSentence(p);
						    String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
						    
						}else{
							subj.push(modifier+depIw.value());
						}
					}else if(gr.equals(EnglishGrammaticalRelations.AGENT)){
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						
						if(obj.size()!=0 && verb.size()!=0){
							p.setSubject(depIw.value());
							p.setVerb(verb.pop());
							p.setObject(obj.pop());
							
							p.setFeature(Feature.PASSIVE, true);
							String out = realiser.realiseSentence(p);
							String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
							
						}else{
							subj.push(depIw.value());
						}
					}else if(gr.equals(EnglishGrammaticalRelations.NOMINAL_PASSIVE_SUBJECT)){
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						IndexedWord govIw = dependency.getNodeByIndex(govIdx);
						
						Collection<GrammaticalRelation> grc = new LinkedList<GrammaticalRelation>();
						grc.add(EnglishGrammaticalRelations.DETERMINER);
						grc.add(EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER);
						grc.add(EnglishGrammaticalRelations.NOUN_COMPOUND_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSION_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSIVE_MODIFIER);
						
						//Negation
						IndexedWord neg = dependency.getChildWithReln(govIw, EnglishGrammaticalRelations.NEGATION_MODIFIER);
						
						List<IndexedWord> dep = dependency.getChildrenWithRelns(depIw, grc);
						String modifier = "";
						if(dep.size()!=0){
							for(int j = 0; j<dep.size(); j++){
								modifier += dep.get(j).value() + " ";
							}
							
						}
						
						if(subj.size()!= 0){
							//System.out.println(subj.peek());
							p.setSubject(subj.pop());
							p.setObject(modifier + depIw.value());
							p.setVerb(govIw.value());
							
							//Any negation
							if(neg != null){
								p.setFeature(Feature.NEGATED, true);
							}
							
							p.setFeature(Feature.PASSIVE, true);
							String out = realiser.realiseSentence(p);
							String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
							
						}else{
							verb.push(govIw.value());
							obj.push(modifier + depIw.value());
						}
					}
					
					//System.out.println(s + td.reln());	
					/*String _str = realiser.realiseSentence(p);
					System.out.println(_str);*/
					
					
					
				}
					        
				//System.out.println(td);
					
				//System.out.println(s);	
				//System.out.println(s);
				
				traverse(tmp, dependency, depth+1, tdl);
			}

	}
	
	public static void negatedTraverse(IndexedWord iw, SemanticGraph dependency, int depth, Iterator<TypedDependency> tdl )
	{
			if(depth == 0){
				subj.clear();
				verb.clear();
				obj.clear();
				sents.clear();
				//System.out.println(iw);
				//System.out.println(dependency);
				
			}
			if(!tdl.hasNext()){
				return;
			}
			
			String s = "->";
			
			
			for(int i = 0; i<depth; i++){
				s += "->";
			}
			
			List<IndexedWord> cl = dependency.getChildList(iw);
			//System.out.println(s+ cl.toString());
			IndexedWord tmp = new IndexedWord();
			
			for(int i = 0; i < cl.size(); i++){
				tmp = cl.get(i);
				String str = tmp.toString();
				s += str + " ";
				
				
				
				//int nodeIdx = tmp.index();
				//IndexedWord tmpNode = dependency.getNodeByIndex(nodeIdx);
					
				/*if(dependency.hasChildWithReln(tmpNode, EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER)){
					System.out.println(dependency.getChildrenWithReln(tmpNode, EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER));
				}*/
				if(tdl.hasNext()){				
					TypedDependency td = (TypedDependency) tdl.next();
					//Skip root
					if(td.reln().toString().equals("root")){
						if(tdl.hasNext()){
							td = tdl.next();
						}
						
					}
					//System.out.println(s+" "+td);
					GrammaticalRelation gr = td.reln();
					int depIdx = td.dep().index();
					int govIdx = td.gov().index();
					
					Lexicon lexicon = Lexicon.getDefaultLexicon();
			        NLGFactory nlgFactory = new NLGFactory(lexicon);
			        Realiser realiser = new Realiser(lexicon);

					SPhraseSpec p = nlgFactory.createClause();
					NPPhraseSpec np1, np2;
					PPPhraseSpec pp;
					
					//DIRECT OBJECT
					if(gr.equals(EnglishGrammaticalRelations.DIRECT_OBJECT)){
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						IndexedWord govIw = dependency.getNodeByIndex(govIdx);
						Collection<GrammaticalRelation> grc = new LinkedList<GrammaticalRelation>();
						
						grc.add(EnglishGrammaticalRelations.POSSESSION_MODIFIER);
						grc.add(EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER);
						grc.add(EnglishGrammaticalRelations.NOUN_COMPOUND_MODIFIER);
						
						//Negation
						IndexedWord neg = dependency.getChildWithReln(govIw, EnglishGrammaticalRelations.NEGATION_MODIFIER);
						
						List<IndexedWord> dep = dependency.getChildrenWithRelns(depIw, grc);
						List<IndexedWord> prepOf = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("of"));
						List<IndexedWord> prepFrom = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("from"));
						List<IndexedWord> prepIn = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("in"));
						List<IndexedWord> prepOn = dependency.getChildrenWithReln(depIw, EnglishGrammaticalRelations.getPrep("on"));
						String adjStr = "";
						String prepStr= "";
						String npAfter = "";
						//System.out.println(depIw);
						
						if(dep.size()>0){
							for(int j = 0; j < dep.size(); j++){
								adjStr += dep.get(j).value() + " ";
							}
						}
						
						if(prepOf.size() > 0){
							prepStr = "of";
							npAfter = prepOf.get(0).value();
						}else if(prepFrom.size() > 0){
							prepStr = "from";
							npAfter = prepFrom.get(0).value();
						}
						
						if(subj.size() != 0){
							
							//System.out.println(depIw.tag());
							
							String test = adjStr + depIw.value() + " " + prepStr + " " + npAfter;
							
							SPhraseSpec sent = nlgFactory.createClause(subj.pop(), govIw.value(), test);
							//NPPhraseSpec fumes = nlgFactory.createNounPhrase("fumes");
							
							//PPPhraseSpec pp1 = nlgFactory.createPrepositionPhrase();
							//pp1.addComplement(fumes);
							//pp1.setPreposition("of");
							//sent.addComplement(pp1);
							if(neg == null){
								sent.setFeature(Feature.NEGATED, true);
							}
							String out  = realiser.realiseSentence(sent);
							String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
							
							
						}else{
							verb.push(govIw.value());
							obj.push(depIw.value());
						}
						
						//p.setSubject("Bell");
						
						
					}else if(gr.equals(EnglishGrammaticalRelations.NOMINAL_SUBJECT)){					
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						Collection<GrammaticalRelation> grc = new LinkedList<GrammaticalRelation>();
						grc.add(EnglishGrammaticalRelations.DETERMINER);
						grc.add(EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER);
						grc.add(EnglishGrammaticalRelations.NOUN_COMPOUND_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSION_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSIVE_MODIFIER);
						List<IndexedWord> dep = dependency.getChildrenWithRelns(depIw, grc);
						String modifier = "";
						if(dep.size()!=0){
							for(int j = 0; j<dep.size(); j++){
								modifier += dep.get(j).value() + " ";
							}
							
						}
						
						//System.out.println(depIw);
						if(verb.size() != 0 && obj.size() != 0){
							
							p.setSubject(modifier+depIw.value());
						    p.setVerb(verb.pop());
						    p.setObject(obj.pop());
						    
						    String out = realiser.realiseSentence(p);
						    String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
						    
						}else{
							subj.push(modifier+depIw.value());
						}
					}else if(gr.equals(EnglishGrammaticalRelations.AGENT)){
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						
						if(obj.size()!=0 && verb.size()!=0){
							p.setSubject(depIw.value());
							p.setVerb(verb.pop());
							p.setObject(obj.pop());
							
							p.setFeature(Feature.PASSIVE, true);
							String out = realiser.realiseSentence(p);
							String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
							
						}else{
							subj.push(depIw.value());
						}
					}else if(gr.equals(EnglishGrammaticalRelations.NOMINAL_PASSIVE_SUBJECT)){
						IndexedWord depIw = dependency.getNodeByIndex(depIdx);
						IndexedWord govIw = dependency.getNodeByIndex(govIdx);
						
						Collection<GrammaticalRelation> grc = new LinkedList<GrammaticalRelation>();
						grc.add(EnglishGrammaticalRelations.DETERMINER);
						grc.add(EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER);
						grc.add(EnglishGrammaticalRelations.NOUN_COMPOUND_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSION_MODIFIER);
						grc.add(EnglishGrammaticalRelations.POSSESSIVE_MODIFIER);
						
						//Negation
						IndexedWord neg = dependency.getChildWithReln(govIw, EnglishGrammaticalRelations.NEGATION_MODIFIER);
						
						List<IndexedWord> dep = dependency.getChildrenWithRelns(depIw, grc);
						String modifier = "";
						if(dep.size()!=0){
							for(int j = 0; j<dep.size(); j++){
								modifier += dep.get(j).value() + " ";
							}
							
						}
						
						if(subj.size()!= 0){
							//System.out.println(subj.peek());
							p.setSubject(subj.pop());
							p.setObject(modifier + depIw.value());
							p.setVerb(govIw.value());
							
							//Any negation
							if(neg == null){
								p.setFeature(Feature.NEGATED, true);
							}
							
							p.setFeature(Feature.PASSIVE, true);
							String out = realiser.realiseSentence(p);
							String[] _out = out.split(" ");
							if(_out.length > 2){
								sents.add(out);	
								//System.out.println(out + "\t Length:" + _out.length);
							}
							
						}else{
							verb.push(govIw.value());
							obj.push(modifier + depIw.value());
						}
					}
					
					//System.out.println(s + td.reln());	
					/*String _str = realiser.realiseSentence(p);
					System.out.println(_str);*/
					
					
					
				}
					        
				//System.out.println(td);
					
				//System.out.println(s);	
				//System.out.println(s);
				
				negatedTraverse(tmp, dependency, depth+1, tdl);
			}

	}

	/**
	 * 
	 * @param sentence A string of sentence
	 * @return An array with Subject Action and Object, Tense and whether is negated. separated by comma
	 * Example: John plays tennis. -> John,plays,tennis,present,
	 * @throws Exception
	 */
	public static String[] extractSAO(String sentence) throws Exception 
	{
		AlchemyAPI alchemyObj = AlchemyAPI
				.GetInstanceFromFile("lib/api_key.txt");

		org.w3c.dom.Document w3cdoc = alchemyObj.TextGetRelations(sentence);
		Document doc = XMLReader.docConverter(w3cdoc);
		List nodeList = doc.selectNodes("//results/relations/relation");
		StringBuffer _SAO = new StringBuffer("");
		ArrayList<String> SAO = new ArrayList<String>();
		
		if(nodeList != null){
			for(Iterator i = nodeList.iterator(); i.hasNext();){
				Element e = (Element)i.next();
				_SAO.append(XMLReader.getRelation(Features.SUBJECT, e));
				_SAO.append(",");
				_SAO.append(XMLReader.getRelation(Features.ACTION, e));
				_SAO.append(",");
				_SAO.append(XMLReader.getRelation(Features.OBJECT, e));
				_SAO.append(",");
				_SAO.append(XMLReader.getVerbFeature(Features.TENSE, e));
				_SAO.append(",");
				_SAO.append(XMLReader.getVerbFeature(Features.NEGATED, e));
				SAO.add(_SAO.toString());
				_SAO = new StringBuffer("");
				
			}
		}
		
		String[] _SAOArr = new String[SAO.size()];
		
		for(int i = 0; i < _SAOArr.length; i++){
			_SAOArr[i] = SAO.get(i);
		}
		
		return _SAOArr;
	}
	
	/**
	 * 
	 * @param paragraph The whole paragraph
	 * @return A <Relevancy, Concept> pair will be returned
	 * @throws Exception
	 */
	public static ArrayList<Pair<Double, String>> extractConcept(String paragraph) throws Exception{
		AlchemyAPI alchemyObj = AlchemyAPI
				.GetInstanceFromFile("lib/api_key.txt");
		org.w3c.dom.Document w3cdoc = alchemyObj.TextGetRankedConcepts(paragraph);
		Document doc = XMLReader.docConverter(w3cdoc);
		List nodeList = doc.selectNodes("//results/concepts/concept");
		
		ArrayList<Pair<Double, String>> conceptPair = new ArrayList<Pair<Double, String>>();
		
		if(nodeList != null){
			for(Iterator i = nodeList.iterator(); i.hasNext();){
				Element e = (Element) i.next();
				conceptPair.add(XMLReader.getConcept(e));
			}
			
			//Do the extract keyword if the concepts returned is smaller than 3
			if(conceptPair.size() <= 3){
				System.out.println("DEBUG********************************* Getting more concept pair");
				ArrayList<Pair<Double, String>> keywordPair = extractKeyword(paragraph);
				for(int i = 0; i < keywordPair.size(); i++){
					conceptPair.add(keywordPair.get(i));
				}
			}
		}
		
		return conceptPair;
	}
	
	public static ArrayList<Pair<Double, String>> extractKeyword(String paragraph) throws Exception{
		AlchemyAPI alchemyObj = AlchemyAPI
				.GetInstanceFromFile("lib/api_key.txt");
		org.w3c.dom.Document w3cdoc = alchemyObj.TextGetRankedKeywords(paragraph);
		Document doc = XMLReader.docConverter(w3cdoc);
		List nodeList = doc.selectNodes("//results/keywords/keyword");
		
		ArrayList<Pair<Double, String>> keywordPair = new ArrayList<Pair<Double, String>>();
		
		if(nodeList != null){
			for(Iterator i = nodeList.iterator(); i.hasNext();){
				Element e = (Element) i.next();
				keywordPair.add(XMLReader.getKeyword(e));
			}
		}
		
		return keywordPair;
	}
	
	//public static 
}
