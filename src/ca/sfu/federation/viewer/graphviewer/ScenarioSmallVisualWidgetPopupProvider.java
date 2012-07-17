/**
 * ScenarioSmallVisualWidgetPopupProvider.java
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

package ca.sfu.federation.viewer.graphviewer;

import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.action.SetCurrentIContextAction;
import ca.sfu.federation.action.EditDeleteAction;
import ca.sfu.federation.action.RenameINamedAction;
import ca.sfu.federation.action.SetPropertySheetFocusAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 * Popup menu provider.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ScenarioSmallVisualWidgetPopupProvider implements PopupMenuProvider, ActionListener {
    
    //----------------------------------------------------------------------

    
    private JPopupMenu menu;
    private AntialiasedScene scene;
    private ScenarioSmallVisualWidget widget;
    
    //----------------------------------------------------------------------

    
    /**
     * Popup menu provider.
     * @param MyScene
     */
    public ScenarioSmallVisualWidgetPopupProvider(AntialiasedScene MyScene, ScenarioSmallVisualWidget MyWidget) {
        // init
        this.scene = MyScene;
        this.widget = MyWidget;
        IContext target = (IContext) this.widget.getTarget();
        // create menu
        menu = new JPopupMenu("Model Object Actions");
        // menu item - set the target as the focus
        SetCurrentIContextAction setfocus = new SetCurrentIContextAction("Open Scenario",null,"Open Scenario",new Integer(KeyEvent.VK_O),target);
        menu.add(setfocus);
        // menu item - rename target object
        RenameINamedAction rename = new RenameINamedAction("Rename Object",null,"Rename Object",new Integer(KeyEvent.VK_R),(INamed)target);
        menu.add(rename);
        // menu item - delete target object
        EditDeleteAction delete = new EditDeleteAction("Delete Object",null,"Delete Object",new Integer(KeyEvent.VK_D),(INamed)target);
        menu.add(delete);
        // separator
        menu.add(new JSeparator());
        // menu item - properties
        SetPropertySheetFocusAction pssfa = new SetPropertySheetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),target);
        menu.add(pssfa);
    }
    
    //----------------------------------------------------------------------

    
    public JPopupMenu getPopupMenu(Widget widget) {
        return menu;
    }
    
    public void actionPerformed(ActionEvent e) {
        this.scene.setActiveTool(e.getActionCommand());
    }
    
} 
