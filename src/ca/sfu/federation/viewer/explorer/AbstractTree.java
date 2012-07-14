/**
 * AbstractTree.java
 * * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
 *
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

package ca.sfu.federation.viewer.explorer;

import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.Component;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.viewer.AssemblyPopupMenu;
import ca.sfu.federation.viewer.ComponentPopupMenu;
import ca.sfu.federation.viewer.ParametricModelPopupMenu;
import ca.sfu.federation.viewer.ScenarioPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author Davis Marques
 * @version 0.1.0
 */
public class AbstractTree extends JTree implements ActionListener {
    
    //--------------------------------------------------------------------------

    
    private JPopupMenu popup;
    private JMenuItem mi;
    private AbstractTree self;
    
    //--------------------------------------------------------------------------

    
    /**
     * AbstractTree constructor.
     */
    public AbstractTree(DefaultMutableTreeNode Node) {
        super(Node);
        self = this;
        // define the popup
        popup = new JPopupMenu();
        mi = new JMenuItem("Insert a children");
        mi.addActionListener(this);
        mi.setActionCommand("insert");
        popup.add(mi);
        mi = new JMenuItem("Remove this node");
        mi.addActionListener(this);
        mi.setActionCommand("remove");
        popup.add(mi);
        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);
        // add mouse listener
        addMouseListener(new MouseAdapter() {
            public void mouseReleased( MouseEvent e ) {
                if (e.isPopupTrigger()) {
                    TreePath path = self.getClosestPathForLocation(e.getX(),e.getY());
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    // set popup menu based on class of target object
                    Object target = node.getUserObject();
                    JPopupMenu menu = null;
                    if (target instanceof ParametricModel) {
                        menu = new ParametricModelPopupMenu();
                    } else if (target instanceof Scenario) {
                        menu = new ScenarioPopupMenu((Scenario)target);
                    } else if (target instanceof Assembly) {
                        menu = new AssemblyPopupMenu((Assembly)target);
                    } else if (target instanceof Component) {
                        menu = new ComponentPopupMenu((Component)target);
                    }
                    menu.show((JComponent)e.getSource(),e.getX(),e.getY());
                }
            }
        });
    }
    
    //--------------------------------------------------------------------------

    
    public void actionPerformed(ActionEvent e) {
    }
    
} // end class
