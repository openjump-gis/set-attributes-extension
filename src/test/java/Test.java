
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import org.openjump.ext.setattributes.SetAttribute;
import org.openjump.ext.setattributes.SetAttributesToolbox;
import org.openjump.ext.setattributes.SetOfAttributes;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by UMichael on 18/04/2015.
 */
public class Test {

    public static void main(String[] args) {

        File xml = new File("src/test/resources/test.xml");
        try {
            JAXBContext jc = JAXBContext.newInstance(SetAttributesToolbox.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SetAttributesToolbox toolbox = (SetAttributesToolbox) unmarshaller.unmarshal(xml);
            System.out.println(toolbox);
            System.out.println(toolbox.getTitle());
            System.out.println(toolbox.getButtons());
            for (SetOfAttributes button : toolbox.getButtons()) {
                System.out.println("  " + button);
                System.out.println("  " + button.getIcon());
                System.out.println("  " + button.getLayer());
                System.out.println("  " + button.isAtomic());
                System.out.println("  " + button.getAttributes());
                for (SetAttribute attribute : button.getAttributes()) {
                    System.out.println("    " + attribute.getName());
                    System.out.println("    " + attribute.getValue());
                }
            }
            //JDialog dialog = toolbox.createDialog(new JUMPWorkbenchContext(new JUMPWorkbench("", new String[0], new JFrame(), new DummyTaskMonitor())));
            // TODO broken test
            JDialog dialog = toolbox.createDialog(JUMPWorkbench.getInstance().getContext(), new File("."));
            dialog.pack();
            dialog.setVisible(true);


        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
