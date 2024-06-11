package com.task.third.service.web.ui;

import com.task.third.service.web.entity.Address;
import com.task.third.service.web.entity.Mail;
import com.task.third.service.web.entity.Person;
import com.task.third.service.web.service.PersonService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@PageTitle("People")
@Route("/people")
public class MainView extends VerticalLayout {
    private final PersonService personService;
    private final EditForm editForm;
    private final Grid<Person> grid = new Grid<>(Person.class);
    private final TextField filterField = new TextField();
    private final Button addPerson = new Button("Add person", VaadinIcon.PLUS.create());

    public MainView(PersonService personService, EditForm editForm) {
        addClassName("main_view");
        this.personService = personService;
        this.editForm = editForm;

        setSizeFull();

        filterField.setPlaceholder("Filter by name");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.addValueChangeListener(e -> listCustomers(e.getValue()));

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("toolbar");
        header.add(filterField);
        header.add(addPerson);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setColumns("fullName", "pin", "addresses", "mails");
        grid.getColumnByKey("fullName").setHeader("Name");
        grid.getColumnByKey("pin").setHeader("PIN");
        grid.getColumnByKey("mails").setHeader("Emails");
        Grid.Column<Person> emails = grid.getColumnByKey("mails");;
        emails.setRenderer(new ComponentRenderer<>(person -> {
            VerticalLayout listLayout = new VerticalLayout();
            for (Address address : person.getAddresses()) {
                Span span = new Span(address.getAddressInfo()+ ", " +address.getAddressType());
                listLayout.add(span);
            }
            listLayout.setSpacing(false);
            listLayout.setPadding(false);
            return listLayout;
        }));

        Grid.Column<Person> addresses = grid.getColumnByKey("addresses");
        addresses.setHeader("Addresses");
        addresses.setRenderer(new ComponentRenderer<>(person -> {
            VerticalLayout listLayout = new VerticalLayout();
            for (Mail mail : person.getMails()) {
                Span span = new Span(mail.getEmail() + ", " + mail.getEmailType());
                listLayout.add(span);
            }
            listLayout.setSpacing(false);
            listLayout.setPadding(false);
            return listLayout;
        }));

        grid.addItemClickListener(e -> {
            if(e.getClickCount() >= 2){
                editForm.editPerson(e.getItem(), false);
            }
        });

        addPerson.addClickListener(
                e -> {
                    Person person = new Person();
                    person.setAddresses(new ArrayList<>());
                    person.setMails(new ArrayList<>());
                    editForm.editPerson(person, true);
                });

        editForm.setChangeHandler(() ->
        {
            editForm.setVisible(false);
            listCustomers(filterField.getValue());
        });

        listCustomers();

        add(header, getContent());
    }


    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, editForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, editForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void listCustomers() {
        grid.setItems(personService.listAll());
    }

    private void listCustomers(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(personService.queryByName(filterText));
        } else {
            grid.setItems(personService.listAll());
        }
    }
}