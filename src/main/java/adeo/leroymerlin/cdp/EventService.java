package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    @Transactional
    public void delete(Long id) {
        eventRepository.delete(id);
    }

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        List<Event> eventListFilterStream = events.stream()
                .filter(event -> event.getBands().stream().flatMap(band -> band.getMembers().stream()).anyMatch(member -> member.getName().contains(query)))
                .collect(Collectors.toList());
        return eventListFilterStream;
    }

    @Transactional
    public void updateEvent(Long id, Event event) {
        Event eventUpdate = eventRepository.findById(id);
        eventUpdate.setBands(event.getBands());
        eventUpdate.setComment(event.getComment());
        eventUpdate.setTitle(event.getTitle());
        eventUpdate.setNbStars(event.getNbStars());
        eventRepository.save(eventUpdate);
    }
}
