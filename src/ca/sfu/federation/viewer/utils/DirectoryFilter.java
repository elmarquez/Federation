/**
 * DirectoryFilter.java
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

package ca.sfu.federation.viewer.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filter an input list to return only directory objects.
 * @author Davis Marques
 * @version 0.1.0
 */
public class DirectoryFilter extends FileFilter {

    //--------------------------------------------------------------------------
    // CONSTRUCTORS
    
    /**
     * DirectoryFilter constructor.
     */
    public DirectoryFilter() {
    }
    
    //--------------------------------------------------------------------------
    // METHODS

    /**
     * Determine whether the file is a directory.
     * @param f File.
     * @return True if the file is a directory, false otherwise.
     */
    public boolean accept(File f) {
        boolean result = false;
        if (f.isDirectory()) {
            result = true;
        }
        return result;
    }

    /**
     * Get the directory description.
     * @return Directory description.
     */
    public String getDescription() {
        return "Filesystem Directory";
    }
    
} // end class
