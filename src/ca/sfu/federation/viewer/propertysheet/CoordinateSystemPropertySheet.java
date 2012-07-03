/**
 * CoordinateSystemPropertySheet.java
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

package ca.sfu.federation.viewer.propertysheet;

import ca.sfu.federation.model.Component;
import ca.sfu.federation.model.ParametricModel;
import ca.sfu.federation.model.exception.NonExistantMethodException;
import ca.sfu.federation.model.exception.NonExistantUpdateAnnotationException;
import ca.sfu.federation.model.ConfigManager;
import com.developer.rose.BeanProxy;
import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * @author  Davis Marques
 * @version 0.1
 */
public class CoordinateSystemPropertySheet extends JPanel implements Observer {

    //--------------------------------------------------------------------------
    // FIELDS
    
    private JScrollPane jScrollPane1;
    private JComboBox jcbUpdateMethod;
    private JTextField jtfCanonicalName;
    private JTextField jtfClass;
    private JTextField jtfDescription;
    private JTextField jtfName;
    private JTextField jtfParentContext;
    private JTextField jtfX;
    private JTextField jtfY;
    private JTextField jtfZ;
    private JLabel lblCanonicalName;
    private JLabel lblClass;
    private JLabel lblDescription;
    private JLabel lblINamedObject;
    private JLabel lblName;
    private JLabel lblParentContext;
    private JLabel lblUpdateMethod;
    private JLabel lblX;
    private JLabel lblY;
    private JLabel lblZ;
    
    private ParametricModel model;
    private Component target;
    
    //--------------------------------------------------------------------------
    // CONSTRUCTORS

    /** 
     * ParametricModelSheet constructor.
     */
    public CoordinateSystemPropertySheet() {
        lblINamedObject = new JLabel();
        lblName = new JLabel();
        lblDescription = new JLabel();
        jtfClass = new JTextField();
        jtfName = new JTextField();
        jtfCanonicalName = new JTextField();
        lblCanonicalName = new JLabel();
        lblClass = new JLabel();
        jtfDescription = new JTextField();
        lblParentContext = new JLabel();
        jtfParentContext = new JTextField();
        lblX = new JLabel();
        lblY = new JLabel();
        lblZ = new JLabel();
        jtfX = new JTextField();
        jtfY = new JTextField();
        jtfZ = new JTextField();
        lblUpdateMethod = new JLabel();
        jcbUpdateMethod = new JComboBox();
        jScrollPane1 = new JScrollPane();

        lblINamedObject.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblINamedObject.setText("jLabel1");
        lblName.setText("Name");
        lblDescription.setText("Description");
        lblCanonicalName.setText("Canonical Name");
        lblClass.setText("Class");
        lblParentContext.setText("Parent Context");
        lblX.setText("X Coordinate");
        lblY.setText("Y Coordinate");
        lblZ.setText("Z Coordinate");
        lblUpdateMethod.setText("Update Method");
        
        jtfParentContext.setEditable(false);
        jtfClass.setEditable(false);
        jtfCanonicalName.setEditable(false);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblINamedObject)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblCanonicalName)
                            .add(lblClass)
                            .add(lblName)
                            .add(lblDescription)
                            .add(lblX)
                            .add(lblZ)
                            .add(lblY)
                            .add(lblUpdateMethod)
                            .add(lblParentContext))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jcbUpdateMethod, 0, 221, Short.MAX_VALUE)
                            .add(jtfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfClass, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                            .add(jtfParentContext, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfX, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfY, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                            .add(jtfZ, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(lblINamedObject)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfClass, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblClass))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCanonicalName)
                    .add(jtfCanonicalName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDescription)
                    .add(jtfDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfParentContext, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblParentContext))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblX)
                    .add(jtfX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblY))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfZ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblZ))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jcbUpdateMethod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblUpdateMethod))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        // get the current object
        this.model = ParametricModel.getInstance();
        
        // disable non editable fields
        this.jtfCanonicalName.setEditable(false);
        this.jtfClass.setEditable(false);
        this.jtfParentContext.setEditable(false);
        
        // set field values
        this.setValues();
        
        // add action listener
        jtfName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNameActionListener(evt);
            }
        });
        jtfDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDescriptionActionListener(evt);
            }
        });
        jtfX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfXActionListener(evt);
            }
        });
        jtfY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfYActionListener(evt);
            }
        });
        jtfZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfZActionListener(evt);
            }
        });
        jcbUpdateMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbUpdateMethodActionListener(evt);
            }
        });
    }
    
    //--------------------------------------------------------------------------
    // METHODS

    private void jtfDescriptionActionListener(java.awt.event.ActionEvent evt) {                                              
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jtfDescriptionActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("description",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }                                             

    private void jtfNameActionListener(java.awt.event.ActionEvent evt) {                                       
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jtfNameActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("name",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }                                      

    private void jtfXActionListener(java.awt.event.ActionEvent evt) {                                    
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jtfXActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("X",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jtfYActionListener(java.awt.event.ActionEvent evt) {                                    
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jtfYActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("Y",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }                                   

    private void jtfZActionListener(java.awt.event.ActionEvent evt) {                                    
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jtfZActionListener fired. " + command);
        try {
            BeanProxy proxy = new BeanProxy(this.target);
            proxy.set("Z",evt.getActionCommand());
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }                                   

    private void jcbUpdateMethodActionListener(java.awt.event.ActionEvent evt) {                                               
        String command = evt.getActionCommand();
        System.out.println("INFO: ComponentSheet jcbUpdateMethodActionListener fired. " + command);
        try {
            this.target.setUpdateMethod(evt.getActionCommand());
            // update the input arguments panel
        } catch (NonExistantMethodException ex) {
            ex.printStackTrace();
        } catch (NonExistantUpdateAnnotationException ex) {
            ex.printStackTrace();
        }
    }                                              
    
    private void setValues() {
        // target
        this.target = (Component) this.model.getViewState(ConfigManager.VIEWER_SELECTION);
        // listen for changes on the target
        if (this.target instanceof Observable) {
            Observable o = (Observable) this.target;
            o.addObserver(this);
        }
        // set field values
        this.lblINamedObject.setText(this.target.getName());
        this.jtfClass.setText(this.target.getClass().toString());
        this.jtfName.setText(this.target.getName());
        // this.jtfDescription.setText(this.target.getDescription());
        this.jtfCanonicalName.setText(this.target.getCanonicalName());
        this.jtfParentContext.setText(this.target.getContext().getName());
        Method[] methods = this.target.getUpdateMethods();
        for (int i=0;i<methods.length;i++) {
            Method method = methods[i];
            jcbUpdateMethod.addItem(method.getName());
        }
    }
    
    /**
     * Update event.
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer) {
            Integer eventId = (Integer) arg;
            System.out.println("INFO: ComponentSheet received event notification id " + eventId);
            switch (eventId) {
                case ConfigManager.EVENT_PROPERTY_CHANGE:
                    System.out.println("INFO: ComponentSheet fired property change event.");
                    this.setValues();
                    break;
            }
        }
    }

} // end class