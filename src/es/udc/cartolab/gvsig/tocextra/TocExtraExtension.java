/*
 * This file is part of TOCExtra
 * Copyright (C) 2009 - 2011 Fundaci�n de Ingenier�a Civil de Galicia
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

import com.iver.andami.plugins.Extension;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

/**
 * Auxiliary extension to add all toc menu entries defined in this project.
 * 
 * @author Javier Est�vez <valdaris at gmail dot com>
 * 
 */
public class TocExtraExtension extends Extension {

	public void execute(String actionCommand) {

	}

	public void initialize() {
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();

		extensionPoints.add("View_TocActions", "ShowActives",
				new ShowActivesTocMenuEntry());
		extensionPoints.add("View_TocActions", "ShowOnlyActives",
				new ShowOnlyActivesTocMenuEntry());
		extensionPoints.add("View_TocActions", "ActivateVisibles",
				new ActivateVisiblesTocMenuEntry());

		extensionPoints.add("View_TocActions", "InvertSelection",
				new InvertSelectionTocMenuEntry());
		extensionPoints.add("View_TocActions", "ShowAll",
				new ShowAllTocMenuEntry());
		extensionPoints.add("View_TocActions", "HideAll",
				new HideAllTocMenuEntry());

		extensionPoints.add("View_TocActions", "ChangeNames",
				new ChangeNamesTocMenuEntry());
		extensionPoints.add("View_TocActions", "ChangeName",
				new ChangeName2TocMenuEntry());

		// Start and stop edition on several layers. It's too slow when there
		// are many layers.
		// extensionPoints.add("View_TocActions", "StartEditing", new
		// StartEdition2TocMenuEntry());
		// extensionPoints.add("View_TocActions", "ActivateEditing", new
		// ActivateEditingTocMenuEntry());
	}

	public boolean isEnabled() {
		return false;
	}

	public boolean isVisible() {
		return false;
	}

}
