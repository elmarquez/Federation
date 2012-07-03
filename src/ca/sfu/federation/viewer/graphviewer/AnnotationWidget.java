/**
 * AnnotationWidget.java
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

import ca.sfu.federation.model.ConfigManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * @author Davis Marques
 * @version 0.1.0
 */
public class AnnotationWidget extends Widget {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private LabelWidget label;
    private int id;
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * AnnotationWidget constructor.
     */
    public AnnotationWidget(Scene MyScene, String Text, int Id) {
        // init
        super(MyScene);
        this.id = Id;
        // set the widget properties
        this.setLayout(LayoutFactory.createVerticalLayout(LayoutFactory.SerialAlignment.JUSTIFY,4));
        this.setBackground(ConfigManager.BACKGROUND_MEDIUM);
        this.setOpaque(true);
        // set label
        label = new LabelWidget(MyScene,Text);
        label.setForeground(ConfigManager.TEXT_LIGHT);
        this.addChild(label);
        // make the widget moveable
        this.getActions().addAction(ActionFactory.createMoveAction());
    }
    
    //--------------------------------------------------------------------------
    // METHODS

    public int getId() {
        return this.id;
    }
    
    //--------------------------------------------------------------------------
    // PRIVATE CLASSES

    /**
     * Popup menu provider.
     */
    private final class MyAnnotationPopupProvider implements PopupMenuProvider, ActionListener {

        //----------------------------------------------------------------------
        // FIELDS
        
        private JPopupMenu menu;
        private Scene scene;
        private AnnotationWidget widget;
        
        //----------------------------------------------------------------------
        // CONSTRUCTORS

        /**
         * Popup menu provider.
         * @param MyScene
         * @param MyWidget
         */
        public MyAnnotationPopupProvider(Scene MyScene, AnnotationWidget MyWidget) {
            // init
            this.scene = MyScene;
            this.widget = MyWidget;
            
            // create menu
            this.menu = new JPopupMenu();
            JMenuItem item = null;
            
            item = new JMenuItem("Edit");
            menu.add(item);
            
            item = new JMenuItem("Properties");
            menu.add(item);
            
            menu.add(new JSeparator());

            item = new JMenuItem("Delete");
            menu.add(item);
                    
        }
        
        //----------------------------------------------------------------------
        // METHODS

        public JPopupMenu getPopupMenu(Widget widget) {
            return menu;
        }
        
        public void actionPerformed(ActionEvent e) {
            this.scene.setActiveTool(e.getActionCommand());
        }

    }
    
} // end class
