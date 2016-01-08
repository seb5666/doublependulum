import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel implements Runnable { 

	private double initial_conditions[]; 
	private double data[][];

	public Excel(double[] initial, double[][] data_in) {

		initial_conditions = initial;
		data = data_in;
	}
	
	@Override
	public void run() {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();

		DecimalFormat df = new DecimalFormat("#.####");

		// Neues Excel-Dokument herstellen
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Doppelpendel");

		// Titel ausgeben
		Row titel_row = sheet1.createRow(0);
		Cell titel = titel_row.createCell(0);
		titel.setCellValue("Doppelpendel/Runge-kutta");

		// Style für Titel
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setFontName("Comic Sans MS");
		font.setFontHeightInPoints((short) 18);
		style.setFont(font);
		titel.setCellStyle(style);

		// Anfangsbedingungen ausgeben
		Cell datum = titel_row.createCell(5);
		datum.setCellValue(dateFormat.format(date));

		Row masse_row = sheet1.createRow(2);
		Cell m1 = masse_row.createCell(0);
		m1.setCellValue("Masse 1:");
		Cell masse1 = masse_row.createCell(1);
		masse1.setCellValue(df.format(initial_conditions[0]));
		Cell m2 = masse_row.createCell(2);
		m2.setCellValue("Masse 2:");
		Cell masse2 = masse_row.createCell(3);
		masse2.setCellValue(df.format(initial_conditions[2]));

		Row langen_row = sheet1.createRow(3);
		Cell l1 = langen_row.createCell(0);
		l1.setCellValue("Länge 1:");
		Cell lange1 = langen_row.createCell(1);
		lange1.setCellValue(df.format(initial_conditions[1]));
		Cell l2 = langen_row.createCell(2);
		l2.setCellValue("Länge 2:");
		Cell lange2 = langen_row.createCell(3);
		lange2.setCellValue(df.format(initial_conditions[3]));

		Row theta_row = sheet1.createRow(4);
		Cell t1 = theta_row.createCell(0);
		t1.setCellValue("Theta 1:");
		Cell theta1 = theta_row.createCell(1);
		theta1.setCellValue(df.format(data[1][0]));
		Cell t2 = theta_row.createCell(2);
		t2.setCellValue("Theta 2:");
		Cell theta2 = theta_row.createCell(3);
		theta2.setCellValue(df.format(data[2][0]));

		Row omega_row = sheet1.createRow(5);
		Cell o1 = omega_row.createCell(0);
		o1.setCellValue("Omega 1:");
		Cell omega1 = omega_row.createCell(1);
		omega1.setCellValue(df.format(data[3][0]));
		Cell o2 = omega_row.createCell(2);
		o2.setCellValue("Omega 2:");
		Cell omega2 = omega_row.createCell(3);
		omega2.setCellValue(df.format(data[4][0]));

		// Info für daten ausgeben
		Row header_row = sheet1.createRow(7);
		Cell cell0 = header_row.createCell(0);
		cell0.setCellValue("t");
		Cell cell1 = header_row.createCell(1);
		cell1.setCellValue("theta1");
		Cell cell2 = header_row.createCell(2);
		cell2.setCellValue("theta2");
		Cell cell3 = header_row.createCell(3);
		cell3.setCellValue("omega1");
		Cell cell4 = header_row.createCell(4);
		cell4.setCellValue("omega2");

		// Info für daten stylen
		CellStyle header_style = workbook.createCellStyle();
		header_style.setBorderBottom(CellStyle.BORDER_THICK);
		header_style.setAlignment(CellStyle.ALIGN_CENTER);

		Font header_font = workbook.createFont();
		header_font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		header_font.setFontName("Arial");

		header_style.setFont(header_font);

		for (Cell cell : header_row) {
			cell.setCellStyle(header_style);
		}

		// Lösungen des Algorithmus ausgeben
		int data_length = data[0].length;
		CellStyle right_border = workbook.createCellStyle();
		right_border.setBorderRight(CellStyle.BORDER_THIN);

		for (int i = 0; i < data_length; i++) {

			Row current_row = sheet1.createRow(i + 8);

			Cell t = current_row.createCell(0);
			t.setCellValue(data[0][i]);
			t.setCellStyle(right_border);

			Cell tet1 = current_row.createCell(1);
			tet1.setCellValue(data[1][i]);
			tet1.setCellStyle(right_border);

			Cell tet2 = current_row.createCell(2);
			tet2.setCellValue(data[2][i]);
			tet2.setCellStyle(right_border);

			Cell ome1 = current_row.createCell(3);
			ome1.setCellValue(data[3][i]);
			ome1.setCellStyle(right_border);

			Cell ome2 = current_row.createCell(4);
			ome2.setCellValue(data[4][i]);

		}

		try {
			URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
			File f = new File(url.toURI().getPath());
			String path1 = f.getParentFile().getPath()+"/spreadsheet.xls";

			f = new File(path1);
			FileOutputStream output = new FileOutputStream(f);
			
			workbook.write(output);

			output.close();

			Desktop dt = Desktop.getDesktop();
			dt.open(f);

			System.out.println("Ok");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
