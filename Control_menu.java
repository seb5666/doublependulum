import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Control_menu implements ActionListener {

	private Menu menu;

	// Eigenschaften des Pendels:
	double masse1 = 0.0; // Masse 1 (in kg)
	double lange1 = 0.0; // Länge 1 (in m)

	double masse2 = 0.0; // Masse 2 (in kg)
	double lange2 = 0.0; // Länge 2 (in m)

	// Anfangsbedigungen:
	double theta1_0 = Math.PI / 16; // Anfangswinkel1 in radiant
	double theta2_0 = Math.PI / 16; // Anfangswinkel2 in radiant
	double omega1_0 = 0.0; // Anfangsgeschwindigkeit1
	double omega2_0 = 0.0; // Anfangsgeschwindigkeit2

	// Erdbeschleunigung
	double g = 9.81;

	// Grösse der Zeitschritte zur modellierung
	double h = 0.01;

	// Dauer des Modells
	double zeit = 10; // in Sekunden

	double[] initial_conditions = new double[7];

	public Control_menu(Menu menu_input) {

		menu = menu_input;
		menu.addListener(this);

	}

	public void actionPerformed(ActionEvent e) {

		getInputValues();
		setInitialConditions();

		long initial_time = (int) System.currentTimeMillis();
		// Algoritmus für eingaben starten
		double[][] data = runge_kutta(zeit, h, g, masse1, masse2, lange1,
				lange2, theta1_0, theta2_0, omega1_0, omega2_0);

		long zwischen_time = System.currentTimeMillis();

		// Graph zeichnen (dauert lange 2-3 sek.) Neuer threat?
		Ihm refIhm = new Ihm(data);
		refIhm.run();

		long zwischen_time2 = System.currentTimeMillis();

		// Klasse, die Daten in Excel-Dokument schreibt
		Excel excel = new Excel(initial_conditions, data);
		excel.run();

		long final_time = System.currentTimeMillis();

		System.out
				.println("Zeit funktion runge_kutta() +txt-Dokument: "
						+ (double) ((zwischen_time - initial_time) / 1000)
						+ "Sekunden");
		System.out.println("Zeit Grapen anzeichen: "
				+ (zwischen_time2 - zwischen_time) + "Millisekunden");
		System.out.println("Zeit Excel-Dokument schreiben: "
				+ (final_time - zwischen_time2) + "Millisekunden");
		System.out.println("Gesamtzeit: "
				+ (double) ((final_time - initial_time) / 1000) + "Sekunden");
	}

	private void setInitialConditions() {
		// Anfangsbedigungen bestimmen
		initial_conditions[0] = masse1; // m1
		initial_conditions[1] = lange1; // l1
		initial_conditions[2] = masse2; // m2
		initial_conditions[3] = lange2; // l2
		initial_conditions[4] = g; // g
		initial_conditions[5] = h; // h
		initial_conditions[6] = zeit; // zeit
	}

	private void getInputValues() {
		// Prüfen der validität der Daten
		try {

			// Initialiesierung der Daten
			masse1 = Double.parseDouble(menu.getInput_m_1().getText());
			lange1 = Double.parseDouble(menu.getInput_l_1().getText());
			masse2 = Double.parseDouble(menu.getInput_m_2().getText());
			lange2 = Double.parseDouble(menu.getInput_l_2().getText());
			theta1_0 = ((Double.parseDouble(menu.getInput_t_1().getText()) % 360) * (2 * Math.PI / 360));
			theta2_0 = ((Double.parseDouble(menu.getInput_t_2().getText()) % 360) * (2 * Math.PI / 360));
			omega1_0 = Double.parseDouble(menu.getInput_o_1().getText());
			omega1_0 = Double.parseDouble(menu.getInput_o_2().getText());
			h = Double.parseDouble(menu.getInput_h().getText());
			zeit = Double.parseDouble(menu.getInput_t().getText());

		} catch (Exception ex) {
			menu.eror_output("Bitte geben sie Zahlen im Format ##.## ein!");
		}

	}

	private static double[][] runge_kutta(double zeit, double h, double g,
			double masse1, double masse2, double lange1, double lange2,
			double theta1_0, double theta2_0, double omega1_0, double omega2_0) {

		// Daten zur Bildung eines Graphens:
		double[][] data = new double[5][((int) (zeit / h)) + 2];
		data[0][0] = 0.0; // Zeit
		data[1][0] = theta1_0; // tehta1
		data[2][0] = theta2_0; // theta2
		data[3][0] = omega1_0; // omega1
		data[4][0] = omega2_0; // omega2

		// Initialiesierung der variablen
		double theta1 = theta1_0;
		double theta2 = theta2_0;
		double omega1 = omega1_0;
		double omega2 = omega2_0;

		// Formatieren für spätere Ausgaben
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();

		DecimalFormat df = new DecimalFormat("#.####");

		// Initialiesierung des Textes für externe Ausgabe
		String outputfile_content = dateFormat.format(date) + "\n";
		String debugfile_content = "";

		outputfile_content += "Zeitschritte: " + h + "\t";
		outputfile_content += "Zeit: " + zeit + "\n";
		outputfile_content += "Theta1: " + theta1_0 + "\t";
		outputfile_content += "Theta2: " + theta2_0 + "\t";
		outputfile_content += "Omega1: " + omega1_0 + "\t";
		outputfile_content += "Omega1: " + omega2_0 + "\n";
		outputfile_content += "Masse 1: " + masse1 + "\t";
		outputfile_content += "Masse 2: " + masse2 + "\t";
		outputfile_content += "Länge 1: " + lange1 + "\t";
		outputfile_content += "Länge 2: " + lange1 + "\n\n";

		// Initialiesierung für Zähler der Schleifendurchführungen
		int count = 1;

		for (double i = 0; i < zeit; i += h) {
			// initialisierung der 4 Zwischenstufen für Theta1/2 und Omega1/2
			double[] k = new double[4]; // Zwischenstufen für theta1
			double[] l = new double[4]; // Zwischenstufen für theta2
			double[] m = new double[4]; // Zwischenstufen für omega1
			double[] n = new double[4]; // Zwischenstufen für omega2

			// Berechnung der Zwischenstufen
			// (http://www.myphysicslab.com/runge_kutta.html multiple variable )
			// Erste Stufe
			k[0] = h * omega1;
			l[0] = h * omega2;
			m[0] = h
					* f1(g, masse1, masse2, lange1, lange2, theta1, theta2,
							omega1, omega2);
			n[0] = h
					* f2(g, masse1, masse2, lange1, lange2, theta1, theta2,
							omega1, omega2);

			// Zweite Stufe
			k[1] = h * (omega1 + ((0.5) * m[0]));
			l[1] = h * (omega2 + ((0.5) * n[0]));
			m[1] = h
					* f1(g, masse1, masse2, lange1, lange2, theta1
							+ ((0.5) * k[0]), theta2 + ((0.5) * l[0]), omega1
							+ ((0.5) * m[0]), omega2 + ((0.5) * n[0]));
			n[1] = h
					* f2(g, masse1, masse2, lange1, lange2, theta1
							+ ((0.5) * k[0]), theta2 + ((0.5) * l[0]), omega1
							+ ((0.5) * m[0]), omega2 + ((0.5) * n[0]));

			// Dritte Stufe
			k[2] = h * (omega1 + ((0.5) * m[1]));
			l[2] = h * (omega2 + ((0.5) * n[1]));
			m[2] = h
					* f1(g, masse1, masse2, lange1, lange2, theta1
							+ ((0.5) * k[1]), theta2 + ((0.5) * l[1]), omega1
							+ ((0.5) * m[1]), omega2 + ((0.5) * n[1]));
			n[2] = h
					* f2(g, masse1, masse2, lange1, lange2, theta1
							+ ((0.5) * k[1]), theta2 + ((0.5) * l[1]), omega1
							+ ((0.5) * m[1]), omega2 + ((0.5) * n[1]));

			// Vierte Stufe
			k[3] = h * (omega1 + m[2]);
			l[3] = h * (omega2 + n[2]);
			m[3] = h
					* f1(g, masse1, masse2, lange1, lange2, theta1 + k[2],
							theta2 + l[2], omega1 + m[2], omega2 + n[2]);
			n[3] = h
					* f2(g, masse1, masse2, lange1, lange2, theta1 + k[2],
							theta2 + l[2], omega1 + m[2], omega2 + n[2]);

			// Annäherung der Werte
			theta1 = theta1
					+ ((1.0 / 6) * (k[0] + (2 * k[1]) + (2 * k[2]) + k[3]));
			theta2 = theta2
					+ ((1.0 / 6) * (l[0] + (2 * l[1]) + (2 * l[2]) + l[3]));
			omega1 = omega1
					+ ((1.0 / 6) * (m[0] + (2 * m[1]) + (2 * m[2]) + m[3]));
			omega2 = omega2
					+ ((1.0 / 6) * (n[0] + (2 * n[1]) + (2 * n[2]) + n[3]));

			// Prüfen on der Pendel eine Umdrehung macht
			if (theta1 > Math.PI) {
				theta1 = -Math.PI + theta1 - Math.PI;
			}
			if (theta1 < -Math.PI) {
				theta1 = Math.PI - theta1 + Math.PI;
			}
			if (theta2 > Math.PI) {
				theta2 = -Math.PI + theta2 - Math.PI;
			}
			if (theta2 < -Math.PI) {
				theta2 = Math.PI - theta2 + Math.PI;
			}

			// Ausgabe der Resultate für externe Ausgabe
			outputfile_content += "Zeit:  " + df.format(i + h) + "\t";
			outputfile_content += "theta1: " + df.format(theta1) + "\t";
			outputfile_content += "theta2: " + df.format(theta2) + "\t";
			outputfile_content += "omega1: " + df.format(omega1) + "\t";
			outputfile_content += "omega2: " + df.format(omega2) + "\n";

			// Ausgabe der Resultate und Zwischenstuffe im Falle eines Fehlers
			debugfile_content += "k1: " + df.format(k[0]) + "\t";
			debugfile_content += "l1: " + df.format(l[0]) + "\t";
			debugfile_content += "m1: " + df.format(m[0]) + "\t";
			debugfile_content += "n1: " + df.format(n[0]) + "\n";

			debugfile_content += "k2: " + df.format(k[1]) + "\t";
			debugfile_content += "l2: " + df.format(l[1]) + "\t";
			debugfile_content += "m2: " + df.format(m[1]) + "\t";
			debugfile_content += "n2: " + df.format(n[1]) + "\n";

			debugfile_content += "k3: " + df.format(k[2]) + "\t";
			debugfile_content += "l3: " + df.format(l[2]) + "\t";
			debugfile_content += "m3: " + df.format(m[2]) + "\t";
			debugfile_content += "n3: " + df.format(n[2]) + "\n";

			debugfile_content += "k4: " + df.format(k[3]) + "\t";
			debugfile_content += "l4: " + df.format(l[3]) + "\t";
			debugfile_content += "m4: " + df.format(m[3]) + "\t";
			debugfile_content += "n4: " + df.format(n[3]) + "\n";

			debugfile_content += "Zeit:  " + df.format(i + h) + "\t";
			debugfile_content += "theta1: " + df.format(theta1) + "\t";
			debugfile_content += "theta2: " + df.format(theta2) + "\t";
			debugfile_content += "omega1: " + df.format(omega1) + "\t";
			debugfile_content += "omega2: " + df.format(omega2) + "\n";

			// Werte in Array für Graphen speichern
			data[0][count] = count * h;
			data[1][count] = theta1;
			data[2][count] = theta2;
			data[3][count] = omega1;
			data[4][count] = omega2;

			// Schleifenzähler aktualisieren
			count++;
		}

		// Trennung zwischen externe Ausgaben
		outputfile_content += "\n\n\n*****************************************************\n";

		// Externe Ausgabe in Dokumenten schreiben
		try {

			URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
			File f = new File(url.toURI().getPath());
			String path1 = f.getParentFile().getPath()+"/test.txt";
			String path2 = f.getParentFile().getPath()+"/debug.txt";
			f = new File(path1);
			
			
			FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(outputfile_content);
			bw.close();

			f = new File(path2);
			fw = new FileWriter(f.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.append(debugfile_content);
			bw.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return data;
	}

	//Beschleuinigung 1 in funktion von (theta 1/2 und omega 1/2)
	public static double f1(double g, double m1, double m2, double l1,
			double l2, double theta1, double theta2, double omega1,
			double omega2) {

		// Prüfen nach Fehlerquellen:
		try {

			// Division durch 0
			if ((((m1 + m2) * l1) - (m2 * l1 * Math.cos(theta1 - theta2) * Math
					.cos(theta1 - theta2))) == 0) {
				return 0.0;
			}

			// Berechnung
			double alpha1 = ((-m2 * l1 * omega1 * omega1
					* Math.sin(theta1 - theta2) * Math.cos(theta1 - theta2))
					+ (m2 * g * Math.sin(theta2) * Math.cos(theta1 - theta2))
					- (m2 * l2 * omega2 * omega2 * Math.sin(theta1 - theta2)) - (g
					* (m1 + m2) * Math.sin(theta1)))
					/ (((m1 + m2) * l1) - (m2 * l1 * Math.cos(theta1 - theta2) * Math
							.cos(theta1 - theta2)));

			// Zu grosse Zahl
			if (alpha1 > Double.MAX_VALUE) {
				throw new Exception(
						"Die Funktion f2(theta1,theta2,omega1,omega2) hat eine zu grosse Zahl ausgegeben");
			}

			return alpha1;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0.0;
		}
	}

	public static double f2(double g, double m1, double m2, double l1,
			double l2, double theta1, double theta2, double omega1,
			double omega2) {

		// Prüfen nach Fehlerquellen:
		try {

			// Division durch 0
			if (m2 * l2 == 0) {
				return 0.0;
			}

			// Berechnung
			double alpha2 = (-(m2 * l1 * Math.cos(theta1 - theta2) * f1(g, m1,
					m2, l1, l2, theta1, theta2, omega1, omega2))
					+ (m2 * l1 * omega1 * omega1 * Math.sin(theta1 - theta2)) - (m2
					* g * Math.sin(theta2)))
					/ (m2 * l2);

			// Zu grosse Zahl
			if (alpha2 > Double.MAX_VALUE) {
				throw new Exception(
						"Die Funktion f2(theta1,theta2,omega1,omega2) hat eine zu grosse Zahl ausgegeben");
			}

			return alpha2;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0.0;
		}
	}

}
