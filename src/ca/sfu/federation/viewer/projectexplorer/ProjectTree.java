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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * Project tree model
 * @author Davis Marques
 */
public class ProjectTree extends JTree {

    private static final Logger logger = Logger.getLogger(ProjectTree.class.getName());

    //--------------------------------------------------------------------------

    /**
     * ProjectTree constructor.
     * @param Node Tree node
     */
    public ProjectTree(DefaultMutableTreeNode Node) {
        super(Node);
        // popup = new ProjectTreePopupMenu();
        addMouseListener(new ProjectTreeMouseAdapter(this));
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        // putClientProperty("JTree.lineStyle","Horizontal");
        setCellRenderer(new ProjectTreeCellRenderer());
    }

} 