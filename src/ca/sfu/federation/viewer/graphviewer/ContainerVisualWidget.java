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

package ca.sfu.federation.viewer.graphviewer;

import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.IViewable;
import ca.sfu.federation.model.ParametricModel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 */
public class ContainerVisualWidget extends Widget implements IVisualWidget, Observer {

    private static final Logger logger = Logger.getLogger(ContainerVisualWidget.class.getName());

    private static final String ACTION_OPEN_SCENARIO = "OPENSCENARIO";
    private static final String ACTION_SET_ACTIVE_SCENARIO = "SETSCENARIOACTIVE";
    private static final String ACTION_MOVE = "MOVE";

    private INamed target; // the model object that this widget points to
    private ImageWidget thumbnailWidget;
    private LabelWidget label;
    private WidgetAction popup;

    private int style = 0;
    private Image thumbnail;
    private Image icon;

    private WidgetAction moveAction = ActionFactory.createMoveAction();

    //--------------------------------------------------------------------------

    /**
     * ContainerVisualWidget constructor.
     * @param MyScene The scene the widget is being displayed in.
     * @param Named The target object.
     */
    public ContainerVisualWidget(Scene MyScene, INamed Named) {
        // init
        super(MyScene);
        this.target = Named;
        // watch the target for changes
        if (Named instanceof Observable) {
            Observable observable = (Observable) Named;
            observable.addObserver(this);
        }
        // set the widget properties
        Border border = null;
        switch (style) {
            case 0:
                this.setLayout(LayoutFactory.createVerticalLayout(LayoutFactory.SerialAlignment.JUSTIFY,4));
                border = BorderFactory.createRoundedBorder(8,8,4,4,ApplicationContext.BACKGROUND_LIGHT,ApplicationContext.BACKGROUND_DARK);
                this.setBorder(border);
                this.setBackground(ApplicationContext.BACKGROUND_MEDIUM);
                this.setOpaque(true);
                break;
            case 1:
                this.setLayout(LayoutFactory.createVerticalLayout(LayoutFactory.SerialAlignment.JUSTIFY,4));
                break;
            case 2:
                this.setLayout(LayoutFactory.createVerticalLayout(LayoutFactory.SerialAlignment.JUSTIFY,4));
                border = BorderFactory.createBevelBorder(true,ApplicationContext.BACKGROUND_MEDIUM);
                this.setBorder(border);
                this.setBackground(ApplicationContext.BACKGROUND_MEDIUM);
                this.setOpaque(true);
                break;
        }

        // set thumbnail or icon
        if (Named instanceof IViewable) {
            IViewable displayobject = (IViewable) Named;
            thumbnail = displayobject.getThumbnail();
            this.thumbnailWidget = new ImageWidget(MyScene,thumbnail);
            this.addChild(thumbnailWidget);
        }

        // set label
        label = new LabelWidget(MyScene, Named.getName());
        label.setForeground(ApplicationContext.TEXT_LIGHT);
        this.addChild(label);

        // make the widget moveable
        this.getActions().addAction(ActionFactory.createMoveAction());

        // add context menu to widget
        IContext context = this.target.getContext();
        ContainerVisualWidgetPopupProvider provider = new ContainerVisualWidgetPopupProvider((Scene)MyScene,this);
        this.popup = ActionFactory.createPopupMenuAction(provider);
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
            logger.log(Level.INFO,"ContainerVisualWidget received event notification id {0}", eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_NAME_CHANGE:
                    logger.log(Level.INFO,"ContainerVisualWidget fired name change event");
                    INamed named = (INamed) o;
                    this.label.setLabel(named.getName());
                    this.repaint();
                    break;
                case ApplicationContext.EVENT_PROPERTY_CHANGE:
                    logger.log(Level.WARNING,"ContainerVisualWidget property change event not implemented");
                    break;
                case ApplicationContext.EVENT_DESCRIPTION_CHANGE:
                    logger.log(Level.WARNING,"ContainerVisualWidget description change event not implemented");
                    break;
                case ApplicationContext.EVENT_ICON_CHANGE:
                    logger.log(Level.WARNING,"ContainerVisualWidget icon change event not implemented");
                    break;
                case ApplicationContext.EVENT_THUMBNAIL_CHANGE:
                    logger.log(Level.WARNING,"ContainerVisualWidget thumbnail change event not implemented");
                    break;
            }
        }
        // repaint the object
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

} // end class
