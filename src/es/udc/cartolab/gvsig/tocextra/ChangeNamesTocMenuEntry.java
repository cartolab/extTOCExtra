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

package es.udc.cartolab.gvsig.tocextra;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeNameTocMenuEntry;


import es.udc.cartolab.gvsig.tocextra.gui.ChangeLayerNamesPanel;

public class ChangeNamesTocMenuEntry extends ChangeNameTocMenuEntry {

	
	public String getText() {
		return PluginServices.getText(this, "Change_names");
	}
	
	public void execute(ITocItem item, FLayer[] selectedItems) {
		
		IWindow view = new ChangeLayerNamesPanel(selectedItems);
		PluginServices.getMDIManager().addCentredWindow(view);
		
	}
	
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item) && ! (selectedItems == null || selectedItems.length <= 1)) {
			return true;
		}
		return false;

	}
}
