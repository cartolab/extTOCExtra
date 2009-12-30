package es.udc.cartolab.gvsig.tocextra.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * With this class, the navTable first column text is forced to
 * be bold.
 *  
 * @author Pablo Sanxiao, Nacho Uve
 *
 */
public class AttribTableCellRenderer extends JTextArea implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private Vector noEditableRows = new Vector();
	
	public void addNoEditableRow(int rowNumber){
		noEditableRows.addElement(new Integer(rowNumber));
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		this.setText((String)value.toString());		
		if (column == 0){
			Font f = new Font("Sans", Font.BOLD, 12);
			this.setFont(f);
		} else {
			Font f = new Font("Sans", Font.PLAIN, 12);
			this.setFont(f);
		}
				
		if (noEditableRows.contains(new Integer(row))){
			if (column == 0){ 
				this.setBackground(new Color(230, 200, 200));
			} else {
				this.setBackground(new Color(240, 230, 230));
			}
		} else {
			if (column == 0) {
				this.setBackground(new Color(240, 240, 240));
			} else {
				this.setBackground(Color.white);
			}
		}
		
		if (isSelected){
			this.setBackground(new Color(195, 212, 232));
			if (noEditableRows.contains(new Integer(row))){
				this.setBackground(new Color(220, 170, 200));
			}
		}		
		return this;
	}	
}
