package com.gromholl.nonogram.entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class NonogramProblem {
	
	private String name;
	
	private ArrayList< ArrayList<Integer> > columns;
	private ArrayList< ArrayList<Integer> > rows;
	
	private int blackCellsNum;
	private int xSize;
	private int ySize;
	
	
	public NonogramProblem() {
		xSize = 0;
		ySize = 0;		
		blackCellsNum = 0;
		
		name = new String();
		
		columns = new ArrayList< ArrayList<Integer> >();
		rows = new ArrayList< ArrayList<Integer> >();
	}
	
	/*  Nonogram problem input file:
	 * 
	 *  Format:
	 *   
	 *  +---------+--------------------------+
	 *  |  Number |                          |
	 *  |    of   |       Information        |
	 *  |  String |                          |
	 *  +---------+--------------------------+
	 *  |    1    | Name: $NONOGRAM_NAME     |
	 *  |    2    | Size: $X x $Y            |
	 *  |    3    | Rows:                    |
	 *  |    4    | // a sequence of numbers |
 	 *  |    .    | .                        |
	 *  |    .    | .                        |
	 *  |    .    | .                        |
	 *  |  4 + $Y | // a sequence of numbers |
	 *  |  5 + $Y | Columns:                 |
	 *  |  6 + $Y | // a sequence of numbers |
	 *  |    .    | .                        |
	 *  |    .    | .                        |
	 *  |    .    | .                        |
	 *  | 6+$Y+$X | // a sequence of numbers |
	 *  | 7+$Y+$X | EOF                      |
	 *  +---------+--------------------------+    
	 *  
	 *  Example:
	 *  
	 *  Name: Heart
	 *  Size: 6 x 7
	 *  Rows:
	 *   2 2
	 *   7
	 *   7
	 *   5
	 *   3
	 *   1
	 *  Columns:
	 *   2
	 *   4
	 *   5
	 *   5
	 *   5
	 *   4
	 *   2
	 */
	
	public void load(File source) throws Exception {
		final String nameTag = "Name:";
		final String sizeTag = "Size:";
		final String separator = "x";
		final String rowsTag = "Rows:";
		final String columnsTag = "Columns:";
		
		int xNum = 0;
		int yNum = 0;		
		int temp;
		
		BufferedReader in = new BufferedReader(new FileReader(source.getPath()));
		Scanner scn;
		try {
			scn = new Scanner(in.readLine());			
			if(!scn.next().equals(nameTag))
				throw new Exception();
			name = scn.next();
			
			scn = new Scanner(in.readLine());			
			if(!scn.next().equals(sizeTag))
				throw new Exception();
			xSize = scn.nextInt(10);
			if(!scn.next().equals(separator))
				throw new Exception();	
			ySize = scn.nextInt(10);
			
			scn = new Scanner(in.readLine());
			if(!scn.next().equals(rowsTag))
				throw new Exception();
			
			for(int i = 0; i < ySize; i++) {
				scn = new Scanner(in.readLine());
				ArrayList<Integer> list = new ArrayList<Integer>();				
				while(scn.hasNext()) {
					temp = scn.nextInt(10);
					list.add(temp);
					xNum += temp;
				}
				rows.add(list);
			}
			
			scn = new Scanner(in.readLine());
			if(!scn.next().equals(columnsTag))
				throw new Exception();
			
			for(int i = 0; i < xSize; i++) {
				scn = new Scanner(in.readLine());
				ArrayList<Integer> list = new ArrayList<Integer>();				
				while(scn.hasNext()) {
					temp = scn.nextInt(10);
					list.add(temp);
					yNum += temp;
				}
				columns.add(list);
			}			
		} catch(Exception exc) {
			clear();
			in.close();
			throw new Exception("File format error.");			
		}
		if(xNum == yNum) {
			blackCellsNum = xNum;
		} else {
			throw new Exception("Sum of numbers in rows (" + xNum + ") not equals numbers in column (" + yNum + ").");
		}	
	}

	public void clear() { 
		name = "";
		rows.clear();
		columns.clear();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isSolution(NonogramSolution solution) {
		for(int i = 0; i < rows.size(); i++) {
			if(!isCorrectRow(solution, i))
				return false;
		}
		for(int i = 0; i < columns.size(); i++) {
			if(!isCorrectColumn(solution, i))
				return false;
		}
		return true;
	}
	public boolean isCorrectRow(NonogramSolution solution, int index) {
		try {
			int pos = 0;
			int groupSize;
			char line[] = solution.getRow(index);
						
			for(int i = 0; i < xSize; i++) {
				if(line[i] == NonogramSolution.UNKNOWN_CELL)
					return false;
				if(line[i] == NonogramSolution.BLACK_CELL) {
					groupSize = rows.get(index).get(pos++);					
					for(int j = i; i < j + groupSize; i++) {
						if(line[i] != NonogramSolution.BLACK_CELL)
							return false;
					}
					if(i < xSize) {
						if(line[i] != NonogramSolution.WHITE_CELL)		
							return false;
					}
				}
			}
			
			if(pos == rows.get(index).size())
				return true;
			else
				return false;
				
		} catch(IndexOutOfBoundsException exc) {
			return false;
		}
	}
	public boolean isCorrectColumn(NonogramSolution solution, int index) {
		try {
			int pos = 0;
			int groupSize;
			char line[] = solution.getColumn(index);
			
			for(int i = 0; i < ySize; i++) {
				if(line[i] == NonogramSolution.UNKNOWN_CELL)
					return false;
				if(line[i] == NonogramSolution.BLACK_CELL) {
					groupSize = columns.get(index).get(pos++);
					for(int j = i; i < j + groupSize; i++) {
						if(line[i] != NonogramSolution.BLACK_CELL)
							return false;
					}
					if(i < ySize) {
						if(line[i] != NonogramSolution.WHITE_CELL)
							return false;
					}
				}
			}
			if(pos == columns.get(index).size())
				return true;
			else 
				return false;
		} catch(IndexOutOfBoundsException exc) {
			return false;
		}	
	}
	
	public int getBlackCellsNum() {
		return blackCellsNum;
	}
	
	public int getWidth() {
		return xSize;
	}
	public int getHeight() {
		return ySize;
	}
	
	public ArrayList<Integer> getRow(int index) throws IndexOutOfBoundsException {
		return rows.get(index);
	}
	public ArrayList<Integer> getColumn(int index) throws IndexOutOfBoundsException {
		return columns.get(index);		
	}
	
}
