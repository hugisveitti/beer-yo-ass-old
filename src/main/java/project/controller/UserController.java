package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import project.persistence.entities.User;
import project.service.UserService;

@Controller
@SessionAttributes("username")
public class UserController {

    // Instance Variables
    private UserService userService;

    // Dependency Injection
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Method that returns the correct view for the URL /signup
    // This handles the GET request for this URL
    // Notice the `method = RequestMethod.GET` part
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupViewGet(Model model){



        model.addAttribute("user",new User());
        // Return the view
        return "Signup";
    }

    // Method that receives the POST request on the URL /signup
    // and receives the ModelAttribute("User")
    // That attribute is the attribute that is mapped to the form, so here
    // we can save the user because we get the data that was entered
    // into the form.
    // Notice the `method = RequestMethod.POST` part
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public RedirectView signupViewPost(@ModelAttribute("user") User user, Model model){

        // Save the user that we received from the form
        //todo encrypt password
        //todo unique users
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());

        System.out.println("############");
        System.out.println(user);
        userService.save(user);

        model.addAttribute("username", user.getUsername());

        // Return the view
        return new RedirectView("/userpage");
    }

    @RequestMapping(value="/userpage", method = RequestMethod.GET)
    public String userPageViewGet(){
        return "UserPage";
    }

    @RequestMapping(value="/sendbet", method = RequestMethod.GET)
    public String sendBetViewGet(){
        return "SendBet";
    }


    @RequestMapping(value="/error", method = RequestMethod.GET)
    public String errorViewGet(){
        return "Index";
    }
}
