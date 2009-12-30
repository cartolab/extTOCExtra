package es.udc.cartolab.gvsig.tocextra.gui;

import javax.swing.table.DefaultTableModel;

/**
 * The model of the table to be shown. It makes 
 * non-editable the first row cells.
 * 
 * @author Pablo Sanxiao
 *
 */
class MyTableModel extends DefaultTableModel {

	public boolean isCellEditable(int row, int col) {  
		if (col == 1) {  
			return true;  
		} else {  
			return false;  
		}         
	} 
	
}