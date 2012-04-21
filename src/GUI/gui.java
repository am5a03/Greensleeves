package GUI;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import essay.Essay;

import questionbank.ExamGenerator;
import questionbank.LibraryInitializer;
import questionbank.Parameters;
import questionbank.Question.QuestionType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
 
public class gui extends JPanel  {
	ArrayList<ArrayList<QuestionType>> qts = new ArrayList<ArrayList<QuestionType>>();
	
    public gui() {  	
    	
        super(new GridLayout(1, 1));
         
        JTabbedPane tabbedPane = new JTabbedPane();
         
        JComponent panel1 = makeTextPanel1("Passage 1");
        JComponent panel4 = makeTextPanel1("Passage 2");
        JComponent panel5 = makeTextPanel1("Passage 3");
        tabbedPane.addTab("Passage 1",null, panel1,
                "Passage 1");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab("Passage 2",null, panel4,
                "Passage 2");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);
        tabbedPane.addTab("Passage 3",null, panel5,
                "Passage 3");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_3);
        JComponent panel2 = makeTextPanel2("Upload Text File");
        tabbedPane.addTab("Upload", null, panel2,
                "Upload Text File");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_4);
         
        JComponent panel3 = makeTextPanel3("HELLO WORLD");
        tabbedPane.addTab("About", null, panel3,
                "About");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_5);
         
        add(tabbedPane);
         
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
     
    protected JComponent makeTextPanel3(String text) {
        JPanel panel = new JPanel();
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    protected JComponent makeTextPanel2(String text) {
        JPanel panel = new FileChooser();
        return panel;
    }
    protected JComponent makeTextPanel1(String text) {
        final JPanel panel = new JPanel(false);
        JPanel passage = new JPanel();
        JPanel qtype = new JPanel();
        final JCheckBox qtype_info = new JCheckBox();
        final JCheckBox qtype_mcq = new JCheckBox();
        final JCheckBox qtype_tf = new JCheckBox();
        final JCheckBox qtype_heading = new JCheckBox();
        final JCheckBox qtype_fact = new JCheckBox();
        final JCheckBox qtype_cloze = new JCheckBox();
        qtype_info.setText("Information identification");
        qtype_mcq.setText("Multiple Choice Questions");
        qtype_tf.setText("True/False Not Given");
        qtype_heading.setText("Paragraph Heading");
        qtype_fact.setText("Factual Question");
        qtype_cloze.setText("Summary Cloze");
        //panel.setLayout(new GridLayout(0, 2));
        JLabel question = new JLabel("Types of Questions");
        final JLabel filler = new JLabel(text);
        final JButton button = new JButton("Save");
        
        if (filler.getText().equals("Passage 3")){
        	button.setText("Generate");
        }
        	
        
        
        JTextArea textarea1 = new JTextArea("");
        JScrollPane scrollPane1 = new JScrollPane(textarea1);
        
        filler.setHorizontalAlignment(JLabel.CENTER);
        passage.setLayout (new GridLayout(1, 1));
        passage.add(scrollPane1);
        qtype.setLayout(new GridLayout(6, 1));
        qtype.add(qtype_info);
        qtype.add(qtype_mcq);
        qtype.add(qtype_tf);
        qtype.add(qtype_heading);
        qtype.add(qtype_fact);
        qtype.add(qtype_cloze);
        panel.setLayout (new BorderLayout());
        panel.add(filler,BorderLayout.NORTH);
        
        panel.add(passage,BorderLayout.CENTER);
        panel.add(qtype,BorderLayout.EAST);
        //if (filler.getText().equals("Passage 3"))
        panel.add(button,BorderLayout.SOUTH);
        
        final JTextArea p1 = textarea1;
        button.addActionListener(new ActionListener(){
 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int count = 0;
				ArrayList<QuestionType> qt = new ArrayList<QuestionType>();
				
				if(button.getText().equals("Generate")){
//					loadAndGen();
					
				}
				
				if (qtype_info.isSelected()){
					count ++;
					qt.add(QuestionType.InfoIdentification);
				}
				if (qtype_mcq.isSelected()){
					count ++;
					qt.add(QuestionType.MCQ);
				}
				if (qtype_tf.isSelected()){
					count ++;
					qt.add(QuestionType.TFNG);
				}
				if (qtype_heading.isSelected()){
					count ++;
					qt.add(QuestionType.ParagraphHeading);
				}
				if (qtype_fact.isSelected()){
					count ++;
					qt.add(QuestionType.SevenTypes);
				}
				if (qtype_cloze.isSelected()){
					count ++;
					qt.add(QuestionType.cloze);
				}
				if (p1.getText().equals(""))
					JOptionPane.showMessageDialog(null,
						    "Please input your passage.",
						    "Inane warning",
						    JOptionPane.WARNING_MESSAGE);
				else if (count == 2) {
					String fileName = filler.getText();//String finalFileName = fileName.getText();
					FileWriter outFile1;
//					for(int i = 0; i < qt.size(); i++){
//						System.out.println(qt.get(i));
//					}
					qts.add(qt);
					for(int i = 0; i < qts.size(); i++){
						System.out.println(qts.get(i));
					}
					System.out.println("");
					try {
						outFile1 = new FileWriter(fileName +".txt");
						p1.write(outFile1);
						outFile1.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else 
					JOptionPane.showMessageDialog(null,
						    "Please choose only two types of questions.",
						    "Inane warning",
						    JOptionPane.WARNING_MESSAGE);
			}
		});
        
        //popup menu.
        JPopupMenu popup = new JPopupMenu();
        JMenuItem copyMenuItem = new JMenuItem(textarea1.getActionMap().get(DefaultEditorKit.copyAction));
        JMenuItem cutMenuItem = new JMenuItem(textarea1.getActionMap().get(DefaultEditorKit.cutAction));
        JMenuItem pasteMenuItem = new JMenuItem(textarea1.getActionMap().get(DefaultEditorKit.pasteAction));

        copyMenuItem.setText("Copy");
        cutMenuItem.setText("Cut");
        pasteMenuItem.setText("Paste");

        popup.add(copyMenuItem);
        popup.add(cutMenuItem);
        popup.add(pasteMenuItem);

        //Add listener to the text area so the popup menu can come up.
        MouseListener popupListener = new PopupListener(popup);
        textarea1.addMouseListener(popupListener);
        return panel;
    } 
     
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = gui.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("AI");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Greensleeves");
         
        //Add content to the window.
        frame.add(new gui(), BorderLayout.CENTER);
        frame.getContentPane().add(BorderLayout.NORTH , label);
         
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        createAndShowGUI();
            }
        });
        LibraryInitializer li = new LibraryInitializer();
        
    }
    
    public void loadAndGen(){    	
    	Essay[] essays = new Essay[3];
    	essays[0] = new Essay(Parameters.PASSAGE1);
    	essays[1] = new Essay(Parameters.PASSAGE2);
    	essays[2] = new Essay(Parameters.PASSAGE3);
    	
    	ExamGenerator eg = new ExamGenerator(essays);
    	Thread t = new Thread(eg);
    	
    	t.start();
    	
//    	try {
//			t.join();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
    }

    class PopupListener extends MouseAdapter {
        JPopupMenu popup;
 
        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }
 
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
}