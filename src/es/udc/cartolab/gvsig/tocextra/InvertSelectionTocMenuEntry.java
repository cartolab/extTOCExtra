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
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class InvertSelectionTocMenuEntry extends AbstractTocContextMenuAction {

	public void execute(ITocItem item, FLayer[] selectedItems) {
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		
		selectAllLayers(view.getMapControl().getMapContext().getLayers());
		
		for (int i=0; i<selectedItems.length; i++) {
			if (!(selectedItems[i] instanceof FLayers)) {
				selectedItems[i].setActive(false);
			}
		}
	}
	
	public static void selectAllLayers(FLayers layers) {
		
		int nLayers = layers.getLayersCount();
		for (int i=0; i<nLayers; i++) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				layer.setActive(false);
				selectAllLayers((FLayers) layer);
			} else {
				layer.setActive(true);
			}
		}
		
	}

	public String getText() {
		return PluginServices.getText(this, "Invert_selection");
		//return "Invertir selección";
	}
	
	public String getGroup() {
		return "tocextra"; //FIXME
	}

	public int getGroupOrder() {
		return 50;
	}

	public int getOrder() {
		return 1;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item) && selectedItems != null) {
			return true;
		}
		return false;

	}

}
