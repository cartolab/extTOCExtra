package es.udc.cartolab.gvsig.tocextra;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class ActivateEditingTocMenuEntry extends AbstractTocContextMenuAction {
	
	public String getText() {
		return PluginServices.getText(this, "Activate_editing");
		//return "Activar capas en edición";
	}
	
	public void execute(ITocItem item, FLayer[] selectedItems) {
		
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		view.getMapControl().getMapContext().getLayers().setAllActives(false);
		FLayers layers = view.getMapControl().getMapContext().getLayers();
				
		activateEditing(layers);
		
	}
	
	private void activateEditing(FLayers layers) {

		for (int i=0; i<layers.getLayersCount(); i++) {
			FLayer layer = layers.getLayer(i);
			if (layer instanceof FLayers) {
				activateEditing((FLayers)layer);
			} else {
				if (layer.isEditing()) {
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
		return 9;
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
