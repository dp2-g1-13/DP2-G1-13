package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FlatController {

    private static final String VIEWS_FLATS_CREATE_OR_UPDATE_FORM = "flats/createOrUpdateFlatForm";

    private final FlatService flatService;
    private final DBImageService dbImageService;

    @Autowired
    public FlatController(FlatService flatService, DBImageService dbImageService) {
        this.flatService = flatService;
        this.dbImageService = dbImageService;
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
            this.flatService.saveFlat(flat);

            return "redirect:/flats/" + flat.getId();
        }
    }

    @GetMapping(value = "/flats/{flatId}")
    public ModelAndView showFlat(@PathVariable("flatId") int flatId) {
        ModelAndView mav = new ModelAndView("flats/flatDetails");
        Flat flat = this.flatService.findFlatById(flatId);
        mav.addObject(flat);
        Collection<DBImage> images = this.dbImageService.getImagesByFlatId(flat.getId());
        mav.addObject("images", images);
//        mav.addObject("imagesEncoded", images.stream().map(x -> Base64.getEncoder().encodeToString(x.getData())).collect(Collectors.toList()));
        return mav;
    }




}
