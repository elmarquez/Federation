/**
 * ParametricModelPopupProvider.java
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

package ca.sfu.federation.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 * Popup menu provider.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ParametricModelPopupProvider implements PopupMenuProvider, ActionListener {
    
    //----------------------------------------------------------------------

    
    private ParametricModelPopupMenu menu;
    
    //----------------------------------------------------------------------

    
    /**
     * Popup menu provider.
     */
    public ParametricModelPopupProvider() {
        this.menu = new ParametricModelPopupMenu();
    }
    
    //----------------------------------------------------------------------


    /**
     * Get the popup menu.
     * @param widget 
     * @return Popup menu.
     */
    public JPopupMenu getPopupMenu(Widget widget) {
        return (JPopupMenu) menu;
    }
    
    /**
     * @param e Action event.
     */
    public void actionPerformed(ActionEvent e) {
    }
    
} // end class
