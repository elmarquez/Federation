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

import ca.sfu.federation.viewer.AboutJPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JDialog;

/**
 * Show about window.
 * @author Davis Marques
 */
public class ShowAboutWindowAction extends AbstractAction {
    
    private static final Logger logger = Logger.getLogger(ShowAboutWindowAction.class.getName());
    
    //--------------------------------------------------------------------------

    public ShowAboutWindowAction() {
        super("About this Application", null);
        this.putValue(Action.LONG_DESCRIPTION, "About this Application");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        this.putValue(Action.SHORT_DESCRIPTION, "About this Application");
    }
    
    /**
     * ShowAboutWindowAction constructor.
     * @param Name Action name that will appear in menus.
     * @param MyIcon Action icon.
     * @param ToolTip Action description that will appear in Tool Tip.
     * @param MnemonicId Key mnemonic.
     */
    public ShowAboutWindowAction(String Name, Icon MyIcon, String ToolTip, Integer MnemonicId) {
        super(Name, MyIcon);
        this.putValue(SHORT_DESCRIPTION,ToolTip);
        this.putValue(MNEMONIC_KEY,MnemonicId);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Show About Application dialog.
     * @param e Event
     */
    public void actionPerformed(ActionEvent e) {
        AboutJPanel aboutpanel = new AboutJPanel();
        JDialog dialog = new JDialog();
        Container container = dialog.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(aboutpanel);
        dialog.setModal(true);
        dialog.setSize(473,300);
        dialog.setTitle("About");
        dialog.pack();
        // position in center of screen
        dialog.setLocationRelativeTo(null);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int height = dialog.getHeight();
        int width = dialog.getWidth();
        // dialog.setLocation((screen.height-height)/2,(screen.width-width)/2);
        // show dialog
        dialog.setVisible(true);
    }
    
} 
