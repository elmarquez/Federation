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
package ca.sfu.federation.viewer;

import ca.sfu.federation.viewer.DraggingListener;
import com.javadocking.DockingManager;
import com.javadocking.drag.DraggerFactory;
import com.javadocking.drag.StaticDraggerFactory;
import com.javadocking.drag.painter.CompositeDockableDragPainter;
import com.javadocking.drag.painter.DockableDragPainter;
import javax.swing.JRadioButtonMenuItem;

/**
 * A check box menu item to enable a dragger.
 * @author dmarques
 */
public class DraggingMenuItem extends JRadioButtonMenuItem {

    /**
     * DraggingMenuItem constructor
     * @param title
     * @param basicDockableDragPainter
     * @param additionalDockableDragPainter
     * @param selected 
     */
    public DraggingMenuItem(String title, 
                            DockableDragPainter basicDockableDragPainter, 
                            DockableDragPainter additionalDockableDragPainter, 
                            boolean selected) {
        super(title);
        // Create the dockable drag painter and dragger factory.
        CompositeDockableDragPainter compositeDockableDragPainter = new CompositeDockableDragPainter();
        compositeDockableDragPainter.addPainter(basicDockableDragPainter);
        if (additionalDockableDragPainter != null) {
            compositeDockableDragPainter.addPainter(additionalDockableDragPainter);
        }
        DraggerFactory draggerFactory = new StaticDraggerFactory(compositeDockableDragPainter);
        // Give this dragger factory to the docking manager.
        if (selected) {
            DockingManager.setDraggerFactory(draggerFactory);
            setSelected(true);
        }
        // Add a dragging listener as action listener.
        addActionListener(new DraggingListener(draggerFactory));
    }
    
} 
