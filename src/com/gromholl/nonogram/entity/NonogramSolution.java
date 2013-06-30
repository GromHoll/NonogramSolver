package com.gromholl.nonogram.entity;

import java.util.Arrays;

public class NonogramSolution  implements Cloneable {
	public static final char UNKNOWN_CELL = '?';
	public static final char WHITE_CELL = '.';
	public static final char BLACK_CELL = '#';
	
	private char[][] field;
	
	public NonogramSolution(int w, int h) {
		field = new char[w][];
		for(int i = 0; i < w; i++) {
			field[i] = new char[h];
		}
		clear();
	}
	
	public char[] getRow(int index) {
		char[] column = new char[ field.length ];		
		for(int i = 0; i < column.length; i++) {
			column[i] = field[i][index];
		}		
		return column;
	}
	public char[] getColumn(int index) {
		return field[index];
	}
	
	public void setRow(int index, char[] line) {
		for(int i = 0; i < line.length; i++) {
			field[i][index] = line[i];
		}
	}
	public void setColumn(int index, char[] line) {
		field[index] = line;
	}
	
	public void clear() {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				field[i][j] = UNKNOWN_CELL;
			}
		}
	}
	public void fillWhite() {
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[i].length; j++) {
				field[i][j] = WHITE_CELL;
			}
		}		
	}
	
	public void setBlackCell(int x, int y) {
		field[x][y] = BLACK_CELL;
	}
	public void setWhiteCell(int x, int y) {
		field[x][y] = WHITE_CELL;
	}
	public void setUnknownCell(int x, int y) {
		field[x][y] = UNKNOWN_CELL;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append(this.getRow(0));
		for(int i = 1; i < field[0].length; i++) {
			out.append("\n");
			out.append(this.getRow(i));
		}
		
		return out.toString();
	}
	
	public Object clone() {
		int w = this.field.length;
		int h = this.field[0].length;
		
		NonogramSolution obj = new NonogramSolution(w, h);
		for(int i = 0; i < w; i++)
			obj.field[i] = this.field[i].clone();
		return obj;
	}

	public boolean equals(NonogramSolution arg) {
		for(int i = 0; i < field.length; i++) {			
			if(!Arrays.equals(this.getColumn(i), arg.getColumn(i))) {
				return false;
			}
		}
		return true;
	}
}
