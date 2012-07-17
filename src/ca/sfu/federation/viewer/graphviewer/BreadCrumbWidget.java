/**
 * BreadCrumbWidget.java
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

import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.action.SetCurrentIContextAction;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 * A graphic widget that represents some element in a path or heirarchy, and is
 * intended to enable the user to traverse the heirarchy quickly.  The widget 
 * action is activated on mouse release.
 *
 * @author Davis Marques
 * @version 0.1.0
 */
public class BreadCrumbWidget extends Widget {
    
    //--------------------------------------------------------------------------

    
    private static final String ACTION_SET_CONTEXT_FOCUS = "OPEN_CONTEXT"; // NOI18N
    
    private IContext target; // the target that this widget points to
    
    //--------------------------------------------------------------------------

    
    /**
     * BreadCrumbWidget2 constructor.
     * 
     * @param MyScene
     * @param MyContext
     */
    public BreadCrumbWidget(AntialiasedScene MyScene, IContext MyContext) {
        // init
        super(MyScene);
        this.target = MyContext;
        
        // set the widget properties
        this.setLayout(LayoutFactory.createVerticalLayout(LayoutFactory.SerialAlignment.JUSTIFY,6));
        
        // set label
        LabelWidget label = new LabelWidget(MyScene, MyContext.getName());
        label.setForeground(ApplicationContext.TEXT_LIGHT);
        this.addChild(label);
        
        // add target menu to widget
        WidgetAction popup = ActionFactory.createPopupMenuAction(new MyPopupProvider(MyScene,this));
        this.getActions().addAction(popup);
        this.createActions(ACTION_SET_CONTEXT_FOCUS).addAction(popup);
    }
    
    //--------------------------------------------------------------------------


    /**
     * Get the target target of the widget.
     * 
     * @return Target Context.
     */
    public IContext getTarget() {
        return this.target;
    }
    
    //--------------------------------------------------------------------------
    // PRIVATE CLASSES

    /**
     * Popup menu provider.
     */
    private final class MyPopupProvider implements PopupMenuProvider, ActionListener {

        //----------------------------------------------------------------------
    
        
        private JPopupMenu menu;
        private AntialiasedScene scene;
        private BreadCrumbWidget widget;
        
        //----------------------------------------------------------------------
    

        /**
         * Popup menu provider.
         * @param MyScene
         */
        public MyPopupProvider(AntialiasedScene MyScene, BreadCrumbWidget MyWidget) {
            // init
            this.scene = MyScene;
            this.widget = MyWidget;
            // create menu
            menu = new JPopupMenu("Model Object Actions");
            JMenuItem item;
            // menu item - set as current target
            IContext context = this.widget.getTarget();
            SetCurrentIContextAction action = new SetCurrentIContextAction("Set as Current Context",null,"Set as Current Context",new Integer(KeyEvent.VK_S),context);
            menu.add(action);
        }
        
        //----------------------------------------------------------------------
    

        public JPopupMenu getPopupMenu(Widget widget) {
            return menu;
        }
        
        public void actionPerformed(ActionEvent e) {
            // e.setSource(this.widget.target);
            this.scene.setActiveTool(e.getActionCommand());
        }

    }
    
} 
