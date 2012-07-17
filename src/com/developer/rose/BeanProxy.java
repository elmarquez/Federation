/*
 * BeanProxy.java
 *
 * Created on May 3, 2006, 1:46 PM
 */

package com.developer.rose;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * BeanProxy is a class for accessing a JavaBean dynamically at runtime. It
 * provides methods for getting properties, setting properties, and invoking
 * methods on a target bean. BeanProxy can be used in situations where bean
 * properties and methods are not known at compile time, or when compile time
 * access to a bean would result in brittle code.
 * <br><br>
 * Here is an example of brittle code:
 * <pre>
 * public class MyTableModel extends DefaultTableModel {
 *    ...
 *    public Object getValueAt(int row, int col) {
 *       LogEntry entry;
 *       Object   value;
 *       try {
 *          entry = log.get(row);
 *          switch(col) {
 *             case 0:
 *                value = entry.getWorkDate();
 *                break;
 *             case 1:
 *                value = entry.getStartTime();
 *                break;
 *             ...
 *          }
 *        } catch(Exception ex) {
 *           return "";
 *       }
 *       return value;
 *    }
 *    ...
 * }
 * </pre>
 *
 * Here is an example of non-brittle code using BeanProxy:
 * <pre>
 * public class MyTableModel extends DefaultTableModel {
 *    ...
 *    public MyTableModel() {
 *       columns = new String[] { "workDate",  "startTime", "endTime",
 *          "mealTime", "hours", "notes" };
 *       entryProxy = new BeanProxy(LogEntry.class);
 *       ...
 *    }
 *    ...
 *    public Object getValueAt(int row, int col) {
 *       LogEntry entry;
 *       Object   value;
 *       try {
 *          entry = log.get(row);
 *          entryProxy.setBean(entry);
 *          value = entryProxy.get(columns[col]);
 *        } catch(Exception ex) {
 *           return "";
 *       }
 *       return value;
 *    }
 * </pre>
 * <p>
 * Source: http://www.developer.com/java/other/print.php/864941
 * </p>
 *
 * @author Thornton Rose
 * @version 1.2
 */
public class BeanProxy implements IBeanProxy {
    
    private Object    bean;
    private Class     beanClass;
    private Hashtable pdsByName;
    
    //--------------------------------------------------------------------------

    /**
     * Constructs a proxy for the given class.
     * @param theBeanClass The target bean class.
     */
    public BeanProxy(Class theBeanClass) throws IntrospectionException {
        // initialize
        pdsByName = new Hashtable();
        //
        BeanInfo bi;
        PropertyDescriptor[] pds;
        String name;
        Method m;
        // Save reference to bean class.
        beanClass = theBeanClass;
        // Build hashtable for bean property descriptors.
        bi = Introspector.getBeanInfo(beanClass);
        pds = bi.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i ++) {
            name = pds[i].getName();
            pdsByName.put(name, pds[i]);
        }
    }
    
    /**
     * Constructs a proxy for the given bean.
     * @param theBean The target bean.
     */
    public BeanProxy(Object theBean) throws IntrospectionException {
        this(theBean.getClass());
        bean = theBean;
    }
    
    //--------------------------------------------------------------------------

    
    public Object getBean() {
        return bean;
    }
    
    public void setBean(Object newBean) {
        bean = newBean;
    }
    
    /**
     * Get bean property.
     * @param name Bean property name.
     * @return Bean property value as Object.
     */
    public Object get(String name) throws Exception {
        PropertyDescriptor pd;
        Method getter;
        
        pd = (PropertyDescriptor) pdsByName.get(name);
        if (pd == null) {
            throw new NoSuchFieldException("Unknown property: " + name);
        }
        getter = pd.getReadMethod();
        if (getter == null) {
            throw new NoSuchMethodException("No read method for: " + name);
        }
        return getter.invoke(bean, new Object[] {});
    }
    
    /**
     * Get properties.
     * @return Map to properties.
     */
    public Hashtable getProperties() {
        return this.pdsByName;
    }

    /**
     * Get bean property names.
     * @return List of property names.
     */
    public List getPropertyNames() {
        ArrayList names = new ArrayList();
        Enumeration e = this.pdsByName.elements();
        while (e.hasMoreElements()) {
            PropertyDescriptor pd = (PropertyDescriptor) e.nextElement();
            names.add(pd.getName());
        }
        return (List) names;
    }
    
    /**
     * Set bean property.
     * @param name Bean property name.
     * @param value Bean property value.
     */
    public Object set(String name, Object value) throws Exception {
        PropertyDescriptor pd;
        Method setter;
        pd = (PropertyDescriptor) pdsByName.get(name);
        if (pd == null) {
            throw new NoSuchFieldException("Unknown property: " + name);
        }
        setter = pd.getWriteMethod();
        if (setter == null) {
            throw new NoSuchMethodException("No write method for: " + name);
        }
        return setter.invoke(bean,new Object[]{value});
    }
    
    /**
     * Invoke named method on target bean.
     * @param name Method name.
     * @param types Parameter types.
     * @param parameters List of parameters passed to method.
     * @return Return value from method (may be null).
     * @throws Throwable When any exception occurs.
     */
    public Object invoke(String name, Class[] types, Object[] parameters) throws Exception {
        return beanClass.getMethod(name,types).invoke(bean,parameters);
    }
    
}  