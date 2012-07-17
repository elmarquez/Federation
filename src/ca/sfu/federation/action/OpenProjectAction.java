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

import ca.sfu.federation.utils.ImageIconUtils;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * Open an existing project.
 * @author Davis Marques
 */
public class OpenProjectAction extends AbstractAction {
    
    private static final Logger logger = Logger.getLogger(CreateProjectAction.class.getName());

    //--------------------------------------------------------------------------

    public OpenProjectAction() {
        super("Open Project", null);
        Icon icon = ImageIconUtils.loadImageIcon("/ca/sfu/federation/resources/icons/16x16/actions/document-open.png");
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift O"));
        this.putValue(Action.LONG_DESCRIPTION, "Open an existing project");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        this.putValue(Action.SHORT_DESCRIPTION, "Open an existing project");
        this.putValue(Action.SMALL_ICON, icon);
    }
    
    /**
     * AssemblyNewInstanceAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     */
    public OpenProjectAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Handle event.
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        // show dialog box
        // try to load project data
        // set project
    }
    
} 
