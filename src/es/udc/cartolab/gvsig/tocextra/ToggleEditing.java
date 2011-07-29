/*
 * This file is part of TOCExtra
 * Copyright (C) 2009 - 2011. Fundación de Ingeniería Civil de Galicia
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

import java.io.IOException;
import java.sql.Types;

import org.gvsig.exceptions.DriverException;

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionManager;
import com.iver.cit.gvsig.EditionUtilities;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.exceptions.layers.CancelEditingLayerException;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.exceptions.layers.StartEditionLayerException;
import com.iver.cit.gvsig.exceptions.table.CancelEditingTableException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.DefaultRow;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.drivers.ILayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.ITableDefinition;
import com.iver.cit.gvsig.fmap.drivers.shp.IndexedShpDriver;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.EditionExceptionOld;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.ISpatialWriter;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.edition.rules.IRule;
import com.iver.cit.gvsig.fmap.edition.rules.RulePolygon;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.rendering.ILegend;
import com.iver.cit.gvsig.fmap.rendering.VectorialUniqueValueLegend;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.iver.cit.gvsig.project.documents.table.ProjectTable;
import com.iver.cit.gvsig.project.documents.table.gui.Table;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Class for start, stop or toggle the editing on a vector layer.
 * Based on the StartingEditing Extension of gvSIG
 * 
 * @author Nacho Varela
 * @author Javier Estevez
 *
 */

public class ToggleEditing {

	public boolean toggle(FLayer layer){

		return true;
	}

	/**
	 * @param layer The vectorial layer to be edited.
	 */
	public void startEditing(FLayer layer){
		startEditing(layer.getName());
	}

