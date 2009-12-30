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
