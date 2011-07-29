/*
 * This file is part of TOCExtra
 * Copyright (C) 2009 - 2011 Fundación de Ingeniería Civil de Galicia
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

import javax.swing.table.DefaultTableModel;

/**
 * The model of the table to be shown. It makes non-editable the first row
 * cells.
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
