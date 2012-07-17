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
import java.util.logging.Logger;
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
public class AbstractModelTree extends JTree implements ActionListener {

    private static final Logger logger = Logger.getLogger(AbstractModelTree.class.getName());

    private JPopupMenu popup;
    private JMenuItem mi;
    private AbstractModelTree self;

    //--------------------------------------------------------------------------

    /**
     * AbstractModelTree constructor.
     */
    public AbstractModelTree(DefaultMutableTreeNode Node) {
        super(Node);
        self = this;
        popup = getPopup();
        addMouseListener(getMouseAdapter());
    }

    //--------------------------------------------------------------------------


    public void actionPerformed(ActionEvent e) {
    }

    private MouseAdapter getMouseAdapter() {
        return new MouseAdapter() {
            @Override
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
        };
    }

    private JPopupMenu getPopup() {
        JPopupMenu menu = new JPopupMenu();
        mi = new JMenuItem("Insert a child");
        mi.addActionListener(this);
        mi.setActionCommand("insert");
        menu.add(mi);
        mi = new JMenuItem("Remove this node");
        mi.addActionListener(this);
        mi.setActionCommand("remove");
        menu.add(mi);
        menu.setOpaque(true);
        menu.setLightWeightPopupEnabled(true);
        return menu;
    }

} 