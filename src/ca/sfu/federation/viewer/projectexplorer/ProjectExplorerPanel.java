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
import java.util.*;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Project explorer panel.
 * @author Davis Marques
 */
public class ProjectExplorerPanel extends JPanel implements ActionListener, Observer, TreeSelectionListener {

    private static final Logger logger = Logger.getLogger(ProjectExplorerPanel.class.getName());

    private JTree tree;
    private JScrollPane scrollPane;

    public static final int PROJECT_ROOT = 1;

    public static final int MODEL_PACKAGE_ID = 10;
    public static final int LIBRARY_NODE_ID = 20;
    public static final int TEST_PACKAGE_NODE_ID = 30;
    public static final int TEST_LIBRARY_ID = 40;

    public static final String MODEL_PACKAGE_LABEL = "Model";
    public static final String LIBRARY_LABEL = "Libraries";
    public static final String TEST_PACKAGES_LABEL = "Test Packages";
    public static final String TEST_LIBRARIES_LABEL = "Test Libraries";

    public static final String CONTEXTUAL_LABEL = "Contextual";
    public static final String TRANSACTIONAL_LABEL = "Transactional";

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

    private DefaultMutableTreeNode buildModelLibraryTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ProjectExplorerPanel.LIBRARY_LABEL);
        root.setUserObject(ProjectExplorerPanel.LIBRARY_NODE_ID);
        return root;
    }

    private DefaultMutableTreeNode buildModelPackageSubTree(INamed Named) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(Named);
        if (Named instanceof Scenario) {
            Scenario scenario = (Scenario) Named;
            // add contextual subnodes
            DefaultMutableTreeNode contextualNodes = new DefaultMutableTreeNode(ProjectExplorerPanel.CONTEXTUAL_LABEL);
            Map<String,INamed> contextual = scenario.getContextualElements();
            Iterator<INamed> iter = contextual.values().iterator();
            while (iter.hasNext()) {
                INamed named = iter.next();
                DefaultMutableTreeNode node = buildModelPackageSubTree(named);
                contextualNodes.add(node);
            }
            root.add(contextualNodes);
            // add transactional subnodes
            DefaultMutableTreeNode transactionalNodes = new DefaultMutableTreeNode(ProjectExplorerPanel.TRANSACTIONAL_LABEL);
            Map<String,INamed> transactional = scenario.getElementMap();
            Iterator<INamed> itr = transactional.values().iterator();
            while (itr.hasNext()) {
                INamed named = itr.next();
                DefaultMutableTreeNode node = buildModelPackageSubTree(named);
                transactionalNodes.add(node);
            }
            root.add(transactionalNodes);
        } else if (Named instanceof IContext) {
            IContext context = (IContext) Named;
            // add subnodes
            LinkedHashMap elements = (LinkedHashMap) context.getElementMap();
            Iterator iter = elements.values().iterator();
            while (iter.hasNext()) {
                INamed named = (INamed) iter.next();
                DefaultMutableTreeNode node = buildModelPackageSubTree(named);
                root.add(node);
            }
        }
        return root;
    }
    
    /**
     * Build the model tree.
     * @param Named Named object.
     */
    private DefaultMutableTreeNode buildModelPackageTree(INamed Named) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ProjectExplorerPanel.MODEL_PACKAGE_LABEL);
        root.setUserObject(ProjectExplorerPanel.MODEL_PACKAGE_ID);
        // create child nodes for model elements
        if (Named instanceof ParametricModel) {
            ParametricModel model = (ParametricModel) Named;
            List<INamed> elements = model.getElements();
            Iterator<INamed> it = elements.iterator();
            while (it.hasNext()) {
                INamed named = it.next();
                DefaultMutableTreeNode node = buildModelPackageSubTree(named);
                root.add(node);
            }
        }        
        // return result
        return root;
    }

    private DefaultMutableTreeNode buildTestLibraryTree() {
        DefaultMutableTreeNode theroot = new DefaultMutableTreeNode(ProjectExplorerPanel.TEST_LIBRARIES_LABEL);
        theroot.setUserObject(ProjectExplorerPanel.TEST_LIBRARY_ID);
        return theroot;
    }

    private DefaultMutableTreeNode buildTestPackageTree() {
        DefaultMutableTreeNode theroot = new DefaultMutableTreeNode(ProjectExplorerPanel.TEST_PACKAGES_LABEL);
        theroot.setUserObject(ProjectExplorerPanel.TEST_PACKAGE_NODE_ID);
        return theroot;
    }

    private void buildTree() {
        DefaultMutableTreeNode model = buildTreeModel();
        tree = new ProjectTree(model);
    }

    /**
     * Build abstract model tree.
     * @return Tree representation of model.
     */
    private DefaultMutableTreeNode buildTreeModel() {
        ParametricModel model = Application.getContext().getModel();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(model);
        DefaultMutableTreeNode modelpackagetree = buildModelPackageTree(model);
        DefaultMutableTreeNode testpackagetree = buildTestPackageTree();
        DefaultMutableTreeNode modellibrarytree = buildModelLibraryTree();
        DefaultMutableTreeNode testlibrarytree = buildTestLibraryTree();

        root.add(modelpackagetree);
        root.add(modellibrarytree);
        root.add(testpackagetree);
        root.add(testlibrarytree);

        return root;
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
                    this.tree.removeAll();
                    this.tree = null;
                    break;
                case ApplicationContext.MODEL_LOADED:
                    buildTree();
                    this.scrollPane.setViewportView(this.tree);
                    break;
            }
        }
    }

}
