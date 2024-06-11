package com.task.third.service.web.ui;

import com.task.third.service.web.entity.Address;
import com.task.third.service.web.entity.Mail;
import com.task.third.service.web.entity.Person;
import com.task.third.service.web.service.PersonService;
import com.task.third.service.web.ui.validation.ValidatorFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

// must split into several components...
@Getter
@Setter
@SpringComponent
@UIScope
public class EditForm extends FormLayout {

    private final PersonService personService;

    private Person person;
    private ChangeHandler changeHandler;

    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel", VaadinIcon.CLOSE.create());
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private Binder<Person> personBinder = new Binder<>(Person.class);
    private Binder<Address> addressBinder = new Binder<>(Address.class);
    private Binder<Mail> mailBinder = new Binder<>(Mail.class);

    private Grid<Address> addressEditGrid = new Grid<>(Address.class);
    private Grid<Mail> mailEditGrid = new Grid<>(Mail.class);

    private HorizontalLayout mainActions = new HorizontalLayout();
    private HorizontalLayout addressCreateLayout = new HorizontalLayout();
    private HorizontalLayout mailCreateLayout = new HorizontalLayout();
    private VerticalLayout personEditLayout = new VerticalLayout();

    @Autowired
    public EditForm(PersonService personService) {
        addClassName("person-edit-form");
        this.personService = personService;

        VerticalLayout mainFormLayout = new VerticalLayout();

        configurePersonEditLayout();
        configureAddressGridForm();
        configureAddressCreate();
        configureEmailGridForm();
        configureAddEmail();
        configureMainActions();

        mainFormLayout.setWidth("1000px");
        mainFormLayout.setFlexGrow(0);
        mainFormLayout.setFlexShrink(0);
        mainFormLayout.setSpacing(true);
        mainFormLayout.setMargin(false);

        mainFormLayout.add(
                personEditLayout,
                addressEditGrid,
                addressCreateLayout,
                mailEditGrid,
                mailCreateLayout,
                mainActions);
        add(mainFormLayout);
        setVisible(false);
    }


    private void configurePersonEditLayout() {
        TextField fullName = new TextField("Full name");
        TextField pin = new TextField("PIN");

        personBinder.forField(fullName)
                .withValidator(ValidatorFactory.getValidator(String.class,"full_name"))
                .bind(Person::getFullName, Person::setFullName);

        personBinder.forField(pin)
                .withValidator(ValidatorFactory.getValidator(String.class,"pin"))
                .bind(Person::getPin, Person::setPin);

        personEditLayout.setPadding(false);
        personEditLayout.setSpacing(false);
        personEditLayout.add(fullName, pin);
    }

    private void configureMainActions() {
        mainActions.setSizeFull();

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(e -> save());
        cancel.addClickListener(e -> {
            this.setVisible(false);
            this.setPerson(null);
        });

        createConfirmDeleteDialog();

        mainActions.add(save, cancel, delete);
    }

