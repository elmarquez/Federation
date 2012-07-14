/**
 * AssemblyNewInstanceAction.java
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

import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.IContext;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * @author Davis Marques
 * @version 0.1.0
 */
public class AssemblyNewInstanceAction extends AbstractAction {
    
    //--------------------------------------------------------------------------

    
    private IContext context;
    private Class clazz;
    private boolean byClass;
    
    //--------------------------------------------------------------------------

    
    /**
     * AssemblyNewInstanceAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param MyContext Context.
     */
    public AssemblyNewInstanceAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, IContext MyContext) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.context = MyContext;
        this.byClass = false;
    }
    
    /**
     * AssemblyNewInstanceAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param MyContext Context.
     * @param MyClass Assembly class to instantiate.
     */
    public AssemblyNewInstanceAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, IContext MyContext, Class MyClass) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.context = MyContext;
        this.byClass = false;
        if (Assembly.class.isAssignableFrom(MyClass)) {
            this.clazz = MyClass;
            this.byClass = true;
        }
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Perform action.
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        if (this.byClass) {
            if (this.clazz != null) {
                try {
                    Constructor constructor = this.clazz.getConstructor(IContext.class);
                    constructor.newInstance(this.context);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return;
        } 
        Assembly assembly = new Assembly(this.context);
    }
    
} // end class
