package com.gromholl.nonogram;

import javax.swing.SwingUtilities;

public class Client {
	
	public static void main(String args[]) {
    	SwingUtilities.invokeLater(new Runnable() {
        	public void run() {  
            	@SuppressWarnings("unused")
				ClientFrame app = new ClientFrame();            	
        	}
        });
	}

}
