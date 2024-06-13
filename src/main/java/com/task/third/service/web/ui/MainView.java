package com.task.third.service.web.ui;

import com.task.third.service.web.entity.Address;
import com.task.third.service.web.entity.Mail;
import com.task.third.service.web.entity.Person;
import com.task.third.service.web.service.PersonService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@PageTitle("People")
@Route("/people")
public class MainView extends VerticalLayout {
    private final PersonService personService;
    private final EditForm editForm;
    private final Grid<Person> grid = new Grid<>(Person.class);
    private final TextField filterField = new TextField();
    private final Button addPerson = new Button("Add person", VaadinIcon.PLUS.create());

    public MainView(PersonService personService, EditForm editForm) {
        this.personService = personService;
        this.editForm = editForm;

        addClassName("main-view");
        setSizeFull();

        configureGrid();

        addActions();

        listPeople();

        add(getHeader(), getBody());
    }

    private HorizontalLayout getHeader() {
        addPerson.addClickListener(
                e -> {
                    Person person = new Person();
                    person.setAddresses(new ArrayList<>());
                    person.setMails(new ArrayList<>());
                    editForm.editPerson(person, true);
                });

        filterField.setPlaceholder("Filter by name");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.addValueChangeListener(e -> listPeople(e.getValue()));

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("toolbar");
        header.add(filterField);
        header.add(addPerson);
        return header;
    }

    private void addActions() {
        editForm.setChangeHandler(() -> {
            editForm.setVisible(false);
            listPeople(filterField.getValue());
        });
    }

    private void configureGrid() {
        grid.setPageSize(15);
        grid.setHeight("70%");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setColumns("fullName", "pin", "addresses", "mails");
        grid.getColumnByKey("fullName").setSortable(true).setHeader("Name");
        grid.getColumnByKey("pin").setSortable(false).setHeader("PIN");
        grid.getColumnByKey("mails").setHeader("Mails").setRenderer(createMailRenderer());
        grid.getColumnByKey("addresses").setHeader("Addresses").setRenderer(createAddressRenderer());
        grid.addItemClickListener(e -> {
            if(e.getClickCount() >= 2){
                editForm.editPerson(e.getItem(), false);
            }
        });
    }

    private static ComponentRenderer<VerticalLayout, Person> createMailRenderer() {
        return new ComponentRenderer<>(person -> {
            VerticalLayout listLayout = new VerticalLayout();
            for (Mail mail : person.getMails()) {
                if (mail.getEmail() != null && !mail.getEmail().isEmpty()) {
                    Span span = new Span(mail.getEmail() + ", " + mail.getEmailType());
                    listLayout.add(span);
                }
            }
            listLayout.setSpacing(false);
            listLayout.setPadding(false);
            return listLayout;
        });
    }

    private ComponentRenderer<VerticalLayout, Person> createAddressRenderer() {
        return new ComponentRenderer<>(person -> {
            VerticalLayout listLayout = new VerticalLayout();
            for (Address address : person.getAddresses()) {
                if (address.getAddressInfo() != null && !address.getAddressInfo().isEmpty()) {
                    Span span = new Span(address.getAddressInfo() + ", " + address.getAddressType());
                    listLayout.add(span);
                }
            }
            listLayout.setSpacing(false);
            listLayout.setPadding(false);
            return listLayout;
        });
    }


    private Component getBody() {
        HorizontalLayout content = new HorizontalLayout(grid, editForm);
        content.setSizeFull();
        content.addClassNames("page-content");
        return content;
    }

    private void listPeople() {
        grid.setItems(personService.listAll());
        sortGrid();
    }

    private void sortGrid() {
        grid.sort(List.of(new GridSortOrder<>(grid.getColumnByKey("fullName"),
                SortDirection.ASCENDING)));
    }

    private void listPeople(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(personService.queryByName(filterText));
        } else {
            grid.setItems(personService.listAll());
        }
        sortGrid();
    }
}