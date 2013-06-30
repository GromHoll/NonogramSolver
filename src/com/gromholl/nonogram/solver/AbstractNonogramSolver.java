package com.gromholl.nonogram.solver;

import java.util.Vector;

import javax.swing.JPanel;

import com.gromholl.nonogram.entity.NonogramProblem;
import com.gromholl.nonogram.entity.NonogramSolution;

public class AbstractNonogramSolver {	
	protected JPanel progressPanel;
	
	protected long startTime;
	protected long finishTime;
	
	protected NonogramProblem problem;	
	protected Vector<NonogramSolution> solutions;

	public AbstractNonogramSolver() {
		progressPanel = new JPanel();
		
		problem = null;
		solutions = new Vector<NonogramSolution>();
	}
	public AbstractNonogramSolver(NonogramProblem newProblem) {
		this();
		setProblem(newProblem);
	}
	
	public JPanel getProgressPanel() {
		return progressPanel;
	}	
	public Vector<NonogramSolution> getSolution() {
		return solutions;
	}
	public long getSolutionTime() {
		return finishTime - startTime;
	}
	
	public void setProblem(NonogramProblem newProblem) { 		
		problem = newProblem;
		solutions.removeAllElements();
		startTime = finishTime = 0;
	}
	
	public void solve() {
		System.out.println("LOLOL");
	}
	
}
