package com.gromholl.nonogram;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.gromholl.nonogram.entity.NonogramProblem;
import com.gromholl.nonogram.entity.NonogramSolution;
import com.gromholl.nonogram.solver.AbstractNonogramSolver;
import com.gromholl.nonogram.solver.BruteForceSearchSolver;
import com.gromholl.nonogram.solver.FiniteStateSolver;
import com.gromholl.nonogram.solver.HeuristicSolver;
import com.gromholl.nonogram.solver.ModBruteFroceSearchSolver;

public class ClientFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private NonogramProblem problem;
	private AbstractNonogramSolver solver;

	private JTextField fileNameTextField;
	private JButton loadProblemButton;
	private JComboBox<String> methodComboBox;
	private JButton solveButton;
	
	public final int BRUTE_FORCE_OPTION = 0;
	public final int MOD_BRUTE_FORCE_OPTION = 1;
	public final int HEURISTIC_OPTION = 2;
	public final int FINITE_STATE_OPTION = 3;
	
	
	public ClientFrame() {
		problem = new NonogramProblem();
		
		fileNameTextField = new JTextField();
		loadProblemButton = new JButton("Load problem");		
		solveButton = new JButton("Solve");
		
		getMethodComboBox();
		
		init();
	}

	private void init() {
    	GridBagConstraints c = new GridBagConstraints();
    	GridBagLayout gbl = new GridBagLayout();
    	this.setLayout(gbl);
    	c.anchor = GridBagConstraints.NORTHWEST;
    	c.fill   = GridBagConstraints.HORIZONTAL;
    	c.gridheight = 1;
    	c.gridwidth = 1;
    	c.gridx = 0;
    	c.gridy = GridBagConstraints.RELATIVE;
    	c.insets = new Insets(2, 5, 2, 5);
    	c.ipadx = 0;
    	c.ipady = 0;
    	c.weightx = 1.0;
    	c.weighty = 0.0;

    	gbl.setConstraints(loadProblemButton, c);
    	this.add(loadProblemButton);
    	loadProblemButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadProblem();
			}
		});
    	fileNameTextField.setEditable(false);
    	gbl.setConstraints(fileNameTextField, c);
    	this.add(fileNameTextField);
    	gbl.setConstraints(getMethodComboBox(), c);
    	this.add(getMethodComboBox());
    	gbl.setConstraints(solveButton, c);
    	this.add(solveButton);
        solveButton.addActionListener(new ActionListener() {			
            @Override
            public void actionPerformed(ActionEvent arg0) {
                solve();
            }
		});
        solveButton.setEnabled(false);
    	
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);    	
        this.pack();
        this.setSize(200, this.getHeight());
        this.setResizable(false);
        this.setVisible(true);
	}

	private JComboBox<String> getMethodComboBox() {
		if(methodComboBox == null) {
			methodComboBox = new JComboBox<String>();
			methodComboBox.addItem("Brute-force search");
			methodComboBox.addItem("Modificate Brute-force search");
			methodComboBox.addItem("Heuristic algorithm");
			methodComboBox.addItem("Finite-state machine algorithm");
			return methodComboBox;
		} else {
			return methodComboBox;
		}
	}
	
	private void loadProblem() {
		JFileChooser chooser = new JFileChooser();
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				problem.load(chooser.getSelectedFile());
				fileNameTextField.setText(problem.getName());
				solveButton.setEnabled(true);
			} catch(Exception exc) {
				fileNameTextField.setText("");
				solveButton.setEnabled(false);				
				JOptionPane.showMessageDialog(this, exc.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}		
	}

	private void solve() {
		switch(methodComboBox.getSelectedIndex()) {
			case BRUTE_FORCE_OPTION:
				solver = new BruteForceSearchSolver(problem);
				break;
			case MOD_BRUTE_FORCE_OPTION:
				solver = new ModBruteFroceSearchSolver(problem);
				break;
			case HEURISTIC_OPTION:
				solver = new HeuristicSolver(problem);
				break;
			case FINITE_STATE_OPTION:
				solver = new FiniteStateSolver(problem);
				break;
			default:
				return;
		}
		
		//solver.solve();
		//showResult();
		
		Thread th = new Thread(new Runnable() {
        	public void run() {
        		solver.solve();
	      		showResult();        	
        	}
		});
		
		th.start();
	}
	
	private void showResult() {
		Vector<NonogramSolution> res = solver.getSolution();
		
		System.out.println("\nResults:");
		if(res.size() != 0) {
			for(int i = 0; i < res.size(); i++) {
				System.out.println("solution n" + (i+1));
				System.out.println(res.get(i));
				System.out.println();
			}
		} else {
			System.out.println("Solution not found.");
		}
	}
	
}
