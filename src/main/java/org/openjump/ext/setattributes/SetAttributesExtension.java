package org.openjump.ext.setattributes;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * Extension initializing the SetAttributes plugin, a plugin displaying
 * buttons to "fill" a set of consistent attribute values for selected
 * features
 */
// 2.0.0 [2021-08-21] Upgrade i18n management, align version number to OJ version
// 1.0.0 [2021-04-12] Refactoring for OpenJUMP 2
// 0.8.0 [2019-09-15] add combo boxes capabilities
// 0.7.1 [2015-  -  ]
// 0.7.0 [2015-06-13]
public class SetAttributesExtension extends Extension {

    public static final I18N I18N = com.vividsolutions.jump.I18N.getInstance("org.openjump.ext.setattributes");

    public String getName() {
        return "Set Attribute Values Extension (Micha\u00EBl Michaud)";
    }

    public String getVersion() {
        return I18N.get("SetAttributesPlugIn.Version");
    }

    public void configure(PlugInContext context) {
        new SetAttributesPlugIn().initialize(context);
    }

}