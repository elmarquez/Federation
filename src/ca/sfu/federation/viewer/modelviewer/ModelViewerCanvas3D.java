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

package ca.sfu.federation.viewer.modelviewer;

import ca.sfu.federation.Application;
import ca.sfu.federation.ApplicationContext;
import ca.sfu.federation.model.*;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

/**
 * IContext 3D viewer panel. 
 * @author Davis Marques
 * @version 0.1.0
 */
public class ModelViewerCanvas3D extends Canvas3D implements MouseListener, Observer {

    private static final Logger logger = Logger.getLogger(ModelViewerCanvas3D.class.getName());
    
    private static final float ORIGIN_AXIS_LENGTH = 1.0f;
    private static final boolean ORIGIN_NEGATIVE_AXES = false;
    
    private ParametricModel model;
    private IContext context;
    private SimpleUniverse universe;
    private BranchGroup scene;
    private PickCanvas pickCanvas;
    private Gizmo gizmo;
    
    private boolean init;
    
    // for screencapture
    private PictureGrabberCanvas3D offscreen;
    private boolean print = false;
    private boolean imageReady = false;
    private BufferedImage screenshot;
    
    //--------------------------------------------------------------------------

    
    /**
     * ModelViewerIContextAdapter constructor.
     */
    public ModelViewerCanvas3D(GraphicsConfiguration Config) {
        super(Config);
        this.model = Application.getContext().getModel();
        // update the context before displaying
        if (this.context != null && this.context instanceof IUpdateable) {
            IUpdateable updateable = (IUpdateable) this.context;
            updateable.update();
        }
        
        // create universe and canvas3d
        this.universe = new SimpleUniverse(this);
        this.scene = new BranchGroup();
        this.scene.setCapability(BranchGroup.ALLOW_DETACH);
        this.scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        this.scene.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        this.scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        this.universe.getLocale().addBranchGraph(this.scene);
        TransformGroup viewPlatform = this.universe.getViewingPlatform().getViewPlatformTransform();
        // add orbit listener
        OrbitBehavior orbit = new OrbitBehavior(this);
        orbit.setTranslateEnable(true);
        orbit.setReverseTranslate(true);
        orbit.setReverseRotate(true);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY));
        this.universe.getViewingPlatform().setViewPlatformBehavior(orbit);
        // add translation listener
        // MouseTranslate mouseTranslate = new MouseTranslate(MouseBehavior.INVERT_INPUT);
        // mouseTranslate.setTransformGroup(viewPlatform);
        // mouseTranslate.setSchedulingBounds(new BoundingSphere());
        // newScene.addChild(mouseTranslate);
        
        // add mouse listener to canvas
        this.addMouseListener(this);
        
        // create an off Screen Canvas, attach it to the view
        // int width = Config.getDevice().getDisplayMode().getWidth();
        // int height = Config.getDevice().getDisplayMode().getHeight();
        /// this.offscreen = new PictureGrabberCanvas3D(PictureGrabberCanvas3D.getOffscreenGraphicsConfig(),width,height);
        // this.offscreenCanvas = new OffScreenCanvas3D(Config,true,width,height);
        // this.universe.getViewer().getView().addCanvas3D(this.offscreen);

        // build and add the scene objects
        this.init = true;
        buildScene();
        this.init = false;
        
        // listen for changes on the model
        if (model instanceof Observable) {
            Observable observable = (Observable) model;
            observable.addObserver(this);
        }
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Build an origin axis to represent the model world origin.
     * @return Line array representing the model origin.
     */
    private Shape3D buildOriginAxis() {
        // axis shape
        LineArray axis = null;
        if (ORIGIN_NEGATIVE_AXES) {
            // show positive and negative axes
            axis = new LineArray(6,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
            // X-Axis
            axis.setCoordinate(0,new Point3f(-(ORIGIN_AXIS_LENGTH),0.0f,0.0f));
            axis.setCoordinate(1,new Point3f(ORIGIN_AXIS_LENGTH,0.0f,0.0f));
            axis.setColor(0,new Color3f(Color.red));
            axis.setColor(1,new Color3f(Color.red));
            // Y-Axis
            axis.setCoordinate(2,new Point3f(0.0f,-(ORIGIN_AXIS_LENGTH),0.0f));
            axis.setCoordinate(3,new Point3f(0.0f,ORIGIN_AXIS_LENGTH,0.0f));
            axis.setColor(2,new Color3f(Color.green));
            axis.setColor(3,new Color3f(Color.green));
            // Z-Axis
            axis.setCoordinate(4,new Point3f(0.0f,0.0f,-(ORIGIN_AXIS_LENGTH)));
            axis.setCoordinate(5,new Point3f(0.0f,0.0f,ORIGIN_AXIS_LENGTH));
            axis.setColor(4,new Color3f(Color.blue));
            axis.setColor(5,new Color3f(Color.blue));
        } else {
            // show positive axes
            axis = new LineArray(6,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
            // X-Axis
            axis.setCoordinate(0,new Point3f(0.0f,0.0f,0.0f));
            axis.setCoordinate(1,new Point3f(ORIGIN_AXIS_LENGTH,0.0f,0.0f));
            axis.setColor(0,new Color3f(Color.red));
            axis.setColor(1,new Color3f(Color.red));
            // Y-Axis
            axis.setCoordinate(2,new Point3f(0.0f,0.0f,0.0f));
            axis.setCoordinate(3,new Point3f(0.0f,ORIGIN_AXIS_LENGTH,0.0f));
            axis.setColor(2,new Color3f(Color.green));
            axis.setColor(3,new Color3f(Color.green));
            // Z-Axis
            axis.setCoordinate(4,new Point3f(0.0f,0.0f,0.0f));
            axis.setCoordinate(5,new Point3f(0.0f,0.0f,ORIGIN_AXIS_LENGTH));
            axis.setColor(4,new Color3f(Color.blue));
            axis.setColor(5,new Color3f(Color.blue));
        }
        // return result
        return new Shape3D(axis);
    }
    
    /**
     * Build the 3D scene.
     */
    private void buildScene() {
        // get the context
        this.context = (IContext) Application.getContext().getViewState(ApplicationContext.VIEWER_CURRENT_CONTEXT);
        // clear the current scene, if it has already been initialized
        if (!init) {
            this.scene.removeAllChildren();
        }
        // create a new scene root
        BranchGroup group = new BranchGroup();
        group.setCapability(BranchGroup.ALLOW_DETACH);
        group.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        group.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        group.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        // add origin axes
        group.addChild(buildOriginAxis());
        // add context objects
        if (this.context != null) {
            LinkedHashMap elements = (LinkedHashMap) this.context.getElementMap();
            Iterator iter = elements.values().iterator();
            while (iter.hasNext()) {
                Object object = iter.next();
                if (object instanceof IViewable) {
                    IViewable displayobject = (IViewable) object;
                    Node node = displayobject.getRenderable();
                    group.addChild(node);
                }
            }
        }
        // compile the new scene then attach it to the branchgraph
        group.compile();
        this.scene.addChild(group);
        
        // set camera position
        this.universe.getViewingPlatform().setNominalViewingTransform();
        
        // add a pick canvas
        pickCanvas = new PickCanvas(this,this.scene);
        pickCanvas.setMode(PickCanvas.GEOMETRY);

        // set the scene thumbnail
//        LinkedHashMap thumbnails = (LinkedHashMap) this.model.getViewState(ConfigManager.VIEWER_ICONTEXT_THUMBNAILS);
//        if (!thumbnails.containsKey(this.context.getCanonicalName())) {
//            this.setThumbnail();
//        }
    }
    
    /**
     * Get a thumbnail of the scene.
     * @return Thumbnail image of the scene.
     */
    public BufferedImage getThumbnail() {
        return this.offscreen.takePicture();
    }
    
    public void mouseClicked(MouseEvent e) {
        pickCanvas.setShapeLocation(e);
        PickResult info = pickCanvas.pickClosest();
        if (info == null) {
            logger.log(Level.INFO,"Nothing picked");
        } else {
            Node node = info.getObject();
            if (node instanceof DisplayObject3D) {
                DisplayObject3D displayobject = (DisplayObject3D) node;
                IViewable target = displayobject.getTarget();
                INamed named = (INamed) target;
                logger.log(Level.INFO,"User picked {0} ", named.getName());
            } else {
                logger.log(Level.INFO,"User picked {0} ", node.getClass().getName());
            }
        }
    }
    
    /**
     * Mouse pressed event.
     * @param e Mouse event.
     */
    public void mousePressed(MouseEvent e) {
    }
    
    /**
     * Mouse released event.
     * @param e Mouse event.
     */
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * Mouse entered event.
     * @param e Mouse event.
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Mouse exited event.
     * @param e Mouse event.
     */
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Set the context thumbnail.
     */
    public void setThumbnail() {
        logger.log(Level.INFO,"ModelViewer3D fired scene thumbnail update button");
        BufferedImage image = this.getThumbnail();
        LinkedHashMap thumbnails = (LinkedHashMap) Application.getContext().getViewState(ApplicationContext.VIEWER_ICONTEXT_THUMBNAILS);
        thumbnails.put(this.context.getCanonicalName(),image);
        // this will force an update event from the model
        Application.getContext().setViewState(ApplicationContext.VIEWER_ICONTEXT_THUMBNAILS,thumbnails);
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            logger.log(Level.INFO,"ModelViewerCanvas3D received event notification id {0}", eventId);
            switch (eventId) {
                case ApplicationContext.EVENT_CHANGE:
                case ApplicationContext.EVENT_CONTEXT_CHANGE:
                case ApplicationContext.EVENT_ELEMENT_ADD:
                case ApplicationContext.EVENT_ELEMENT_CHANGE:
                case ApplicationContext.EVENT_ELEMENT_DELETE_REQUEST:
                case ApplicationContext.EVENT_ELEMENT_RENAME:
                    logger.log(Level.INFO,"ModelViewer3D fired scene update update");
                    this.buildScene();
                    break;
            }
        }
    }
    
} 
