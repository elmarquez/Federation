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

import ca.sfu.federation.viewer.MessageAction;
import com.javadocking.DockingManager;
import com.javadocking.dock.LineDock;
import com.javadocking.dock.Position;
import com.javadocking.dockable.ButtonDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockingMode;
import com.javadocking.drag.DragListener;
import com.javadocking.util.ToolBarButton;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Project toolbar.
 * @author dmarques
 */
public class ViewToolbar extends LineDock {

    /**
     * ProjectToolbar default constructor
     */
    public ViewToolbar() {
        super(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        init();
    }
    
    /**
     * Creates a dockable with a button as content.
     * @param id	The ID of the dockable that has to be created.
     * @param title	The title of the dialog that will be displayed.
     * @param icon	The icon that will be put on the button.
     * @param message	The message that will be displayed when the action is
     * performed.
     * @return	The dockable with a button as content.
     */
    private Dockable createButtonDockable(String id, String title, Icon icon, String message) {
        MessageAction action = new MessageAction(this, title, icon, message);
        ToolBarButton button = new ToolBarButton(action);
        ButtonDockable buttonDockable = new ButtonDockable(id, button);
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
        Dockable btn1 = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add.png")), "Add!");
        Dockable btn2 = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add.png")), "Add!");
        Dockable btn3 = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add.png")), "Add!");
        Dockable btn4 = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add.png")), "Add!");
        Dockable btn5 = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add.png")), "Add!");
        Dockable btn6 = createButtonDockable("ButtonDockableAdd", "Add", new ImageIcon(getClass().getResource("/com/javadocking/resources/icons/add.png")), "Add!");
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