	/**
	 * Checks the layers from the TOC and get the
	 * layer to be edited with its name.
	 * 
	 * @param layerName The name of the layer to be edited.
	 */
	public void startEditing(String layerName){
		CADExtension.initFocus();
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		.getActiveWindow();

		if (f instanceof View) {
			View vista = (View) f;

			MapControl mapControl = vista.getMapControl();

			IProjectView model = vista.getModel();
			FLayers layers = model.getMapContext().getLayers();
			layers.setAllActives(false);
			FLayer layer = layers.getLayer(layerName);

			if (layer instanceof FLyrVect) {
				layer.setActive(true);
				//				mapControl.getMapContext().clearAllCachingImageDrawnLayers();
				//				vista.showConsole();
				EditionManager editionManager = CADExtension.getEditionManager();
				editionManager.setMapControl(mapControl);

				FLyrVect lv = (FLyrVect) layer;

				lv.addLayerListener(editionManager);
				try {
					ILegend legendOriginal=lv.getLegend();

					lv.setEditing(true);
					VectorialEditableAdapter vea = (VectorialEditableAdapter) lv
					.getSource();

					vea.getRules().clear();
					if (vea.getShapeType() == FShape.POLYGON)
					{
						IRule rulePol = new RulePolygon();
						vea.getRules().add(rulePol);
					}

					if (!(lv.getSource().getDriver() instanceof IndexedShpDriver)){
						VectorialLayerEdited vle=(VectorialLayerEdited)editionManager.getLayerEdited(lv);
						vle.setLegend(legendOriginal);
					}
					vea.getCommandRecord().addCommandListener(mapControl);
					// If there's a table linked to this layer, its model is changed
					// to VectorialEditableAdapter.
					ProjectExtension pe = (ProjectExtension) PluginServices
					.getExtension(ProjectExtension.class);
					ProjectTable pt = pe.getProject().getTable(lv);
					if (pt != null){
						pt.setModel(vea);
						changeModelTable(pt,vea);
					}

					//COMENTADA POR NACHO
					//startCommandsApplicable(vista,lv);
					vista.repaintMap();
					vista.hideConsole();

				} catch (StartEditionLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}


	/**
	 * @param layer  The layer wich edition will be stoped.
	 * @param cancel false if we want to save the layer, true if we don't.
	 */
	public void stopEditing(FLayer layer, boolean cancel){
		stopEditing(layer.getName(), cancel);
	}

	/**
	 * @param layer  The name of the layer wich edition will be stoped.
	 * @param cancel false if we want to save the layer, true if we don't.
	 */
	public void stopEditing(String layerName, boolean cancel){
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
		.getActiveWindow();

		try {
			if (f instanceof View) {
				View vista = (View) f;

				MapControl mapControl = vista.getMapControl();

				IProjectView model = vista.getModel();
				FLayers layers = model.getMapContext().getLayers();
				layers.setAllActives(false);
				FLyrVect layer = (FLyrVect)layers.getLayer(layerName);
				if (cancel){
					cancelEdition(layer);
				} else {
					saveLayer(layer);
				}
				VectorialEditableAdapter vea = (VectorialEditableAdapter) layer
				.getSource();
				vea.getCommandRecord().removeCommandListener(mapControl);
				if (!(layer.getSource().getDriver() instanceof IndexedShpDriver)){
					VectorialLayerEdited vle=(VectorialLayerEdited)CADExtension.getEditionManager().getLayerEdited(layer);
					layer.setLegend((VectorialUniqueValueLegend)vle.getLegend());
				}
				layer.setEditing(false);
				// The layer should be the active one and the view must be repainted
				//layer.getMapContext().redraw();
				layer.setActive(true);
			}
		} catch (EditionExceptionOld e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LegendLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StartEditionLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeModelTable(ProjectTable pt, VectorialEditableAdapter vea){
		com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices.getMDIManager().getAllWindows();

		for (int i=0 ; i<views.length ; i++){
			if (views[i] instanceof Table){
				Table table=(Table)views[i];
				ProjectTable model =table.getModel();
				if (model.equals(pt)){
					table.setModel(pt);
					vea.getCommandRecord().addCommandListener(table);
				}
			}
		}
	}

	private void cancelEdition(FLyrVect layer) throws IOException {
		layer.setProperty("stoppingEditing",new Boolean(true));
		com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices
		.getMDIManager().getAllWindows();
		VectorialEditableAdapter vea = (VectorialEditableAdapter) layer
		.getSource();
		try {
			vea.cancelEdition(EditionEvent.GRAPHIC);
		} catch (CancelEditingLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int j = 0; j < views.length; j++) {
			if (views[j] instanceof Table) {
				Table table = (Table) views[j];
				if (table.getModel().getAssociatedTable() != null
						&& table.getModel().getAssociatedTable().equals(layer)) {
					try {
						table.cancelEditing();
					} catch (CancelEditingTableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		layer.setProperty("stoppingEditing",new Boolean(false));
	}

	private void saveLayer(FLyrVect layer) throws DriverException, EditionExceptionOld {
		layer.setProperty("stoppingEditing",new Boolean(true));
		VectorialEditableAdapter vea = (VectorialEditableAdapter) layer
		.getSource();

		ISpatialWriter writer = (ISpatialWriter) vea.getWriter();
		com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices
		.getMDIManager().getAllWindows();
		for (int j = 0; j < views.length; j++) {
			if (views[j] instanceof Table) {
				Table table = (Table) views[j];
				if (table.getModel().getAssociatedTable() != null
						&& table.getModel().getAssociatedTable().equals(layer)) {
					table.stopEditingCell();
				}
			}
		}
		vea.cleanSelectableDatasource();
		try {
			layer.setRecordset(vea.getRecordset());

			// The layer recordset must have the changes we made
			ILayerDefinition lyrDef = EditionUtilities.createLayerDefinition(layer);
			String aux="FIELDS:";
			FieldDescription[] flds = lyrDef.getFieldsDesc();
			for (int i=0; i < flds.length; i++) {
				aux = aux + ", " + flds[i].getFieldAlias();
			}
			System.err.println("Escribiendo la capa " + lyrDef.getName() +
					" con los campos " + aux);
			lyrDef.setShapeType(layer.getShapeType());
			writer.initialize(lyrDef);
			vea.stopEdition(writer, EditionEvent.GRAPHIC);
			layer.setProperty("stoppingEditing",new Boolean(false));
		} catch (ReadDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InitializeWriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StopWriterVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Modidify a single value of a register.
	 * IMPORTANT: StartEditing and StopEditing is required before and after call this method.
	 * 
	 * @param layer		the layer that contains the feature to be changed.
	 * @param rowIdx	the data row to be changed.
	 * @param attrIdx	the attribute to be changed.
	 * @param newValue	the value to be stored on the table.
	 * 
	 * @deprecated It'll be replaced by modifyValue2
	 */
	//	public void modifyValue(FLyrVect layer, int rowIdx, int attrIdx, Object newValue){
	//	    // NachoV IDEA: Podemos meter los datos en el Table asociado mediante su getModel,
	//	    //               sin hacer show, pero en edicion...para que se
	//	    //                  se encargue de guardarlo gvSIG.
	//
	//	    // It should be done out of this method
	//	    boolean stopEditRequired = false;
	//	    if (!layer.isEditing()) {
	//	    	startEditing(layer);
	//	    	stopEditRequired = true;
	//	    }
	//
	//	    ////////////////////////////////
	//	    // Copied from ShowTable de gvSIG
	//	    ////////////////////////////////
	//	    Table t = null;
	//	    ProjectTable projectTable = null;
	//
	//	    BaseView vista = (BaseView) PluginServices.getMDIManager().getActiveWindow();
	//	    FLayer[] actives = vista.getModel().getMapContext().getLayers().getActives();
	//
	//	    try {
	//	    for (int i = 0; i < actives.length; i++) {
	//	        if (actives[i] instanceof AlphanumericData) {
	//	        AlphanumericData co = (AlphanumericData) actives[i];
	//
	//	        //SelectableDataSource dataSource;
	//	        //dataSource = co.getRecordset();
	//
	//	        ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
	//	        projectTable = ext.getProject().getTable(co);
	//	        EditableAdapter ea=null;
	//	        ReadableVectorial rv=((FLyrVect)actives[i]).getSource();
	//	        if (rv instanceof VectorialEditableAdapter){
	//	            ea=(EditableAdapter)((FLyrVect)actives[i]).getSource();
	//	        } else {
	//	            ea=new EditableAdapter();
	//	            SelectableDataSource sds=((FLyrVect)actives[i]).getRecordset();
	//	            ea.setOriginalDataSource(sds);
	//	        }
	//
	//	        if (projectTable == null) {
	//	            projectTable = ProjectFactory.createTable(PluginServices.getText(this, "Tabla_de_Atributos") + ": " + actives[i].getName(),
	//	                                  ea);
	//	            projectTable.setProjectDocumentFactory(new ProjectTableFactory());
	//	            projectTable.setAssociatedTable(co);
	//	            ext.getProject().addDocument(projectTable);
	//	        }
	//	        projectTable.setModel(ea);
	//	        t = new Table();
	//	        t.setModel(projectTable);
	//	        t.getModel().setModified(true);
	//	        if (ea.isEditing()){
	//	            ea.getCommandRecord().addCommandListener(t);
	//	        }
	//	        //PluginServices.getMDIManager().addWindow(t);
	//	        }
	//	    }
	//	    } catch (com.hardcode.gdbms.engine.data.driver.DriverException e) {
	//	    NotificationManager.addError(PluginServices.getText(this,"errorGettingTable"), e);
	//	    } catch (DriverException e) {
	//	    e.printStackTrace();
	//	    NotificationManager.addError(PluginServices.getText(this,"errorGettingTable"), e);
	//	    }
	//
	//	    ///////////////////////////////
	//	    // END ShowTable de gvSIG
	//	    ///////////////////////////////
	//
	//	    ProjectTable pt = t.getModel();
	//
	//	    try {
	//	    if (pt != null) {
	//	        IEditableSource tableModel= pt.getModelo();
	//	        IRow row;
	//	        row = tableModel.getRow(rowIdx).getLinkedRow();
	//	        Value[] attributes = row.getAttributes();
	//	        ITableDefinition tableDef;
	//	        tableDef = tableModel.getTableDefinition();
	//	        FieldDescription[] fieldDesc = tableDef.getFieldsDesc();
	//	        int type = fieldDesc[attrIdx].getFieldType();
	//	        if (type == 16) {
	//	        	// in this case type is boolean
	//	        	type = Types.BIT;
	//	        }
	//	        System.out.println("El valor " + newValue + " es de tipo " + FieldDescription.typeToString(type));
	//	        attributes[attrIdx] = ValueFactory.createValueByType(newValue.toString(), type);
	//	        row.setAttributes(attributes);
	//	        tableModel.doModifyRow(rowIdx, row, EditionEvent.ALPHANUMERIC);
	//	        pt.setModel(tableModel);
	//	    }
	//	    if (stopEditRequired){
	//	    	stopEditing(layer, false);
	//	    }
	//	    } catch (DriverIOException e) {
	//	    // TODO Auto-generated catch block
	//	    e.printStackTrace();
	//	    } catch (IOException e) {
	//	    // TODO Auto-generated catch block
	//	    e.printStackTrace();
	//	    } catch (DriverLoadException e) {
	//	    // TODO Auto-generated catch block
	//	    e.printStackTrace();
	//	    } catch (com.hardcode.gdbms.engine.data.driver.DriverException e) {
	//	    // TODO Auto-generated catch block
	//	    e.printStackTrace();
	//	    } catch (ParseException e) {
	//	    // TODO Auto-generated catch block
	//	    e.printStackTrace();
	//	    }
	//	}

	/**
	 * Modidify a single value of a register. It creates the new value from its
	 * String representation.
	 * IMPORTANT: StartEditing and StopEditing is required before and after call this method.
	 * 
	 * @param layer		the layer that contains the feature to be changed.
	 * @param rowPos	the data row to be changed.
	 * @param colPos	the data column position to be changed.
	 * @param newValue	the String representation of the value to be saved.
	 * 
	 */
	public void modifyValue(FLyrVect layer, int rowPos, int colPos, String newValue) throws Exception {
		VectorialEditableAdapter edAdapter = (VectorialEditableAdapter) layer.getSource();
		if (newValue == null) {
			newValue = "";
		}
		ITableDefinition tableDef;
		tableDef = edAdapter.getTableDefinition();
		FieldDescription[] fieldDesc = tableDef.getFieldsDesc();
		int type = fieldDesc[colPos].getFieldType();
		if (type == 16) {
			// in this case type is boolean
			type = Types.BIT;
		}
		System.out.println("El valor " + newValue + " es de tipo " + FieldDescription.typeToString(type));
		Value val;
		if (newValue.length() == 0){
			val = ValueFactory.createNullValue();
		}else {
			val = ValueFactory.createValueByType(newValue, type);
		}
		modifyValue(layer, rowPos, colPos, val);
	}

	/**
	 * Modidify a single value of a register.
	 * IMPORTANT: StartEditing and StopEditing is required before and after call this method.
	 * 
	 * @param layer		the layer that contains the feature to be changed.
	 * @param rowPos	the data row to be changed.
	 * @param colPos	the data column position to be changed.
	 * @param newValue	the value to be saved.
	 * 
	 * @throws DriverException
	 * @throws IOException
	 * 
	 */
	public void modifyValue(FLyrVect layer, int rowPos, int colPos, Value newValue) throws Exception {

		if (newValue == null) {
			newValue = ValueFactory.createNullValue();
		}

		VectorialEditableAdapter edAdapter = (VectorialEditableAdapter) layer.getSource();
		IRowEdited row;
		row = edAdapter.getRow(rowPos);
		Value[] attributes = row.getAttributes();
		ITableDefinition tableDef;
		tableDef = edAdapter.getTableDefinition();
		FieldDescription[] fieldDesc = tableDef.getFieldsDesc();
		int type = fieldDesc[colPos].getFieldType();
		if (type == 16) {
			// in this case type is boolean
			type = Types.BIT;
		}

		int valueType = newValue.getSQLType();
		if (valueType == 0 || valueType == -1 || valueType == type) {
			if (row.getLinkedRow() instanceof IFeature) {
				attributes[colPos] = newValue;
				IGeometry geometry = ((DefaultFeature) row.getLinkedRow())
				.getGeometry();
				IRow newRow = new DefaultFeature(geometry, attributes,
						row.getID());
				edAdapter.modifyRow(rowPos, newRow, "NAVTABLE MODIFY", EditionEvent.ALPHANUMERIC);
			} else {
				System.out.println("This is not a geometry!");
			}
		} else {
			System.out.println("Tipo incorrecto: values es " + newValue.getSQLType() + " y el campo es " + type);
		}
	}

	public void modifyValue(IEditableSource source, int rowPos, int colPos, String newValue) throws Exception {

		if (newValue == null) {
			newValue = "";
		}
		ITableDefinition tableDef;
		tableDef = source.getTableDefinition();
		FieldDescription[] fieldDesc = tableDef.getFieldsDesc();
		int type = fieldDesc[colPos].getFieldType();
		if (type == 16) {
			// in this case type is boolean
			type = Types.BIT;
		}
		System.out.println("El valor " + newValue + " es de tipo " + FieldDescription.typeToString(type));
		Value val;
		if (newValue.length() == 0){
			val = ValueFactory.createNullValue();
		}else {
			val = ValueFactory.createValueByType(newValue, type);
		}
		IRowEdited row = source.getRow(rowPos);
		Value[] attributes = row.getAttributes();
		attributes[colPos] = val;
		IRow newRow = new DefaultRow(attributes);
		source.modifyRow(rowPos, newRow, "NAVTABLE MODIFY", EditionEvent.ALPHANUMERIC);
	}

}
