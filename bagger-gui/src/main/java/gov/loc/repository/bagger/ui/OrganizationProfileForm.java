
package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.DefaultBagInfo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;

public class OrganizationProfileForm extends AbstractForm implements PropertyChangeListener, FocusListener {
    public static final String PROFILE_FORM_PAGE = "profilePage";

    private JComponent form;
    private JComponent contactName;
    private JComponent field;
    private BagView bagView;
    private Dimension dimension = new Dimension(400, 370);

    public OrganizationProfileForm(FormModel formModel, BagView bagView) {
        super(formModel, PROFILE_FORM_PAGE);
        this.bagView = bagView;
    }

    protected JComponent createFormControl() {
    	form = new JPanel();
		form.setLayout(new BorderLayout());
		JPanel buttonPanel = createButtonPanel(true);
		form.add(buttonPanel, BorderLayout.NORTH);
        JComponent formFields = createFormFields();
        form.add(formFields, BorderLayout.CENTER);

        return form;
    }

    protected JComponent createFormFields() {
        JComponent fieldForm;
        BagTableFormBuilder formBuilder = new BagTableFormBuilder(getBindingFactory());
        JTextField nameTextField = new JTextField();
        int fieldHeight = nameTextField.getFontMetrics(nameTextField.getFont()).getHeight();
        int rowCount = 0;

        formBuilder.row();
        rowCount++;
        JComponent orgLabel = formBuilder.addLabel("Send from Organization")[0];
        formBuilder.row();
        rowCount++;
        this.field = formBuilder.add("sourceOrganization")[1];
        this.field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        JComponent orgAddress = formBuilder.add("organizationAddress")[1];
        orgAddress.addFocusListener(this);

        formBuilder.row();
        rowCount++;
        JComponent contactLabel = formBuilder.addLabel("Send from Contact")[0];
        formBuilder.row();
        rowCount++;
        this.contactName = formBuilder.add("srcContactName")[1];
        this.contactName.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        this.field = formBuilder.add("srcContactPhone")[1];
        this.field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        this.field = formBuilder.add("srcContactEmail")[1];
        this.field.addFocusListener(this);

        formBuilder.row();
        rowCount++;
        contactLabel = formBuilder.addLabel("Send to Contact")[0];
        formBuilder.row();
        rowCount++;
        this.field = formBuilder.add("toContactName")[1];
        this.field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        this.field = formBuilder.add("toContactPhone")[1];
        this.field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        this.field = formBuilder.add("toContactEmail")[1];
        this.field.addFocusListener(this);
        formBuilder.row();
        rowCount++;

        this.contactName.requestFocus();
        fieldForm = formBuilder.getForm();
        rowCount++;
        rowCount++;
        int height = 2 * fieldHeight * rowCount;
        dimension = new Dimension(400, height);
        fieldForm.setPreferredSize(dimension);

        return fieldForm;
    }

    private JPanel createButtonPanel(boolean enabled) {
    	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

    	JButton saveButton = new JButton(bagView.getPropertyMessage("bag.button.field.save"));
    	saveButton.addActionListener(new SaveFieldHandler());
    	saveButton.setOpaque(true);
    	saveButton.setToolTipText(bagView.getPropertyMessage("bag.button.field.save.help"));
    	saveButton.setEnabled(enabled);
    	buttonPanel.add(saveButton);
    	
    	JButton loadDefaultsButton = new JButton(bagView.getPropertyMessage("bag.button.field.load"));
    	loadDefaultsButton.addActionListener(new LoadFieldHandler());
    	loadDefaultsButton.setOpaque(true);
    	loadDefaultsButton.setToolTipText(bagView.getPropertyMessage("bag.button.field.load.help"));
    	loadDefaultsButton.setEnabled(enabled);
    	buttonPanel.add(loadDefaultsButton);

    	JButton clearDefaultsButton = new JButton(bagView.getPropertyMessage("bag.button.field.clear"));
    	clearDefaultsButton.addActionListener(new ClearFieldHandler());
    	clearDefaultsButton.setOpaque(true);
    	clearDefaultsButton.setToolTipText(bagView.getPropertyMessage("bag.button.field.clear.help"));
    	clearDefaultsButton.setEnabled(enabled);
    	buttonPanel.add(clearDefaultsButton);

    	return buttonPanel;
    }
    
    private class SaveFieldHandler extends AbstractAction {
       	private static final long serialVersionUID = 1L;

    	public void actionPerformed(ActionEvent e) {
    		bagView.saveProfiles();
    		bagView.bagInfoInputPane.setSelectedIndex(1);
       	}
    }

    private class LoadFieldHandler extends AbstractAction {
       	private static final long serialVersionUID = 1L;

    	public void actionPerformed(ActionEvent e) {
    		bagView.loadProfiles();
    		bagView.bagInfoInputPane.setSelectedIndex(1);
       	}
    }

    private class ClearFieldHandler extends AbstractAction {
       	private static final long serialVersionUID = 1L;

    	public void actionPerformed(ActionEvent e) {
    		bagView.clearProfiles();
    		bagView.bagInfoInputPane.setSelectedIndex(1);
       	}
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (bagView != null && !this.hasErrors()) {
        	bagView.updatePropButton.setEnabled(true);
        }
    }

    public boolean requestFocusInWindow() {
        return contactName.requestFocusInWindow();
    }

    public void focusGained(FocusEvent evt) {
    }

    public void focusLost(FocusEvent evt) {
    	if (bagView != null && !this.hasErrors() && this.isDirty()) {
        	//bagView.infoInputPane.updateBagHandler.updateBag(bagView.getBag());
    	}
    }
}