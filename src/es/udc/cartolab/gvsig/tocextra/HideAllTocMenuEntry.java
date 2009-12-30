package es.udc.cartolab.gvsig.tocextra;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class HideAllTocMenuEntry extends AbstractTocContextMenuAction {

	public void execute(ITocItem item, FLayer[] selectedItems) {
		
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		view.getMapControl().getMapContext().getLayers().setAllVisibles(false);
		
	}

	public String getText() {
		return PluginServices.getText(this, "Hide_all");
	}
	
	public String getGroup() {
		return "tocextra"; //FIXME
	}

	public int getGroupOrder() {
		return 50;
	}

	public int getOrder() {
		return 13;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		
		boolean visible = false;
		if (isTocItemBranch(item)) {
			View view = (View) PluginServices.getMDIManager().getActiveWindow();
			FLayers layers = view.getMapControl().getMapContext().getLayers();
			
			for (int i=0; i<layers.getLayersCount(); i++) {
				FLayer layer = layers.getLayer(i);
				if (layer.isVisible()) {
					visible = true;
					break;
				}
			}
		}
		return visible;

	}
	
}
