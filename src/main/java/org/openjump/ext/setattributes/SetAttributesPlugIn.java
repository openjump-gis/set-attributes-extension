package org.openjump.ext.setattributes;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.Logger;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

/**
 * A plugin displaying buttons to "fill" a set of consistent attribute
 * values for selected features.
 */
public class SetAttributesPlugIn extends AbstractPlugIn {

    final I18N i18n = SetAttributesExtension.I18N;

    final Map<String,JDialog> dialogsMap = new HashMap<>();

    public void initialize(PlugInContext context) {
        context.getFeatureInstaller().
            addMainMenuPlugin(this, new String[]{MenuNames.PLUGINS});
    }

    public String getName() {
        return i18n.get("SetAttributesPlugIn");
    }

    public ImageIcon getIcon(){
        return new ImageIcon(Objects.requireNonNull(this.getClass()
            .getResource("/set_attributes/set_attributes.png")));
    }

    public boolean execute(final PlugInContext context) throws Exception {

        Map<String,File> fileMap = new HashMap<>();
        Map<String,JCheckBox> checkBoxMap = new HashMap<>();
        // find folder in distro _and_ during development
        File folder = context.getWorkbenchContext().getWorkbench().getPlugInManager().findFileOrFolderInExtensionDirs("set_attributes");
        if (folder!=null)
          addFiles(folder.getAbsolutePath(), fileMap);
        else
          Logger.error("Missing default set-attributes/ folder in extension dir.");
        addFiles("~/.OpenJUMP/set_attributes", fileMap);
        MultiInputDialog dialog = new MultiInputDialog(context.getWorkbenchFrame(), getName(), true);
        for (Map.Entry<String,File> entry : fileMap.entrySet()) {
            JDialog dia = dialogsMap.get(entry.getKey());
            JCheckBox jcb = dialog.addCheckBox(entry.getKey(), dia!=null && dia.isValid() && dia.isVisible());
            if (dia != null) dia.dispose();
            checkBoxMap.put(entry.getKey(), jcb);
        }

        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        if (dialog.wasOKPressed()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(SetAttributesToolbox.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            for (Map.Entry<String,File> entry : fileMap.entrySet()) {
                if (checkBoxMap.get(entry.getKey()).isSelected()) {
                    SetAttributesToolbox toolbox = (SetAttributesToolbox) unmarshaller.unmarshal(entry.getValue());
                    JDialog jaDialog = toolbox.createDialog(
                            context.getWorkbenchContext(),
                            entry.getValue().getParentFile());
                    jaDialog.pack();
                    jaDialog.setResizable(false);
                    jaDialog.setLocationRelativeTo(context.getWorkbenchFrame());
                    jaDialog.setVisible(true);
                    dialogsMap.put(entry.getKey(), jaDialog);
                }
            }
            return true;
        }
        return false;
    }

    // Adds xml files located in path into mapFile.
    // If two files have the same name, the second replace the first one
    private void addFiles(String path, Map<String,File> mapFile) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles(pathname ->
                pathname.getName().toLowerCase().endsWith(".xml"));
            if (files != null) {
                for (File f : files) {
                    mapFile.put(f.getName().substring(0, f.getName().length() - 4), f);
                }
            }
        }
    }

}
