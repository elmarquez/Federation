/**
 * ViewerSetContextFocusAction .java
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

package ca.sfu.federation.viewer.action;

import ca.sfu.federation.model.ConfigManager;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.IContext;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * Set the current context of the model.  If the context is an instance of
 * Parametric Model, then we set the viewer type to 
 * @author Davis Marques
 * @version 0.1.0
 */
public class IContextSetCurrentAction extends AbstractAction {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private ParametricModel model;
    private IContext context;

    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * IContextSetCurrentAction constructor.
     * 
     * @param Name The fully qualified context name to set as the current context.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param MyContext Context to set the model to when this action is fired.
     */
    public IContextSetCurrentAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, IContext MyContext) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.context = MyContext;
        this.model = ParametricModel.getInstance();
    }
    
    //--------------------------------------------------------------------------
    // METHODS

    /**
     * Set the current context.
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        System.out.println("INFO: IContextSetCurrentAction performed action. Set context to " + this.context.getCanonicalName());
        model.setViewState(ConfigManager.VIEWER_CURRENT_CONTEXT,this.context);
    }
    
} // end class
