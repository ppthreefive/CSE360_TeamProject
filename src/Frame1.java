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
import java.util.Scanner;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

public class Frame1 {

	private JFrame frmGradeAnalytics;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField txtEnterGrade;
	private ArrayList<Float> grades = new ArrayList<Float>(); 

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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(){
		frmGradeAnalytics = new JFrame();
		frmGradeAnalytics.setBackground(Color.BLUE);
		frmGradeAnalytics.setFont(new Font("Dialog", Font.PLAIN, 14));
		frmGradeAnalytics.setTitle("Grade Analytics");
		frmGradeAnalytics.setBounds(100, 100, 450, 300);
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
		
		textField = new JTextField();
		textField.setBounds(182, 10, 86, 20);
		manData.add(textField);
		textField.setColumns(10);
		
		JLabel lblCurrentBounds = new JLabel("Current Bounds:    Low =  ; High =  ;");
		lblCurrentBounds.setBounds(93, 181, 215, 19);
		manData.add(lblCurrentBounds);
		
		JLabel lblSetHighBoundary = new JLabel("Set High Boundary:");
		lblSetHighBoundary.setBounds(48, 56, 124, 14);
		manData.add(lblSetHighBoundary);
		
		textField_1 = new JTextField();
		textField_1.setBounds(182, 53, 86, 20);
		manData.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnSet_1 = new JButton("Set Bounds");
		btnSet_1.setBounds(294, 30, 108, 23);
		manData.add(btnSet_1);
		
		JLabel lblAppendGradeTo = new JLabel("Append Grade (1 input at a time)");
		lblAppendGradeTo.setBounds(48, 94, 190, 28);
		manData.add(lblAppendGradeTo);
		
		txtEnterGrade = new JTextField();
		txtEnterGrade.setBounds(248, 98, 86, 20);
		manData.add(txtEnterGrade);
		txtEnterGrade.setColumns(10);
		
		JPanel analytics = new JPanel();
		tabbedPane.addTab("Analytics", null, analytics, null);
		
		JPanel displayData = new JPanel();
		tabbedPane.addTab("Display Data", null, displayData, null);
		
		JPanel displayGraph = new JPanel();
		tabbedPane.addTab("Display Graph", null, displayGraph, null);
		
		JPanel errorLog = new JPanel();
		tabbedPane.addTab("Error Log", null, errorLog, null);
		
		
	}
}
