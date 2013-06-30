package com.gromholl.nonogram.solver;

import java.util.ArrayList;
import java.util.Arrays;

import com.gromholl.nonogram.entity.NonogramProblem;
import com.gromholl.nonogram.entity.NonogramSolution;

public class FiniteStateSolver extends AbstractNonogramSolver {

	private NonogramSolution solutionTemp;
	private int xSizeTemp;
	private int ySizeTemp;
	
	ArrayList<Node> machine;
	char[][] machineField;
	char[] machineLine;
	
	public static final char BLACK = NonogramSolution.BLACK_CELL;
	public static final char WHITE = NonogramSolution.WHITE_CELL;
	public static final char UNKNOWN = NonogramSolution.UNKNOWN_CELL;
	public static final char BOTH = '+';
		
	public FiniteStateSolver(NonogramProblem newProblem) {
		super(newProblem);
		solutionTemp = new NonogramSolution(newProblem.getWidth(),
											newProblem.getHeight());
		xSizeTemp = problem.getWidth();
		ySizeTemp = problem.getHeight();
	}
	
	public void solve() {
		System.out.println("Finite-State start");		

		NonogramSolution newSol = (NonogramSolution) solutionTemp.clone();
		
		while(true) {			
			for(int i = 0; i < ySizeTemp; i++)
				newSol.setRow(i, solveLine(newSol.getRow(i), problem.getRow(i)));

			for(int i = 0; i < xSizeTemp; i++)
				newSol.setColumn(i, solveLine(newSol.getColumn(i), problem.getColumn(i)));
			
			if(newSol.equals(solutionTemp)) {
				solutionTemp = newSol;
				break;
			}
			solutionTemp = (NonogramSolution) newSol.clone();
			if(problem.isSolution(solutionTemp))
				break;
		}
		
		if(problem.isSolution(solutionTemp)) {
			solutions.add( (NonogramSolution) solutionTemp.clone());
		}		
	}
	
	private char[] solveLine(char[] line, ArrayList<Integer> numbers) {
		machineLine = line.clone();
		machine = new ArrayList<Node>();
		
		for(int i = 0; i < numbers.size(); i++) {
			machine.add(new LoopNode());
			for(int j = 0; j < numbers.get(i)-1; j++) {
				machine.add(new BlackNode());
			}
			if(i == numbers.size() - 1) {
				machine.add(new LoopNode());
			} else {
				machine.add(new WhiteNode());
			}
		}
		
		machineField = new char[machine.size()][];
		for(int i = 0; i < machine.size(); i++) {
			machineField[i] = new char[machineLine.length];
			Arrays.fill(machineField[i], UNKNOWN);
		}
		
		step(0, -1);

		char[] res = line.clone();
		
		boolean b;
		boolean w;
		
		for(int i = 0; i < res.length; i++) {
			if(res[i] == UNKNOWN) {
				b = false;	w = false;
				
				for(int j = 0; j < machine.size(); j++) {
					switch(machineField[j][i]) {
					case UNKNOWN:
						break;
					case BLACK:
						b = true;
						break;
					case WHITE:
						w = true;
						break;
					case BOTH:
						b = true;
						w = true;
						break;
					}
				}
				
				if(b && !w)		res[i] = BLACK;
				if(!b && w)		res[i] = WHITE;
			}
		}
		
		return res;
	}
	
	boolean step(int nodePos, int linePos) {
		Node node = machine.get(nodePos);
		
		if(machineLine.length == linePos+1) {
			return nodePos==machine.size()-1;
		}
		
		boolean bool;
		
		if(node.isLoop()) {
			if(nodePos == machine.size() - 1) {
				if(machineLine[linePos+1] != NonogramSolution.BLACK_CELL) {
					setFieldChar(nodePos, linePos+1, false);
					if(!step(nodePos, linePos + 1)) {
						machineField[nodePos][linePos+1] = UNKNOWN;
					} else {
						return true;
					}						
				}
				return false;
			} else {				
				if(machineLine[linePos+1] == NonogramSolution.UNKNOWN_CELL) {
					bool = false;
					setFieldChar(nodePos, linePos+1, false);
					if(!step(nodePos, linePos + 1)) {
						machineField[nodePos][linePos+1] = UNKNOWN;
					} else {
						bool = true;
					}
					setFieldChar(nodePos+1, linePos+1, true);
					if(!step(nodePos + 1, linePos + 1)) {
						machineField[nodePos+1][linePos+1] = UNKNOWN;
					} else {
						bool = true;
					}
					return bool;
				} else {
					if(machineLine[linePos+1] == NonogramSolution.WHITE_CELL) {
						setFieldChar(nodePos, linePos+1, false);
						if(!step(nodePos, linePos + 1)) {
							machineField[nodePos][linePos+1] = UNKNOWN;
							return false;
						} else {
							return true;
						}
					} else {
						setFieldChar(nodePos+1, linePos+1, true);
						if(!step(nodePos + 1, linePos + 1)) {
							machineField[nodePos+1][linePos+1] = UNKNOWN;
							return false;
						} else {
							return true;
						}
					}
				}
			}
		} else {
			if(node.isBlack()) {
				if(machineLine[linePos+1] != NonogramSolution.WHITE_CELL) {
					setFieldChar(nodePos+1, linePos+1, true);
					if(!step(nodePos + 1, linePos + 1)) {
						machineField[nodePos+1][linePos+1] = UNKNOWN;
					} else {
						return true;
					}
				}
				return false;
			} else {
				if(machineLine[linePos+1] != NonogramSolution.BLACK_CELL) {
					setFieldChar(nodePos+1, linePos+1, false);
					if(!step(nodePos + 1, linePos + 1)) {
						machineField[nodePos+1][linePos+1] = UNKNOWN;
					} else {
						return true;
					}
				}
				return false;
			}
		}
	}
	
	void setFieldChar(int nodePos, int linePos, boolean isBlack) {
		if(machineField[nodePos][linePos] == UNKNOWN) {
			if(isBlack)
				machineField[nodePos][linePos] = BLACK;
			else
				machineField[nodePos][linePos] = WHITE;
		} else {
			if( (machineField[nodePos][linePos] == BLACK && !isBlack) ||
				(machineField[nodePos][linePos] == WHITE && isBlack))
				machineField[nodePos][linePos] = BOTH;			
		}
	}
}

abstract class Node {
	abstract public boolean isLoop();
	abstract public boolean isBlack();
}

class BlackNode extends Node {
	@Override
	public boolean isLoop() {
		return false;
	}
	@Override
	public boolean isBlack() {
		return true;
	}
	public String toString() {
		return "Black node";
	}
}

class WhiteNode extends Node {
	@Override
	public boolean isLoop() {
		return false;
	}
	@Override
	public boolean isBlack() {
		return false;
	}
	public String toString() {
		return "White node";
	}
}

class LoopNode extends Node {
	@Override
	public boolean isLoop() {
		return true;
	}
	@Override
	public boolean isBlack() {
		return true;
	}
	public String toString() {
		return "Loop node";
	}
}
