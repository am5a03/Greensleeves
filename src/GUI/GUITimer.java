package GUI;

import java.util.*;

public class GUITimer extends TimerTask {
	
	private long startTime = 0L;
	private long currTime = 0L;
	
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long currTime = System.currentTimeMillis() - this.startTime;
		int second = (int)(currTime/1000);
		int mins = second/60;
		int hour = mins/60;
		second %= 60;
		mins %= 60;
		
		GUI.timerText.setText("Time Elapsed: " + String.format("%d:%02d:%02d", hour, mins, second));
	}
	
	public static void main(String[] args) {
        Timer timer = new Timer();
        GUITimer gt = new GUITimer();
        gt.setStartTime(System.currentTimeMillis());
        timer.schedule(gt, 0, 1000);
    }

}
