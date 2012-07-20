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
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Create scenario action.
 * @author Davis Marques
 */
public class CreateScenarioAction extends AbstractAction {
    
    private static final Logger logger = Logger.getLogger(CreateScenarioAction.class.getName());
    
    //--------------------------------------------------------------------------
    
    public CreateScenarioAction() {
        super("Create Scenario", null);
        Icon icon = ImageIconUtils.loadIconById("model-create-scenario");
        this.putValue(Action.LONG_DESCRIPTION, "Create Scenario");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        this.putValue(Action.SHORT_DESCRIPTION, "Create Scenario");
        this.putValue(Action.SMALL_ICON, icon);
    }

    /**
     * CreateScenarioAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     */
    public CreateScenarioAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId) {
        super(Name, MyIcon);
        this.putValue(LONG_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Perform action.
     * @param e Event.
     */
    public void actionPerformed(ActionEvent e) {
        ParametricModel model = Application.getContext().getModel();
        if (model != null) {
            String name = model.getNextName(Scenario.DEFAULT_NAME); 
            Scenario scenario = new Scenario(name);
            try {
                scenario.registerInContext(model);
            } catch (Exception ex) {
                String stack = ExceptionUtils.getFullStackTrace(ex);
                logger.log(Level.WARNING,"{0}",stack);
            }
        } else {
            logger.log(Level.WARNING,"Could not create new scenario because model does not exist");
        }
    }
    
} 
