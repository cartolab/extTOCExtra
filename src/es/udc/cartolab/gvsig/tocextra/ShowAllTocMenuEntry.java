package es.udc.cartolab.gvsig.tocextra;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class ShowAllTocMenuEntry extends AbstractTocContextMenuAction {

	public void execute(ITocItem item, FLayer[] selectedItems) {
		
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		view.getMapControl().getMapContext().getLayers().setAllVisibles(true);
		
	}

	public String getText() {
		return PluginServices.getText(this, "Show_all");
	}
	
	public String getGroup() {
		return "tocextra"; //FIXME
	}

	public int getGroupOrder() {
		return 50;
	}

	public int getOrder() {
		return 12;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		
		if (isTocItemBranch(item)) {
			View view = (View) PluginServices.getMDIManager().getActiveWindow();
			FLayers layers = view.getMapControl().getMapContext().getLayers();
			return !areAllVisible(layers);
		}
		return false;

	}
	
	private boolean areAllVisible(FLayers layers) {
		
		boolean allVisible = false;
		if (layers.isVisible()) {
			allVisible = true;
			for (int i=0; i<layers.getLayersCount(); i++) {
				FLayer layer = layers.getLayer(i);
				if (layer instanceof FLayers) {
					allVisible = allVisible && areAllVisible((FLayers) layer);
				} else {
					allVisible = allVisible && layer.isVisible();
				}
				if (!allVisible) {
					break;
				}
			}
			
		}
		return allVisible;
	}

}
