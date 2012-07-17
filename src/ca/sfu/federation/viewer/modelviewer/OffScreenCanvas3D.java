/**
 * OffScreenCanvas3D.java
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

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.Screen3D;

/**
 * PictureGrabber is designed to be used as an offscreen canvas which can take a
 * picture of a model with an alpha masked background. This is done by reading
 * the canvas into a raster with a depth componant, and then checking which
 * pixels were written too and which were not.
 * Excerpted from: http://forums.java.net/jive/thread.jspa?threadID=5287&tstart=0
 * @author mikofclassx@dev.java.net
 */
class OffScreenCanvas3D extends Canvas3D {
    
    //-------------------------------------------------------------------------

    
    private BufferedImage bImage;

    //-------------------------------------------------------------------------


    public OffScreenCanvas3D(GraphicsConfiguration graphicsConfiguration,boolean offScreen, int width, int height) {
        super(graphicsConfiguration, offScreen);
        this.setSize(width, height);
        Screen3D scr = this.getScreen3D();
        scr.setSize(320,256);
        scr.setPhysicalScreenWidth(0.36124);
        scr.setPhysicalScreenHeight(0.28899555);
        bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        ImageComponent2D buffer = new ImageComponent2D(ImageComponent.FORMAT_RGBA,bImage);
        this.setOffScreenBuffer(buffer);
    }

    //-------------------------------------------------------------------------


    /**
     * Render off screen image.
     * @param saveImage Save the image.
     */
    public BufferedImage doRender(boolean saveImage) {
        renderOffScreenBuffer();
        waitForOffScreenRendering();
        if (saveImage) {
            bImage = getOffScreenBuffer().getImage();
        }
        return bImage;
    }
    
    /**
     * No-op since we always wait for off-screen rendering to complete
     */
    public void postSwap() {
    }
    
} 