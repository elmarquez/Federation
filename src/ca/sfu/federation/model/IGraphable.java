/*
 * IGraphable.java
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

import java.util.Map;

/**
 * A model object that may have dependancies on other models objects, that can 
 * be represented in a graph.
 *
 * @author Davis Marques
 * @version 0.1.0
 */
public interface IGraphable extends INamed {

    /**
     * Get object dependancies.
     *
     * @return Objects upon which this object is dependant.
     */
    public Map getDependancies();
    
} // end interface
