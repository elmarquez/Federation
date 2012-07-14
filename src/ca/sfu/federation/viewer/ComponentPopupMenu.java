/**
 * ComponentPopupMenu.java
 * * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
 *
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

package ca.sfu.federation.viewer;

import ca.sfu.federation.model.Component;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.action.PropertySheetSetFocusAction;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;

/**
 * Component popup menu.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ComponentPopupMenu extends JPopupMenu {
    
    //--------------------------------------------------------------------------

    
    private Component target;
    
    //--------------------------------------------------------------------------

    
    /**
     * ComponentPopupMenu constructor.
     * @param MyComponent Target component.
     */
    public ComponentPopupMenu(Component MyComponent) {
        // init
        this.target = MyComponent;
        // menu item - properties
        PropertySheetSetFocusAction pssfa = new PropertySheetSetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),(INamed)this.target);
        this.add(pssfa);
    }
    
    //--------------------------------------------------------------------------

    
} // end class
