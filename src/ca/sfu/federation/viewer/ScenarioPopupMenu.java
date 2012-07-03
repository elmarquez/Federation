/**
 * ScenarioPopupMenu.java
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

import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.viewer.action.AssemblyNewInstanceAction;
import ca.sfu.federation.viewer.action.ComponentNewInstanceAction;
import ca.sfu.federation.viewer.action.INamedObjectDeleteAction;
import ca.sfu.federation.viewer.action.INamedObjectRenameAction;
import ca.sfu.federation.viewer.action.IContextSetCurrentAction;
import ca.sfu.federation.viewer.action.PropertySheetSetFocusAction;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * Scenario popup menu.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ScenarioPopupMenu extends JPopupMenu {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private Scenario scenario;

    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * ScenarioPopupMenu constructor.
     */
    public ScenarioPopupMenu(Scenario MyScenario) {
        // init
        this.scenario = MyScenario;
        // add sub elements
        JMenu submenu = new JMenu("Add");
        AssemblyNewInstanceAction ania = new AssemblyNewInstanceAction("Assembly",null,"Assembly",new Integer(KeyEvent.VK_A),this.scenario);
        submenu.add(ania);
        ComponentNewInstanceAction cnia = new ComponentNewInstanceAction("Component",null,"Component",new Integer(KeyEvent.VK_C),this.scenario);
        submenu.add(cnia);
        this.add(submenu);
        // separator
        this.add(new JSeparator());
        // edit this scenario
        IContextSetCurrentAction vscfa = new IContextSetCurrentAction("Open Scenario",null,"Open Scenario",new Integer(KeyEvent.VK_O),this.scenario);
        this.add(vscfa);
        // rename
        INamedObjectRenameAction inora = new INamedObjectRenameAction("Rename",null,"Rename",new Integer(KeyEvent.VK_R),this.scenario);
        this.add(inora);
        // delete
        INamedObjectDeleteAction inoda = new INamedObjectDeleteAction("Delete",null,"Delete",new Integer(KeyEvent.VK_D),this.scenario);
        this.add(inoda);
        // properties
        PropertySheetSetFocusAction pssfa = new PropertySheetSetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),(INamed)ParametricModel.getInstance());
        this.add(pssfa);
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
} // end class
