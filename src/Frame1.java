


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Frame1 {
	private JFrame frmGradeAnalytics;
	private JTextField textField_lowBoundary;
	private JTextField textField_highBoundary;
	private JTextField txtEnterGrade;
	private ArrayList<Float> grades = new ArrayList<Float>();
	//private ArrayList<String> errors = new ArrayList<String>();
	private ArrayList<String> userActionLog = new ArrayList<String>();
	private JLabel boundariesInvalidNotification;
	private JLabel gradeAddedNotification;
	private Float low_boundary = (float) 0;
	private Float high_boundary = (float) 100;
	private JTextArea errors_log;
	private JTextArea gradesDisplay;
	private JTextField txtDeleteGrade;
	private JTextArea analyticsDisplay;
	private JTextArea graphDisplay;

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

	private void ExportToFile(String fileName, String content) {
		JFileChooser fileCh = new JFileChooser();
		fileCh.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileCh.showOpenDialog(frmGradeAnalytics);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File path = fileCh.getSelectedFile();

			File outputReport = null;
			try {
				outputReport = new File(path.toString() + File.separator + fileName);
				FileWriter reportWriter = new FileWriter(outputReport);
				reportWriter.write(content);
				// outputReport.close();
				System.out.println(fileName + "has been exported at " + path + File.separator + "report.txt");
				reportWriter.close();
			} catch (Exception e1) {
				String errorMes = "Exporting report file failed. Tried path: " + path.toString();
				AppendErrorMessage(errorMes);
			}
		}
	}

	/**
	 * This method allows us to dynamically update the data display tab whenever the
	 * dataset is changed.
	 * 
	 */
	private void UpdateDataDisplay() {
		int dataLength = grades.size();

		if (this.gradesDisplay.getText().length() > 1) {
			this.gradesDisplay.setText("");
		}

		// clear display when there is not a data
		if (dataLength == 0) {
			this.gradesDisplay.setText(
					"Please add data using the Data Manipulation tab. \nOr to add a data file select File->Open a File.");
		} else {
			ArrayList<Integer> columnLengthes = new ArrayList<Integer>();

			for (int index = 0; index < 4; index++) {
				columnLengthes.add(dataLength / 4);
			}

			for (int index = 0; index < dataLength % 4; index++) {
				columnLengthes.set(index, columnLengthes.get(index) + 1);
			}

			// grades.sort(new gradeComparetor());
			Collections.sort(grades);
			Collections.reverse(grades);

			ArrayList<ArrayList<Float>> columns = new ArrayList<ArrayList<Float>>();

			int index = 0;
			for (int i = 0; i < 4; i++) {
				// create an array list as a column
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

					if (rowIndex < column.size()) {
						gradesDisplay.append(column.get(rowIndex).toString() + "\t");
					}
				}

				gradesDisplay.append("\n");
			}
		}
	}

	/**
	 * This method allows us to dynamically update the analytics tab whenever the
	 * data set is changed.
	 * 
	 */
	private void updateAnalyticsDisplay() {
		// Declare our variables
		String output = "";

		int dataLength = grades.size();

		// Clears the text box if there is anything already in it
		if (analyticsDisplay.getText().length() > 1) {
			analyticsDisplay.setText("");
		}

		// If there is no data, then we will still print out a display
		if (dataLength == 0) {
			output = "";
			output += "Amount of grades in this dataset: " + "0" + "\n";
			output += "Mean: " + "0.0" + "\n";
			output += "Median: " + "0.0" + "\n";
			output += "Mode: " + "0.0" + "\n";
			output += "Highest grade: " + "0.0" + "\n";
			output += "Lowest grade: " + "0.0" + "\n";

			analyticsDisplay.setText(output);
		} else // Appends our analysis to the Analysis tab
		{
			output += "Amount of grades in this dataset: " + this.grades.size() + "\n";
			output += "Mean: " + getMean() + "\n";
			output += "Median: " + getMedian() + "\n";
			output += "Mode: " + getMode() + "\n";
			output += "Highest grade: " + getMaxGrade() + "\n";
			output += "Lowest grade: " + getMinGrade() + "\n";

			analyticsDisplay.append(output);
		}
	}

	/**
	 * Finds the most frequent number in our data set
	 * 
	 * @return float (mode)
	 */
	private float getMode() {
		float mode = 0.0f;
		int maxCount = 0;

		for (int indexOne = 0; indexOne < this.grades.size(); indexOne++) {
			int count = 0;

			for (int indexTwo = 0; indexTwo < this.grades.size(); indexTwo++) {
				if (this.grades.get(indexTwo) == this.grades.get(indexOne)) {
					count++;
				}
			}

			if (count > maxCount) {
				maxCount = count;
				mode = this.grades.get(indexOne);
			}
		}

		return mode;
	}

	/**
	 * This method sorts the grades ArrayList and then gets the median.
	 * 
	 * @return float (median)
	 */
	private float getMedian() {
		float median = 0.0f;
		Collections.sort(this.grades);

		if (this.grades.size() > 0) {
			int middle = (this.grades.size() / 2);
			median = this.grades.get(middle);
		}

		return median;
	}

	/**
	 * This calculates the average of all grades imported into the grades ArrayList
	 * 
	 * @return float (mean)
	 */
	private float getMean() {
		float mean = 0.0f;
		float sum = 0.0f;

		for (int index = 0; index < grades.size(); index++) {
			sum += this.grades.get(index);
		}

		if (this.grades.size() > 0) {
			mean = sum / this.grades.size();
		}

		return mean;
	}

	/**
	 * This calculates the highest grade in the grades ArrayList.
	 * 
	 * @return float (maximum)
	 */
	private float getMaxGrade() {
		float max = 0.0f;

		for (int index = 0; index < this.grades.size(); index++) {
			if (this.grades.get(index) > max) {
				max = this.grades.get(index);
			}
		}

		return max;
	}

	/**
	 * This calculates the lowest grade in the grades ArrayList.
	 * 
	 * @return float (minimum)
	 */
	private float getMinGrade() {
		float min = 0.0f;
		if (!(grades.isEmpty())) {
			min = grades.get(0);
		}

		for (int index = 0; index < this.grades.size(); index++) {
			if (this.grades.get(index) < min) {
				min = this.grades.get(index);
			}
		}

		return min;
	}

	/**
	 * This method allows the graph display tab to dynamically update the histogram
	 * based on the boundaries set by the user. It will always have an distribution
	 * of 10 different ranges.
	 * 
	 */
	private void updateGraphDisplay() {
		// Declare our variables here
		String output = "";
		float gradeRange = this.high_boundary - this.low_boundary;
		float incremental = gradeRange / 10;
		float sum = this.low_boundary;
		ArrayList<Integer> numbers = new ArrayList<Integer>();

		// Makes sure to clear the text box if there is anything in it already
		if (graphDisplay.getText().length() > 0) {
			graphDisplay.setText("");
			// graphString = ""; // Used for Report
		}

		// Pushes the lowest number into the ArrayList first
		numbers.add((int) sum);

		// Sets our increments and distribution range for our histogram
		for (int indexOne = 0; indexOne < 10; indexOne++) {
			sum += incremental;
			numbers.add((int) sum);
		}

		/*
		 * Here we do our comparisons to figure out where we print an 'X' character,
		 * meaning a number in our dataset falls within the range
		 */
		for (int index = 0; index < numbers.size(); index++) {
			if (index < numbers.size() - 1) {
				int count = 0;

				if (numbers.get(index + 1) != numbers.get(numbers.size() - 1)) {
					output += (Integer.toString(numbers.get(index)) + " to "
							+ Integer.toString(numbers.get(index + 1) - 1) + ":\t|");

					for (int indexTwo = 0; indexTwo < this.grades.size(); indexTwo++) {
						if (this.grades.get(indexTwo) >= numbers.get(index)
								&& this.grades.get(indexTwo) <= (numbers.get(index + 1) - 1)) {
							count++;
							output += "X";
						}
					}

					// This just makes it easier to see how many X's are in a range
					output += "    (" + Integer.toString(count) + ")";
				} else {
					output += (Integer.toString(numbers.get(index)) + " to " + Integer.toString(numbers.get(index + 1))
							+ ":\t|");

					for (int indexTwo = 0; indexTwo < this.grades.size(); indexTwo++) {
						if (this.grades.get(indexTwo) >= numbers.get(index)
								&& this.grades.get(indexTwo) <= (numbers.get(index + 1))) {
							count++;
							output += "X";
						}
					}

					// This just makes it easier to see how many X's are in a range
					output += "    (" + Integer.toString(count) + ")";
				}

				output += "\n";
			}
		}

		// Append our graph to the Graph Display tab
		graphDisplay.append(output);
		// graphString = output;
	}

	// add an error message to the log and the errors array list.
	private void AppendErrorMessage(String errorMes) {
		// errors.add(errorMes);
		// Adds new line here instead of in original message so there aren't any new
		// lines in error log array
		errors_log.append(errorMes + "\n");
	}

	private void AppendGradesFromFile() {
		JFileChooser fileCh = new JFileChooser("."); // current folder
		int returnVal = fileCh.showOpenDialog(frmGradeAnalytics);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileCh.getSelectedFile();
			Scanner sc;

			try {
				sc = new Scanner(selectedFile);
				
				
				//OLD WAY NO ERROR DETECTION
//				while (sc.hasNextFloat()) {
//					grades.add(sc.nextFloat());
//				}
				
				//File detection
				if(selectedFile.toString().contains(".txt")) {
					//.txt FILE Parse
					//SKIPS SPACES & INVALID NUMS
					while(sc.hasNextLine()) {
						String input = sc.nextLine();
						try {
							Float x = Float.valueOf(input.trim()).floatValue();
							if(x >= low_boundary && x <= high_boundary) {
								grades.add(x);
							}
							else {
								AppendErrorMessage("Error adding value: " + x + " it is out of bounds.");
							}
						}
						catch(Exception e) {
							AppendErrorMessage("Error adding \"" + input + "\" from imported file.");
						}
					}
				} 
				else if(selectedFile.toString().contains(".csv")) {
					//.CSV FILE PARSE
					String inputLine = "";
					while(sc.hasNextLine()) {
						inputLine += sc.nextLine() + "\n";
					}
					inputLine = inputLine.replaceAll(",", "\n");//Changes commas to newlines
					inputLine = inputLine.replaceAll(" ", "");//Gets rid of all spaces
					//Process file string
					String temp = "";
					for(int i = 0; i < inputLine.length(); i++) {
						if(!(inputLine.charAt(i) == '\n')) {
							temp += inputLine.charAt(i);
						} 
						else if(inputLine.charAt(i) == '\n') {
							try{
								Float x = Float.valueOf(temp.trim()).floatValue();
								if(x >= low_boundary && x <= high_boundary) {
									grades.add(x);
								}
								else {
									AppendErrorMessage("Error adding value: " + x + " it is out of bounds.");
								}
								temp = "";
							}
							catch(Exception e){
								if(!(temp.isEmpty())) {
									AppendErrorMessage("Error adding \"" + temp + "\" from imported file.");
									temp = "";
								}

							}
						}
					}
					
				}
				else {
					AppendErrorMessage("Error adding file! (Unsupported file type)");
					userActionLog.add("User failed to append data from a file.");
				}	

				userActionLog.add("User successfully appended grades from a file.");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				userActionLog.add("User failed to append data from a file.");
			}

			UpdateDataDisplay();
			updateAnalyticsDisplay();
			updateGraphDisplay();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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

		JMenuItem mntmOpenAFile = new JMenuItem("Open File...");

		mntmOpenAFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				grades.clear();
				AppendGradesFromFile();
			}
		});

		mnFile.add(mntmOpenAFile);

		JMenuItem mntmCloseProgram = new JMenuItem("Close Program");

		mntmCloseProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JMenuItem mntmAppendFile = new JMenuItem("Append Grades From File...");
		mntmAppendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppendGradesFromFile();
			}
		});
		mnFile.add(mntmAppendFile);

		mnFile.add(mntmCloseProgram);

		JMenu mnExport = new JMenu("Export");
		menuBar.add(mnExport);

		JMenuItem mntmExportGradesReport = new JMenuItem("Export Grades Report");
		mntmExportGradesReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

				if(userActionLog.isEmpty()) {
					ExportToFile("Grades Report.txt", "Report: " + "\n\nAnalytics:\n\n"
							+ analyticsDisplay.getText() + "\n\nGraph:\n\n" + graphDisplay.getText());
				}
				else {
					String userActions = "";
					for(int i = 0; i < userActionLog.size(); i++) {
						userActions += userActionLog.get(i) + "\n";
					}
					ExportToFile("Grades Report.txt", "Report: " + "\n\nAnalytics:\n\n"
							+ analyticsDisplay.getText() + "\n\nGraph:\n\n" + graphDisplay.getText() + "\n\nUser Actions:\n\n" + userActions);
				}
				

			}
		});
		mnExport.add(mntmExportGradesReport);

		JMenuItem mntmExportErrorLog = new JMenuItem("Export Error Log");
		mntmExportErrorLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExportToFile("Errors Log.txt", "Errors: \n\n" + errors_log.getText());
			}
		});
		mnExport.add(mntmExportErrorLog);

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

		JLabel lblCurrentBounds = new JLabel(
				"Current Bounds:    Low = " + low_boundary + " ; High = " + high_boundary + " ;");
		lblCurrentBounds.setBounds(48, 243, 404, 19);
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
					float high_boundary_local = Float.parseFloat(highBoundary_str);
					float low_boundary_local = Float.parseFloat(lowBoundary_str);
					if (low_boundary_local > high_boundary_local) {
						throw new Exception();
					} else {
						high_boundary = high_boundary_local;
						low_boundary = low_boundary_local;
					}

					lblCurrentBounds
							.setText("Current Bounds:    Low = " + low_boundary + " ; High = " + high_boundary + " ;");

					userActionLog
							.add("User Manually Set Bounds. (Low: " + low_boundary + ", High:" + high_boundary + ")");

					boundariesInvalidNotification.setText("");
					updateGraphDisplay();
				} catch (Exception exception) {
					// do something here to handle the non-number input

					String error_mes = "input values are not all numbers, input(low boundary and high boundary): "
							+ textField_lowBoundary.getText() + ", " + textField_highBoundary.getText();
					boundariesInvalidNotification.setText("Invalid new boundaries");
					boundariesInvalidNotification.setForeground(Color.RED);
					// adding error to the log and the errors array list
					AppendErrorMessage(error_mes);
					// for (String a : errors)
					// System.out.println(a);
				}
				textField_highBoundary.setText("");
				textField_lowBoundary.setText("");
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
					txtEnterGrade.setText("");
					// Checks if the grade is in bounds
					if ((newGrade >= low_boundary) && (newGrade <= high_boundary)) {
						gradeAddedNotification.setText("Grade added!");
						grades.add(newGrade);

						// set notification to be color for success appending
						gradeAddedNotification.setForeground(new Color(2, 200, 2));

						// Add Success to log
						userActionLog.add("User successfully added new grade to data set. New Grade: " + newGrade);

						UpdateDataDisplay();
						updateAnalyticsDisplay();
						updateGraphDisplay();
					} else {
						userActionLog.add("User attempted to add " + newGrade + " to the data set.");
						String error_mes = "appended grade is not in bounds! Grade Entered: " + newGrade
								+ ", Boundary Range: [" + low_boundary + "," + high_boundary + "]";
						// adding error to the log and the errors array list
						AppendErrorMessage(error_mes);
						gradeAddedNotification.setText("Out of bounds!");

						// set notification to be red for success appending
						gradeAddedNotification.setForeground(Color.red);
					}
				} catch (Exception exception) {
					userActionLog.add("User attempted to add " + txtEnterGrade.getText() + " to the data set.");

					String error_mes = "appended grade is not number, it is: " + txtEnterGrade.getText();
					// adding error to the log and the errors array list
					AppendErrorMessage(error_mes);
					gradeAddedNotification.setText("Appending failed!");

					// set notification to be red for success appending
					gradeAddedNotification.setForeground(Color.red);
					// for (String a : errors)
					// System.out.println(a);
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
					txtDeleteGrade.setText("");
					if (grades.contains(delGrade)) {
						grades.remove(delGrade);
						gradeDeletedNotification.setText("Grade deleted!");
						gradeDeletedNotification.setForeground(new Color(2, 200, 2));

						// Add Success to log
						userActionLog
								.add("User successfully removed a grade from the data set. Grade Removed: " + delGrade);

						UpdateDataDisplay();
						updateAnalyticsDisplay();
						updateGraphDisplay();
					} else {
						error_mes = "grade: " + delGrade + " attempted to delete is not found in data set";
						AppendErrorMessage(error_mes);
						gradeDeletedNotification.setText("Grade not found!");
						gradeDeletedNotification.setForeground(Color.red);
					}
				} catch (Exception exception) {
					error_mes = "grade attempted to delete is not number, it is: " + txtDeleteGrade.getText();
					// adding error to the log and the errors array list
					AppendErrorMessage(error_mes);
					gradeDeletedNotification.setText("Deletion failed!");
					gradeDeletedNotification.setForeground(Color.red);
					// for (String a : errors)
					// System.out.println(a);
				}
			}
		});

		btnDelete.setBounds(245, 210, 89, 23);
		manData.add(btnDelete);

		boundariesInvalidNotification = new JLabel("");

		boundariesInvalidNotification.setBounds(294, 60, 181, 13);
		manData.add(boundariesInvalidNotification);

		/* This is the analytics tab */
		JPanel analytics = new JPanel();
		tabbedPane.addTab("Analytics", null, analytics, null);
		analytics.setLayout(null);
		JLabel lblAnalysis = new JLabel("Analysis:");
		lblAnalysis.setBounds(10, 11, 60, 14);
		analytics.add(lblAnalysis);
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 36, 467, 252);
		analyticsDisplay = new JTextArea();
		analyticsDisplay.setEditable(false);
		scrollPane_2.setViewportView(analyticsDisplay);
		analytics.add(scrollPane_2);
		updateAnalyticsDisplay(); // UPDATES ON CREATION

		/* This is the display data tab */
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
		gradesDisplay.setEditable(false);
		scrollPane_1.setViewportView(gradesDisplay);
		UpdateDataDisplay(); // UPDATES ON CREATION

		/* This is the display graph tab */
		JPanel displayGraph = new JPanel();
		tabbedPane.addTab("Display Graph", null, displayGraph, null);
		displayGraph.setLayout(null);
		JLabel lblGraph = new JLabel("Graph:");
		lblGraph.setBounds(10, 11, 60, 14);
		displayGraph.add(lblGraph);
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 36, 467, 252);
		graphDisplay = new JTextArea();
		graphDisplay.setEditable(false);
		scrollPane_3.setViewportView(graphDisplay);
		displayGraph.add(scrollPane_3);

		/* This is the error log tab */
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
		errors_log.setEditable(false);
		// for (int i = 0; i < 1000; i++)
		// gradesDisplay.append(i + "\n");
	}
}

