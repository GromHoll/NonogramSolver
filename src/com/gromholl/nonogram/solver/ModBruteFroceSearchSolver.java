package com.gromholl.nonogram.solver;

import com.gromholl.nonogram.entity.NonogramProblem;
import com.gromholl.nonogram.entity.NonogramSolution;

public class ModBruteFroceSearchSolver extends AbstractNonogramSolver {

	private NonogramSolution solutionTemp;
	private int xSizeTemp;
	private int ySizeTemp;
		
	public ModBruteFroceSearchSolver(NonogramProblem newProblem) {
		super(newProblem);
		solutionTemp = new NonogramSolution(newProblem.getWidth(),
											newProblem.getHeight());
		
		xSizeTemp = problem.getWidth();
		ySizeTemp = problem.getHeight();
	}
	
	public void solve() {
		System.out.println("Mod Brute-Froce start");	
		
		int black = problem.getBlackCellsNum();
		
		solutionTemp.fillWhite();
		
		if(black > 0)
			recursiveStep(0, 0, black);
		else
			solutions.add( (NonogramSolution) solutionTemp.clone());
		
	}
	
	private void recursiveStep(int x, int y, int black) {
			
		int newX = x + 1;
		int newY = y;
		if(newX == xSizeTemp) {
			newX = 0;
			newY++;
		}
		
		if(x == 0 && y != 0) {
			if(!problem.isCorrectRow(solutionTemp, y - 1)) {
				return;
			}
		}
		
		//
		// Продолжение аналогично функции представленной в предыдущем методе
		//
		
		solutionTemp.setBlackCell(x, y);
		
		if(black - 1 > 0) {
			recursiveStep(newX, newY, black - 1);
		} else {
			if(problem.isSolution(solutionTemp)) {
				solutions.add( (NonogramSolution) solutionTemp.clone());
			}
		}
		
		solutionTemp.setWhiteCell(x, y);
		
		if(xSizeTemp*ySizeTemp - (xSizeTemp*y + x + 1) >= black) {
			recursiveStep(newX, newY, black);
		}
		
	}
}
