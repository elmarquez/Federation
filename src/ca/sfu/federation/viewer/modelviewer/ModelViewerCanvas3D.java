/**
 * ModelViewerCanvas3D.java
 * Copyright (c) 2006 Davis M. Marques <dmarques@sfu.ca>
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

package ca.sfu.federation.viewer.modelviewer;

import ca.sfu.federation.model.ConfigManager;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.IViewable;
import ca.sfu.federation.model.INamed;
import ca.sfu.federation.model.IUpdateable;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.universe.SimpleUniverse;
import gnu.trove.THashMap;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

/**
 * IContext 3D viewer panel. 
 * @author Davis Marques, Sun Microsystems
 * @version 0.1.0
 * 
 * Portions of code excerpted from: http://www.java2s.com/Code/Java/3D/PrintCanvas3D.htm
 * Copyright (c) 1996-2002 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistribution in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 */
public class ModelViewerCanvas3D extends Canvas3D implements MouseListener, Observer {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
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
    // CONSTRUCTORS
    
    /**
     * ModelViewerIContextAdapter constructor.
     */
    public ModelViewerCanvas3D(GraphicsConfiguration Config) {
        super(Config);
        this.model = ParametricModel.getInstance();
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
    // METHODS
    
    /**
     * Build an origin axis to represent the model world origin.
     * @return Line array representing the model origin.
     */
    private Shape3D buildOriginAxis() {
        // axis shape
        LineArray axis = null;
        if (this.ORIGIN_NEGATIVE_AXES) {
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
        this.context = (IContext) this.model.getViewState(ConfigManager.VIEWER_CURRENT_CONTEXT);
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
            THashMap elements = (THashMap) this.context.getElements();
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
//        THashMap thumbnails = (THashMap) this.model.getViewState(ConfigManager.VIEWER_ICONTEXT_THUMBNAILS);
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
            System.out.println("Nothing picked");
        } else {
            Node node = info.getObject();
            if (node instanceof DisplayObject3D) {
                DisplayObject3D displayobject = (DisplayObject3D) node;
                IViewable target = displayobject.getTarget();
                INamed named = (INamed) target;
                System.out.println("INFO: User picked " + named.getName() + " " + named.toString());
            } else {
                System.out.println("INFO: User picked " + node.getClass().getName());
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
        System.out.println("INFO: ModelViewer3D fired scene thumbnail update button.");
        BufferedImage image = this.getThumbnail();
        THashMap thumbnails = (THashMap) this.model.getViewState(ConfigManager.VIEWER_ICONTEXT_THUMBNAILS);
        thumbnails.put(this.context.getCanonicalName(),image);
        // this will force an update event from the model
        this.model.setViewState(ConfigManager.VIEWER_ICONTEXT_THUMBNAILS,thumbnails);
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            System.out.println("INFO: ModelViewerCanvas3D received event notification id " + eventId);
            switch (eventId) {
                case ConfigManager.EVENT_CHANGE:
                case ConfigManager.EVENT_CONTEXT_CHANGE:
                case ConfigManager.EVENT_ELEMENT_ADD:
                case ConfigManager.EVENT_ELEMENT_CHANGE:
                case ConfigManager.EVENT_ELEMENT_DELETE_REQUEST:
                case ConfigManager.EVENT_ELEMENT_RENAME:
                    System.out.println("INFO: ModelViewer3D fired scene update update.");
                    this.buildScene();
                    break;
            }
        }
    }
    
} // end class
