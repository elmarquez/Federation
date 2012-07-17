/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ca.sfu.federation.viewer.toolbars;

import ca.sfu.federation.action.*;
import com.javadocking.DockingManager;
import com.javadocking.dock.LineDock;
import com.javadocking.dock.Position;
import com.javadocking.dockable.ButtonDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockingMode;
import com.javadocking.drag.DragListener;
import com.javadocking.util.ToolBarButton;
import java.util.ArrayList;
import javax.swing.Action;

/**
 * Edit toolbar.
 * @author dmarques
 */
public class EditToolbar extends LineDock {

    /**
     * ProjectToolbar default constructor
     */
    public EditToolbar() {
        super(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        init();
    }
    
    /**
     * Creates a dockable with a button as content.
     * @param id The ID of the dockable that has to be created.
     * @param A Action
     * @return	The dockable with a button as content.
     */
    private Dockable createButtonDockable(String Id, Action A) {
        ToolBarButton button = new ToolBarButton(A);
        ButtonDockable buttonDockable = new ButtonDockable(Id, button);
        createDockableDragger(buttonDockable);
        return buttonDockable;
    }

    /**
     * Adds a drag listener on the content component of a dockable.
     */
    private void createDockableDragger(Dockable dockable) {
        DragListener dragListener = DockingManager.getDockableDragListenerFactory().createDragListener(dockable);
        dockable.getContent().addMouseListener(dragListener);
        dockable.getContent().addMouseMotionListener(dragListener);
    }

    /**
     * Initialize the component.
     */
    private void init() {
        // create buttons
        ArrayList<Dockable> items = new ArrayList<Dockable>();
        Dockable btn1 = createButtonDockable("edit-undo", new EditUndoAction());
        Dockable btn2 = createButtonDockable("edit-redo", new EditRedoAction());
        Dockable btn3 = createButtonDockable("edit-cut", new EditCutAction());
        Dockable btn4 = createButtonDockable("edit-copy", new EditCopyAction());
        Dockable btn5 = createButtonDockable("edit-paste", new EditPasteAction());
        Dockable btn6 = createButtonDockable("edit-delete", new EditDeleteAction());
        // add items to list
        items.add(btn1);
        items.add(btn2);
        items.add(btn3);
        items.add(btn4);
        items.add(btn5);
        items.add(btn6);        
        // add buttons to dock
        for (int i=0;i<items.size();i++) {
            this.addDockable(items.get(i), new Position(i));
        }
    }
    
}
