package com.hopeback.service.event;

import com.hopeback.dto.event.EventDto;
import com.hopeback.entity.event.Event;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.event.EventRepository;
import com.hopeback.security.SecurityUtil;
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
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EventService(EventRepository eventRepository, ModelMapper modelMapper, MemberRepository memberRepository){
        this.eventRepository = eventRepository;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    public List<EventDto> findEventsByMemberId() {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        log.warn("1111 : " + currentMemberId);
        return eventRepository.findByMemberMemberId(currentMemberId).stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberId(currentMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + currentMemberId));

        eventDto.setMember(currentMemberId); // memberId

        Event event = modelMapper.map(eventDto, Event.class);
        event.setMember(member); // Event 엔티티에 Member 설정
        Event savedEvent = eventRepository.save(event);
        return modelMapper.map(savedEvent, EventDto.class);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        String currentMemberId = SecurityUtil.getCurrentMemberId();

        // 이벤트 소유자와 현재 사용자 ID 비교
        if (!event.getMember().getMemberId().equals(currentMemberId)) {
            throw new RuntimeException("You do not have permission to delete this event.");
        }

        eventRepository.deleteById(id);
    }


    @Transactional
    public EventDto updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // 현재 인증된 사용자의 ID를 가져옴
        String currentMemberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberId(currentMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + currentMemberId));

        event.setMember(member);
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());

        return modelMapper.map(event, EventDto.class);
    }
}
