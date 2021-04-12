package org.openjump.ext.setattributes;

import com.vividsolutions.jump.workbench.plugin.PlugInContext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Defines what happens when the user click on a SetAttributes button
 */
public class SetAttributesButtonActionListener implements ActionListener {

    final PlugInContext pluginContext;
    final SetOfAttributes setOfAttributes;
    final boolean unselect;

    SetAttributesButtonActionListener(final PlugInContext pluginContext,
                                     final SetOfAttributes setOfAttributes,
                                     final boolean unselect) {
        this.pluginContext = pluginContext;
        this.setOfAttributes = setOfAttributes;
        this.unselect = unselect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setOfAttributes.setAttributes(pluginContext, unselect);
    }
}
