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

package es.udc.cartolab.gvsig.tocextra;

import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeNameTocMenuEntry;

/**
 * Auxiliary class to change visibility of gvSIG default change layer name TOC
 * menu entry. This makes it visible only when there's just one layer selected.
 * 
 * @author Javier Estévez <valdaris at gmail dot com>
 * 
 */
public class ChangeName2TocMenuEntry extends ChangeNameTocMenuEntry {

	/**
	 * Method to set this menu entry visibility.
	 * 
	 * @return true if the menu entry should be visible, false otherwise.
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item)
				&& !(selectedItems == null || selectedItems.length <= 0)) {
			return selectedItems.length == 1;
		}
		return false;
	}

}
