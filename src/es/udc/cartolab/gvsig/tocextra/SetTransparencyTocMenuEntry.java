/*
 * This file is part of TOCExtra
 * Copyright (C) 2009 - 2011 Fundación de Ingeniería Civil de Galicia
 *
 * This software have been originally developed in Cartolab http://cartolab.udc.es
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

import java.util.ArrayList;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

import es.udc.cartolab.gvsig.tocextra.gui.AdjustTransparencyPanel;

/**
 * TOC Menu Entry to set the transparency of the active layers
 *
 * @author Francisco Puga <fpuga at cartolab.es> http://conocimientoabierto.es
 *
 */
public class SetTransparencyTocMenuEntry extends AbstractTocContextMenuAction {

    public String getText() {
	return PluginServices.getText(this, "set_transparency");
    }



    @Override
    public void execute(ITocItem item, FLayer[] selectedItems) {

	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();

	if (iWindow instanceof BaseView) {

	    ArrayList<FLyrDefault> layers = new ArrayList<FLyrDefault>();

	    for (int i = 0; i < selectedItems.length; i++) {
		if (selectedItems[i] instanceof FLyrRasterSE) {
		    layers.add((FLyrDefault) selectedItems[i]);
		}
	    }

	    AdjustTransparencyPanel panel = new AdjustTransparencyPanel(layers,
		    (BaseView) iWindow);
	    panel.openJDialog();
	}
    }



    public String getGroup() {
	return "RasterLayer";
    }

    public int getGroupOrder() {
	return 60;
    }

    public int getOrder() {
	return 100;
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
