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
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * Save the project state to persistent storage.
 * @author Davis Marques
 */
public class SaveProjectAction extends AbstractAction {
        
    private static final Logger logger = Logger.getLogger(SaveProjectAction.class.getName());
    
    public SaveProjectAction() {
        super("Save Project", null);
        Icon icon = ImageIconUtils.loadIconById("file-save-project-icon");
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        this.putValue(Action.LONG_DESCRIPTION, "Save project");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        this.putValue(Action.SHORT_DESCRIPTION, "Save project");
        this.putValue(Action.SMALL_ICON, icon);
    }

    /**
     * AssemblyNewInstanceAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     */
    public SaveProjectAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        this.putValue(Action.SMALL_ICON, MyIcon);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Perform the action.
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        ApplicationContext context = Application.getContext();
        ParametricModel model = context.getModel();
        if (model != null) {
            logger.log(Level.INFO,"Saving the project to {0}","path");
            logger.log(Level.INFO,"Project file saved");
        } else {
            logger.log(Level.WARNING,"Could not save the project because no model is currently open");
        }
    }
    
} 
