/*
 * IBeanProxy.java
 * Created on May 3, 2006, 1:46 PM
 */

package com.developer.rose;

import java.util.List;

/**
 * <p>
 * BeanProxy is a class for accessing a JavaBean dynamically at runtime. It
 * provides methods for getting properties, setting properties, and invoking
 * methods on a target bean. BeanProxy can be used in situations where bean
 * properties and methods are not known at compile time, or when compile time
 * access to a bean would result in brittle code.
 * </p>
 * <p>
 * Source: http://www.developer.com/java/other/print.php/864941
 * </p>
 *
 * @author Thornton Rose
 * @version 1.2
 */
public interface IBeanProxy {
    
    /**
     * Get bean property.
     * @param name Property name.
     * @throws Exception
     * @return Property value.
     */
    public Object get(String name) throws Exception;
    
    /**
     * Get bean property names.
     * @return List of property names.
     */
    public List getPropertyNames();
    
    /**
     * Set bean property.
     */
    public Object set(String name, Object value) throws Exception;
    
    /**
     * Invoke named method on target bean.
     */
    public Object invoke(String name, Class[] types, Object[] parameters) throws Exception;
    
} // end class IBeanProxy