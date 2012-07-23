/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author dmarques
 */
public class ProjectTreeMouseAdapter extends MouseAdapter {

    private JTree tree;
    
    /**
     * ProjectTreeMouseAdapter constructor
     * @param Tree 
     */
    public ProjectTreeMouseAdapter(JTree Tree) {
        tree = Tree;
    }

    /**
     * Handle mouse released event.
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            TreePath path = tree.getClosestPathForLocation(e.getX(),e.getY());
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

}
    
