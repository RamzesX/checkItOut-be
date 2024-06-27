package com.sm.instagram.instagramPlatform.influencer;

import com.sm.instagram.instagramPlatform.exceptions.ItemNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;
import java.util.Optional;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

@RestController
@CrossOrigin
@RequestMapping("influencer")
public class InfluencerController {
    @Autowired
    InfluencerService influencerService;
    ModelMapper modelMapper = new ModelMapper();


    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody InfluencerDto getInfluencer(@PathVariable Long id) throws ItemNotFoundException {
        Optional<Influencer> influencer = influencerService.findInfluencerById(id);
        InfluencerDto influencerDto = modelMapper.map(influencer, InfluencerDto.class);
        return influencerDto;
    }

    @GetMapping(value = "/paged", params = { "page", "size" }, produces = "application/json")
    public @ResponseBody Page<InfluencerDto> findPaginated(@RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          UriComponentsBuilder uriBuilder,
                                          HttpServletResponse response) throws ItemNotFoundException {

        Page<Influencer> infuencerPage = influencerService.fetchCustomerDataAsPageWithFiltering(page, size);

        if (page > infuencerPage.getTotalPages()) {
            throw new ItemNotFoundException("Page with given value don't exist.", new Throwable());
        }

        Page<InfluencerDto> influencerDtoPage = infuencerPage.map(e -> modelMapper.map(e, InfluencerDto.class));

        return influencerDtoPage;
    }

    @Secured("SCOPE_RAEAD")
    @GetMapping(value = "/list", produces = "application/json")
    public @ResponseBody List<InfluencerDto> getInfluencerList() throws ItemNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        authentication.getAuthorities();

        List<Influencer> influencerList = influencerService.fetchCustomerDataAsList();
        List<InfluencerDto> influencerDtoList = influencerList.stream().map(e -> modelMapper.map(e, InfluencerDto.class)).toList();
        return influencerDtoList;
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public @ResponseBody InfluencerDto createInfluencer(@RequestBody InfluencerDto influencerDto) {
        Influencer newInfluencer = influencerService.createInfluencer(modelMapper.map(influencerDto, Influencer.class));
        return modelMapper.map(newInfluencer, InfluencerDto.class);
    }

    @PostMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public @ResponseBody InfluencerDto updateInfluencer(@RequestBody InfluencerDto influencerDto, HttpServletResponse response) {
        response.setHeader("Location", ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/findPerson/" + influencerDto.getId()).toUriString());

        Influencer updatedInfluencer = influencerService.updateInfluencer(modelMapper.map(influencerDto, Influencer.class));
        return modelMapper.map(updatedInfluencer, InfluencerDto.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        influencerService.deleteInfluencer(id);
    }
}