/**
 * InputTextField.java
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

import ca.sfu.federation.model.InputTable;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JTextField;

/**
 * @author Davis Marques
 * @version 0.1.0
 */
public class InputTextField extends JTextField implements Observer {
    
    //--------------------------------------------------------------------------

    
    private InputTable inputTable;
    private String inputKey;
    
    //--------------------------------------------------------------------------

    
    /**
     * InputTextField constructor.
     */
    public InputTextField(InputTable MyInputTable, String InputKey) {
        // init
        this.inputTable = MyInputTable;
        this.inputKey = InputKey;
        // set the field value
        this.setText((String) this.inputTable.getInputValue(InputKey));
        // listen for changes on this text field
        this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setValueActionListener(evt);
            }
        });
    }
    
    //--------------------------------------------------------------------------


    /**
     * Set the input value.
     * @param evt
     */
    public void setValueActionListener(ActionEvent evt) {
        try {
            this.inputTable.setInput(this.inputKey,this.getText());
            this.setBackground(Color.WHITE);
        } catch (Exception ex) {
            this.setBackground(Color.RED);
        }
    }
    
    /**
     * @param o Observable object.
     * @param arg Update argument.
     */
    public void update(Observable o, Object arg) {
    }
    
} 
