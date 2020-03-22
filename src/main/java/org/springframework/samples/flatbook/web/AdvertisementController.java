package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.exceptions.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, DBImageService dbImageService, FlatService flatService, HostService hostService) {
        this.advertisementService = advertisementService;
        this.dbImageService = dbImageService;
        this.flatService = flatService;
        this.hostService = hostService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/flats/{flatId}/advertisements/new")
    public String initCreationForm(@PathVariable("flatId") int flatId, Map<String, Object> model) {
        Flat flat = this.flatService.findFlatById(flatId);
        if(flat == null || !validateHost(flatId) || this.advertisementService.isAdvertisementWithFlatId(flat.getId())) {
            RuntimeException ex = new RuntimeException("Illegal access");
            model.put("exception", ex);
            return "exception";
        }
        FormAdvertisement advertisement = new FormAdvertisement();
        model.put("advertisement", advertisement);
        return VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/flats/{flatId}/advertisements/new")
        public String processCreationForm(@Valid FormAdvertisement adv, @PathVariable("flatId") int flatId, BindingResult result, Map<String, Object> model) {
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
    public String initEditForm(@PathVariable("advertisementId") int advertisementId, Map<String, Object> model) {
        Advertisement adv = this.advertisementService.findAdvertisementById(advertisementId);
        if(adv == null || !validateHost(adv.getFlat().getId())) {
            RuntimeException ex = new RuntimeException("Illegal access");
            model.put("exception", ex);
            return "exception";
        }
        FormAdvertisement fa = new FormAdvertisement(adv);
        model.put("advertisement", fa);
        return VIEWS_ADVERTISEMENTS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/advertisements/{advertisementId}/edit")
    public String processEditForm(@Valid FormAdvertisement adv, @PathVariable("advertisementId") int advertisementId, BindingResult result, Map<String, Object> model) {
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
        if(country == null && address.getPostalCode() == null) {
            results = this.advertisementService.findAdvertisementsByCity(city);
        } else if(country == null && address.getPostalCode() != null) {
            results = this.advertisementService.findAdvertisementsByCityAndPostalCode(city, address.getPostalCode());
        } else if(country != null && address.getPostalCode() == null){
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
