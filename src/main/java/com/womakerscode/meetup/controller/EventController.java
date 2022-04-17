package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.EventRequest;
import com.womakerscode.meetup.model.EventResponse;
import com.womakerscode.meetup.model.entity.Event;
import com.womakerscode.meetup.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@RequestBody @Valid EventRequest request) {
        return eventService.save(request).toEventResponse();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponse get(@PathVariable Long id) {

        return eventService
                .getEventById(id)
                .map(Event::toEventResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        eventService.delete(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponse update(@PathVariable Long id, @RequestBody @Valid EventRequest request) {
        return eventService.update(request, id)
                .toEventResponse();
    }

}
