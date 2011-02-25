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
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionManager;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.actions.StartEditingTocMenuEntry;

/**
 * Class to change gvSIG Start Editing menu entry behavior. This class can be
 * executed on several layers in one step. It is disabled due to performance.
 * 
 * @author Javier Estévez <valdaris at gmail dot com>
 * 
 */
public class StartEdition2TocMenuEntry extends StartEditingTocMenuEntry {

	/**
	 * Method to this menu entry visibility
	 * 
	 * @return true if the menu entry should be visible, false otherwise.
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {

		boolean visible = false;
		if (isTocItemBranch(item)) {
			IWindow window = PluginServices.getMDIManager().getActiveWindow();
			if (window instanceof View) {
				visible = true;
				for (int i = 0; i < selectedItems.length; i++) {
					visible = visible && selectedItems[i].isAvailable()
							&& selectedItems[i] instanceof FLyrVect
							&& !((FLyrVect) selectedItems[i]).isEditing();
					if (!visible) {
						break;
					}
				}

			}
		}
		return visible;
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

		CADExtension.initFocus();
		View view = (View) PluginServices.getMDIManager().getActiveWindow();

		MapControl mapControl = view.getMapControl();

		// mapControl.getMapContext().clearAllCachingImageDrawnLayers();
		view.showConsole();
		EditionManager editionManager = CADExtension.getEditionManager();
		editionManager.setMapControl(mapControl);

		ToggleEditing te = new ToggleEditing();

		for (int i = 0; i < selectedItems.length; i++) {
			if (selectedItems[i] instanceof FLyrVect) {
				FLyrVect lv = (FLyrVect) selectedItems[i];
				te.startEditing(lv);
			}
		}
	}

}
