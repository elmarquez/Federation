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

import com.javadocking.DockingManager;
import com.javadocking.drag.DraggerFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A listener that installs a dragger factory.
 * @author dmarques
 */
public class DraggingListener implements ActionListener {

    private DraggerFactory draggerFactory;

    /**
     * DraggingListener constructor
     * @param draggerFactory
     */
    public DraggingListener(DraggerFactory draggerFactory) {
        this.draggerFactory = draggerFactory;
    }

    /**
     * Action performed listener.
     * @param actionEvent 
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        DockingManager.setDraggerFactory(draggerFactory);
    }

} // end class
