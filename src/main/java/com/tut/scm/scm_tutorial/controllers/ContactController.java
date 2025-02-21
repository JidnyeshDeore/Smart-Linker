package com.tut.scm.scm_tutorial.controllers;


import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tut.scm.scm_tutorial.entities.Contact;
import com.tut.scm.scm_tutorial.entities.User;
import com.tut.scm.scm_tutorial.forms.ContactForm;
import com.tut.scm.scm_tutorial.forms.ContactSearchForm;
import com.tut.scm.scm_tutorial.helpers.AppConstants;
import com.tut.scm.scm_tutorial.helpers.Helper;
import com.tut.scm.scm_tutorial.services.ContactService;
import com.tut.scm.scm_tutorial.services.ImageService;
import com.tut.scm.scm_tutorial.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;


    @Autowired
    private ImageService imageService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);


    @RequestMapping("/add")
    public String addContactView(Model model, HttpSession session){

        ContactForm contactForm=new ContactForm();
        // contactForm.setFavourite(true);
        // contactForm.setContactName("Jidnyesh");
        model.addAttribute("contactForm", contactForm);


        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message"); // Clear the message after displaying it
        }
        return "user/add_contact";
    }

    @PostMapping("/process-contact")
    public String processContactForm(@Valid @ModelAttribute ContactForm contactForm,BindingResult result
        ,Authentication authentication, HttpSession session){

        System.out.println("Inside process-contact");

           if(result.hasErrors()){
            System.out.println("Errors occured..");
            return "user/add_contact";
           } 

        String userName=Helper.getEmailOfLoggedInUser(authentication);
        User user= userService.getUserByEmail(userName);

        //image process

        // logger.info("file information: {}",contactForm.getContactImage().getOriginalFilename());
        //upload image
        
        String fileName=UUID.randomUUID().toString();

        String  fileURL=imageService.uploadImage(contactForm.getContactImage(),fileName);

        Contact contact=new Contact();

        contact.setName(contactForm.getContactName());
        contact.setEmail(contactForm.getContactEmail());
        contact.setFavourite(contactForm.isFavourite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(fileName);
        contactService.save(contact);

        System.out.println(contactForm);

        session.setAttribute("message", "Contact added Successfully..!!");

        return "redirect:/user/contact/add";
    }

    @GetMapping("/view-contacts")
    public String viewContacts(

        @RequestParam( value="page",defaultValue="0")int page,
        @RequestParam(value="size",defaultValue="8") int size,
        @RequestParam(value="sortBy",defaultValue="name") String sortBy,
        @RequestParam(value="direction",defaultValue="asc") String direction
        ,Model model ,Authentication authentication, HttpSession session){

        //load all the users

        String userName=Helper.getEmailOfLoggedInUser(authentication);
        User user=userService.getUserByEmail(userName); 
        Page<Contact>pageContact= contactService.getByUserName(user,page,size,sortBy,direction); //getByUser

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        model.addAttribute("contactSearchForm", new ContactSearchForm());

        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message"); // Clear the message after displaying it
        }
        
        return "user/view_contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        Model model,
        Authentication authentication
    ){


        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;
        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                    direction, user);
        }

       
        model.addAttribute("contactSearchForm", contactSearchForm);

        model.addAttribute("pageContact", pageContact);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/search";
    }

    @RequestMapping("/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") String id, HttpSession session){


        contactService.delete(id);
        System.out.println("Contact deleted with id: "+id);


        session.setAttribute("message", "Contact Deleted Successfully..!!");
        return "redirect:/user/contact/view-contacts";

    }
    
    @GetMapping("/update-contact-view/{contactId}")
    public String updateContactView(@PathVariable("contactId") String contactId
    ,Model model
    ,HttpSession session){

        var contact=contactService.getById(contactId);

        ContactForm contactForm=new ContactForm();

        contactForm.setContactName(contact.getName());
        contactForm.setContactEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        model.addAttribute("message", session.getAttribute("message"));
        

        return "user/update_contact";
    }


    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable("contactId") String contactId
        ,@Valid @ModelAttribute ContactForm contactForm
        ,BindingResult rBindingResult
        ,Model model
        ,HttpSession session){

            System.out.println("Inside update contact post method");

            if(rBindingResult.hasErrors()){
                logger.info("Inside updated contact erros occurs.................................................................................................");
                return "user/update_contact";
            }

            logger.info("Inside updated contact erros not occurs.................................................................................................");

            var newContact=contactService.getById(contactId);

            //  var newContact=new Contact();

            newContact.setId(contactId);
            newContact.setName(contactForm.getContactName());
            newContact.setEmail(contactForm.getContactEmail());
            newContact.setPhoneNumber(contactForm.getPhoneNumber());
            newContact.setAddress(contactForm.getAddress());
            newContact.setDescription(contactForm.getDescription());
            newContact.setFavourite(contactForm.isFavourite());
            newContact.setWebsiteLink(contactForm.getWebsiteLink());
            newContact.setLinkedInLink(contactForm.getLinkedInLink());
            // newContact.setPicture(contactForm.getPicture());

            //process image
            if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){

                logger.info("update contact process image if condition...");
                String imagePublicId=UUID.randomUUID().toString();
                String imageURL=imageService.uploadImage(contactForm.getContactImage(), imagePublicId);
                newContact.setCloudinaryImagePublicId(imagePublicId);
                newContact.setPicture(imageURL);
                // newContact.setPicture(contactForm.getPicture());
                contactForm.setPicture(imageURL);

            }else{
                logger.info("file is empty..");
            }

            var updatedContact=contactService.update(newContact);

            System.out.println("Updated Contact...................................");
            logger.info("updated contact {}", updatedContact);


            session.setAttribute("message", "Contact Updated Successfully..!!");


        return "redirect:/user/contact/update-contact-view/" +contactId;

    }
}
