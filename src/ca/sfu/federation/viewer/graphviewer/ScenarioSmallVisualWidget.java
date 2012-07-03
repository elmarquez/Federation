/**
 * ScenarioSmallVisualWidget.java
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
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.IViewable;
import ca.sfu.federation.model.INamed;
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
import org.netbeans.api.visual.widget.Widget;

/**
 * A graphic representation of a Scenario object.
 * @author Davis Marques
 * @version 0.0.2
 */
public class ScenarioSmallVisualWidget extends Widget implements Observer {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private static final String ACTION_OPEN_SCENARIO = "OPENSCENARIO";
    private static final String ACTION_SET_ACTIVE_SCENARIO = "SETSCENARIOACTIVE";
    private static final String ACTION_MOVE = "MOVE";
    
    private IContext target;
    private LabelWidget label;
    private Image thumbnail;
    private Image icon;

    private WidgetAction moveAction = ActionFactory.createMoveAction();
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * ScenarioVisualWidget constructor.
     * @param MyScene Scene.
     * @param MyContext Context.
     */
    public ScenarioSmallVisualWidget(AntialiasedScene MyScene, IContext MyContext) {
        // init
        super(MyScene);
        this.target = MyContext;
        // watch the target for changes
        if (MyContext instanceof Observable) {
            Observable observable = (Observable) MyContext;
            observable.addObserver(this);
        }
        // set the widget properties
        this.setLayout(LayoutFactory.createHorizontalLayout(LayoutFactory.SerialAlignment.JUSTIFY,4));
        this.setBackground(ConfigManager.BACKGROUND_MEDIUM);
        Border border = BorderFactory.createLineBorder(1,ConfigManager.BACKGROUND_LIGHT);
        border.getInsets().set(10,10,10,10);
        this.setBorder(border);
        this.setOpaque(true);
        // set thumbnail or icon
        if (MyContext instanceof IViewable) {
            IViewable displayobject = (IViewable) MyContext;
            thumbnail = displayobject.getThumbnail();
            Image scaled = thumbnail.getScaledInstance(60,38,Image.SCALE_DEFAULT);
            ImageWidget image = new ImageWidget(MyScene,thumbnail);
            this.addChild(image);
        }
        // set label
        label = new LabelWidget(MyScene,MyContext.getName());
        label.setForeground(ConfigManager.TEXT_LIGHT);
        this.addChild(label);
        // add context menu to widget
        IContext context = this.target.getContext();
        ScenarioSmallVisualWidgetPopupProvider provider = new ScenarioSmallVisualWidgetPopupProvider(MyScene,this);
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
            switch (eventId) {
                case ConfigManager.EVENT_NAME_CHANGE:
                    INamed named = (INamed) o;
                    this.label.setLabel(named.getName());
                    break;
                case ConfigManager.EVENT_DESCRIPTION_CHANGE:
                    System.out.println("ERROR: Not implemented. VisualWidget.update:DescriptionChange");
                    break;
                case ConfigManager.EVENT_ICON_CHANGE:
                    System.out.println("ERROR: Not implemented. VisualWidget.update:Icon Change");
                    break;
                case ConfigManager.EVENT_THUMBNAIL_CHANGE:
                    System.out.println("ERROR: Not implemented. VisualWidget.update:Thumbnail Change");
                    break;
            }
        }
    }
    
} // end class