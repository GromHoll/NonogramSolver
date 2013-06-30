package com.gromholl.nonogram.solver;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.gromholl.nonogram.entity.NonogramProblem;
import com.gromholl.nonogram.entity.NonogramSolution;

public class HeuristicSolver  extends AbstractNonogramSolver{

	private NonogramSolution solutionTemp;
	
	private int xSizeTemp;
	private int ySizeTemp;
	
	private boolean rowStatus[];
	private boolean columnStatus[];
		
	public HeuristicSolver(NonogramProblem newProblem) {
		super(newProblem);
		solutionTemp = new NonogramSolution(newProblem.getWidth(),
											newProblem.getHeight());
		
		xSizeTemp = problem.getWidth();
		ySizeTemp = problem.getHeight();
		
		rowStatus = new boolean[ySizeTemp];
		columnStatus = new boolean[xSizeTemp];
	}
	
	public void solve() {
		System.out.println("Heuristic start");	
		
		for(int i = 0; i < ySizeTemp; i++) {
			fillFullRow(i);
		}
		
		for(int j = 0; j < xSizeTemp; j++) {
			fillFullColumn(j);
		}
		
		char[] real;
		char[] left;
		char[] right;

		NonogramSolution newSol = (NonogramSolution) solutionTemp.clone();
				
		while(true) {		
			for(int i = 0; i < ySizeTemp; i++) {
				real = newSol.getRow(i);
				left = leftSolution(real, problem.getRow(i));
				right = rightSolution(real, problem.getRow(i));
			
				newSol.setRow(i, procedure1(real, left, right));
			}		
			for(int i = 0; i < xSizeTemp; i++) {
				real = newSol.getColumn(i);
				left = leftSolution(real, problem.getColumn(i));
				right = rightSolution(real, problem.getColumn(i));
				
				newSol.setColumn(i, procedure1(real, left, right));
			}
		
			for(int i = 0; i < ySizeTemp; i++) {
				real = newSol.getRow(i);				
				left = leftSolution(real, problem.getRow(i));
				right = rightSolution(real, problem.getRow(i));				
				
				newSol.setRow(i, procedure2(real, left, right));				
			}				
			for(int i = 0; i < xSizeTemp; i++) {
				real = newSol.getColumn(i);				
				left = leftSolution(real, problem.getColumn(i));
				right = rightSolution(real, problem.getColumn(i));
				
				newSol.setColumn(i, procedure2(real, left, right));
			}
	
			for(int i = 0; i < ySizeTemp; i++) {
				real = newSol.getRow(i);				
				newSol.setRow(i, procedure3(real, problem.getRow(i)));
			}		
			for(int i = 0; i < xSizeTemp; i++) {
				real = newSol.getColumn(i);				
				newSol.setColumn(i, procedure3(real, problem.getColumn(i)));		
			}
			
			for(int i = 0; i < ySizeTemp; i++) {
				real = newSol.getRow(i);				
				newSol.setRow(i, procedure4(real, problem.getRow(i)));
			}			
			for(int i = 0; i < xSizeTemp; i++) {
				real = newSol.getColumn(i);				
				newSol.setColumn(i, procedure4(real, problem.getColumn(i)));		
			}
			
			for(int i = 0; i < ySizeTemp; i++) {
				real = newSol.getRow(i);
				left = leftSolution(real, problem.getRow(i));
				right = rightSolution(real, problem.getRow(i));
				
				newSol.setRow(i, procedure5(real, left, right));
			}
			
			for(int i = 0; i < xSizeTemp; i++) {
				real = newSol.getColumn(i);
				left = leftSolution(real, problem.getColumn(i));
				right = rightSolution(real, problem.getColumn(i));
				
				newSol.setColumn(i, procedure5(real, left, right));		
			}

			if(newSol.equals(solutionTemp)) {
				solutionTemp = newSol;
				break;
			}
			solutionTemp = (NonogramSolution) newSol.clone();
			if(problem.isSolution(solutionTemp)) {
				break;
			}
		}
		
		if(problem.isSolution(solutionTemp)) {
			solutions.add( (NonogramSolution) solutionTemp.clone());
		}
	}

