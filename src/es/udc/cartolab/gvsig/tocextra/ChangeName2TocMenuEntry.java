package es.udc.cartolab.gvsig.tocextra;

import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeNameTocMenuEntry;

public class ChangeName2TocMenuEntry extends ChangeNameTocMenuEntry {
	
	//No modifica el funcionamiento del cambio de nombre, solo se limita
	//la aparicion de la opcion en el menu contextual a cuando hay una sola
	//capa activa.
	
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item) && ! (selectedItems == null || selectedItems.length <= 0)) {
			return selectedItems.length == 1;
		}
		return false;
	}

}
