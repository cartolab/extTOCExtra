package es.udc.cartolab.gvsig.tocextra;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class ShowActivesTocMenuEntry extends AbstractTocContextMenuAction {

	public String getText() {
		return PluginServices.getText(this, "Show_actives");
	}
	
	public void execute(ITocItem item, FLayer[] selectedItems) {
		
		for (int i=0; i<selectedItems.length; i++) {
			selectedItems[i].setVisible(true);
		}
		
	}
	
	public String getGroup() {
		return "tocextra"; //FIXME
	}

	public int getGroupOrder() {
		return 50;
	}

	public int getOrder() {
		return 10;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		
		if (isTocItemBranch(item) && ! (selectedItems == null || selectedItems.length <= 0)) {
			for (int i=0; i<selectedItems.length; i++) {
				if (!selectedItems[i].isVisible()) {
					return true;
				}
			}
		}
		return false;

	}
	
}
