package es.udc.cartolab.gvsig.tocextra;

import com.iver.andami.plugins.Extension;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

public class TocExtraExtension extends Extension {

	public void execute(String actionCommand) {
		
	}

	public void initialize() {
		ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
		
		extensionPoints.add("View_TocActions", "ShowActives", new ShowActivesTocMenuEntry());
		extensionPoints.add("View_TocActions", "ShowOnlyActives", new ShowOnlyActivesTocMenuEntry());
		extensionPoints.add("View_TocActions", "ActivateVisibles", new ActivateVisiblesTocMenuEntry());
		
		extensionPoints.add("View_TocActions", "InvertSelection", new InvertSelectionTocMenuEntry());
		extensionPoints.add("View_TocActions", "ShowAll", new ShowAllTocMenuEntry());
		extensionPoints.add("View_TocActions", "HideAll", new HideAllTocMenuEntry());
		
		extensionPoints.add("View_TocActions", "ChangeNames", new ChangeNamesTocMenuEntry());
		extensionPoints.add("View_TocActions", "ChangeName", new ChangeName2TocMenuEntry());
		
		//comenzar y cerrar edicion de varias capas. Es lento cuando hay muchas capas.
		//extensionPoints.add("View_TocActions", "StartEditing", new StartEdition2TocMenuEntry());
		//extensionPoints.add("View_TocActions", "ActivateEditing", new ActivateEditingTocMenuEntry());
	}

	public boolean isEnabled() {
		return false;
	}

	public boolean isVisible() {
		return false;
	}

}
