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

public class StartEdition2TocMenuEntry extends StartEditingTocMenuEntry {

	@Override
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {

		boolean visible = false;
		if (isTocItemBranch(item)) {
			IWindow window=PluginServices.getMDIManager().getActiveWindow();
			if (window instanceof View){
				visible = true;
				for (int i=0; i<selectedItems.length; i++) {
					visible = visible && selectedItems[i].isAvailable() && selectedItems[i] instanceof FLyrVect && !((FLyrVect) selectedItems[i]).isEditing();
					if (!visible) {
						break;
					}
				}

			}
		}
		return visible;
	}

	@Override
	public void execute(ITocItem item, FLayer[] selectedItems) {

		CADExtension.initFocus();
		View view = (View) PluginServices.getMDIManager().getActiveWindow();

		MapControl mapControl = view.getMapControl();

		//mapControl.getMapContext().clearAllCachingImageDrawnLayers();
		view.showConsole();
		EditionManager editionManager=CADExtension.getEditionManager();
		editionManager.setMapControl(mapControl);

		ToggleEditing te = new ToggleEditing();

		for (int i=0; i<selectedItems.length; i++) {
			if (selectedItems[i] instanceof FLyrVect) {
				FLyrVect lv = (FLyrVect) selectedItems[i];
				te.startEditing(lv);
			}
		}
	}

}
