/**
 * Main.java - Starts the application.
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

package ca.sfu.federation;

import ca.sfu.federation.viewer.ParametricModeller;

/**
 * Starts the research demonstration interface.
 * @author Davis Marques
 * @version 0.1.0
 */
public class Main {
    
    //--------------------------------------------------------------------------
    // FIELDS
    
    private static final boolean debug = true; // debug mode
    private static final int debugApp = 1;     // application number to run in debug mode
    private static final int panelType = 0;    // interface version
    
    //--------------------------------------------------------------------------
    // MAIN
    
    /**
     * Main application.
     * @param args Command line arguements.
     */
    public static void main(String[] args) {
        // run application
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // if running in debug mode
                if (debug) {
                    // select which application to launch
                    switch (debugApp) {
                        case 1:
                            ParametricModeller app = new ParametricModeller();
                            break;
                    }
                }
            }
        });
    }
    
} // end class
