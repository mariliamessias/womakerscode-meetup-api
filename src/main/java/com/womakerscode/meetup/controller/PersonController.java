package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.PersonRequest;
import com.womakerscode.meetup.model.PersonResponse;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.model.entity.Person;
import com.womakerscode.meetup.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse create(@RequestBody @Valid PersonRequest request) {
        return personService.save(request).toPersonResponse();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse get(@PathVariable Long id) {

        return personService
                .getPersonById(id)
                .map(Person::toPersonResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        personService.delete(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse update(@PathVariable Long id, @RequestBody @Valid PersonRequest request) {
        return personService.update(request, id)
                .toPersonResponse();
    }
}
