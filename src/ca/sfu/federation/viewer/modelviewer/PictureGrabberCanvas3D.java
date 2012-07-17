/**
 * PictureGrabberCanvas3D.java
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;

/**
 * PictureGrabber is designed to be used as an offscreen canvas which can take a
 * picture of a model with an alpha masked background. This is done by reading
 * the canvas into a raster with a depth componant, and then checking which
 * pixels were written too and which were not.
 * Excerpted from: http://forums.java.net/jive/thread.jspa?threadID=5627&tstart=0
 * @author  mikofclassx@dev.java.net
 */
class PictureGrabberCanvas3D extends Canvas3D {

    //-------------------------------------------------------------------------

    
    private int width = -1;
    private int height = -1;
    private ImageComponent2D offscreenBuffer = null;
    private Raster raster = null;
    private BufferedImage pic = null;
    private BufferedImage canvas_img = null;

    //-------------------------------------------------------------------------

    
    public PictureGrabberCanvas3D(GraphicsConfiguration config, int width, int height) {
        super(config, true);
        setSize(width, height);
    }

    //-------------------------------------------------------------------------

    
    public static GraphicsConfiguration getOffscreenGraphicsConfig() {
        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
        template.setDoubleBuffer(GraphicsConfigTemplate3D.UNNECESSARY);
        template.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);
        GraphicsConfiguration gcfg = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(template);
        return gcfg;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        pic = takePicture();
        if (pic != null) {
            int w = getWidth();
            int h = getHeight();
            // create double buffer canvas if there isn't one
            if (canvas_img == null) {
               // NOT SUPPORTED IN JAVA 1.2
               //canvas_img = getGraphicsConfiguration().createCompatibleImage(w,h);
                canvas_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            }
            // paint over canvas_img
            Graphics2D gc = (Graphics2D) canvas_img.getGraphics();
            gc.setComposite(AlphaComposite.Src);
            paintCheckerboard(w, h, gc, 8, 8, Color.DARK_GRAY, Color.GRAY);
            gc.setComposite(AlphaComposite.SrcOver);
            gc.drawImage(pic, 0, 0, null);
            // now paint the result into the canvas
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.drawImage(canvas_img, 0, 0, null);
        }
    }

    /**
     * Paints this component with a checkerboard.
     */
    public static void paintCheckerboard(int width, int height, Graphics g, int patternW, int patternH, Color color0, Color color1) {
           // save old color
        Color oldcol = g.getColor();
        // fill background with color0
        g.setColor(color0);
        g.fillRect(0, 0, width, height);
        // set color1
        g.setColor(color1);
        boolean a = false;
        boolean b = false;
        for (int x = 0; x < width; x += patternW) {
            a = !a;
            b = !a;
            for (int y = 0; y < height; y += patternH) {
                b = !b;
                if (b) {
                    g.fillRect(x, y, patternW, patternH);
                }
            }
        }
        // restore color
        g.setColor(oldcol);
    }

    /**
     * Our post swap is used to grab an image of the canvas into a raster and
     * then use the depth component to mask out the background from the image.
     */
    public void postSwap() {
           // do nothing at the moment
    }

    // someone is resizing us, so we must resize the offscreen too
    public void setSize(int newWidth, int newHeight) {
        super.setSize(newWidth, newHeight);
        // resize only when we have a different size
        if ((pic == null) || (newWidth != width) || (newHeight != height)) {
            this.width = newWidth;
            this.height = newHeight;
            // this avoids invalid image sizes
            if (width <= 0) width = 1;
            if (height <= 0) height = 1;
            // setup the screen
            this.getScreen3D().setSize(width, height);
            this.getScreen3D().setPhysicalScreenWidth(0.0254 / 90.0 * width);
            this.getScreen3D().setPhysicalScreenHeight(0.0254 / 90.0 * height);
            // create the offscreen buffer
            //pic = getGraphicsConfiguration().createCompatibleImage(width,height,Transparency.TRANSLUCENT);
            pic = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            offscreenBuffer = new ImageComponent2D(ImageComponent2D.FORMAT_RGBA, pic, true, false);
            offscreenBuffer.setCapability(ImageComponent2D.ALLOW_IMAGE_READ);
            this.setOffScreenBuffer(offscreenBuffer);
            // force the creation of the canvas img buffer
            canvas_img = null;
        }
    }

    /**
     * This will take the picture immediately by forcing a frame draw. This is
     * only acceptable if the canvas is an offscreen canvas.
     */
    public BufferedImage takePicture() {
           // start the rendering and wait
        this.renderOffScreenBuffer();
        this.waitForOffScreenRendering();
        // return the image
        return pic;
    }

    /**
     * Update the contents (avoid flickering)
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * When this canvas is layouted we resize the offscreen.
     */
    public void validate() {
        super.validate();
        setSize(getWidth(), getHeight());
    }

} 

//public class OffScreenTest extends JFrame {
//  
//
//    //-------------------------------------------------------------------------
//
//    private SimpleUniverse u = null;
//
//    //-------------------------------------------------------------------------
//
//    public OffScreenTest() {
//        init();
//    }
//
//    //-------------------------------------------------------------------------
//
//    public BranchGroup createSceneGraph() {
//           // Create the root of the branch graph
//        BranchGroup objRoot = new BranchGroup();
//        // spin object has composited transformation matrix
//        Transform3D spin = new Transform3D();
//        Transform3D tempspin = new Transform3D();
//        spin.rotX(Math.PI / 4.0d);
//        tempspin.rotY(Math.PI / 5.0d);
//        spin.mul(tempspin);
//        spin.setScale(0.7);
//        spin.setTranslation(new ArrayList3d(-0.4, 0.3, 0.0));
//        TransformGroup objTrans = new TransformGroup(spin);
//        objRoot.addChild(objTrans);
//
//        // Create a simple shape leaf node, add it to the scene graph.
//        // ColorCube is a Convenience Utility class
//        ColorCube cube = new ColorCube(0.4);
//        objTrans.addChild(cube);
//
//        // Let Java 3D perform optimizations on this scene graph.
//        objRoot.compile();
//
//        return objRoot;
//    }
//
//    public void init() {
//        setLayout(new BorderLayout());
//        // create the canvas
//        PictureGrabberCanvas3D d = new PictureGrabberCanvas3D(PictureGrabberCanvas3D.getOffscreenGraphicsConfig(), 320, 200);
//        add("Center", d);
//        // create a simple universe
//        u = new SimpleUniverse(d);
//        // This will move the ViewPlatform back a bit so the
//        // objects in the scene can be viewed.
//        u.getViewingPlatform().setNominalViewingTransform();
//        // add the scene
//        BranchGroup scene = createSceneGraph();
//        u.addBranchGraph(scene);
//    }
//    
//    public void destroy() {
//        u.cleanup();
//    }
//    
//    public static void main(String argv[]) {
//        JFrame f = new OffScreenTest();
//        f.setSize(400, 400);
//        f.setVisible(true);
//    }
//}