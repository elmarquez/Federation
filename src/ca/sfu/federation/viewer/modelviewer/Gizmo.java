/**
 * Gizmo.java
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

package ca.sfu.federation.viewer.modelviewer;

import com.sun.j3d.utils.geometry.Cone;
import java.awt.Color;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 * Gizmo is a Shape3D object that aids the user in applying transformations to a
 * 3D object, and in representing the current transformation mode.  The Gizmo is
 * comprised of graphical objects that represent translation, rotation and
 * scaling controls respectively.  The Gizmo has all three objects, but displays
 * only the control that applies to the selected transformation mode.
 *
 * @author Davis Marques
 * @version 0.1.0
 */
public class Gizmo extends Group {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    public static final int GIZMO_TRANSLATE = 0;
    public static final int GIZMO_ROTATE = 1;
    public static final int GIZMO_SCALE = 2;

    public static final int UNIT_SEGMENT_LENGTH = 1;
    public static final int CIRCLE_SEGMENTS = 64;
    public static final float CONE_CONTROL_RADIUS = 0.25f;
    public static final float CONE_CONTROL_HEIGHT = 0.5f;

    private Color3f RED;
    private Color3f GREEN;
    private Color3f BLUE;
    private Appearance APP_RED;
    private Appearance APP_GREEN;
    private Appearance APP_BLUE;
    
    private int mode;
    private TransformGroup tg;
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * Gizmo constructor.
     */
    public Gizmo() {
        // defaults to transform
        mode = GIZMO_TRANSLATE;
        // create a transform group for the gizmo
        tg = new TransformGroup();
        this.addChild(tg);
        // add the graphical widgets
        setColorDefaults();
        this.addChild(buildGizmo());
    }
    
    /**
     * Gizmo constructor.
     * @param TransformMode
     */
    public Gizmo(int TransformMode) {
        // set the transform mode
        mode = TransformMode;
        // create a transform group for the gizmo
        tg = new TransformGroup();
        this.addChild(tg);
        // add the graphical widgets
        setColorDefaults();
        this.addChild(buildGizmo());
    }
    
    //--------------------------------------------------------------------------
    // METHODS

