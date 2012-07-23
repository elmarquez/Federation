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

import ca.sfu.federation.model.IContext;
import ca.sfu.federation.model.INamed;
import java.util.Map;

/**
 * Common utility methods for INamed objects.
 * @author dmarques
 */
public class INamedUtils {

    /**
     * Get the fully qualified object name.
     * @param Named Named object
     * @return Canonical name
     */
    public static String getCanonicalName(INamed Named) {
        String name = "";
        if (Named.getContext() != null) {
            name = Named.getContext().getCanonicalName() + ".";
        }
        name += Named.getName();
        return name;
    }

    /**
     *
     * @param Context
     * @param Named
     * @throws Exception
     */
    public static void registerInContext(IContext Context,INamed Named) throws Exception {
        Context.add(Named);
        Named.setContext(Context);
    }

    /**
     * Set the object name.
     * @param Name Name
     * @throws Exception
     */
    public static void setName(INamed Named, String Name) throws Exception {
        IContext context = Named.getContext();
        Map<String,INamed> map = context.getElementMap();
        if (!map.containsKey(Name)) {
            
        } else {
            
        }
    }

}
