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
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.ParametricModel;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * SetPropertySheetFocusAction
 * @author Davis Marques
 */
public class SetPropertySheetFocusAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(SetPropertySheetFocusAction.class.getName());
    private INamed target;

    /**
     * SetPropertySheetFocusAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param Target Target object.
     */
    public SetPropertySheetFocusAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, INamed Target) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.target = Target;
    }

    //--------------------------------------------------------------------------

    /**
     * Set the current context.
     */
    public void actionPerformed(ActionEvent e) {
        Application.getContext().setViewState(ApplicationContext.VIEWER_SELECTION,this.target);
        logger.log(Level.ALL,"Set PropertySheet focus to {0}", this.target.getName());
    }

} 