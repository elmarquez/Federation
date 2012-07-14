/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.sfu.federation.viewer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 *
 * @author dmarques
 */
public class MessageAction extends AbstractAction {

    private Component parentComponent;
    private String message;
    private String name;

    public MessageAction(Component parentComponent, String name, Icon icon, String message) {
        super(null, icon);
        putValue(Action.SHORT_DESCRIPTION, name);
        this.message = message;
        this.name = name;
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(parentComponent,
                message, name, JOptionPane.INFORMATION_MESSAGE);
    }
    
}
