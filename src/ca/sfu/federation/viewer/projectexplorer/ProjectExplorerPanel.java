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

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;
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
public class ProjectExplorerPanel extends JPanel implements ActionListener, Observer, TreeSelectionListener {

    private static final Logger logger = Logger.getLogger(ProjectExplorerPanel.class.getName());

    private AbstractModelTree tree;
    private JScrollPane scrollPane;
    private DefaultMutableTreeNode root;

    //--------------------------------------------------------------------------

    /**
     * ProjectExplorerPanel constructor.
     */
    public ProjectExplorerPanel() {
        // observe the application context
        Application.getContext().addObserver(this);
        // add scrollpane to panel
        this.scrollPane = new JScrollPane();
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
        // create child nodes for model elements
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
            LinkedHashMap transactional = (LinkedHashMap) scenario.getElementMap();
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
            LinkedHashMap elements = (LinkedHashMap) context.getElementMap();
            Iterator iter = elements.values().iterator();
            while (iter.hasNext()) {
                INamed named = (INamed) iter.next();
                DefaultMutableTreeNode subnode = buildSubTree(named);
                node.add(subnode);
            }
        }
        // create child nodes for library packages
        DefaultMutableTreeNode testpackages = new DefaultMutableTreeNode("Test Packages");
        DefaultMutableTreeNode libraries = new DefaultMutableTreeNode("Libraries");
        DefaultMutableTreeNode testlibraries = new DefaultMutableTreeNode("Test Libraries");
        node.add(testpackages);
        node.add(libraries);
        node.add(testlibraries);
        // return result
        return node;
    }

    /**
     * Build abstract model tree.
     * @return Tree representation of model.
     */
    private DefaultMutableTreeNode buildTree() {
        // create tree
        ParametricModel model = Application.getContext().getModel();
        DefaultMutableTreeNode theroot = buildSubTree(model);
        // return tree
        return theroot;
    }

    /**
     * Tree selection listener.
     * @param e Tree selection event.
     */
    public void valueChanged(TreeSelectionEvent e) {
    }

    /**
     * Handle update event.
     * @param o Obserable object
     * @param arg Update Argument
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            switch (eventId) {
                case ApplicationContext.MODEL_CLOSED:
                    this.scrollPane.getViewport().removeAll();
                    this.root.removeAllChildren();
                    this.tree.removeAll();
                    this.root = null;
                    this.tree = null;
                    break;
                case ApplicationContext.MODEL_LOADED:
                    this.root = buildTree();
                    this.tree = new AbstractModelTree(this.root);
                    this.tree.addTreeSelectionListener(this);
                    this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                    this.tree.putClientProperty("JTree.lineStyle","Horizontal");
                    this.tree.setCellRenderer(new INamedTreeCellRenderer());
                    this.scrollPane.setViewportView(this.tree);
                    break;
            }
        }
    }

} 
