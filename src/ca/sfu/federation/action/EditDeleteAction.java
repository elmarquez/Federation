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

import ca.sfu.federation.model.INamed;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Delete the INamed from the model.
 * @author Davis Marques
 */
public class EditDeleteAction extends AbstractAction {
    
    private static final Logger logger = Logger.getLogger(EditDeleteAction.class.getName());
    private INamed target;
    
    //--------------------------------------------------------------------------
    
    public EditDeleteAction() {
        super("Delete", null);
        Icon icon = ImageIconUtils.loadIconById("edit-delete-icon");
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("delete"));
        this.putValue(Action.LONG_DESCRIPTION, "Delete selected items");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        this.putValue(Action.SHORT_DESCRIPTION, "Delete selected items");
        this.putValue(Action.SMALL_ICON, icon);
    }

    /**
     * INamedObjectDeleteAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param MyTarget Target object.
     */
    public EditDeleteAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, INamed MyTarget) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.target = MyTarget;
    }
    
    //--------------------------------------------------------------------------
    
    public void actionPerformed(ActionEvent e) {
        // confirm delete action before proceeding
        int i = JOptionPane.showOptionDialog(null,"Are you sure you want to delete '" + this.target.getName() + "'?","Confirm Delete",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,null,JOptionPane.OK_OPTION);
        if (i==0) {
            logger.log(Level.INFO,"INamedObjectDeleteAction performed action");
            this.target.delete();
        }
    }
    
} 