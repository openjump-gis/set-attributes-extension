package org.openjump.ext.setattributes;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

import javax.swing.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;


/**
 * A Toolbox containing buttons to set attributes to specific values
 */
@XmlRootElement
public class SetAttributesToolbox {

    @XmlAttribute ()
    String title = "";

    @XmlAttribute ()
    Integer maxCol = 6;

    @XmlAttribute
    Integer iconWidth = 32;

    @XmlAttribute
    Integer iconHeight = 32;

    @XmlAttribute ()
    boolean unselect;

    @XmlElement (name="combo")
    List<ListOfSetOfAttributes> comboboxes;

    @XmlElement (name="button")
    List<SetOfAttributes> buttons;

    public SetAttributesToolbox() {}

    public JDialog createDialog(WorkbenchContext context, File dir) {
        JDialog dialog = new JDialog(context.getWorkbench().getFrame(), title, false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(1,1,1,1);
        final PlugInContext pluginContext = context.createPlugInContext();
        if (comboboxes != null) {
            for (final ListOfSetOfAttributes listOfSetOfAttributes : comboboxes) {
                final JComboBox<SetOfAttributes> combo = listOfSetOfAttributes.createCombo(dir);
                constraints.gridx = 0;
                dialog.getContentPane().add(new JLabel(listOfSetOfAttributes.text), constraints);
                constraints.gridx = 1;
                combo.addItemListener(new SetAttributesComboItemListener(pluginContext, unselect));
                dialog.getContentPane().add(combo, constraints);
                constraints.gridy += 1;
            }
        }
        if (buttons != null) {
            for (final SetOfAttributes setOfAttributes : buttons) {
                ImageIcon icon = new ImageIcon(dir.getPath() + "/" + setOfAttributes.icon);
                final JButton button;
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    button = new JButton(icon);
                } else {
                    button = new JButton(setOfAttributes.text);
                    if (setOfAttributes.getBackgroundColor() != null) {
                        button.setBackground(setOfAttributes.getBackgroundColor());
                        //button.setContentAreaFilled(false);
                        button.setOpaque(true);
                    }
                }
                if (setOfAttributes.getAttributes() == null || setOfAttributes.getAttributes().size() == 0) {
                    button.setEnabled(false);
                }
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setPreferredSize(new Dimension(iconWidth, iconHeight));
                button.setToolTipText(setOfAttributes.getTooltip());

                button.addActionListener(new SetAttributesButtonActionListener(pluginContext, setOfAttributes, unselect));

                button.addMouseListener(new SetAttributesButtonMouseListener(setOfAttributes, button));
                dialog.getContentPane().add(button, constraints);
                constraints.gridx += 1;
                if (constraints.gridx >= maxCol) {
                    constraints.gridx = 0;
                    constraints.gridy += 1;
                }
            }
        }
        return dialog;
    }

    public String getTitle() {
        return title;
    }

    public void addSetOfAttributes(SetOfAttributes set) {
        if (buttons == null) buttons = new ArrayList<>();
        buttons.add(set);
    }

    public List<SetOfAttributes> getButtons() {
        return buttons;
    }

}
