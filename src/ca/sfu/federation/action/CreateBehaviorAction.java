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

import ca.sfu.federation.model.IContext;
import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

/**
 * Create behavior action
 * @author Davis Marques
 */
public class CreateBehaviorAction extends AbstractAction {
    
    private static final Logger logger = Logger.getLogger(CreateBehaviorAction.class.getName());
    
    private IContext context;
    private Class clazz;
    private boolean byClass;
    
    //--------------------------------------------------------------------------

    public CreateBehaviorAction() {
        super("Create Behavior", null);
        Icon icon = ImageIconUtils.loadIconById("model-create-behavior");
        this.putValue(Action.LONG_DESCRIPTION, "Create Behavior");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);
        this.putValue(Action.SHORT_DESCRIPTION, "Create Behavior");
        this.putValue(Action.SMALL_ICON, icon);
    }
        
    //--------------------------------------------------------------------------

    /**
     * Perform action.
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
        logger.log(Level.WARNING,"Not implemented yet");
    }
    
} 