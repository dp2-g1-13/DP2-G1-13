package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.service.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Controller
public class FlatController {

    private static final String VIEWS_FLATS_CREATE_OR_UPDATE_FORM = "flats/createOrUpdateFlatForm";

    private final FlatService flatService;
    private final DBImageService dbImageService;
    private final PersonService personService;
    private final HostService hostService;
    private final AdvertisementService advertisementService;

    @Autowired
    public FlatController(FlatService flatService, DBImageService dbImageService, PersonService personService, HostService hostService, AdvertisementService advertisementService) {
        this.flatService = flatService;
        this.dbImageService = dbImageService;
        this.personService = personService;
        this.hostService = hostService;
        this.advertisementService = advertisementService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/flats/new")
    public String initCreationForm(Map<String, Object> model) {
        Flat flat = new Flat();
        model.put("flat", flat);
        return VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/flats/new")
    public String processCreationForm(@Valid Flat flat, BindingResult result) {
        if(result.hasErrors()) {
           return VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Host host = (Host) this.personService.findUserById(((User)auth.getPrincipal()).getUsername());
            host.addFlat(flat);
            this.flatService.saveFlat(flat);

            return "redirect:/flats/" + flat.getId();
        }
    }

    @GetMapping(value = "/flats/{flatId}/edit")
    public String initEditForm(@PathVariable("flatId") int flatId, Map<String, Object> model) {
        if(!validateHost(flatId)) {
            RuntimeException e = new RuntimeException("Illegal access");
            model.put("exception", e);
            return "exception";
        }
        Flat flat = this.flatService.findFlatById(flatId);
        model.put("flat", flat);
        model.put("images", flat.getImages());
        return VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/flats/{flatId}/edit")
    public String processEditForm(@Valid Flat flat, @PathVariable("flatId") int flatId, BindingResult result, Map<String, Object> model) {
        if(result.hasErrors()) {
            return VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
        } else {
            if(!validateHost(flatId)) {
                RuntimeException e = new RuntimeException("Illegal access");
                model.put("exception", e);
                return "exception";
            }
            Set<DBImage> images = this.flatService.findFlatById(flatId).getImages();
            images.addAll(flat.getImages());
            flat.setImages(images);
            flat.setId(flatId);
            this.flatService.saveFlat(flat);

            return "redirect:/flats/" + flat.getId();
        }
    }

    @GetMapping(value = "/flats/{flatId}/images/{imageId}/delete")
    public String processDeleteImage(@PathVariable("flatId") int flatId, @PathVariable("imageId") int imageId, Map<String, Object> model) {
        if(!validateHost(flatId)) {
            RuntimeException e = new RuntimeException("Illegal access");
            model.put("exception", e);
            return "exception";
        }
        Flat flat = this.flatService.findFlatById(flatId);
        DBImage image = this.dbImageService.getImageById(imageId);
        flat.deleteImage(image);
        this.dbImageService.deleteImage(image);
        model.put("flat", flat);
        model.put("images", flat.getImages());
        return "redirect:/flats/" + flat.getId() + "/edit";
    }

    @GetMapping(value = "/flats/{flatId}")
    public ModelAndView showFlat(@PathVariable("flatId") int flatId) {
        ModelAndView mav = new ModelAndView("flats/flatDetails");
        Flat flat = this.flatService.findFlatById(flatId);
        mav.addObject(flat);
        Host host = this.hostService.findHostByFlatId(flat.getId());
        mav.addObject("host", host.getUsername());
        Collection<DBImage> images = this.dbImageService.getImagesByFlatId(flat.getId());
        mav.addObject("images", images);
        Boolean existAd = this.advertisementService.isAdvertisementWithFlatId(flat.getId());
        mav.addObject("existAd", existAd);
        return mav;
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
