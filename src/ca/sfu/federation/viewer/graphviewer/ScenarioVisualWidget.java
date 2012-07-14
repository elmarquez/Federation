/**
 * ScenarioVisualWidget.java
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

import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.Scenario;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.IViewable;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.action.IContextSetCurrentAction;
import ca.sfu.federation.action.DeleteINamedAction;
import ca.sfu.federation.action.RenameINamedAction;
import ca.sfu.federation.action.PropertySheetSetFocusAction;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 * A graphic representation of a Scenario object.
 * @author Davis Marques
 * @version 0.0.2
 */
public class ScenarioVisualWidget extends Widget implements Observer {
    
    //--------------------------------------------------------------------------

    
    private static final String ACTION_OPEN_SCENARIO = "OPENSCENARIO";
    private static final String ACTION_SET_ACTIVE_SCENARIO = "SETSCENARIOACTIVE";
    private static final String ACTION_MOVE = "MOVE";
    
    private Scenario target;
    private LabelWidget labelWidget;
    private ImageWidget thumbnailWidget;
    private Image thumbnail;
    private Image icon;
    
    private WidgetAction moveAction = ActionFactory.createMoveAction();
    
    //--------------------------------------------------------------------------

    
    /**
     * ScenarioVisualWidget constructor.
     * @param MyScene Scene.
     * @param MyScenario Scenario.
     */
    public ScenarioVisualWidget(MutableSceneModel MyScene, Scenario MyScenario) {
        // init
        super(MyScene);
        this.target = MyScenario;
        
        // watch the target for changes
        if (MyScenario instanceof Observable) {
            Observable observable = (Observable) MyScenario;
            observable.addObserver(this);
        }
        
        // set the widget properties
        this.setLayout(LayoutFactory.createVerticalLayout(LayoutFactory.SerialAlignment.JUSTIFY,4));
        Border border = BorderFactory.createLineBorder(1,ApplicationContext.BACKGROUND_LIGHT);
        border.getInsets().set(5,5,5,5);
        this.setBorder(border);
        this.setBackground(ApplicationContext.BACKGROUND_LIGHT);
        this.setOpaque(true);

        // set thumbnail or icon
        ImageWidget image = null;
        if (MyScenario instanceof IViewable) {
            IViewable displayobject = (IViewable) MyScenario;
            thumbnail = displayobject.getThumbnail();
            this.thumbnailWidget = new ImageWidget(MyScene,thumbnail);
            this.addChild(this.thumbnailWidget);
        }
        
        // set labelWidget
        labelWidget = new LabelWidget(MyScene,MyScenario.getName());
        labelWidget.setForeground(ApplicationContext.TEXT_LIGHT);
        this.addChild(labelWidget);
        
        // make the widget moveable
        this.getActions().addAction(ActionFactory.createMoveAction());
        
        // add context menu to widget
        IContext context = this.target.getContext();
        MyScenarioPopupProvider provider = new MyScenarioPopupProvider(MyScene,this);
        WidgetAction popup = ActionFactory.createPopupMenuAction(provider);
        this.getActions().addAction(popup);
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Get the target of the widget.
     * @return Target object.
     */
    public INamed getTarget() {
        return this.target;
    }
    
    /**
     * @param o Observable object.
     * @param arg Update message.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            System.out.println("INFO: ScenarioVisualWidget received event notification id " + eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_NAME_CHANGE:
                    INamed named = (INamed) o;
                    this.labelWidget.setLabel(named.getName());
                    System.out.println("INFO: ScenarioVisualWidget fired label update.");
                    break;
                case ApplicationContext.EVENT_DESCRIPTION_CHANGE:
                    System.out.println("ERROR: Not implemented. VisualWidget.update:DescriptionChange");
                    break;
                case ApplicationContext.EVENT_ICON_CHANGE:
                    System.out.println("ERROR: Not implemented. VisualWidget.update:Icon Change");
                    break;
                case ApplicationContext.EVENT_THUMBNAIL_CHANGE:
                    System.out.println("INFO: ScenarioVisualWidget fired thumbnail update.");
                    this.updateThumbnail();
                    break;
            }
        }
        // update the widget
        this.repaint();
    }
    
    /**
     * Update the thumbnail for this widget.
     */
    protected void updateThumbnail() {
        // get thumbnails 
        ParametricModel model = ParametricModel.getInstance();
        LinkedHashMap thumbnails = (LinkedHashMap) model.getViewState(ApplicationContext.VIEWER_ICONTEXT_THUMBNAILS);
        // set thumbnail
        BufferedImage image = (BufferedImage) thumbnails.get(this.target.getCanonicalName());
        if (image != null) {
            this.thumbnailWidget.setImage(image);
        }
    }    
    
    //--------------------------------------------------------------------------
    // PRIVATE CLASSES
    
    /**
     * Popup menu provider.
     */
    private final class MyScenarioPopupProvider implements PopupMenuProvider, ActionListener {
        
        //----------------------------------------------------------------------
    
        
        private JPopupMenu menu;
        private MutableSceneModel scene;
        private ScenarioVisualWidget widget;
        
        //----------------------------------------------------------------------
    
        
        /**
         * Popup menu provider.
         * @param MyScene Scene.
         * @param MyWidget Widget to which this popup is attached.
         */
        public MyScenarioPopupProvider(MutableSceneModel MyScene, ScenarioVisualWidget MyWidget) {
            // init
            this.scene = MyScene;
            this.widget = MyWidget;
            Scenario scenario = this.widget.target;
            // create menu
            menu = new JPopupMenu("Model Object Actions");
            // menu item - set the target as the focus
            IContext context = (IContext) scenario;
            IContextSetCurrentAction setfocus = new IContextSetCurrentAction("Open Scenario",null,"Open Scenario",new Integer(KeyEvent.VK_O),context);
            menu.add(setfocus);
            // menu item - rename context object
            RenameINamedAction rename = new RenameINamedAction("Rename Object",null,"Rename Object",new Integer(KeyEvent.VK_R),target);
            menu.add(rename);
            // menu item - delete context object
            DeleteINamedAction delete = new DeleteINamedAction("Delete Object",null,"Delete Object",new Integer(KeyEvent.VK_D),target);
            menu.add(delete);
            // separator
            menu.add(new JSeparator());
            // menu item - properties
            PropertySheetSetFocusAction pssfa = new PropertySheetSetFocusAction("Properties",null,"Properties",new Integer(KeyEvent.VK_P),MyWidget.target);
            menu.add(pssfa);
        }
        
        //----------------------------------------------------------------------
    
        
        public JPopupMenu getPopupMenu(Widget widget) {
            return menu;
        }
        
        public void actionPerformed(ActionEvent e) {
            if (this.widget.target instanceof IContext) {
                IContext context = (IContext) this.widget.getTarget();
                ParametricModel.getInstance().setViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT,context);
                this.scene.setActiveTool(e.getActionCommand());
            }
        }
        
    }
    
} // end class