    private void configureAddressCreate() {
        TextField addressAddField = new TextField("Address");
        TextField addressAddType = new TextField("Type");
        addressAddType.setWidth("20%");

        Binder<TextField> fieldBinder = new Binder<>();
        fieldBinder.forField(addressAddField)
                .withValidator(ValidatorFactory.getValidator(String.class,"type"))
                .bind(TextField::getValue, TextField::setValue);


        Button addressAddButton = new Button("Add", event -> {
            String address = addressAddField.getValue();
            String type = addressAddType.getValue();
            if (person != null) {
                Address addressEntity = new Address();
                addressEntity.setAddressInfo(address);
                addressEntity.setAddressType(type);
                addressEntity.setPerson(person);
                person.getAddresses().add(addressEntity);
                addressEditGrid.setItems(person.getAddresses());
                // find another way...
                fieldBinder.setValidatorsDisabled(true);
                addressAddField.clear();
                addressAddType.clear();
                fieldBinder.setValidatorsDisabled(false);
            }
        });
        HorizontalLayout addressHorizontal = new HorizontalLayout();
        addressHorizontal.setAlignItems(FlexComponent.Alignment.END);
        addressHorizontal.add(addressAddField, addressAddType, addressAddButton);

        addressCreateLayout.add(addressHorizontal);
    }
    private void configureAddressGridForm() {
        addressEditGrid.setColumns("addressInfo", "addressType");
        Grid.Column<Address> addressInfo = addressEditGrid.getColumnByKey("addressInfo").setWidth("300px").setFlexGrow(0).setHeader("Address");
        Grid.Column<Address> addressType = addressEditGrid.getColumnByKey("addressType").setWidth("100px").setFlexGrow(0).setHeader("Type");
        addressEditGrid.setAllRowsVisible(true);
        addressEditGrid.setWidthFull();

        Editor<Address> editor = addressEditGrid.getEditor();
        editor.setBinder(addressBinder);
        editor.setBuffered(true);

        TextField addressInfoField = new TextField();
        addressInfoField.setWidthFull();
        addressBinder.forField(addressInfoField)
                .asRequired("Address field can't be empty")
                .withValidator(info -> info.length() <= 300, "Exceeds maximum address length of 300")
                .bind(Address::getAddressInfo, Address::setAddressInfo);
        addressInfo.setEditorComponent(addressInfoField);

        TextField addressTypeField = new TextField();
        addressTypeField.setWidthFull();
        addressBinder.forField(addressTypeField)
                .withValidator(type -> type!= null && type.length() > 0 && type.length() <= 5 ,
                        "Address type must be between 1 and 5 characters")
                .bind(Address::getAddressType, Address::setAddressType);
        addressType.setEditorComponent(addressTypeField);

        Grid.Column<Address> editColumn = addressEditGrid.addComponentColumn(address -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.cancel();
                }
                addressEditGrid.getEditor().editItem(address);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);

        addressEditGrid.addComponentColumn(address -> {
            Button editButton = new Button(VaadinIcon.TRASH.create());
            editButton.addClickListener(e -> {
                if (person != null) {
                    person.getAddresses().remove(address);
                    addressEditGrid.setItems(person.getAddresses());
                }
            });
            return editButton;
        }).setWidth("80px").setFlexGrow(0);


