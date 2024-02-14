package com.hopeback.service.event;

import com.hopeback.dto.event.EventDto;
import com.hopeback.entity.event.Event;
import com.hopeback.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public EventService(EventRepository eventRepository, ModelMapper modelMapper){
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    public List<EventDto> findAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        LocalDate eventDate = eventDto.getDate().plusDays(1); // 하루 다음 날짜
        eventDto.setDate(eventDate);

        Event event = modelMapper.map(eventDto, Event.class);
        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDto.class);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Transactional
    public EventDto updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());
        Event updatedEvent = eventRepository.save(event);
        return modelMapper.map(updatedEvent, EventDto.class);
    }

}