	private boolean isCorrecrLine(char[] line, char[] res) {
		for(int i = 0; i < line.length; i++) {
			if(line[i] != NonogramSolution.UNKNOWN_CELL) {
				if(line[i] == NonogramSolution.WHITE_CELL) {
					if(res[i] != NonogramSolution.WHITE_CELL) {
						return false;
					}
				} else {
					if(res[i] == NonogramSolution.WHITE_CELL) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private char[] leftSolution(char[] line, ArrayList<Integer> numbers) {	
				
		int space[] = new int[numbers.size() + 1];
		int sum = 0;
		for(int i : numbers) 
			sum += i;
		
		space[0] = 0;
		for(int i = 1; i < space.length-1; i++) {
			space[i] = 1;
		}
		space[space.length-1] = line.length - sum - numbers.size() + 1;
		
		char temp[] = new char[line.length];
		while(true) {
			int pos = 0;
			for(char i = 0; i < space.length - 1; i++) {
				for(int j = 0; j < space[i]; j++) {
					temp[pos] = NonogramSolution.WHITE_CELL;
					pos++;
				}
				for(int j = 0; j < numbers.get(i); j++) {
					temp[pos] = i;
					pos++;
				}
			}
			for(int j = 0; j < space[space.length-1]; j++) {
				temp[pos] = NonogramSolution.WHITE_CELL;
				pos++;
			}
			for(; pos < line.length; pos++) {
				temp[pos] = NonogramSolution.WHITE_CELL;
			}
			
			if(isCorrecrLine(line, temp)) {
				return temp;
			}
			
			for(int i = space.length-1; i >= 0; i--) {
				if(i == space.length-1) {
					if(space[i] != 0) {
						pos = i;
						break;
					}
				} else {
					if(space[i] != 1) {
						pos = i;
						break;
					}
				}
			}
			
			if(pos == 0) {
				break;
			} else {
				space[pos-1]++;
				for(int i = pos; i < space.length-1; i++) {
					space[i] = 1;
				}
				int spaceSum = 0;
				for(int i = 0; i < space.length-1; i++) {
					spaceSum += space[i];
				}
				space[space.length-1] = line.length - sum - spaceSum;
			}
		}
		System.out.println("fail");
		return null;
	}
	private char[] rightSolution(char[] line, ArrayList<Integer> numbers) {
		char[] lineReverse = line.clone();
		for(int i = 0; i < line.length/2; i++) {
			lineReverse[i] = line[line.length-1-i];
			lineReverse[line.length-1-i] = line[i];
		}
		
		ArrayList<Integer> numReverse = new ArrayList<Integer>();
		
		for(int i = numbers.size()-1; i >= 0; i--) {
			numReverse.add(numbers.get(i));
		}
		
		char res[] = leftSolution(lineReverse, numReverse);
		
		char temp; 
		for(int i = 0; i < res.length/2; i++) {
			temp = res[i];
			res[i] = res[res.length-1-i];
			res[res.length-1-i] = temp;
		}
		
		for(int i = 0; i < res.length; i++) {
			if(res[i] != NonogramSolution.WHITE_CELL) {
				res[i] = Character.toChars(numbers.size() - 1 - res[i])[0];
			}
		}
		return res;
	}
	
	/*
	 * Если пересечение правого и левого размещения одного и того же блока не пусто
	 * - клетки входящие в пересечение закрашены. 
	 */
	private char[] procedure1(char[] real, char[] left, char[] right) {
		char res[] = real.clone();
		
		for(int i = 0; i < res.length; i++) {
			if(real[i] == NonogramSolution.UNKNOWN_CELL &&
					left[i] != NonogramSolution.WHITE_CELL &&
					left[i] == right[i])
				res[i] = NonogramSolution.BLACK_CELL;
		}
				
		return res;
	}
	
	/*
	 * Если клетка находится между блоками ni и ni+1 и в правом и левом размещении,
	 * то эта клетка однозначно незакрашена.
	 */
	private char[] procedure2(char[] real, char[] left, char[] right) {
		char res[] = real.clone();
		
		int leftBlock = -1;
		int rightBlock = -1;
				
		for(int i = 0; i < res.length; i++) {
			if(left[i] == NonogramSolution.WHITE_CELL && right[i] == NonogramSolution.WHITE_CELL) {
				if(leftBlock == rightBlock)
					res[i] = NonogramSolution.WHITE_CELL;
			} else {
				if(left[i] != NonogramSolution.WHITE_CELL)
					leftBlock = left[i];
				if(right[i] != NonogramSolution.WHITE_CELL)
					rightBlock = right[i];
			}
		}
		
		return res;
	}	
	
	/*
	 * Если чёрная и белая клетка расположены по соседству, 
	 * достраиваем от чёрной клетки блок равный min(ni) в сторону, 
	 * противоположную белой клетке. 
	 */
	private char[] procedure3(char[] real, ArrayList<Integer> numbers) {
		char res[] = real.clone();
		
		int min = numbers.get(0);
		for(int i : numbers) {
			if(min > i)
				min = i;
		}
		
		if(min <= 1)
			return res;
		
		for(int i = 1; i < res.length; i++) {
			if(res[i-1] == NonogramSolution.WHITE_CELL && res[i] == NonogramSolution.BLACK_CELL) {
				for(int j = i+1; j < i + min; j++) {
					res[j] = NonogramSolution.BLACK_CELL;
				}
			}
			
			if(res[i-1] == NonogramSolution.BLACK_CELL && res[i] == NonogramSolution.WHITE_CELL) {
				for(int j = i-min; j < i-1; j++) {
					res[j] = NonogramSolution.BLACK_CELL;
				}
			}
		}
		
		return res;
	}
	
	/*
	 * Если длина неизвестных блоков меньше min(ni) (только для тех блоков ni, которые ещё не расположены), 
	 * то клетки этого неизвестного блока однозначно незакрашены.
	 */
	private char[] procedure4(char[] real, ArrayList<Integer> numbers) {
		char res[] = real.clone();
		ArrayList<Integer> num = (ArrayList<Integer>) numbers.clone();

		int start;
		int finish;
		int size;
		
		for(int i = 0; i < res.length; i++) {			
			boolean flag = true;
			if(res[i] == NonogramSolution.BLACK_CELL) {
				start = i;
				for(i++; i < res.length; i++) {
					if(res[i] != NonogramSolution.BLACK_CELL)
						break;
				}
				
				finish = i - 1;
				
				if(start != 0) {
					if(res[start-1] != NonogramSolution.WHITE_CELL)
						flag = false;
				}				
				if(finish != res.length - 1) {
					if(res[finish + 1] != NonogramSolution.WHITE_CELL)
						flag = false;
				}
				
				if(flag) {
					size = finish - start + 1;					
					for(Iterator<Integer> it= num.iterator(); it.hasNext(); ) {
						if(it.next().intValue() == size) {
							it.remove();
							break;
						}
					}					
				}				
			}
		}		
		if(num.isEmpty())
			return res;
		
		int min = res.length;		
		for(Iterator<Integer> it = num.iterator(); it.hasNext(); ) {
			int n = it.next().intValue();
			if(min > n)
				min = n;
		}
				
		for(int i = 0; i < res.length; i++) {
			if(res[i] == NonogramSolution.UNKNOWN_CELL) {
				start = i;
				finish = start + 1;
				
				for(i++; i < res.length; i++) {
					finish = i;
					if(res[i] != NonogramSolution.UNKNOWN_CELL) {
						break;
					}
				}
				
				if(start != 0 ) {
					if(res[start] != NonogramSolution.WHITE_CELL) {
						continue;
					}
				}				
				if(finish != res.length) {
					if(res[finish] != NonogramSolution.WHITE_CELL) {
						continue;
					}
				}

				if(finish == res.length) {
					if(finish - start < min) {
						for(int j = start; j < finish; j++) {
							res[j] = NonogramSolution.WHITE_CELL;
						}
					}					
				} else if(res[finish] == NonogramSolution.WHITE_CELL) {
					if(finish - start < min) {
						for(int j = start; j < finish; j++) {
							res[j] = NonogramSolution.WHITE_CELL;
						}
					}
				}
			}
		}
		
		return res;
	}
	
	/*
	 * Процедура используется в случае, когда закрашенный блок соседствует с неизвестными клетками.
	 * Если и в правом и в левом размещении этот блок граничит с незакрашенной клеткой, 
	 * этак клетка является незакрашенной.
	 */
	private char[] procedure5(char[] real, char[] left, char[] right) {
		char res[] = real.clone();
		
		for(int i = 0; i < res.length; i++) {
			if(real[i] == NonogramSolution.BLACK_CELL && left[i] == right[i]) {
				if(i != 0) {
					if(real[i-1] == NonogramSolution.UNKNOWN_CELL &&
							left[i-1] == NonogramSolution.WHITE_CELL &&
							right[i-1] == NonogramSolution.WHITE_CELL) {
						res[i-1] = NonogramSolution.WHITE_CELL;
					}
				}
				if(i != res.length - 1) {
					if(real[i+1] == NonogramSolution.UNKNOWN_CELL &&
							left[i+1] == NonogramSolution.WHITE_CELL &&
							right[i+1] == NonogramSolution.WHITE_CELL) {
						res[i+1] = NonogramSolution.WHITE_CELL;
					}
				}
			} 
		}		
		
		return res;
	}
	
	private void fillFullRow(int index) {
		ArrayList<Integer> list = problem.getRow(index);
		
		if(list.size() == 1 && list.get(0).intValue() == 0) {
			rowStatus[index] = true;
			for(int i = 0; i < xSizeTemp; i++)
				solutionTemp.setWhiteCell(i, index);
			return;
		}
		
		int sum = list.size() - 1;
		for(Integer i : list)
			sum += i.intValue();
		
		if(sum == xSizeTemp) {
			rowStatus[index] = true;
			
			int pos;
			for(pos = 0; pos < list.get(0); pos++)
				solutionTemp.setBlackCell(pos, index);
			
			for(int i = 1; i < list.size(); i++) {
				solutionTemp.setWhiteCell(pos, index);
				pos++;
				for(int j = 0; j < list.get(i); j++, pos++) {
					solutionTemp.setBlackCell(pos, index);
				}				
			}			
		}		
	}
	private void fillFullColumn(int index) {
		ArrayList<Integer> list = problem.getColumn(index);
		
		if(list.size() == 1 && list.get(0).intValue() == 0) {
			columnStatus[index] = true;
			for(int i = 0; i < ySizeTemp; i++)
				solutionTemp.setWhiteCell(index, i);
			return;
		}
		
		int sum = list.size() - 1;
		for(Integer i : list)
			sum += i.intValue();
		
		if(sum == ySizeTemp) {
			columnStatus[index] = true;
			
			int pos;
			for(pos = 0; pos < list.get(0); pos++)
				solutionTemp.setBlackCell(index, pos);
			
			for(int i = 1; i < list.size(); i++) {
				solutionTemp.setWhiteCell(index, pos);
				pos++;
				for(int j = 0; j < list.get(i); j++, pos++) {
					solutionTemp.setBlackCell(index, pos);
				}				
			}			
		}
	}
	
}

