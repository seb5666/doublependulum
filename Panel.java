import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Panel extends JPanel{
	
	
	private double[][] data;
	private int steps;
	private double step_weight;
	
	public Panel(double[][] data_in)
	{
		data = data_in;
		steps = data[0].length;
		if(steps<550){
			step_weight=2;
		}
		else if(steps>1500){
			step_weight=0.5;
		}
		else{
			step_weight=1;
		}
		
	}
	
	
	public void paintComponent(Graphics g){   
		
		DecimalFormat df = new DecimalFormat("#.####");
		DecimalFormat df2 = new DecimalFormat("#.###");
		
		int x0 = 100;
		int y0 = 150;
		
		//X-Achse
		g.drawLine(x0,y0,(int) (x0+steps*step_weight+10),y0);
		//Y-Achse
		g.drawLine(x0,y0-120,x0,y0+120);
		
		g.setColor(Color.GRAY);
		//+PI Linie
		g.drawLine(x0,y0-100,(int) (x0+steps*step_weight+10),y0-100);
		//-PI Linie
		g.drawLine(x0,y0+100,(int) (x0+steps*step_weight+10),y0+100);
		
		g.setColor(Color.BLACK);
		//+PI beschriftung
		g.drawString("+¹",x0-25,y0-100);
		//-PI Beschriftunh
		g.drawString("-¹",x0-25,y0+100);
		//Theta1 Beschriftung
		g.drawString("Theta1",x0-50,y0);
		//Zeit-Achse Beschriftung
		g.drawString("t",(int) (x0+steps*step_weight+20),y0+5);
		g.drawLine((int)(x0+steps*step_weight),y0-10,(int)(x0+steps*step_weight),y0+10);
		g.drawString(""+df.format(data[0][steps-2])+"",(int) (x0+steps*step_weight-10),y0+25);
		
		//Zeichnung des Graphens:
		ArrayList<Double> used = new ArrayList();
		
		for(int i=0; i<steps; i++) {
			int y = (int) ((data[1][i]/Math.PI)*100);
			g.drawOval((int)(x0+(i-1)*step_weight),y0-y-1,2,2);
			
			if(y==0){
				
				//PrŸfen ob diese Stelle schon auf der Zeit Achse markiert wurde
				boolean found = false;
				for(int j=0; j<used.size(); j++){
					double temp = (double) used.get(j);
					if(data[0][i]>temp-0.1 && data[0][i]<temp+0.1){
						found = true;
					}
				}
				
				//Wenn nicht, dann Stelle auf Zeit-Achse markieren
				if(!found){
					g.setColor(Color.BLUE);
					g.drawLine(x0+i,y0-10,x0+i,y0+10);
					g.drawString(""+df2.format(data[0][i])+"",x0+i-10,y0+25);
					g.setColor(Color.BLACK);
					used.add(data[0][i]);
				}
			} 
		}
		
		
		//GRAPH 2
		
		x0 = 100;
		y0 = 550;
		
		//X-Achse
		g.drawLine(x0,y0,x0+steps+10,y0);
		//Y-Achse
		g.drawLine(x0,y0-120,x0,y0+120);
		
		g.setColor(Color.GRAY);
		//+PI Linie
		g.drawLine(x0,y0-100,x0+steps+10,y0-100);
		//-PI Linie
		g.drawLine(x0,y0+100,x0+steps+10,y0+100);
		
		g.setColor(Color.BLACK);
		//+PI beschriftung
		g.drawString("+¹",x0-25,y0-100);
		//-PI Beschriftunh
		g.drawString("-¹",x0-25,y0+100);
		//Theta1 Beschriftung
		g.drawString("Theta2",x0-50,y0);
		//Zeit-Achse Beschriftung
		g.drawString("t",x0+steps+20,y0+5);
		g.drawLine(x0+steps,y0-10,x0+steps,y0+10);
		g.drawString(""+df.format(data[0][steps-2])+"",x0+steps-10,y0+25);
		
		//Zeichnung des Graphens:
		ArrayList<Double> used2 = new ArrayList();
		
		for(int i=0; i<steps; i++) {
			int y = (int) ((data[2][i]/Math.PI)*100);
			g.drawOval(x0+i-1,y0-y-1,2,2);
			
			if(y==0){
				
				//PrŸfen ob diese Stelle schon auf der Zeit Achse markiert wurde
				boolean found = false;
				for(int j=0; j<used2.size(); j++){
					double temp = (double) used2.get(j);
					if(data[0][i]>temp-0.1 && data[0][i]<temp+0.1){
						found = true;
					}
				}
				
				//Wenn nicht, dann Stelle auf Zeit-Achse markieren
				if(!found){
					g.setColor(Color.BLUE);
					g.drawLine(x0+i,y0-10,x0+i,y0+10);
					g.drawString(""+df2.format(data[0][i])+"",x0+i-10,y0+25);
					g.setColor(Color.BLACK);
					used2.add(data[0][i]);
				}
			} 
		}
	}       

}
