/**
 * DisplayObject3D.java
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

import ca.sfu.federation.model.IViewable;
import javax.media.j3d.Shape3D;

/**
 * 
 * @author Davis Marques
 * @version 0.1.0
 */
public class DisplayObject3D extends Shape3D {
    
    //--------------------------------------------------------------------------

    
    private IViewable target; // the model object that this object represents in the 3D viewer
    
    //--------------------------------------------------------------------------

    
    /**
     * DisplayObject3D constructor.
     */
    public DisplayObject3D() {
    }

    /**
     * DisplayObject3D constructor.
     * @param Target
     */
    public DisplayObject3D(IViewable Target) {
        this.target = Target;
    }
    
    //--------------------------------------------------------------------------

    
    /**
     * Get the target object.
     * @return The target object.
     */
    public IViewable getTarget() {
        return this.target;
    }
    
    /**
     * Set the target object.
     * @param Target
     */
    public void setTarget(IViewable Target) {
        this.target = Target;
    }
    
} // end class