    /**
     * @return Circle.
     */
    public Shape3D buildCircle() {
        LineArray array = new LineArray(CIRCLE_SEGMENTS,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        Shape3D shape = new Shape3D();
        // construct circle shape
        
        // return result
        return shape;
    }
    
    /**
     * Build a transformation gizmo.  Display the appropriate icon depending on 
     * the current transformation mode.
     * @return Group with Gizmo icons.
     */
    public Group buildGizmo() {
        // the gizmo icon group
        Group gizmogroup = new Group();
        
        // translate icon
        LineArray translate_icon = new LineArray(6,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        // X-Axis
        translate_icon.setCoordinate(0,new Point3f(0.0f,0.0f,0.0f));
        translate_icon.setCoordinate(1,new Point3f(UNIT_SEGMENT_LENGTH,0.0f,0.0f));
        translate_icon.setColor(0,RED);
        translate_icon.setColor(1,RED);
        // Y-Axis
        translate_icon.setCoordinate(2,new Point3f(0.0f,0.0f,0.0f));
        translate_icon.setCoordinate(3,new Point3f(0.0f,UNIT_SEGMENT_LENGTH,0.0f));
        translate_icon.setColor(2,GREEN);
        translate_icon.setColor(3,GREEN);
        // Z-Axis
        translate_icon.setCoordinate(4,new Point3f(0.0f,0.0f,0.0f));
        translate_icon.setCoordinate(5,new Point3f(0.0f,0.0f,UNIT_SEGMENT_LENGTH));
        translate_icon.setColor(4,BLUE);
        translate_icon.setColor(5,BLUE);
        // Cone control widgets
        Cone conex = new Cone(CONE_CONTROL_RADIUS,CONE_CONTROL_HEIGHT);
        conex.setAppearance(APP_RED);
        Cone coney = new Cone(CONE_CONTROL_RADIUS,CONE_CONTROL_HEIGHT);
        coney.setAppearance(APP_GREEN);
        Cone conez = new Cone(CONE_CONTROL_RADIUS,CONE_CONTROL_HEIGHT);
        conez.setAppearance(APP_BLUE);
        // add line array to gizmogroup
        gizmogroup.addChild(new Shape3D(translate_icon));
        
        // rotate icon
        LineArray rotate_icon = new LineArray(CIRCLE_SEGMENTS, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        // create groups and transform groups under the main gizmo gizmogroup
        Group gx = new Group();
        Group gy = new Group();
        Group gz = new Group();
        gizmogroup.addChild(gx);
        gizmogroup.addChild(gy);
        gizmogroup.addChild(gz);
        TransformGroup tgx = new TransformGroup();
        TransformGroup tgy = new TransformGroup();
        TransformGroup tgz = new TransformGroup();
        Transform3D tx = new Transform3D();
        Transform3D ty = new Transform3D();
        Transform3D tz = new Transform3D();
        // tx.setRotation();
        // ty.setRotation();
        // tz.setRotation();
        tgx.setTransform(tx);
        tgy.setTransform(ty);
        tgz.setTransform(tz);
        gx.addChild(tgx);
        gy.addChild(tgy);
        gz.addChild(tgz);
        // create circle shapes
        Shape3D cx = buildCircle();
        Shape3D cy = buildCircle();
        Shape3D cz = buildCircle();
        //

        // needs small boxes at the end of each axis to act as controls
        // scale icon
        LineArray scale_icon = new LineArray(6,GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        // X-Axis
        scale_icon.setCoordinate(0,new Point3f(0.0f,0.0f,0.0f));
        scale_icon.setCoordinate(1,new Point3f(UNIT_SEGMENT_LENGTH,0.0f,0.0f));
        scale_icon.setColor(0,new Color3f(Color.red));
        scale_icon.setColor(1,new Color3f(Color.red));
        // Y-Axis
        scale_icon.setCoordinate(2,new Point3f(0.0f,0.0f,0.0f));
        scale_icon.setCoordinate(3,new Point3f(0.0f,UNIT_SEGMENT_LENGTH,0.0f));
        scale_icon.setColor(2,new Color3f(Color.green));
        scale_icon.setColor(3,new Color3f(Color.green));
        // Z-Axis
        scale_icon.setCoordinate(4,new Point3f(0.0f,0.0f,0.0f));
        scale_icon.setCoordinate(5,new Point3f(0.0f,0.0f,UNIT_SEGMENT_LENGTH));
        scale_icon.setColor(4,new Color3f(Color.blue));
        scale_icon.setColor(5,new Color3f(Color.blue));
        // add line array to gizmogroup
        gizmogroup.addChild(new Shape3D(scale_icon));

        // 
        
        // return gizmo
        return gizmogroup;
    }

    private void setColorDefaults() {
        RED = new Color3f(Color.red);
        GREEN = new Color3f(Color.green);
        BLUE = new Color3f(Color.blue);
        APP_RED = new Appearance();
        APP_GREEN = new Appearance();
        APP_BLUE = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(RED);
        APP_RED.setColoringAttributes(ca);
        ca.setColor(GREEN);
        APP_GREEN.setColoringAttributes(ca);
        ca.setColor(BLUE);
        APP_BLUE.setColoringAttributes(ca);
    }
    
    /**
     * Set the gizmo transform mode.
     * @param Mode The transform mode.
     */
    public void setTransformMode(int Mode) {
        switch (Mode) {
            case GIZMO_TRANSLATE:
                // hide all icon groups
                // show this icon group
                break;
            case GIZMO_ROTATE:
                // hide all icon groups
                // show this icon group
                break;
            case GIZMO_SCALE:
                // hide all icon groups
                // show this icon group
                break;
        }
    }
    
} // end class
