/*
 * Created on 18-sep-2004
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package es.udc.cartolab.gvsig.tocextra.gui;

import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.ValidatingTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.gui.GUIUtil;
/**
 * Panel de Ajuste de Transparencia.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class AdjustTransparencyPanel extends JPanel implements IWindow  {
	JDialog dlg = null;
	private JLabel jLabel = null;
	private JSlider jSlider = null;
	private ValidatingTextField jTxtTransparency = null;
	private JPanel buttonsPanel = null;
	private FLyrDefault fLayer = null;

	private WindowInfo m_ViewInfo = null;

	private JLabel percentLabel = null;
	private JButton btnApply = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;

    private ComandosListener m_actionListener;
    private int oldAlpha = 0;
	public AdjustTransparencyPanel(FLyrDefault layer) {
		super();
		initialize(layer);
	}
	private void initialize(FLyrDefault layer) {
		percentLabel = new JLabel();
		jLabel = new JLabel();
		setBounds(0,0,255,133);
        setLayout(null);

        setFLayer(layer);
        oldAlpha = layer.getTransparency();
        jLabel.setBounds(15, 17, 195, 21);
        jLabel.setText("Nivel de transparencia:");
        jLabel.setText(PluginServices.getText(this, "Nivel_de_transparencia")+":");
        percentLabel.setBounds(225, 55, 21, 20);
        percentLabel.setText("%");
        this.add(jLabel, null);
        this.add(getJSlider(), null);
        this.add(getJTxtTransparency(), null);
        this.add(percentLabel, null);

        this.add(getButtonsPanel(), null);

	}

	public void setFLayer(FLyrDefault f) {
		fLayer = f;
		setAlpha(fLayer.getTransparency());
	}
	public void setAlpha(int alpha) {
		getJSlider().setValue(alpha*100/255);
	}

	public int getAlpha() {
		return getJSlider().getValue()*255/100;
	}

	/**
	 * This method initializes jSlider
	 *
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setBounds(15, 55, 165, 21);
			jSlider.setMaximum(100);
			jSlider.setMajorTickSpacing(5);
		}
		return jSlider;
	}
	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTxtTransparency() {
		if (jTxtTransparency == null) {
			jTxtTransparency = GUIUtil.createSyncdTextField(jSlider, 3);
			jTxtTransparency.setBounds(190, 55, 30, 21);
			//jTxtTransparency.setText("" + getAlpha());
		}
		return jTxtTransparency;
	}
	public JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			m_actionListener = new ComandosListener(this);
			buttonsPanel = new JPanel();
	        buttonsPanel.setBounds(10, 98, 235, 25);

			FlowLayout flowLayout2 = new FlowLayout();
			buttonsPanel.setLayout(flowLayout2);
			flowLayout2.setHgap(5);
			flowLayout2.setAlignment(java.awt.FlowLayout.RIGHT);
			flowLayout2.setVgap(1);
			//buttonsPanel.setPreferredSize(new java.awt.Dimension(225,25));
			buttonsPanel.setName("buttonPanel");

	        buttonsPanel.add(getBtnOk(), null);
	        buttonsPanel.add(getBtnApply(), null);
	        buttonsPanel.add(getBtnCancel(), null);
		}
		return buttonsPanel;
	}

	public JButton getBtnOk() {
		if (btnOk == null) {
	        btnOk = new JButton("Aceptar");
	        btnOk.setText(PluginServices.getText(this,"Aceptar"));
	        btnOk.setActionCommand("OK");
	        btnOk.addActionListener(m_actionListener);
		}
		return btnOk;
	}

	public JButton getBtnApply() {
		if (btnApply == null) {
	        btnApply = new JButton("Aplicar");
	        btnApply.setText(PluginServices.getText(this,"Aplicar"));
	        btnApply.setActionCommand("APPLY");
	        btnApply.addActionListener(m_actionListener);
		}
		return btnApply;
	}

	public JButton getBtnCancel() {
		if (btnCancel == null) {
	        btnCancel = new JButton("Cancelar");
	        btnCancel.setText(PluginServices.getText(this,"Cancelar"));
	        btnCancel.setActionCommand("CANCEL");
	        btnCancel.addActionListener(m_actionListener);
		}
		return btnCancel;
	}

    private class ComandosListener implements ActionListener {
        private AdjustTransparencyPanel m_tp;

        /**
         * Creates a new ComandosListener object.
         *
         * @param lg DOCUMENT ME!
         */
        public ComandosListener(AdjustTransparencyPanel tp) {
            m_tp = tp;
        }

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == "OK") {
        		fLayer.setTransparency(getAlpha());
				AdjustTransparencyPanel.this.closeJDialog();
            } else if (e.getActionCommand() == "APPLY") {
        		fLayer.setTransparency(getAlpha());
            } else if (e.getActionCommand() == "CANCEL") {
            	if (oldAlpha != fLayer.getTransparency())
            		fLayer.setTransparency(oldAlpha);
				AdjustTransparencyPanel.this.closeJDialog();
            }
		}
    }

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		if (m_ViewInfo==null){
			m_ViewInfo=new WindowInfo(WindowInfo.MODALDIALOG);
			m_ViewInfo.setTitle(PluginServices.getText(this,"Ajustar_transparencia"));
		}
		return m_ViewInfo;
	}
	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#viewActivated()
	 */
	public void viewActivated() {
		// TODO Auto-generated method stub

	}
	public void openJDialog() {
		if (PluginServices.getMainFrame() == null) {
			dlg = new JDialog();
			Rectangle bnds = getBounds();
			bnds.width += 10;
			bnds.height += 33;
			dlg.setBounds(bnds);
			dlg.setSize(getSize());
			dlg.getContentPane().add(this);
			dlg.setModal(true);
			dlg.pack();
			dlg.setVisible(true);
		} else
			PluginServices.getMDIManager().addWindow(this);
	}
	public void closeJDialog() {
		if (PluginServices.getMainFrame() == null) {
			dlg.setVisible(false);
			dlg.dispose();
			dlg = null;
		} else
			PluginServices.getMDIManager().closeWindow(this);


	}
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

   }  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
