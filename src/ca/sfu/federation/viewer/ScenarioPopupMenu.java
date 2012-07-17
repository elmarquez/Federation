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

package ca.sfu.federation.viewer;

import ca.sfu.federation.Application;
import ca.sfu.federation.action.*;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.Scenario;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * Scenario popup menu.
 * @author Davis Marques
 */
public class ScenarioPopupMenu extends JPopupMenu {

    private static final Logger logger = Logger.getLogger(ScenarioPopupMenu.class.getName());
    
    private Scenario scenario;

    //--------------------------------------------------------------------------
    
    /**
     * ScenarioPopupMenu constructor.
     */
    public ScenarioPopupMenu(Scenario MyScenario) {
        // init
        this.scenario = MyScenario;
        // add sub elements
        JMenu submenu = new JMenu("Add");
        CreateAssemblyAction ania = new CreateAssemblyAction("Assembly",null,"Assembly",new Integer(KeyEvent.VK_A),this.scenario);
        CreateComponentAction cnia = new CreateComponentAction("Component",null,"Component",new Integer(KeyEvent.VK_C),this.scenario);
        submenu.add(ania);
        submenu.add(cnia);
        this.add(submenu);
        // separator
        this.add(new JSeparator());
        // edit this scenario
        SetCurrentIContextAction vscfa = new SetCurrentIContextAction("Open Scenario",null,"Open Scenario",new Integer(KeyEvent.VK_O),this.scenario);
        this.add(vscfa);
        // rename
        RenameINamedAction inora = new RenameINamedAction("Rename",null,"Rename",new Integer(KeyEvent.VK_R),this.scenario);
        this.add(inora);
        // delete
        EditDeleteAction inoda = new EditDeleteAction("Delete",null,"Delete",new Integer(KeyEvent.VK_D),this.scenario);
        this.add(inoda);
        // properties
        SetPropertySheetFocusAction pssfa = new SetPropertySheetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),
                (INamed)Application.getContext().getModel());
        this.add(pssfa);
    }
    
    //--------------------------------------------------------------------------

    
} 
