package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.model.mappers.AdvertisementForm;
import org.springframework.samples.flatbook.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Controller
public class AdvertisementController {

    private static final String VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM = "advertisements/createOrUpdateAdvertisementForm";

    private AdvertisementService advertisementService;
    private FlatService flatService;
    private DBImageService dbImageService;
    private HostService hostService;
    private RequestService requestService;
    private PersonService personService;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, DBImageService dbImageService, FlatService flatService, HostService hostService,
                                   RequestService requestService, PersonService personService) {
        this.advertisementService = advertisementService;
        this.dbImageService = dbImageService;
        this.flatService = flatService;
        this.hostService = hostService;
        this.requestService = requestService;
        this.personService = personService;
    }

    @GetMapping(value = "/flats/{flatId}/advertisements/new")
    public String initCreationForm(@PathVariable("flatId") int flatId, Map<String, Object> model) {
        Flat flat = this.flatService.findFlatById(flatId);
        if(flat == null || !validateHost(flatId) || this.advertisementService.isAdvertisementWithFlatId(flat.getId())) {
            RuntimeException ex = new RuntimeException("Illegal access");
            model.put("exception", ex);
            return "exception";
        }
        AdvertisementForm advertisement = new AdvertisementForm();
        model.put("advertisementForm", advertisement);
        return VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/flats/{flatId}/advertisements/new")
        public String processCreationForm(ModelMap model, @Valid AdvertisementForm adv, BindingResult result, @PathVariable("flatId") int flatId) {
        if(result.hasErrors()) {
            return VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
        } else {
            Flat flat = this.flatService.findFlatById(flatId);
            if(flat == null || !validateHost(flatId) || this.advertisementService.isAdvertisementWithFlatId(flat.getId())) {
                RuntimeException ex = new RuntimeException("Illegal access");
                model.put("exception", ex);
                return "exception";
            }
            Advertisement advertisement = new Advertisement(adv);
            advertisement.setFlat(flat);
            this.advertisementService.saveAdvertisement(advertisement);
            return "redirect:/advertisements/" + advertisement.getId();
        }
    }

    @GetMapping(value = "/advertisements/{advertisementId}/edit")
    public String initUpdateForm(@PathVariable("advertisementId") int advertisementId, Map<String, Object> model) {
        Advertisement adv = this.advertisementService.findAdvertisementById(advertisementId);
        if(adv == null || !validateHost(adv.getFlat().getId())) {
            RuntimeException ex = new RuntimeException("Illegal access");
            model.put("exception", ex);
            return "exception";
        }
        AdvertisementForm af = new AdvertisementForm(adv);
        model.put("advertisementForm", af);
        return VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/advertisements/{advertisementId}/edit")
    public String processUpdateForm(@Valid AdvertisementForm adv, BindingResult result, @PathVariable("advertisementId") int advertisementId, Map<String, Object> model) {
        if(result.hasErrors()) {
            return VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
        } else {
            Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
            if(advertisement == null || !validateHost(advertisement.getFlat().getId())) {
                RuntimeException ex = new RuntimeException("Illegal access");
                model.put("exception", ex);
                return "exception";
            }
            Advertisement newAdvertisement = new Advertisement(adv);
            newAdvertisement.setFlat(advertisement.getFlat());
            newAdvertisement.setCreationDate(advertisement.getCreationDate());
            newAdvertisement.setRequests(advertisement.getRequests());
            newAdvertisement.setId(advertisement.getId());
            this.advertisementService.saveAdvertisement(newAdvertisement);
            return "redirect:/advertisements/" + newAdvertisement.getId();
        }
    }

    @GetMapping(value = "/advertisements/{advertisementId}/delete")
    public String processDeleteAdvertisement(@PathVariable("advertisementId") int advertisementId, Map<String, Object> model) {
        Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
        if(advertisement == null || !validateHost(advertisement.getFlat().getId())) {
            RuntimeException ex = new RuntimeException("Illegal access");
            model.put("exception", ex);
            return "exception";
        }
        this.advertisementService.deleteAdvertisement(advertisement);
        return "redirect:/";
    }

    @GetMapping(value = "/advertisements/{advertisementId}")
    public ModelAndView showAdvertisement(@PathVariable("advertisementId") int advertisementId) {
        ModelAndView mav = new ModelAndView("advertisements/advertisementDetails");
        Advertisement advertisement = this.advertisementService.findAdvertisementById(advertisementId);
        mav.addObject(advertisement);
        Collection<DBImage> images = this.dbImageService.getImagesByFlatId(advertisement.getFlat().getId());
        mav.addObject("images", images);
        String hostUsername = this.hostService.findHostByFlatId(advertisement.getFlat().getId()).getUsername();
        mav.addObject("host", hostUsername);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("ROLE_ANONYMOUS"))) {
            Person person = this.personService.findUserById(((User) auth.getPrincipal()).getUsername());
            if (person instanceof Tennant) {
                mav.addObject("requestMade", this.requestService.isThereRequestOfTenantByAdvertisementId(person.getUsername(), advertisementId));
                mav.addObject("hasFlat", ((Tennant) person).getFlat() != null);
            }
        }
        return mav;
    }

    @GetMapping(value = "/advertisements")
    public String processFindForm(Address address, BindingResult result, Map<String, Object> model) {
        if(address.getCity() == null || address.getCity().equals("")) {
            result.rejectValue("city", "cityNotNull", "The field 'city' can't be null.");
            return "welcome";
        }

        String[] sp = address.getCity().split(",");
        String city = sp[0].trim();
        String country = sp.length > 1? sp[1].trim() : null;

        Set<Advertisement> results;
        if(country == null && (address.getPostalCode() == null || address.getPostalCode().isEmpty())) {
            results = this.advertisementService.findAdvertisementsByCity(city);
        } else if(country == null && address.getPostalCode() != null && !address.getPostalCode().isEmpty()) {
            results = this.advertisementService.findAdvertisementsByCityAndPostalCode(city, address.getPostalCode());
        } else if(country != null && (address.getPostalCode() == null || address.getPostalCode().isEmpty())){
            results = this.advertisementService.findAdvertisementsByCityAndCountry(city, country);
        } else {
            results = this.advertisementService.findAdvertisementsByCityAndCountryAndPostalCode(city, country, address.getPostalCode());
        }

        if(results.isEmpty()) {
            result.rejectValue("postalCode", "advNotFound", "Not found.");
            return "welcome";
        } else {
            model.put("selections", results);
            return "advertisements/advertisementsList";
        }
    }

    public Boolean validateHost(int flatId) {
        Boolean userIsHost = true;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("admin"))) {
            String username = ((User)auth.getPrincipal()).getUsername();
            String hostUsername = this.hostService.findHostByFlatId(flatId).getUsername();
            userIsHost = username.equals(hostUsername);
        }
        return userIsHost;
    }

}
