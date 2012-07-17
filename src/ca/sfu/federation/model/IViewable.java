/**
 * IViewable.java
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

package ca.sfu.federation.model;

import java.awt.Image;
import javax.media.j3d.Node;
import javax.swing.ImageIcon;

/**
 * An object that can be displayed in a 2D or 3D viewer.
 * @author Davis Marques
 * @version 0.1
 */
public interface IViewable extends INamed {
    
    /**
     * Get an icon representation of the object.
     * @return Image.
     */
    public abstract ImageIcon getIcon();
    
    /**
     * Get a Java3D renderable object.
     * @return Java3D renderable node.
     */
    public abstract Node getRenderable();
    
    /**
     * Get a thumbnail representation of the object.
     * TODO: consider returning a thumbnail set at different resolutions rather than a single thumbnail
     * @return Thumbnail image representation of the object.
     */
    public abstract Image getThumbnail();
    
    /**
     * Get visibility state for the object.
     * @return True if visible, false otherwise.
     */
    public abstract boolean getVisible();

    /**
     * Set the visibility.
     * @param state True to set visible, false to turn off visibility.
     */
    public abstract void setVisible(boolean state);

} 