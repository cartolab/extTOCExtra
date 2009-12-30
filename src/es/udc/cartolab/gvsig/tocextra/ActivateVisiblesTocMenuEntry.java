package es.udc.cartolab.gvsig.tocextra;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class ActivateVisiblesTocMenuEntry extends AbstractTocContextMenuAction{

	public String getText() {
		return PluginServices.getText(this, "Activate_visible");
	}
	
	public void execute(ITocItem item, FLayer[] selectedItems) {
		
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		view.getMapControl().getMapContext().getLayers().setAllActives(false);
		FLayers layers = view.getMapControl().getMapContext().getLayers();
				
		activateVisibles(layers);
		
	}
	
	private void activateVisibles(FLayers layers) {

		for (int i=0; i<layers.getLayersCount(); i++) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				activateVisibles((FLayers)layer);
			} else {
				if (layer.isVisible()) {
					layer.setActive(true);
				}
			}
		}

	}
	
	public String getGroup() {
		return "tocextra"; //FIXME
	}

	public int getGroupOrder() {
		return 50;
	}

	public int getOrder() {
		return 2;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item) &&  selectedItems != null) {
			return true;
		}
		return false;

	}
	
}
