package id.co.nio.dualjdbc.sampler;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.IntegerPropertyEditor;
import org.apache.jmeter.testbeans.gui.PasswordEditor;
import org.apache.jmeter.testbeans.gui.TextAreaEditor;

import java.beans.PropertyDescriptor;

public abstract class AbstractDualJdbcSamplerBeanInfo extends BeanInfoSupport {

    public AbstractDualJdbcSamplerBeanInfo(Class<? extends AbstractDualJdbcSampler> clazz) {
        super(clazz);

        createPropertyGroup("Source Database", // $NON-NLS-1$
                new String[]{
                    "jdbc1Driver", // $NON-NLS-1$
                    "jdbc1Url", // $NON-NLS-1$
                    "jdbc1Username", // $NON-NLS-1$
                    "jdbc1Password", // $NON-NLS-1$
                    "upsertQuery" // $NON-NLS-1$
                });

        createPropertyGroup("Target Database", // $NON-NLS-1$
                new String[]{
                    "jdbc2Driver", // $NON-NLS-1$
                    "jdbc2Url", // $NON-NLS-1$
                    "jdbc2Username", // $NON-NLS-1$
                    "jdbc2Password", // $NON-NLS-1$
                    "checkingQuery" // $NON-NLS-1$
                });

        createPropertyGroup("Connection", // $NON-NLS-1$
                new String[]{
                    "connectionTimeout"
                });

        PropertyDescriptor p;
        p = property("jdbc1Driver"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "org.postgresql.Driver");

        p = property("jdbc2Driver"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "org.postgresql.Driver");

        p = property("jdbc1Url"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "jdbc:postgresql://localhost/test1");

        p = property("jdbc2Url"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "jdbc:postgresql://localhost/test2");

        p = property("jdbc1Username"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "postgres");

        p = property("jdbc2Username"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "postgres");

        p = property("jdbc1Password"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setPropertyEditorClass(PasswordEditor.class);
        p.setValue(DEFAULT, "postgres");

        p = property("jdbc2Password"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setPropertyEditorClass(PasswordEditor.class);
        p.setValue(DEFAULT, "postgres");

        p = property("upsertQuery"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        //p.setPropertyEditorClass(TextAreaEditor.class);
        p.setValue(DEFAULT, "INSERT INTO X values ()");

        p = property("checkingQuery"); // $NON-NLS-1$
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setPropertyEditorClass(TextAreaEditor.class);
        p.setValue(DEFAULT, "SELECT TRUE/FALSE FROM X WHERE ...");

        p = property("connectionTimeout");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setPropertyEditorClass(IntegerPropertyEditor.class);
        p.setValue(DEFAULT, 15000);

    }
}
