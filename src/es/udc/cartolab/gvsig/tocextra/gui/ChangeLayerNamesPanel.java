/*
 * This file is part of TOCExtra
 * Copyright (C) 2009 - 2011 Fundación de Ingeniería Civil de Galicia
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

package es.udc.cartolab.gvsig.tocextra.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.layers.FLayer;

public class ChangeLayerNamesPanel extends JPanel implements IWindow,
		ActionListener {

	private FLayer[] layers = null;
	private JPanel southPanel = null;
	private JPanel centerPanel = null;
	private JButton okButton, cancelButton;
	private WindowInfo viewInfo = null;
	private AttribTableCellRenderer cellRenderer = null;
	private JTable table;

	public WindowInfo getWindowInfo() {

		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
					| WindowInfo.PALETTE);
			viewInfo.setTitle(PluginServices.getText(this,
					"Change_Names_window"));
			viewInfo.setWidth(425);
			if (layers != null) {
				if (layers.length < 4) {
					viewInfo.setHeight(75);
				} else if (layers.length < 7) {
					viewInfo.setHeight(140);
				} else if (layers.length < 12) {
					viewInfo.setHeight(225);
				} else {
					viewInfo.setHeight(400);
				}
			} else {
				viewInfo.setHeight(100);
			}
		}
		return viewInfo;

	}

	public ChangeLayerNamesPanel(FLayer[] layers) {
		this.layers = layers;
		init();
	}

	private void init() {

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(getCenterPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		add(getSouthPanel(), new GridBagConstraints(0, 25, 1, 1, 0, 0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

	}

	private JPanel getCenterPanel() {

		if (centerPanel == null) {
			centerPanel = new JPanel();

			GridBagLayout glayout = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();

			c.weightx = 1.0;
			c.weighty = 1.0;
			c.fill = GridBagConstraints.BOTH;
			centerPanel.setLayout(glayout);

			MyTableModel model = new MyTableModel();
			table = new JTable(model) {

				public void changeSelection(final int row, final int column,
						boolean toggle, boolean extend) {
					if (column == 0) {
						changeSelection(row, column + 1, toggle, extend);
					} else {
						super.changeSelection(row, column, toggle, extend);
						table.editCellAt(row, column);
						table.transferFocus();
					}
				}

			};

			this.cellRenderer = new AttribTableCellRenderer();

			model.addColumn(PluginServices.getText(this, "headerTableOldValue"));
			model.addColumn(PluginServices.getText(this, "headerTableNewValue"));

			TableColumn attribColumn = table.getColumn(PluginServices.getText(
					this, "headerTableOldValue"));
			attribColumn.setCellRenderer(this.cellRenderer);
			attribColumn = table.getColumn(PluginServices.getText(this,
					"headerTableNewValue"));
			attribColumn.setCellRenderer(this.cellRenderer);

			for (int i = layers.length - 1; i >= 0; i--) {
				Vector aux = new Vector();
				String layerName = layers[i].getName();
				aux.add(layerName);
				aux.add(layerName);
				model.addRow(aux);
				model.fireTableRowsInserted(model.getRowCount() - 1,
						model.getRowCount() - 1);
			}

			JScrollPane scrollPane = new JScrollPane(table);
			centerPanel.add(scrollPane, c);

			// table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(true);

		}
		return centerPanel;
	}

	protected JPanel getSouthPanel() {

		if (southPanel == null) {
			southPanel = new JPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.RIGHT);
			southPanel.setLayout(layout);
			okButton = new JButton("Aceptar");
			cancelButton = new JButton("Cancelar");
			okButton.addActionListener(this);
			cancelButton.addActionListener(this);
			southPanel.add(okButton);
			southPanel.add(cancelButton);
		}
		return southPanel;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == cancelButton) {
			PluginServices.getMDIManager().closeWindow(this);
		}
		if (e.getSource() == okButton) {
			if (table.isEditing()) {
				if (table.getCellEditor() != null) {
					table.getCellEditor().stopCellEditing();
				}
			}
			TableModel model = table.getModel();
			for (int i = 0; i < model.getRowCount(); i++) {
				String text = (String) model.getValueAt(i, 1);
				String currentLayerName = layers[layers.length - i - 1]
						.getName();
				if (text.compareTo(currentLayerName) != 0) {
					layers[layers.length - i - 1].setName(text);
				}
			}

			PluginServices.getMDIManager().closeWindow(this);
		}
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

}
