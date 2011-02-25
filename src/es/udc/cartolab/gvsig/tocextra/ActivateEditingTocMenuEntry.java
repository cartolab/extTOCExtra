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

/**
 * TOC menu entry to activate all editing layers in view. It is disabled due to
 * performance.
 * 
 * @author Javier Estévez <valdaris at gmail dot com>
 * 
 */
public class ActivateEditingTocMenuEntry extends AbstractTocContextMenuAction {

	public String getText() {
		return PluginServices.getText(this, "Activate_editing");
		// return "Activar capas en edición";
	}

	/**
	 * Method that will be executed when user clicks on this TOC menu entry.
	 * 
	 * @param item
	 *            TOC item clicked.
	 * @param selectedItems
	 *            Array of layers selected in TOC.
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {

		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		view.getMapControl().getMapContext().getLayers().setAllActives(false);
		FLayers layers = view.getMapControl().getMapContext().getLayers();

		activateEditing(layers);

	}

	private void activateEditing(FLayers layers) {

		for (int i = 0; i < layers.getLayersCount(); i++) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				activateEditing((FLayers) layer);
			} else {
				if (layer.isEditing()) {
					layer.setActive(true);
				}
			}
		}

	}

	public String getGroup() {
		return "tocextra"; // FIXME
	}

	public int getGroupOrder() {
		return 50;
	}

	public int getOrder() {
		return 9;
	}

	/**
	 * Method to enable or disable this menu entry.
	 * 
	 * @return true if the menu entry should be enabled, false otherwise.
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	/**
	 * Method to this menu entry visibility.
	 * 
	 * @return true if the menu entry should be visible, false otherwise.
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item) && selectedItems != null) {
			return true;
		}
		return false;

	}

}
