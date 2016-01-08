import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Menu extends JFrame {

	private JPanel titelPanel;
	private JLabel titel;

	private JLabel text_m_1;
	private JLabel text_l_1;
	private JLabel text_m_2;
	private JLabel text_l_2;
	private JLabel labels[];

	private JTextField input_m_1;
	private JTextField input_l_1;
	private JTextField input_m_2;
	private JTextField input_l_2;
	private JTextField input_t_1;
	private JTextField input_t_2;
	private JTextField input_o_1;
	private JTextField input_o_2;
	private JTextField input_h;
	private JTextField input_t;

	private JPanel pan;

	private JButton button_go;

	public Menu() {

		init_components();

		this.setSize(400, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Doppelpendel");
		this.setLocationRelativeTo(null);

		this.setContentPane(pan);
		this.setVisible(true);

	}

	private void init_components() {

		titel = new JLabel("Doppelpendel");
		titel.setFont(new Font("Arial", 40, 40));
		titel.setFocusable(false);
		titel.setForeground(Color.BLACK);
		titel.setHorizontalAlignment(SwingConstants.CENTER);
		
		titelPanel = new JPanel();
		titelPanel.setBackground(Color.LIGHT_GRAY);
		titelPanel.add(titel);

		text_m_1 = new JLabel("Masse 1 (kg):");
		text_m_1.setFocusable(false);

		text_l_1 = new JLabel("Länge 1 (m):");
		text_l_1.setFocusable(false);

		text_m_2 = new JLabel("Masse 2 (kg):");
		text_m_2.setFocusable(false);

		text_l_2 = new JLabel("Länge 2 (m)");
		text_l_1.setFocusable(false);

		int labels_size = 6;
		labels = new JLabel[labels_size];
		labels[0] = new JLabel("Theta1 (deg):");
		labels[1] = new JLabel("Theta2 (deg):");
		labels[2] = new JLabel("Omega1 (rad/s):");
		labels[3] = new JLabel("Omega2 (rad/s):");
		labels[4] = new JLabel("Zeitintervalle zwischen Annäherungen (s):");
		labels[5] = new JLabel("Dauer der Annäherung (s):");
		for (int i = 0; i < labels_size; i++) {
			labels[i].setFocusable(false);
		}

		setInput_m_1(new JTextField(10));
		setInput_l_1(new JTextField(10));
		setInput_m_2(new JTextField(10));
		setInput_l_2(new JTextField(10));
		setInput_t_1(new JTextField(10));
		setInput_t_2(new JTextField(10));
		setInput_o_1(new JTextField(10));
		setInput_o_2(new JTextField(10));
		setInput_h(new JTextField(10));
		setInput_t(new JTextField(10));

		button_go = new JButton("Go");
		button_go.setSize(10, 0);

		pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.setBackground(Color.WHITE);
		
		pan.add(titelPanel, BorderLayout.PAGE_START);

		JPanel[] input_panels = new JPanel[10];
		for (int i = 0; i < input_panels.length; i++) {
			input_panels[i] = new JPanel();
			input_panels[i].setBackground(Color.WHITE);
		}

		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
		center.setBackground(Color.WHITE);

		input_panels[0].add(text_m_1);
		input_panels[0].add(getInput_m_1());
		center.add(input_panels[0]);

		input_panels[1].add(text_l_1);
		input_panels[1].add(getInput_l_1());
		center.add(input_panels[1]);

		input_panels[2].add(text_m_2);
		input_panels[2].add(getInput_m_2());
		center.add(input_panels[2]);

		input_panels[3].add(text_l_2);
		input_panels[3].add(getInput_l_2());
		center.add(input_panels[3]);

		input_panels[4].add(labels[0]);
		input_panels[4].add(getInput_t_1());
		center.add(input_panels[4]);

		input_panels[5].add(labels[1]);
		input_panels[5].add(getInput_t_2());
		center.add(input_panels[5]);
		
		input_panels[6].add(labels[2]);
		input_panels[6].add(getInput_o_1());
		center.add(input_panels[6]);
		
		input_panels[7].add(labels[3]);
		input_panels[7].add(getInput_o_2());
		center.add(input_panels[7]);
		
		input_panels[8].add(labels[4]);
		input_panels[8].add(getInput_h());
		center.add(input_panels[8]);
		
		input_panels[9].add(labels[5]);
		input_panels[9].add(getInput_t());
		center.add(input_panels[9]);

		pan.add(center, BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		bottom.add(button_go);
		bottom.setBackground(Color.WHITE);
		pan.add(bottom, BorderLayout.PAGE_END);

	}

	//
	public void addListener(Control_menu control) {
		button_go.addActionListener(control);
	}

	// Pop-up Fenster
	public void eror_output(String error_message) {
		JOptionPane.showMessageDialog(null, error_message, "Fehler",
				JOptionPane.ERROR_MESSAGE);

	}

	// Getters & Setters
	public JTextField getInput_m_1() {
		return input_m_1;
	}

	public void setInput_m_1(JTextField input_m_1) {
		this.input_m_1 = input_m_1;
	}

	public JTextField getInput_l_1() {
		return input_l_1;
	}

	public void setInput_l_1(JTextField input_l_1) {
		this.input_l_1 = input_l_1;
	}

	public JTextField getInput_m_2() {
		return input_m_2;
	}

	public void setInput_m_2(JTextField input_m_2) {
		this.input_m_2 = input_m_2;
	}

	public JTextField getInput_l_2() {
		return input_l_2;
	}

	public void setInput_l_2(JTextField input_l_2) {
		this.input_l_2 = input_l_2;
	}

	public JTextField getInput_t_1() {
		return input_t_1;
	}

	public void setInput_t_1(JTextField input_t_1) {
		this.input_t_1 = input_t_1;
	}

	public JTextField getInput_t_2() {
		return input_t_2;
	}

	public void setInput_t_2(JTextField input_t_2) {
		this.input_t_2 = input_t_2;
	}

	public JTextField getInput_o_1() {
		return input_o_1;
	}

	public void setInput_o_1(JTextField input_o_1) {
		this.input_o_1 = input_o_1;
	}

	public JTextField getInput_o_2() {
		return input_o_2;
	}

	public void setInput_o_2(JTextField input_o_2) {
		this.input_o_2 = input_o_2;
	}

	public JTextField getInput_h() {
		return input_h;
	}

	public void setInput_h(JTextField input_h) {
		this.input_h = input_h;
	}

	public JTextField getInput_t() {
		return input_t;
	}

	public void setInput_t(JTextField input_t) {
		this.input_t = input_t;
	}

}