        Button saveButton = new Button("Save", e -> {
            if (editor.save()) {
                addressEditGrid.setItems(person.getAddresses());
            }
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout editAddressActions = new HorizontalLayout(saveButton,
                cancelButton);
        editAddressActions.setPadding(false);
        editColumn.setEditorComponent(editAddressActions);
    }

    private void configureEmailGridForm() {
        mailEditGrid.setColumns("email", "emailType");
        Grid.Column<Mail> emailColumn = mailEditGrid.getColumnByKey("email")
                .setWidth("300px")
                .setFlexGrow(0)
                .setHeader("Email");
        Grid.Column<Mail> emailTypeColumn = mailEditGrid.getColumnByKey("emailType")
                .setWidth("100px")
                .setFlexGrow(0)
                .setHeader("Type");
        mailEditGrid.setAllRowsVisible(true);
        mailEditGrid.setWidthFull();

        Editor<Mail> editor = mailEditGrid.getEditor();
        editor.setBinder(mailBinder);
        editor.setBuffered(true);

        EmailField mailEditField = new EmailField();
        mailEditField.setRequired(false);
        mailEditField.setWidthFull();
        mailBinder.forField(mailEditField)
                .withValidator(ValidatorFactory.getValidator(String.class,"email"))
                .bind(Mail::getEmail, Mail::setEmail);
        emailColumn.setEditorComponent(mailEditField);


        TextField emailTypeEditField = new TextField();
        emailTypeEditField.setWidthFull();
        mailBinder.forField(emailTypeEditField)
                .withValidator(ValidatorFactory.getValidator(String.class,"type"))
                .bind(Mail::getEmailType, Mail::setEmailType);
        emailTypeColumn.setEditorComponent(emailTypeEditField);

        Grid.Column<Mail> editColumn = mailEditGrid.addComponentColumn(email -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.cancel();
                }
                mailEditGrid.getEditor().editItem(email);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);

        mailEditGrid.addComponentColumn(mail -> {
            Button editButton = new Button(VaadinIcon.TRASH.create());
            editButton.addClickListener(e -> {
                if (person != null) {
                    person.getMails().remove(mail);
                    mailEditGrid.setItems(person.getMails());
                }
            });
            return editButton;
        }).setWidth("80px").setFlexGrow(0);

        Button saveButton = new Button("Save", e -> {
            if (editor.save()) {
                mailEditGrid.setItems(person.getMails());
            }
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> {
                   editor.cancel();
                }
        );
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout editMailsActions = new HorizontalLayout(saveButton,
                cancelButton);
        editMailsActions.setPadding(false);
        editColumn.setEditorComponent(editMailsActions);
    }

    private void configureAddEmail() {
        TextField emailAddField = new TextField("Email");
        TextField emailAddType = new TextField("Type");

        Binder<TextField> fieldBinder = new Binder<>();
        fieldBinder.forField(emailAddType)
                .withValidator(ValidatorFactory.getValidator(String.class,"type"))
                .bind(TextField::getValue, TextField::setValue);
        fieldBinder.forField(emailAddField)
                .withValidator(ValidatorFactory.getValidator(String.class,"email"))
                .bind(TextField::getValue, TextField::setValue);
        emailAddType.setWidth("20%");


        Button emailAddButton = new Button("Add", event -> {
            String email = emailAddField.getValue();
            String type = emailAddType.getValue();
            if (person != null && fieldBinder.validate().isOk()) {
                Mail mailEntity = new Mail();
                mailEntity.setEmail(email);
                mailEntity.setEmailType(type);
                mailEntity.setPerson(person);
                person.getMails().add(mailEntity);
                mailEditGrid.setItems(person.getMails());
                fieldBinder.setValidatorsDisabled(true);
                emailAddField.clear();
                emailAddType.clear();
                fieldBinder.setValidatorsDisabled(false);
            }
        });
        HorizontalLayout emailAddLayout = new HorizontalLayout();
        emailAddLayout.setAlignItems(FlexComponent.Alignment.END);
        emailAddLayout.add(emailAddField, emailAddType, emailAddButton);

        mailCreateLayout.add(emailAddLayout);
    }


    private void createConfirmDeleteDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm delete");
        dialog.setText("Are you sure you want delete this person?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Confirm");
        dialog.addConfirmListener(event -> delete());

        delete.addClickListener(event -> dialog.open());
    }

    private void delete() {
        try {
            personService.deletePerson(person);
            changeHandler.onChange();
            Notification.show("Person has been deleted!", 4000, Notification.Position.MIDDLE);
        } catch (Exception ex) {
            Notification.show("Failed to delete person!", 4000, Notification.Position.MIDDLE);
        }
    }

    private void save() {
        try {
            personService.savePerson(person);
            changeHandler.onChange();
            Notification.show("Person has been saved!", 4000, Notification.Position.MIDDLE);
        } catch (Exception ex) {
            Notification.show("Failed to save person!", 4000, Notification.Position.MIDDLE);
        }
    }

    public void editPerson(Person person, boolean isCreate) {
        if (person == null) {
            setVisible(false);
            return;
        }

        delete.setVisible(!isCreate);

        this.person = person;

        if (this.person.getAddresses() == null) {
            addressEditGrid.setItems(Collections.emptyList());
        } else {
            addressEditGrid.setItems(this.person.getAddresses());
        }

        if (this.person.getMails() == null) {
            mailEditGrid.setItems(Collections.emptyList());
        } else {
            mailEditGrid.setItems(this.person.getMails());
        }

        personBinder.setBean(this.person);

        setVisible(true);


        //fullName.focus();
    }

    public void setChangeHandler(ChangeHandler ch) {
        changeHandler = ch;
    }
}
