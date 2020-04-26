package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.*;
import org.springframework.samples.flatbook.service.*;
import org.springframework.samples.flatbook.web.utils.ReviewUtils;
import org.springframework.samples.flatbook.web.validators.FlatValidator;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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

    @InitBinder("flat")
    public void initBinder(final WebDataBinder dataBinder, final HttpServletRequest http) {
        if (http.getRequestURI().split("[/]")[http.getRequestURI().split("[/]").length - 1].equals("new")) {
            dataBinder.addValidators(new FlatValidator());
        }
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
            if(flat.getImages().size() < 6) {
                result.rejectValue("images", "", "a minimum of 6 images is required.");
                return VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Host host = (Host) this.personService.findUserById(((User)auth.getPrincipal()).getUsername());
            host.addFlat(flat);
            this.flatService.saveFlat(flat);

            return "redirect:/flats/" + flat.getId();
        }
    }

    @GetMapping(value = "/flats/{flatId}/edit")
    public String initUpdateForm(@PathVariable("flatId") int flatId, Map<String, Object> model) {
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
    public String processUpdateForm(@Valid Flat flat, BindingResult result, @PathVariable("flatId") int flatId, Map<String, Object> model) {
        if(result.hasErrors()) {
            return VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
        } else {
            if(!validateHost(flatId)) {
                RuntimeException e = new RuntimeException("Illegal access");
                model.put("exception", e);
                return "exception";
            }
            Set<DBImage> newImages = flat.getImages().stream().filter(x -> !x.getFileType().equals("application/octet-stream")).collect(Collectors.toSet());
            Flat oldFlat = this.flatService.findFlatById(flatId);
            if(oldFlat.getImages().size() + newImages.size() < 6) {
                result.rejectValue("images", "", "a minimum of 6 images is required.");
                return VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
            }
            Set<DBImage> images = oldFlat.getImages();
            images.addAll(newImages);
            flat.setImages(images);
            flat.setId(flatId);
            this.flatService.saveFlat(flat);

            return "redirect:/flats/{flatId}";
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
        if(flat.getImages().size() == 6) {
            RuntimeException e = new RuntimeException("Illegal access");
            model.put("exception", e);
            return "exception";
        } else {
            DBImage image = this.dbImageService.getImageById(imageId);
            flat.deleteImage(image);
            this.dbImageService.deleteImage(image);
            return "redirect:/flats/{flatId}/edit";
        }
    }

    @GetMapping(value = "/flats/{flatId}")
    public ModelAndView showFlat(@PathVariable("flatId") int flatId, Principal principal) {
        if(!validateHost(flatId) && !validateTenant(flatId)) {
            throw new RuntimeException("Illegal access");
        }
        ModelAndView mav = new ModelAndView("flats/flatDetails");
        Flat flat = this.flatService.findFlatById(flatId);
        mav.addObject(flat);

        Host host = this.hostService.findHostByFlatId(flat.getId());
        mav.addObject("host", host.getUsername());
        mav.addObject("images", flat.getImages());

        Boolean existAd = this.advertisementService.isAdvertisementWithFlatId(flat.getId());
        mav.addObject("existAd", existAd);

        List<FlatReview> reviews = new ArrayList<>(flat.getFlatReviews());
        reviews.sort(Comparator.comparing(FlatReview::getCreationDate).reversed());
        mav.addObject("reviews", reviews);
        mav.addObject("flatId", flat.getId());
        mav.addObject("canCreateReview", principal != null &&ReviewUtils.isAllowedToReviewAFlat(principal.getName(), flat.getId()));
        return mav;
    }

    @GetMapping(value = "/flats/my-flats")
    public ModelAndView showFlatsOfHost() {
        ModelAndView mav = new ModelAndView("flats/flatsOfHost");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((User)auth.getPrincipal()).getUsername();
        List<Flat> flats = new ArrayList<>(this.flatService.findFlatByHostUsername(username));
        List<Integer> advIds = flats.stream()
            .map(x -> {
                Advertisement adv = this.advertisementService.findAdvertisementWithFlatId(x.getId());
                return adv == null? null : adv.getId();
            }).collect(Collectors.toList());
        mav.addObject("flats", flats);
        mav.addObject("advIds", advIds);
        return mav;
    }

    public boolean validateHost(int flatId) {
        Boolean userIsHost = true;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("admin"))) {
            String username = ((User)auth.getPrincipal()).getUsername();
            String hostUsername = this.hostService.findHostByFlatId(flatId).getUsername();
            userIsHost = username.equals(hostUsername);
        }
        return userIsHost;
    }

    private boolean validateTenant(int flatId) {
        boolean userIsTenantOfFlat = true;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals("admin"))) {
            String username = ((User) auth.getPrincipal()).getUsername();
            Flat flat = this.flatService.findFlatById(flatId);
            userIsTenantOfFlat = flat.getTenants().stream().anyMatch(x -> x.getUsername().equals(username));
        }
        return userIsTenantOfFlat;
    }




}
