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
import ca.sfu.federation.utils.ImageIconUtils;
import ca.sfu.federation.viewer.ScenarioPopupMenu;
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
public class ProjectTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Logger logger = Logger.getLogger(ProjectTreeCellRenderer.class.getName());
    
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
                    renderINamed(node,(INamed) userObject);
                } else if (node.getUserObject().equals(ProjectExplorerPanel.CONTEXTUAL_LABEL)) {
                    this.setIcon(null);
                    this.setLeafIcon(null);
                    this.setClosedIcon(null);
                    this.setText(ProjectExplorerPanel.CONTEXTUAL_LABEL);
                    this.setToolTipText(ProjectExplorerPanel.CONTEXTUAL_LABEL);
                } else if (node.getUserObject().equals(ProjectExplorerPanel.TRANSACTIONAL_LABEL)) {
                    this.setIcon(null);
                    this.setLeafIcon(null);
                    this.setClosedIcon(null);
                    this.setText(ProjectExplorerPanel.TRANSACTIONAL_LABEL);
                    this.setToolTipText(ProjectExplorerPanel.TRANSACTIONAL_LABEL);
                } else if (node.getUserObject().equals(ProjectExplorerPanel.MODEL_PACKAGE_ID)) {
                    ImageIcon icon = ImageIconUtils.loadIconById("view-explorer-icon");
                    this.setIcon(icon);
                    this.setText(ProjectExplorerPanel.MODEL_PACKAGE_LABEL);
                } else if (node.getUserObject().equals(ProjectExplorerPanel.LIBRARY_NODE_ID)) {
                    ImageIcon icon = ImageIconUtils.loadIconById("view-explorer-icon");
                    this.setIcon(icon);
                    this.setText(ProjectExplorerPanel.LIBRARY_LABEL);
                } else if (node.getUserObject().equals(ProjectExplorerPanel.TEST_PACKAGE_NODE_ID)) {
                    ImageIcon icon = ImageIconUtils.loadIconById("view-explorer-icon");
                    this.setIcon(icon);
                    this.setText(ProjectExplorerPanel.TEST_PACKAGES_LABEL);
                } else if (node.getUserObject().equals(ProjectExplorerPanel.TEST_LIBRARY_ID)) {
                    ImageIcon icon = ImageIconUtils.loadIconById("view-explorer-icon");
                    this.setIcon(icon);
                    this.setText(ProjectExplorerPanel.TEST_LIBRARIES_LABEL);
                }
            }
        }
        // return result
        return this;
    }

    private void renderAssembly() {
        
    }

    private void renderINamed(DefaultMutableTreeNode Node, INamed Named) {
        this.setText(Named.getName());
        this.setToolTipText(Named.getName());
        if (Named instanceof ParametricModel) {
            ParametricModel obj = (ParametricModel) Named;
            ImageIcon icon = obj.getIcon();
            this.setIcon(icon);
            this.setLeafIcon(icon);
            this.setClosedIcon(icon);
        } else if (Named instanceof Scenario) {
            Scenario obj = (Scenario) Named;
            ImageIcon icon = obj.getIcon();
            this.add(new ScenarioPopupMenu(obj));
            // this.setBackground(Color.LIGHT_GRAY);
            this.setClosedIcon(icon);
            this.setIcon(icon);
            this.setLeafIcon(icon);
        } else if (Named instanceof Assembly) {
            Assembly obj = (Assembly) Named;
            ImageIcon icon = obj.getIcon();
            this.setIcon(icon);
            this.setLeafIcon(icon);
            this.setClosedIcon(icon);
        } else if (Named instanceof Component) {
            Component obj = (Component) Named;
            ImageIcon icon = obj.getIcon();
            this.setIcon(icon);
            this.setLeafIcon(icon);
            this.setClosedIcon(icon);
        } else if (Named instanceof IContext) {
            IContext obj = (IContext) Named;
            ImageIcon icon = obj.getIcon();
            this.setIcon(icon);
            this.setLeafIcon(icon);
            this.setClosedIcon(icon);
        } else {
            INamed obj = (INamed) Named;
            ImageIcon icon = obj.getIcon();
            this.setIcon(icon);
            this.setLeafIcon(icon);
            this.setClosedIcon(icon);
        }
    }
    
    private void renderLabel() {
        
    }
    
    private void renderParametricModel() {
        
    }
    
    private void renderPackage() {
        
    }
    
}  
