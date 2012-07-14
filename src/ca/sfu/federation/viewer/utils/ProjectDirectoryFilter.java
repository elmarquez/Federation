/**
 * ProjectFilter.java
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
import java.io.IOException;
import javax.swing.filechooser.FileFilter;

/**
 * Filter an input list to return only project directories.
 * @author Davis Marques
 * @version 0.1.0
 */
public class ProjectDirectoryFilter extends FileFilter {

    //--------------------------------------------------------------------------

    
    /**
     * DirectoryFilter constructor.
     */
    public ProjectDirectoryFilter() {
    }
    
    //--------------------------------------------------------------------------


    /**
     * Determine whether the file is a project directory.
     * @param f File.
     * @return True if the file is a project directory, false otherwise.
     */
    public boolean accept(File f) {
        boolean found = false;
        if (f.isDirectory()) {
            // check to see if f contains project metadata file
            File[] files = f.listFiles();
            int i = 0;
            while (!found && i < files.length) {
                if (files[i].getName().equals("build.xml")) {
                    found = true;
                }
                i++;
            }
        }
        return found;
    }

    /**
     * Get the directory description.
     * @return Directory description.
     */
    public String getDescription() {
        return "Filesystem Directory";
    }
    
} // end class
