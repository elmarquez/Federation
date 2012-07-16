/**
 * CreatePropertySheetAction.java
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

package ca.sfu.federation.action;

import ca.sfu.federation.model.INamed;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * Set the current context of the property sheet viewer.
 * @author Davis Marques
 * @version 0.1.0
 */
public class CreatePropertySheetAction extends AbstractAction {
    
    //--------------------------------------------------------------------------

    
    private INamed target;

    //--------------------------------------------------------------------------

    
    /**
     * PropertySheetSetFocusAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param Target Target object.
     */
    public CreatePropertySheetAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, INamed Target) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.target = Target;
    }
    
    //--------------------------------------------------------------------------


    /**
     * Set the current context.
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        // PropertySheetViewer psv = new PropertySheetViewer(this.target);
        // PropertySheetForm form = new PropertySheetForm();
    }
    
} 