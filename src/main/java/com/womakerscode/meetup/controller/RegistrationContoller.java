package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.RegistrationRequest;
import com.womakerscode.meetup.model.RegistrationResponse;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registration")
public class RegistrationContoller {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse create(@RequestBody @Valid RegistrationRequest request) {
        return registrationService.save(request).toRegistrationResponse();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponse get(@PathVariable Long id) {

        return registrationService
                .getRegistrationById(id)
                .map(Registration::toRegistrationResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        registrationService.delete(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponse update(@PathVariable Long id, @RequestBody @Valid RegistrationRequest request) {
        return registrationService.update(request, id)
                .toRegistrationResponse();
    }

    @GetMapping
    public Page<RegistrationResponse> getPaginable(RegistrationRequest request, Pageable pageRequest) {
        Page<Registration> result = registrationService.find(request.toFindRegistration(), pageRequest);

        List<RegistrationResponse> list = result.getContent()
                .stream()
                .map(Registration::toRegistrationResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageRequest, result.getTotalElements());
    }

}
