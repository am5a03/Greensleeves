package GUI;

import javax.swing.JOptionPane;

import itext.PDFFiller;
import questionbank.ExamGenerator;
import essay.Essay;

public class GenThreadWrapper implements Runnable {
	
	Essay[] essays;
	public GenThreadWrapper(Essay[] essays){
		this.essays = essays;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ExamGenerator eg = new ExamGenerator(essays);
    	Thread t = new Thread(eg);
    	t.start();
    	
    	try {
    		t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	PDFFiller pdf = new PDFFiller(essays, GUI.outputPath, eg.getQuestionList());
//    	System.out.println(eg.getQuestionList());
    	pdf.generate();
    	JOptionPane.showMessageDialog(null,
			    "Exam paper is ready at " + GUI.outputPath  + ", please check.",
			    "Inane warning",
			    JOptionPane.INFORMATION_MESSAGE);

	}

}
