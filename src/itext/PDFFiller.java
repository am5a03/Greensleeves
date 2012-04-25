package itext;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import essay.Essay;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import questionbank.*;
import questionbank.Question.QuestionType;

public class PDFFiller {
	private Essay[] essays;
	private Document doc;
	private Document doc_ans;
	private String docName;
	private ArrayList<Question> questionList;
	private int questinCount = 1;
	
	//Common font
	private Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
	private Font italicFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
	private Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
	private Font instructFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC);
	private Font boldQuestionNum = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC|Font.BOLD);
	private Font boldItalic = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC);
	
	/**
	 * An an exam paper only consist of 3 essays
	 */
	public PDFFiller(Essay[] essays, String docName, ArrayList<Question> questionList){
		this.essays = essays; 
		this.docName = docName;
		this.doc = new Document(PageSize.A4, 60, 60, 60, 60);
		this.questionList = questionList;
		this.doc_ans = new Document(PageSize.A4, 60, 60, 60, 60);
		
		try {
			PdfWriter.getInstance(this.doc,
					new FileOutputStream(docName + ".pdf"));
			doc.open();
			PdfWriter.getInstance(this.doc_ans,
					new FileOutputStream(docName + "_Answer.pdf"));
			doc_ans.open();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public PDFFiller(Essay[] essays, String docName){
		this.essays = essays; 
		this.docName = docName;
		this.doc = new Document(PageSize.A4, 60, 60, 60, 60);
		this.doc_ans = new Document(PageSize.A4, 60, 60, 60, 60);
	
		
		try {
			PdfWriter.getInstance(this.doc,
					new FileOutputStream(docName + ".pdf"));
			doc.open();
			PdfWriter.getInstance(this.doc_ans,
					new FileOutputStream(docName + "_Answer.pdf"));
			doc_ans.open();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public Essay getEssay(int i){
		return this.essays[i];
	}
	
	public void generate(){		
		//Passage 1
		fillHeader(0, 1, 13);
		Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD|Font.UNDERLINE);
	    Font paraFont = new Font(Font.FontFamily.TIMES_ROMAN, 14);
		fillEssay(this.essays[0], 0, headingFont, paraFont, true);
		this.doc.newPage();
		try {
			this.doc_ans.add(new Paragraph("Answer:"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int size = this.questionList.size();
		//int size = this.questionList.size();
		/*
		for(int i = 0; i < size; i++){
			Question q = this.questionList.get(i);
			if(q instanceof InfoIdentification){
				InfoIdentification ii = (InfoIdentification)q;
				((InfoIdentification) q).getQuestionAnsPair();
			}
			if(q instanceof ParagraphHeading){
				ArrayList<Pair<Integer, String>> temp;
				temp = ((ParagraphHeading) q).getQuestionAnsPair();
				int numOfParagraphs = this.essays[0].getNumOfParas();
				String[] headings = new String[numOfParagraphs];
				for(int j = 0; j < numOfParagraphs; j++){
					headings[j] = temp.get(j).getRight();
				}
				fillQuestionHeading(0, QuestionType.ParagraphHeading, 1, 13);
				fillHeadingMatching(numOfParagraphs,headings,1);
			}
			
		}
		*/
		int numOfParagraphs = this.essays[0].getNumOfParas();
		//************************test heading matching******************************
		//************************hard core arraylist<pair>**************************
//		ArrayList<Pair<Integer, String>> temp = new ArrayList();
//		temp.add(new Pair(1,"The problem effects of the new international trade agreement"));
//		temp.add(new Pair(1,"The enviromental impact of modern farming"));
//		temp.add(new Pair(3,"Farming and soil erosion"));
//		temp.add(new Pair(1,"The effects of government policy in rich countries"));
//		temp.add(new Pair(1,"Governments and management of the environment"));
//		temp.add(new Pair(1,"The effects of government policy in poor countries"));
//		temp.add(new Pair(1,"Farming and food output"));
//		temp.add(new Pair(1,"The effects of government policy on food output"));
//		//****************************************************************************
//		//get num of headings
//		int numOfParagraphs = this.essays[0].getNumOfParas();
//		//save all headings to a string array
//		String[] headings = new String[numOfParagraphs];
//		for(int j = 0; j < numOfParagraphs; j++){
//			headings[j] = temp.get(j).getRight();
//		}
//		fillQuestionHeading(0, QuestionType.ParagraphHeading, 1, 13);
//		//1.num of headings, 2.heading string array, 3. starting question num
//		fillHeadingMatching(numOfParagraphs,headings,1);
//		//**************************************************************************
//		
//		//***********************test identifying information************************
//		//***********************hard core arraylist<pair>**************************
//		//i use the arraylist<pair> above to test it
//		//**************************************************************************
//		//int numOfParagraphs = this.essays[0].getNumOfParas();
//		//a string array stores all info
//		String[] info = new String[numOfParagraphs];
//		for(int j = 0; j < numOfParagraphs; j++){
//			info[j] = temp.get(j).getRight();
//		}
//		fillQuestionHeading(0, QuestionType.InfoIdentification, 14, 21);
//		//1. num of info, info string array, starting question num
//		fillInfoIdentification(numOfParagraphs,info,14);
		//*****************************************************************************
		
		
		//fillQuestionHeading(0, QuestionType.InfoIdentification, 1, 13);
		//fillQuestionHeading(0, QuestionType.ParagraphHeading, 1, 13);
		//fillQuestionHeading(0, QuestionType.TFNG, 1, 13);
		
		fillQuestions(13, 0, numOfParagraphs);
		
		
		
		
		
		
		
		
		
		
		
		
		
		numOfParagraphs = this.essays[1].getNumOfParas();
		fillHeader(1, 14, 26);
		fillEssay(this.essays[1], 1, headingFont, paraFont, true);
		this.doc.newPage();
		fillQuestions(13, 2, numOfParagraphs);
		
		
		numOfParagraphs = this.essays[2].getNumOfParas();
		fillHeader(2, 27, 40);
		fillEssay(this.essays[2], 2, headingFont, paraFont, true);
		this.doc.newPage();
		fillQuestions(14, 4, numOfParagraphs);
		
		this.doc.close();
		this.doc_ans.close();
	}
	
	private void fillImg(ImageHandler ih, String concept){
		try {
			ih.setConcept(concept);
			Image img = Image.getInstance(ih.getImgByteArray());
			img.setAlignment(Element.ALIGN_CENTER);
			this.doc.add(img);
		}catch (Exception e){
			
		}
	}
	
	//Filling all types of quesitons
	//1. Number of questions for a section; 2. Starting index of the QuestionList array 3.Number of paragraphs
	private void fillQuestions(int quota, int startIdx, int numOfParagraphs){
		int min = 0;
		int _quota = quota;

		for(int i = startIdx; i < startIdx + 2; i++){
			Question q = this.questionList.get(i);
			if(q instanceof InfoIdentification){
				_quota -= numOfParagraphs;
				min = Math.min(numOfParagraphs, _quota);
				System.out.println("Min: " + min);
				InfoIdentification ii = (InfoIdentification)q;

				fillQuestionHeading(i/2, QuestionType.InfoIdentification, this.questinCount, numOfParagraphs);
				fillInfoIdentification(numOfParagraphs, ii.getQuestionAnsPair(), this.questinCount);
			}else if(q instanceof ParagraphHeading){
				ParagraphHeading ph = (ParagraphHeading)q;
				int sum = 0;
				int qNum = 0;
				if(min == 0){
					sum = this.questinCount + numOfParagraphs - 1;
					qNum = numOfParagraphs;
				}else{
					sum = this.questinCount + min - 1;
					qNum = min;
				}			
				
				//System.out.println("QuestionRange : " + tmp);

				fillQuestionHeading(i/2, QuestionType.ParagraphHeading, this.questinCount, sum);
				fillHeadingMatching(qNum, ph.getQuestionAnsPair(), this.questinCount);
			}else if(q instanceof MCQs){
				MCQs mcqs = (MCQs)q;
				
				fillQuestionHeading(i/2, QuestionType.MCQ, this.questinCount, this.questinCount + mcqs.getQuestionAnsPair().size() - 1);
				int numOfMCs = mcqs.getQuestionAnsPair().size();
				fillMCQ(numOfMCs, mcqs.getQuestionAnsPair(), mcqs.getInstructions(), this.questinCount);
				
			}else if(q instanceof TFNGs){
				TFNGs tfngs = (TFNGs)q;
				
				fillQuestionHeading(i/2, QuestionType.TFNG, this.questinCount, this.questinCount + tfngs.getQuestionAnsPair().size() - 1);
				fillTFNG(tfngs.getQuestionAnsPair().size(), tfngs.getQuestionAnsPair(), this.questinCount);
				
			}else if(q instanceof Matching){
				Matching matching = (Matching)q;
				
				fillQuestionHeading(i/2, QuestionType.Matching, this.questinCount, this.questinCount + matching.getQuestionAnsPair().getRight().size() - 1);
				fillMatching(matching.getQuestionAnsPair().getRight().size(), matching.getQuestionAnsPair(), this.questinCount);
			}else if(q instanceof SevenTypes){
				
			}else if(q instanceof SummaryClozes){
				
			}
		}
	}
	
	//Filling the header of the passage, 1. Essay number 2.Starting question 3.Ending question
	private void fillHeader(int essayNum, int startQ, int endQ){
		Font readingPassageFont = new Font(Font.FontFamily.HELVETICA, 17, Font.BOLD);
		
		
		int realEssayNum = essayNum + 1;
		
		try{
			Paragraph _readingPassageHeader = new Paragraph();
			_readingPassageHeader.add(new Phrase("READING PASSAGE " + realEssayNum, readingPassageFont));
			_readingPassageHeader.setSpacingAfter(10);
			_readingPassageHeader.setIndentationLeft(10);
			this.doc.add(_readingPassageHeader);
			
			Paragraph instruction = new Paragraph();
			instruction.add(new Phrase("You should spend about 20 minutes on ", instructFont));
			instruction.add(new Phrase("Question "+ startQ +"-" + endQ+" ", boldQuestionNum));
			instruction.add(new Phrase("which are based on Reading Passage "+ realEssayNum  + " below.", instructFont));
			instruction.setSpacingAfter(30);
			instruction.setIndentationLeft(10);
			this.doc.add(instruction);
			
			
		}catch(Exception de){
			de.printStackTrace();
		}
	}
	
	//Filling the essay inside
	private void fillEssay(Essay e, int essayNum, 
			Font titleFont, Font paraFont, boolean showSection)
	{		
		ImageHandler ih = new ImageHandler("");
		String heading = e.getHeader().toUpperCase();
		int numOfParagraphs = e.getNumOfParas();
		
		 Paragraph _heading = new Paragraph();
		 _heading.add(new Phrase(heading, titleFont));
		 _heading.setLeading(25);
		 _heading.setSpacingAfter(30);
		 _heading.setAlignment(Element.ALIGN_CENTER);
		 
		 try {
			 doc.add(_heading);
			 String concept = e.getConcept();
			 fillImg(ih, concept);
			 
			 
		
		 Table passage_table = new Table();
		for(int i = 0; i < numOfParagraphs; i++){
			newParagraph(e.getParagraphStr(i), paraFont, i, showSection, passage_table);
		}
		doc.add(passage_table.getTable());
		} catch (DocumentException de) {
			 // TODO Auto-generated catch block
			 de.printStackTrace();
		}
	}
	
	//Fill in paragraph
	private void newParagraph(String paragraphStr, Font paraFont, 
			int paragraphNum, boolean showSection, Table pt)
	{
		Paragraph paragraph = new Paragraph();
		//Phrase section;
		paragraph.add(new Phrase(paragraphStr,paraFont));
		paragraph.setSpacingAfter(30);
		paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
		
		//if(showSection) section = new Phrase("Section "+(char)(65+paragraphNum),sectionFont);
		//else section = new Phrase(""+(char)(65+paragraphNum),sectionFont);
		char section;
		if(showSection == true)
			section = (char)(65+paragraphNum);
		else
			section = ' ';
		
		//this.doc.add(section);
		//this.doc.add(paragraph);
		pt.add_cells(paragraph, section);
	}
	
	//Fill question heading
	private void fillQuestionHeading(int essayNum, QuestionType qType, 
			int startQ, int endQ)
	{
		int realEssayNum = essayNum + 1;
		try{
			addQuestionNum(startQ, endQ);
			
			if(qType == QuestionType.InfoIdentification){
				Paragraph[] instructions = new Paragraph[4];
				for(int i = 0; i < instructions.length; i++) instructions[i] = new Paragraph();
				
				
				int numOfParas = this.essays[essayNum].getNumOfParas();
				char startChar = Question.getQuestionCharacter(0);
				char endChar = Question.getQuestionCharacter(numOfParas-1);
				
				instructions[0].add(new Phrase("Reading Passage " + realEssayNum + " has " + numOfParas 
						+ " paragraphs labelled " ));
				instructions[0].add(new Phrase(startChar + "-" + endChar , boldFont));
				instructions[0].setSpacingAfter(10);
				
				instructions[1].add(new Phrase("Which paragraphs contains the following information?"));
				instructions[1].setSpacingAfter(10);
				
				instructions[2].add(new Phrase("Write the correct letter ", italicFont));
				instructions[2].add(new Phrase(startChar + "-" + endChar , boldItalic));
				instructions[2].add(new Phrase(" in boxes " + startQ + "-" + endQ + " on your answer sheet.", italicFont));
				
				
				instructions[3].add(new Phrase("NB ", boldItalic));
				instructions[3].add(new Phrase("You may use any letter more than once.", italicFont));
				instructions[3].setSpacingAfter(10);
				
				for(int i = 0; i < instructions.length; i++) this.doc.add(instructions[i]);
				
			}else if(qType == QuestionType.ParagraphHeading){
				Paragraph[] instructions = new Paragraph[3];
				for(int i = 0; i < instructions.length; i++) instructions[i] = new Paragraph();
				
				int numOfParas = this.essays[essayNum].getNumOfParas();
				char startChar = Question.getQuestionCharacter(0);
				char endChar = Question.getQuestionCharacter(numOfParas-1);
				
				instructions[0].add(new Phrase("Reading Passage " + realEssayNum + " has " + numOfParas 
						+ " sections, " ));
				instructions[0].add(new Phrase(startChar + "-" + endChar , boldFont));
				instructions[0].setSpacingAfter(10);
				
				instructions[1].add(new Phrase("Choose the correct heading for sections  ", italicFont));
				instructions[1].add(new Phrase(startChar + "-" + endChar, boldItalic));
				instructions[1].add(new Phrase(" from the list of headings below.", italicFont));
				instructions[1].setSpacingAfter(10);
				
				
				
				instructions[2].add(new Phrase("Write the correct number " + RomanConversion.binaryToRoman(1).toLowerCase() 
						+ "-" + RomanConversion.binaryToRoman(endQ - startQ + 1).toLowerCase() + " in boxes "
						+ startQ + "-" + endQ +" on your answer sheet.", italicFont));
				instructions[2].setSpacingAfter(10);
				
				for(int i = 0; i < instructions.length; i++) this.doc.add(instructions[i]);
				
			}else if(qType == QuestionType.MCQ){
				Paragraph instruction = new Paragraph();
				Paragraph instruction2 = new Paragraph();
				
				Phrase first_line = new Phrase();
				first_line.add(new Phrase("Choose the appropriate letters ",italicFont));
				first_line.add(new Phrase("A, B, C ",boldItalic));
				first_line.add(new Phrase("or ",italicFont));
				first_line.add(new Phrase("D.", boldItalic));
				instruction.add(first_line);
				instruction.setSpacingAfter(10);
				instruction2.add(new Phrase("Write your answers in boxes "+ startQ+"-"+endQ+" on your answer sheet.",italicFont));
				instruction2.setSpacingAfter(10);
				this.doc.add(instruction);
				this.doc.add(instruction2);
				
			}else if(qType == QuestionType.TFNG){
				
				Paragraph instruction = new Paragraph();
				Paragraph instruction2 = new Paragraph();
				Paragraph trueInstruct = new Paragraph();
				Paragraph falseInstruct = new Paragraph();
				Paragraph ngInstruct = new Paragraph();
				
				
				
				instruction.add(new Phrase("Do the following statement agree with the " +
						"information given in Reading Passage " + realEssayNum + "?"));
				instruction.setSpacingAfter(10);
				
				instruction2.add(new Phrase("In boxes " + startQ + "-" + endQ + " on your answer sheet, write", italicFont));
				instruction2.setSpacingAfter(10);
				
				trueInstruct.add(new Phrase("TRUE", boldItalic));
				trueInstruct.add(new Phrase("    if the statement agrees with the information", italicFont));
				trueInstruct.setIndentationLeft(20);
				
				falseInstruct.add(new Phrase("FALSE", boldItalic));
				falseInstruct.add(new Phrase("    if the statement contradicts the information", italicFont));
				falseInstruct.setIndentationLeft(20);
				
				ngInstruct.add(new Phrase("NOT GIVEN", boldItalic));
				ngInstruct.add(new Phrase("    if there is no information on this", italicFont));
				ngInstruct.setIndentationLeft(20);
				
				this.doc.add(instruction);
				this.doc.add(instruction2);
				this.doc.add(trueInstruct);
				this.doc.add(falseInstruct);
				this.doc.add(ngInstruct);
			}else if(qType == QuestionType.Matching){
				Paragraph[] instructions = new Paragraph[2];
				for(int i = 0; i < instructions.length; i++) instructions[i] = new Paragraph();
				
				instructions[0].add(new Phrase("Complete each sentence with the correct ending, ", italicFont));
				instructions[0].add(new Phrase(Question.getQuestionCharacter(0) + "-" + Question.getQuestionCharacter(endQ - startQ + 1), boldItalic));
				instructions[0].add(new Phrase(", below.", italicFont));
				instructions[0].setSpacingAfter(10);
				
				instructions[1].add(new Phrase("Write the correct letter, ", italicFont));
				instructions[1].add(new Phrase(Question.getQuestionCharacter(0) + "-" + Question.getQuestionCharacter(endQ - startQ + 1) + ",", boldItalic));
				instructions[1].add(new Phrase("in boxes " + startQ + "-" + endQ + " on your answer sheet.", italicFont));
				instructions[1].setSpacingAfter(10);
				
				for(int i = 0; i < instructions.length; i++) this.doc.add(instructions[i]);
			}else if(qType == QuestionType.SevenTypes){
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void fillMatching(int qNum, Pair<ArrayList<Pair<Integer, String>>, ArrayList<String>> pair_list, int startQ){
		Table question_table = new Table(2,105f);
		Table answer_table = new Table(2,105f);
		String question;
		int ans;
		
		for(int i = 0;i < qNum; i++){
			this.questinCount++;
			question = pair_list.getLeft().get(i).getRight();
			Phrase p = new Phrase();
			p.add(new Phrase(question));
			question_table.add_cells2(p, startQ+i);
			ans = pair_list.getLeft().get(i).getLeft();
			answer_table.add_cells2(new Phrase((char)(65+ans)+""), startQ+i);
		}
		
		String choices;
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(70f);
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);
		Table list_table = new Table(2,1,10,100f);
		for(int i = 0; i < qNum; i++){
			choices = pair_list.getRight().get(i);
			list_table.add_cells_mc_inner(new Paragraph(choices), (char)(65+i));
		}
		cell.addElement(list_table.getTable());
		//cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		table.setSpacingBefore(20);
		table.setSpacingAfter(20);
		
		try {
			question_table.getTable().setSpacingBefore(10);
			this.doc.add(question_table.getTable());
			this.doc.add(table);
			this.doc.newPage();
			this.doc_ans.add(answer_table.getTable());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void fillCloze(int qNum, ArrayList<Pair<String,String>> pair_list, int startQ){
		Table question_table = new Table(2,105f);
		Table answer_table = new Table(2,105f);
		String question;
		String ans;
		
		for(int i = 0 ; i < qNum; i++){
			question = pair_list.get(i).getLeft();
			Phrase p = new Phrase();
			p.add(new Phrase(question));
			question_table.add_cells2(p, startQ+i);
			ans = pair_list.get(i).getRight();
			answer_table.add_cells2(new Phrase(ans), startQ+i);
		}
		try {
			question_table.getTable().setSpacingBefore(20);
			this.doc.add(question_table.getTable());
			this.doc.newPage();
			this.doc_ans.add(answer_table.getTable());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void fillST(int qNum ,Pair<ArrayList<Pair<String, Integer>>, ArrayList<String>> pair_list, int startQ){
		Table question_table = new Table(2,105f);
		Table answer_table = new Table(2,105f);
		String question;
		int ans;
		
		for(int i = 0;i < qNum; i++){
			question = pair_list.getLeft().get(i).getLeft();
			Phrase p = new Phrase();
			p.add(new Phrase(question));
			question_table.add_cells2(p, startQ+i);
			ans = pair_list.getLeft().get(i).getRight();
			answer_table.add_cells2(new Phrase((char)(65+ans)+""), startQ+i);
		}
		
		String choices;
		Paragraph listofheadings = new Paragraph("List",boldFont);
		listofheadings.setSpacingAfter(5);
		listofheadings.setAlignment(Element.ALIGN_CENTER);
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(50f);
		PdfPCell cell = new PdfPCell();
		cell.setPadding(10);
		cell.addElement(listofheadings);
		Table list_table = new Table(2,4,7,80f);
		for(int i = 0; i < qNum; i++){
			choices = pair_list.getRight().get(i);
			list_table.add_cells_mc_inner(new Paragraph(choices), (char)(65+i));
		}
		cell.addElement(list_table.getTable());
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		table.setSpacingBefore(20);
		table.setSpacingAfter(20);
		
		try {
			question_table.getTable().setSpacingBefore(10);
			this.doc.add(question_table.getTable());
			this.doc.add(table);
			this.doc.newPage();
			this.doc_ans.add(answer_table.getTable());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void fillTFNG(int qNum, ArrayList<Pair<Integer, String>> pair_list, int startQ){
		String tfng;
		int ans;
		String[] str_ans = {"TRUE", "FALSE", "NOT GIVEN"};
		Table question_table = new Table(2,105f);
		Table answer_table = new Table(2,105f);
		
		for(int i = 0 ; i < qNum; i++){
			this.questinCount++;
			tfng = pair_list.get(i).getRight();
			Phrase p = new Phrase();
			p.add(new Phrase(tfng));
			question_table.add_cells2(p, startQ+i);
			ans = pair_list.get(i).getLeft();
			answer_table.add_cells2(new Phrase(str_ans[ans]), startQ+i);
		}
		try {
			question_table.getTable().setSpacingBefore(20);
			this.doc.add(question_table.getTable());
			this.doc.newPage();
			this.doc_ans.add(answer_table.getTable());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void fillMCQ(int qNum, ArrayList<Pair<Integer, String[]>> pair_list, String[] instruct, int startQ){
		String[] mcs;
		int ans;
		String[] str_ans = {"A","B","C","D"};
		Table question_table = new Table(2,105f);
		Table answer_table = new Table(2,105f);
		for(int i = 0; i < qNum ;i++){
			this.questinCount++;
			Phrase p = new Phrase(instruct[i]);
			question_table.add_cells2(p, startQ+i);
			Table mc_table = new Table(2, 2, 10,90f);
			mcs = pair_list.get(i).getRight();
			for(int j = 0; j < 4 ; j++){
				Paragraph mc_choice = new Paragraph(mcs[j]);
				mc_table.add_cells_mc_inner(mc_choice, (char)(65+j));
			}
			question_table.add_cells_mc_outer(mc_table.getTable());
			ans = pair_list.get(i).getLeft();
			answer_table.add_cells2(new Phrase(str_ans[ans]), startQ+i);
		}
		try {
			question_table.getTable().setSpacingBefore(10);
			this.doc.add(question_table.getTable());
			this.doc.newPage();
			this.doc_ans.add(answer_table.getTable());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void fillInfoIdentification(int qNum, ArrayList<Pair<Integer, String>> pair_list, int startQ){
		String info;
		int ans;
		Table question_table = new Table(2,105f);
		Table answer_table = new Table(2,105f);
		for(int i = 0 ; i < qNum; i++){
			this.questinCount++;
			info = pair_list.get(i).getRight();
			Phrase p = new Phrase();
			p.add(new Phrase(info));
			question_table.add_cells2(p, startQ+i);
			ans = pair_list.get(i).getLeft();
			answer_table.add_cells2(new Phrase((char)(65+ans)+""), startQ+i);
		}
		try {
			question_table.getTable().setSpacingBefore(10);
			this.doc.add(question_table.getTable());
			this.doc.newPage();
			this.doc_ans.add(answer_table.getTable());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void fillHeadingMatching(int qNum, ArrayList<Pair<Integer, String>> pair_list, int startQ){
		String headings;
		int[] ans = new int[qNum];
		String[] ans_in_seq = new String[qNum];
		Paragraph listofheadings = new Paragraph("List of Headings",boldFont);
		listofheadings.setSpacingAfter(15);
		listofheadings.setAlignment(Element.ALIGN_CENTER);
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(90f);
		PdfPCell cell = new PdfPCell();
		cell.setPadding(20);
		cell.addElement(listofheadings);
		RomanList list = new RomanList();
		list.setLowercase(true);
		for(int i = 0; i < pair_list.size(); i++){
			headings = pair_list.get(i).getRight();
			list.add(new ListItem(headings));
			if(i < qNum){
				ans[i] = pair_list.get(i).getLeft();
				this.questinCount++;
			}
		}
		for(int i = 0; i < pair_list.size(); i++){
			if(i < qNum) ans_in_seq[ans[i]] = RomanConversion.binaryToRoman(i+1);
		}
		cell.addElement(list);
		table.addCell(cell);
		table.setSpacingBefore(20);
		table.setSpacingAfter(20);
		try {
			this.doc.add(table);
			
			Table question_table = new Table(2,105f);
			Table answer_table = new Table(2,105f);
			for(int j = 0 ; j < qNum; j++){
				Phrase p = new Phrase();
				p.add("Section ");
				p.add(new Phrase((char)(j+65)+"",boldFont));
				question_table.add_cells2(p, startQ+j);
				answer_table.add_cells2(new Phrase(ans_in_seq[j]), startQ+j);
			}
			this.doc.add(question_table.getTable());
			this.doc.newPage();
			this.doc_ans.add(answer_table.getTable());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	//Add the "Question x-y" headings
	private void addQuestionNum(int startQ, int endQ){
		Paragraph questionNum = new Paragraph();
		questionNum.add(new Phrase("Question " + startQ + "-" + endQ, instructFont));
		questionNum.setSpacingAfter(10);
		try {
			this.doc.add(questionNum);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
