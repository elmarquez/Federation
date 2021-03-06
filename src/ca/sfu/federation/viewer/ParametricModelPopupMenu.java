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
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.action.RenameProjectAction;
import ca.sfu.federation.action.SetPropertySheetFocusAction;
import ca.sfu.federation.action.CreateScenarioAction;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/**
 * Parametric model popup menu.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ParametricModelPopupMenu extends JPopupMenu {
    
    private static final Logger logger = Logger.getLogger(ParametricModelPopupMenu.class.getName());
    
    /**
     * ParametricModelPopupMenu constructor.
     */
    public ParametricModelPopupMenu() {
        // menu item - add subelement submenu
        JMenu submenu = new JMenu("Add");
        submenu.setMnemonic(KeyEvent.VK_A);
        // scenario new instance
        CreateScenarioAction snia = new CreateScenarioAction("New Scenario",null,"New Scenario",new Integer(KeyEvent.VK_N));
        submenu.add(snia);
        this.add(submenu);
        // menu item - rename        
        RenameProjectAction pmra = new RenameProjectAction("Rename",null,"Rename",new Integer(KeyEvent.VK_R));
        this.add(pmra);
        // menu item - separator
        this.add(new JSeparator());
        // menu item - properties
        SetPropertySheetFocusAction pssfa = new SetPropertySheetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),
                (INamed)Application.getContext().getModel());
        this.add(pssfa);
    }
    
    //--------------------------------------------------------------------------

    
} 
