package GUI;

import java.util.Timer;

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
		GUITimer guiTimer = new GUITimer();
		Timer timer = new Timer();
		
    	Thread t = new Thread(eg);
    	t.start();
    	guiTimer.setStartTime(System.currentTimeMillis());
    	timer.schedule(guiTimer, 0, 1000);
    	
    	GUI.isGenerating = true;
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
    	GUI.generatingBtn.setText("Generate");
    	GUI.generatingBtn.setEnabled(true);
    	GUI.isGenerating = false;
    	guiTimer.cancel();

	}

}
