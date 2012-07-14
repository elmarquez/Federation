/**
 * AssemblyPopupMenu.java
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

import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.action.ComponentNewInstanceAction;
import ca.sfu.federation.action.DeleteINamedAction;
import ca.sfu.federation.action.RenameINamedAction;
import ca.sfu.federation.action.IContextSetCurrentAction;
import ca.sfu.federation.action.PropertySheetSetFocusAction;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * Assembly popup menu.
 * @author Davis Marques
 * @version 0.1.0
 */
public class AssemblyPopupMenu extends JPopupMenu {
    
    //--------------------------------------------------------------------------

    
    private Assembly target;
    
    //--------------------------------------------------------------------------

    
    /**
     * AssemblyPopupMenu constructor.
     */
    public AssemblyPopupMenu(Assembly MyAssembly) {
        // init
        this.target = MyAssembly;
        // add sub elements
        JMenu submenu = new JMenu("Add");
        ComponentNewInstanceAction cnia = new ComponentNewInstanceAction("Component",null,"Component",new Integer(KeyEvent.VK_C),target);
        submenu.add(cnia);
        this.add(submenu);
        // separator
        this.add(new JSeparator());
        // edit
        IContextSetCurrentAction vscfa = new IContextSetCurrentAction("Open Assembly",null,"Open Assembly",new Integer(KeyEvent.VK_O),target);
        this.add(vscfa);
        // rename
        RenameINamedAction inora = new RenameINamedAction("Rename",null,"Rename",new Integer(KeyEvent.VK_R),target);
        this.add(inora);
        // delete
        DeleteINamedAction inoda = new DeleteINamedAction("Delete",null,"Delete",new Integer(KeyEvent.VK_D),target);
        this.add(inoda);
        // properties
        PropertySheetSetFocusAction pssfa = new PropertySheetSetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),(INamed)this.target);
        this.add(pssfa);
    }
    
    //--------------------------------------------------------------------------

    
} // end class
