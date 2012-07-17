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

package ca.sfu.federation.action;

import ca.sfu.federation.Application;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 * Rename the parametric model.
 * @author Davis Marques
 */
public class RenameProjectAction extends AbstractAction {
        
    private static final Logger logger = Logger.getLogger(RenameProjectAction.class.getName());
    
    public RenameProjectAction() {
        super("Rename Project",null);
        this.putValue(Action.LONG_DESCRIPTION, "Rename project");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
        this.putValue(Action.SHORT_DESCRIPTION, "Rename project");
    }
    
    /**
     * ParametricModelRenameAction constructor.
     * @param Name The action label to display in menus.
     * @param MyIcon Action icon.
     * @param ToolTip The description to display in ToolTips.
     * @param MnemonicId Key mnemonic.
     */
    public RenameProjectAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId) {
        super(Name,MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Action performed.
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        ParametricModel model = Application.getContext().getModel();
        if (model != null) {
            String newname = JOptionPane.showInputDialog(null,"Rename Model",model.getName());
            model.setName(newname);
        } else {
            logger.log(Level.WARNING,"Could not peform project rename because no project model is currently loaded.");
        }
    }
    
}  