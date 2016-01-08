import java.awt.Color;

import javax.swing.JFrame;

public class Ihm extends JFrame implements Runnable {

	private JFrame fenetre;
	private double data[][];

	public Ihm(double[][] data_in) {
		data = data_in;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		fenetre = new JFrame();

		fenetre.setTitle("Double Pendulum");
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setSize(1200, 800);
		fenetre.setContentPane(new Panel(data));
		fenetre.setResizable(false);
		fenetre.setBackground(Color.WHITE);
		fenetre.setVisible(true);
	}

}
