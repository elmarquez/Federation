/**
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package ca.sfu.federation.viewer.projectexplorer;

import ca.sfu.federation.model.*;
import ca.sfu.federation.viewer.ScenarioPopupMenu;
import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * INamed tree cell renderer.
 * @author Davis Marques
 */
public class INamedTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Logger logger = Logger.getLogger(INamedTreeCellRenderer.class.getName());

    private static final String CONTEXTUAL_LABEL = "Contextual";
    private static final String TRANSACTIONAL_LABEL = "Transactional";
    
    //--------------------------------------------------------------------------
    
    /**
     * Get the cell renderer component.
     * @param tree
     * @param value
     * @param sel
     * @param expanded
     * @param leaf
     * @param row
     * @param hasFocus
     * @return Component
     */
    @Override
    public JComponent getTreeCellRendererComponent(JTree tree,
                                                   Object value,
                                                   boolean sel,
                                                   boolean expanded,
                                                   boolean leaf,
                                                   int row,
                                                   boolean hasFocus) {
        // init
        super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
        // set icon and label values
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();
            if (userObject != null) {
                if (userObject instanceof INamed) {
                    INamed named = (INamed) userObject;
                    this.setText(named.getName());
                    this.setToolTipText(named.getName());
                    if (named instanceof ParametricModel) {
                        ParametricModel obj = (ParametricModel) named;
                        ImageIcon icon = obj.getIcon();
                        this.setIcon(icon);
                        this.setLeafIcon(icon);
                        this.setClosedIcon(icon);
                    } else if (named instanceof Scenario) {
                        Scenario obj = (Scenario) named;
                        ImageIcon icon = obj.getIcon();
                        this.add(new ScenarioPopupMenu(obj));
                        this.setBackground(Color.LIGHT_GRAY);
                        this.setClosedIcon(icon);
                        this.setIcon(icon);
                        this.setLeafIcon(icon);
                    } else if (named instanceof Assembly) {
                        Assembly obj = (Assembly) named;
                        ImageIcon icon = obj.getIcon();
                        this.setIcon(icon);
                        this.setLeafIcon(icon);
                        this.setClosedIcon(icon);
                    } else if (named instanceof Component) {
                        Component obj = (Component) named;
                        ImageIcon icon = obj.getIcon();
                        this.setIcon(icon);
                        this.setLeafIcon(icon);
                        this.setClosedIcon(icon);
                    } else if (named instanceof IContext) {
                        IContext obj = (IContext) named;
                        ImageIcon icon = obj.getIcon();
                        this.setIcon(icon);
                        this.setLeafIcon(icon);
                        this.setClosedIcon(icon);
                    } else {
                        INamed obj = (INamed) named;
                        ImageIcon icon = obj.getIcon();
                        this.setIcon(icon);
                        this.setLeafIcon(icon);
                        this.setClosedIcon(icon);
                    }
                } else if (node.getUserObject().equals(CONTEXTUAL_LABEL)) {
                    this.setIcon(null);
                    this.setLeafIcon(null);
                    this.setClosedIcon(null);
                    this.setText(CONTEXTUAL_LABEL);
                    this.setToolTipText(CONTEXTUAL_LABEL);
                } else if (node.getUserObject().equals(TRANSACTIONAL_LABEL)) {
                    this.setIcon(null);
                    this.setLeafIcon(null);
                    this.setClosedIcon(null);
                    this.setText(TRANSACTIONAL_LABEL);
                    this.setToolTipText(TRANSACTIONAL_LABEL);
                }
            }
        }
        // return result
        return this;
    }
    
}  
