import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Frame1 {

	private JFrame frmGradeAnalytics;
	private JTextField textField_lowBoundary;
	private JTextField textField_highBoundary;
	private JTextField txtEnterGrade;
	private ArrayList<Float> grades = new ArrayList<Float>(); 
	private ArrayList<String> errors = new ArrayList<String>();
	private JLabel gradeAddedNotification;
	private Float low_boundary = (float) 0;
	private Float high_boundary = (float) 100;
	private JTextArea errors_log;
	private JTextArea gradesDisplay;
	private JTextField txtDeleteGrade;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frmGradeAnalytics.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	// update data display with the latest grades array list 
	private void UpdateDataDisplay () {
		class gradeComparetor implements Comparator<Float>{
			@Override
			public int compare(Float o1, Float o2) {
				// TODO Auto-generated method stub
				return (o1 < o2) ? 1 : 0;
			}
		}
		
		int dataLength = grades.size();
		// clear display when there is not a data
		if (dataLength == 0) {
			gradesDisplay.setText(null);
		} else {
			ArrayList<Integer> columnLengthes = new ArrayList<Integer>();
			for (int index = 0; index < 4; index++)
				columnLengthes.add(dataLength/4);
			
			for (int index = 0; index < dataLength % 4; index++)
				columnLengthes.set(index, columnLengthes.get(index) + 1);
			//grades.sort(new gradeComparetor());
			Collections.sort(grades);
			Collections.reverse(grades);
			ArrayList<ArrayList<Float>> columns = new ArrayList<ArrayList<Float>>();
			
			int index = 0;
			for (int i = 0; i < 4; i++) {
				//create an array list as a column
				ArrayList<Float> column = new ArrayList<Float>();
				
				// append data to column
				for (int j = 0; j < columnLengthes.get(i) && index < dataLength; j++) {
					column.add(grades.get(index));
					index++;
				}
				columns.add(column);
			}
			
			for (int rowIndex = 0; rowIndex < columnLengthes.get(0); rowIndex++) {
				for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
					ArrayList<Float> column = columns.get(columnIndex);
					if (rowIndex < column.size())
						gradesDisplay.append(column.get(rowIndex).toString() + "\t");
				}
				gradesDisplay.append("\n");
			}
		}
		
		
	}
	
	// add an error message to the log and the errors array list.
	private void AppendErrorMessage(String errorMes) {
		errors.add(errorMes);
		errors_log.append(errorMes);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(){
		frmGradeAnalytics = new JFrame();
		frmGradeAnalytics.setBackground(Color.BLUE);
		frmGradeAnalytics.setFont(new Font("Dialog", Font.PLAIN, 14));
		frmGradeAnalytics.setTitle("Grade Analytics");
		frmGradeAnalytics.setBounds(100, 100, 504, 384);
		frmGradeAnalytics.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmGradeAnalytics.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenAFile = new JMenuItem("Open a File...");
		mntmOpenAFile.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e){
		         JFileChooser fileCh = new JFileChooser("."); //current folder
				 int returnVal = fileCh.showOpenDialog(frmGradeAnalytics);
				 if (returnVal == JFileChooser.APPROVE_OPTION) {
	                    File selectedFile = fileCh.getSelectedFile();
	                    Scanner sc;
						try {
							sc = new Scanner(selectedFile);
		                    while(sc.hasNextFloat()) {
		                    	grades.add(sc.nextFloat());
		                    }
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                    UpdateDataDisplay();
	                   //  JOptionPane.showMessageDialog(frmGradeAnalytics, sb.toString());
	                }
			}
		});
		mnFile.add(mntmOpenAFile);
		
		JMenuItem mntmCloseProgram = new JMenuItem("Close Program");
		mntmCloseProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmCloseProgram);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmGradeAnalytics.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel manData = new JPanel();
		tabbedPane.addTab("Data Manipulation", null, manData, null);
		manData.setLayout(null);
		
		JLabel lblSetLowBoundary = new JLabel("Set Low Boundary:");
		lblSetLowBoundary.setBounds(48, 11, 124, 19);
		manData.add(lblSetLowBoundary);
		
		textField_lowBoundary = new JTextField();
		textField_lowBoundary.setBounds(182, 10, 86, 20);
		manData.add(textField_lowBoundary);
		textField_lowBoundary.setColumns(10);
		
		JLabel lblCurrentBounds = new JLabel("Current Bounds:    Low = " + low_boundary + " ; High = " + high_boundary + " ;");
		lblCurrentBounds.setBounds(94, 265, 286, 19);
		manData.add(lblCurrentBounds);
		
		JLabel lblSetHighBoundary = new JLabel("Set High Boundary:");
		lblSetHighBoundary.setBounds(48, 56, 124, 14);
		manData.add(lblSetHighBoundary);
		
		textField_highBoundary = new JTextField();
		textField_highBoundary.setBounds(182, 53, 86, 20);
		manData.add(textField_highBoundary);
		textField_highBoundary.setColumns(10);
		
		JButton btnSet_1 = new JButton("Set Bounds");
		btnSet_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String highBoundary_str = textField_highBoundary.getText();
					String lowBoundary_str = textField_lowBoundary.getText();
					high_boundary = Float.parseFloat(highBoundary_str);
					low_boundary = Float.parseFloat(lowBoundary_str);
					lblCurrentBounds.setText("Current Bounds:    Low = " + low_boundary + " ; High = " + high_boundary + " ;");
				} catch(Exception exception) {
					//do something here to handle the non-number input
					String error_mes = "input values are not all numbers, input(low boundary and high boundar): " + textField_highBoundary.getText() + ", " + textField_lowBoundary.getText() + "\n";
					
					// adding error to the log and the errors array list
					AppendErrorMessage(error_mes);
					//for (String a : errors)
					//	System.out.println(a);
				}
				
			}
		});
		btnSet_1.setBounds(294, 30, 108, 23);
		manData.add(btnSet_1);
		
		JLabel lblAppendGradeTo = new JLabel("Append Grade (1 input at a time):");
		lblAppendGradeTo.setBounds(48, 94, 190, 28);
		manData.add(lblAppendGradeTo);
		
		txtEnterGrade = new JTextField();
		txtEnterGrade.setBounds(248, 98, 86, 20);
		manData.add(txtEnterGrade);
		txtEnterGrade.setColumns(10);
		
		JButton btnAppend = new JButton("Append");
		btnAppend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Float newGrade;
				try {
					newGrade = Float.parseFloat(txtEnterGrade.getText());
					gradeAddedNotification.setText("Grade added!");
					grades.add(newGrade);
					
					// set notification to be color for success appending
					gradeAddedNotification.setForeground(new Color(2, 200, 2));
				} catch (Exception exception) {
					String error_mes = "appended grade is not number, it is: " + txtEnterGrade.getText() + "\n";
					// adding error to the log and the errors array list
					AppendErrorMessage(error_mes);
					gradeAddedNotification.setText("Appending failed!");
					
					// set notification to be red for success appending
					gradeAddedNotification.setForeground(Color.red);
					//for (String a : errors)
					//	System.out.println(a);
				}
					
			}
		});
		btnAppend.setBounds(245, 129, 89, 23);
		manData.add(btnAppend);
		
		gradeAddedNotification = new JLabel("");
		gradeAddedNotification.setBounds(344, 101, 108, 14);
		manData.add(gradeAddedNotification);
		
		JLabel lblDeleteGrade = new JLabel("Delete Grade (1 occurence): ");
		lblDeleteGrade.setBounds(48, 182, 190, 14);
		manData.add(lblDeleteGrade);
		
		txtDeleteGrade = new JTextField();
		txtDeleteGrade.setBounds(248, 179, 86, 20);
		manData.add(txtDeleteGrade);
		txtDeleteGrade.setColumns(10);
		
		JLabel gradeDeletedNotification = new JLabel("");
		gradeDeletedNotification.setBounds(344, 182, 108, 14);
		manData.add(gradeDeletedNotification);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String error_mes;
				Float delGrade;
				try {
					delGrade = Float.parseFloat(txtDeleteGrade.getText());
					if(grades.contains(delGrade)) {
						grades.remove(delGrade);
						gradeDeletedNotification.setText("Grade deleted!");
						gradeDeletedNotification.setForeground(new Color(2, 200, 2));
						
					}
					else {
						error_mes = "grade: " + delGrade + " attempted to delete is not found in data set \n";
						AppendErrorMessage(error_mes);
						gradeDeletedNotification.setText("Grade not found!");
						gradeDeletedNotification.setForeground(Color.red);
					}
				} catch (Exception exception) {
					error_mes = "grade attempted to delete is not number, it is: " + txtDeleteGrade.getText() + "\n";
					// adding error to the log and the errors array list
					AppendErrorMessage(error_mes);
					gradeDeletedNotification.setText("Deletion failed!");
					gradeDeletedNotification.setForeground(Color.red);
					//for (String a : errors)
					//	System.out.println(a);
				}
			}
		});
		
		btnDelete.setBounds(245, 210, 89, 23);
		manData.add(btnDelete);
		
		
		
		JPanel analytics = new JPanel();
		tabbedPane.addTab("Analytics", null, analytics, null);
		
		JPanel displayData = new JPanel();
		tabbedPane.addTab("Display Data", null, displayData, null);
		displayData.setLayout(null);
		
		JLabel lblGrades = new JLabel("Grades:");
		lblGrades.setBounds(10, 11, 48, 14);
		displayData.add(lblGrades);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 36, 467, 252);
		displayData.add(scrollPane_1);
		
		gradesDisplay = new JTextArea();
		scrollPane_1.setViewportView(gradesDisplay);
		
		JPanel displayGraph = new JPanel();
		tabbedPane.addTab("Display Graph", null, displayGraph, null);
		
		JPanel errorLog = new JPanel();
		tabbedPane.addTab("Error Log", null, errorLog, null);
		errorLog.setLayout(null);
		
		JLabel lblErrorsLogs = new JLabel("Errors logs:");
		lblErrorsLogs.setBounds(10, 11, 89, 14);
		errorLog.add(lblErrorsLogs);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 467, 252);
		errorLog.add(scrollPane);
		
		errors_log = new JTextArea();
		scrollPane.setViewportView(errors_log);
		//for (int i = 0; i < 1000; i++)
		//	gradesDisplay.append(i + "\n");
	}
}
