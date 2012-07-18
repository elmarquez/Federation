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

import ca.sfu.federation.model.Assembly;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Create assembly action
 * @author Davis Marques
 */
public class CreateAssemblyAction extends AbstractAction {
    
    private static final Logger logger = Logger.getLogger(CreateAssemblyAction.class.getName());
    
    private IContext context;
    private Class clazz;
    private boolean byClass;
    
    //--------------------------------------------------------------------------

    public CreateAssemblyAction() {
        super("Create Assembly", null);
        Icon icon = ImageIconUtils.loadIconById("model-create-assembly");
        this.putValue(Action.LONG_DESCRIPTION, "Create Assembly");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        this.putValue(Action.SHORT_DESCRIPTION, "Create Assembly");
        this.putValue(Action.SMALL_ICON, icon);
    }
    
    /**
     * CreateAssemblyAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param MyContext Context.
     */
    public CreateAssemblyAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, IContext MyContext) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
        this.context = MyContext;
        this.byClass = false;
    }
    
    /**
     * CreateAssemblyAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     * @param MyContext Context.
     * @param MyClass Assembly class to instantiate.
     */
    public CreateAssemblyAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId, IContext MyContext, Class MyClass) {
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
                    String stack = ExceptionUtils.getFullStackTrace(ex);
                    logger.log(Level.WARNING,"{0}",stack);
                }
            }
            return;
        } 
        Assembly assembly = new Assembly(this.context);
    }
    
} 