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

import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * Copy action.
 * @author dmarques
 */
public class EditCopyAction extends AbstractAction {

    public EditCopyAction() {
        super("Copy", null);
        Icon icon = ImageIconUtils.loadIconById("edit-copy-icon");
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        this.putValue(Action.LONG_DESCRIPTION, "Copy selected item or items");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Y);
        this.putValue(Action.SHORT_DESCRIPTION, "Copy selected item or items");
        this.putValue(Action.SMALL_ICON, icon);
    }
    
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
} 