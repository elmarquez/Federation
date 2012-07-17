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

package ca.sfu.federation.utils;

import java.io.File;
import java.util.logging.Logger;
import javax.swing.filechooser.FileFilter;

/**
 * Filter an input list to return only project directories.
 * @author Davis Marques
 */
public class ProjectDirectoryFilter extends FileFilter {

    private static final Logger logger = Logger.getLogger(ProjectDirectoryFilter.class.getName());
    
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
    
} 
