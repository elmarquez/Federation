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
 * Undo action.
 * @author dmarques
 */
public class EditUndoAction extends AbstractAction {

    public EditUndoAction() {
        super("Undo", null);
        Icon icon = ImageIconUtils.loadImageIcon("/ca/sfu/federation/resources/icons/16x16/actions/edit-undo.png");
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
        this.putValue(Action.LONG_DESCRIPTION, "Undo previous action");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
        this.putValue(Action.SHORT_DESCRIPTION, "Undo previous action");
        this.putValue(Action.SMALL_ICON, icon);
    }
    
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
} 