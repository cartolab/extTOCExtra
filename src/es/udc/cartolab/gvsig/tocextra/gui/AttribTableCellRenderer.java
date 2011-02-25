/*
 * This file is part of TOCExtra
 * Copyright (C) 2009 - 2011  Cartolab (Universidade da Coruña)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.cartolab.gvsig.tocextra.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * With this class, the navTable first column text is forced to be bold.
 * 
 * @author Pablo Sanxiao, Nacho Uve
 * 
 */
public class AttribTableCellRenderer extends JTextArea implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;

	private Vector noEditableRows = new Vector();

	public void addNoEditableRow(int rowNumber) {
		noEditableRows.addElement(new Integer(rowNumber));
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		this.setText(value.toString());
		if (column == 0) {
			Font f = new Font("Sans", Font.BOLD, 12);
			this.setFont(f);
		} else {
			Font f = new Font("Sans", Font.PLAIN, 12);
			this.setFont(f);
		}

		if (noEditableRows.contains(new Integer(row))) {
			if (column == 0) {
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

		if (isSelected) {
			this.setBackground(new Color(195, 212, 232));
			if (noEditableRows.contains(new Integer(row))) {
				this.setBackground(new Color(220, 170, 200));
			}
		}
		return this;
	}
}
