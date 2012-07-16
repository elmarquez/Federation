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
package ca.sfu.federation.viewer.explorer;

import ca.sfu.federation.Application;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * Project explorer panel.
 * @author Davis Marques
 */
public class AbstractTreeExplorerPanel extends JPanel implements ActionListener, TreeSelectionListener {
    
    private AbstractTree tree;
    private JScrollPane scrollPane;
    
    //--------------------------------------------------------------------------

    /**
     * AbstractTreeExplorerPanel constructor.
     */
    public AbstractTreeExplorerPanel() {
        // create tree
        this.tree = new AbstractTree(buildTree());
        this.tree.addTreeSelectionListener(this);
        this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);        
        this.tree.putClientProperty("JTree.lineStyle","Horizontal");
        this.tree.setCellRenderer(new AbstractTreeExplorerCellRenderer());
        // create scrollpane for tree
        this.scrollPane = new JScrollPane(this.tree);
        // add scrollpane to panel
        this.setLayout(new BorderLayout());
        this.add(this.scrollPane,BorderLayout.CENTER);
    }
    
    //--------------------------------------------------------------------------

    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Build the subtree for the object.
     * @param Named Named object.
     */
    private DefaultMutableTreeNode buildSubTree(INamed Named) {
        // create node for self
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(Named);
        // create nodes for sub elements
        if (Named instanceof Scenario) {
            Scenario scenario = (Scenario) Named;
            // add contextual subnodes
            DefaultMutableTreeNode contextualNodes = new DefaultMutableTreeNode("Contextual Elements");
            LinkedHashMap contextual = (LinkedHashMap) scenario.getContextualElements();
            Iterator iter = contextual.values().iterator();
            while (iter.hasNext()) {
                INamed named = (INamed) iter.next();
                DefaultMutableTreeNode subnode = buildSubTree(named);
                contextualNodes.add(subnode);
            }
            node.add(contextualNodes);
            // add transactional subnodes
            DefaultMutableTreeNode transactionalNodes = new DefaultMutableTreeNode("Transactional Elements");
            LinkedHashMap transactional = (LinkedHashMap) scenario.getElements();
            Iterator itr = transactional.values().iterator();
            while (itr.hasNext()) {
                INamed named = (INamed) itr.next();
                DefaultMutableTreeNode subnode = buildSubTree(named);
                transactionalNodes.add(subnode);
            }
            node.add(transactionalNodes);
        } else if (Named instanceof IContext) {
            IContext context = (IContext) Named;
            // add subnodes
            LinkedHashMap elements = (LinkedHashMap) context.getElements();
            Iterator iter = elements.values().iterator();
            while (iter.hasNext()) {
                INamed named = (INamed) iter.next();
                DefaultMutableTreeNode subnode = buildSubTree(named);
                node.add(subnode);
            }
        }
        // return result
        return node;
    }
    
    /**
     * Build abstract model tree.
     * @return JTree representation of parametric model.
     */
    private DefaultMutableTreeNode buildTree() {
        // create tree
        ParametricModel model = Application.getContext().getModel();
        DefaultMutableTreeNode root = buildSubTree(model);
        // return tree
        return root;
    }

    /**
     * Tree selection listener.
     * @param e Tree selection event.
     */
    public void valueChanged(TreeSelectionEvent e) {
    }
    
} // end class
