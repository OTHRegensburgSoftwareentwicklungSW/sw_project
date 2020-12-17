package de.othr.sw.bank.controller;

import de.othr.sw.bank.entity.AccountRequest;
import de.othr.sw.bank.entity.Customer;
import de.othr.sw.bank.service.CustomerServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class AccountController {

    @Autowired
    CustomerServiceIF customerService;
    @Autowired
    HomeController homeController;

    @RequestMapping("/accounts/opening")
    public String getFormForNewAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            model.addAttribute("username", currentUserName);
        }

        AccountRequest accountRequest = new AccountRequest();
        model.addAttribute("accountRequest", accountRequest);

        return "/customer/account_apply";
    }

    @PostMapping("/accounts")
    public String getFormForNewAccount(Model model, @ModelAttribute AccountRequest accountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Customer customer = (Customer) authentication.getPrincipal();
            accountRequest.setUsername(customer.getUsername());

            ResponseEntity<AccountRequest> responseEntity = customerService.createAccount(accountRequest);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED)
                return homeController.showDashboard(model, null, "Your request for an account has been sent. It will be verified by our employees.");
            else
                return homeController.showDashboard(model, "An error occurred while sending your request.", null);

        }
        return "login";
    }

    @GetMapping("/accounts/{id}")
    public String getAccountView(Model model, @PathVariable long id) {
        return "/customer/account";
    }
}
