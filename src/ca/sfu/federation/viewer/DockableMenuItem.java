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

import ca.sfu.federation.viewer.DockableMediator;
import com.javadocking.dockable.Dockable;
import javax.swing.JCheckBoxMenuItem;

/**
 * A check box menu item to add or remove the dockable.
 * @author dmarques
 */
public class DockableMenuItem extends JCheckBoxMenuItem {

    /**
     * DockableMenuItem constructor
     * @param D Dockable
     */
    public DockableMenuItem(Dockable D) {
        super(D.getTitle(), D.getIcon());
        setSelected(D.getDock() != null);
        DockableMediator dockableMediator = new DockableMediator(D, this);
        D.addDockingListener(dockableMediator);
        addItemListener(dockableMediator);
    }
    
} // end class
