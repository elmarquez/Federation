/**
 * ComponentVisualWidget.java
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

import ca.sfu.federation.model.Component;
import ca.sfu.federation.model.ConfigManager;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.IViewable;
import ca.sfu.federation.model.INamed;
import java.awt.Font;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * A graphic representation of a model object.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ComponentVisualWidget extends Widget implements IVisualWidget, Observer {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private static final String ACTION_OPEN_SCENARIO = "OPENSCENARIO";
    private static final String ACTION_SET_ACTIVE_SCENARIO = "SETSCENARIOACTIVE";
    private static final String ACTION_MOVE = "MOVE";
    
    private INamed target; // the model object that this widget points to
    private LabelWidget nameLabelWidget;
    private LabelWidget updateMethodLabelWidget;
    private ImageWidget iconWidget;    
    private WidgetAction moveAction = ActionFactory.createMoveAction();
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * ContainerVisualWidget constructor.
     * @param MyScene The scene the widget is being displayed in.
     * @param Named The target object.
     */
    public ComponentVisualWidget(Scene MyScene, INamed Named) {
        // init
        super(MyScene);
        this.target = Named;
        // watch the target for changes
        if (Named instanceof Observable) {
            Observable observable = (Observable) Named;
            observable.addObserver(this);
        }
        // set the widget properties
        this.setLayout(LayoutFactory.createHorizontalLayout(LayoutFactory.SerialAlignment.CENTER,8));
        Border border = BorderFactory.createRoundedBorder(8,8,8,8,ConfigManager.BACKGROUND_LIGHT,ConfigManager.BACKGROUND_DARK);
        this.setBorder(border);
        this.setBackground(ConfigManager.BACKGROUND_LIGHT);
        this.setOpaque(true);
        // set thumbnail or icon
        if (Named instanceof IViewable) {
            IViewable displayobject = (IViewable) Named;
            Image icon = displayobject.getIcon();
            this.iconWidget = new ImageWidget(MyScene,icon);
            // image.setBorder(BorderFactory.createLineBorder(1,ConfigManager.BACKGROUND_LIGHT));
            this.addChild(iconWidget);
        }
        // set label, which is composed of the object name and the object class or update method name
        Widget labelwidget = new Widget(MyScene);
        labelwidget.setLayout(LayoutFactory.createVerticalLayout(LayoutFactory.SerialAlignment.JUSTIFY,1));
        nameLabelWidget = new LabelWidget(MyScene, Named.getName());
        nameLabelWidget.setForeground(ConfigManager.TEXT_LIGHT);
        labelwidget.addChild(nameLabelWidget);
        if (Named instanceof Component) {
            Component c = (Component) Named;
            // this.updateMethodLabelWidget = new LabelWidget(MyScene,c.getUpdateMethodName());
            this.updateMethodLabelWidget = new LabelWidget(MyScene,Named.getCanonicalName());
            Font font = new Font("SansSerif",Font.PLAIN,10);
            this.updateMethodLabelWidget.setFont(font);
            this.updateMethodLabelWidget.setForeground(ConfigManager.TEXT_LIGHT);
            labelwidget.addChild(updateMethodLabelWidget);            
        }
        this.addChild(labelwidget);
        // make the widget moveable
        this.getActions().addAction(ActionFactory.createMoveAction());
        // add context menu to widget
        IContext context = this.target.getContext();
        ComponentVisualWidgetPopupProvider provider = new ComponentVisualWidgetPopupProvider((Scene)MyScene,this);
        WidgetAction popup = ActionFactory.createPopupMenuAction(provider);
        this.getActions().addAction(popup);
    }
    
    //--------------------------------------------------------------------------
    // METHODS
    
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
            INamed named = (INamed) o;
            switch (eventId) {
                case ConfigManager.EVENT_DESCRIPTION_CHANGE:
                case ConfigManager.EVENT_NAME_CHANGE:
                    System.out.println("INFO: ComponentVisualWidget fired name or description change event.");
                    this.nameLabelWidget.setLabel(named.getName());
                    this.updateMethodLabelWidget.setLabel(named.getCanonicalName());
                    if (named instanceof Component) {
                        Component c = (Component) named;
                        this.updateMethodLabelWidget.setLabel(c.getUpdateMethodName());
                    }
                    break;
                case ConfigManager.EVENT_ELEMENT_CHANGE:
                    System.out.println("INFO: ComponentVisualWidget fired element change event.");
                    this.nameLabelWidget.setLabel(named.getName());
                    this.updateMethodLabelWidget.setLabel(named.getCanonicalName());
                    if (named instanceof Component) {
                        Component c = (Component) named;
                        this.updateMethodLabelWidget.setLabel(c.getUpdateMethodName());
                    }
                    this.iconWidget.setImage(named.getIcon());
                    break;
                case ConfigManager.EVENT_ICON_CHANGE:
                    System.out.println("INFO: ComponentVisualWidget fired icon change event.");
                    this.iconWidget.setImage(named.getIcon());
                    break;
            }
        }
        // repaint the object
        this.repaint();
    }
    
} // end class
