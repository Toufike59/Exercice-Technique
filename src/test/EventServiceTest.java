import adeo.leroymerlin.cdp.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {

    @InjectMocks
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    @Captor
    ArgumentCaptor<Event> eventArgumentCaptor;

    @Test
    public void getFilteredEventsTest() {
        Member member = new Member();
        member.setName("Julie");
        Member member2 = new Member();
        member2.setName("Tarik");
        Member member3 = new Member();
        member3.setName("Oumeyma");

        Band band = new Band();
        band.setName("band 1");
        band.setMembers(Stream.of(member).collect(Collectors.toSet()));

        Band band2 = new Band();
        band2.setName("band 2");
        band2.setMembers(Stream.of(member2, member3).collect(Collectors.toSet()));

        Event event = new Event();
        event.setTitle("Event 1");
        event.setBands(Stream.of(band).collect(Collectors.toSet()));

        Event event2 = new Event();
        event2.setTitle("Event 2");
        event2.setBands(Stream.of(band2).collect(Collectors.toSet()));
        //I create the object for the mock
        List<Event> events = Arrays.asList(event,event2);

        //I mock the return BDD
        Mockito.when(eventRepository.findAllBy()).thenReturn(events);
        List<Event> eventList = eventService.getFilteredEvents("Ta");

        Assert.assertNotNull(eventList);
        Assert.assertEquals(eventList,Stream.of(event2).collect(Collectors.toList()));
        Assert.assertEquals(eventList.get(0).getBands(),event2.getBands());

    }

    @Test
    public void updateEventTest() {
        Member member2 = new Member();
        member2.setName("Oumeyma");

        Band band2 = new Band();
        band2.setName("band 2");
        band2.setMembers(Stream.of(member2).collect(Collectors.toSet()));

        Event event = new Event();
        event.setTitle("Event 2");
        event.setBands(Stream.of(band2).collect(Collectors.toSet()));


        Member member = new Member();
        member.setName("Tarik");

        Band band = new Band();
        band.setName("band 1");
        band.setMembers(Stream.of(member).collect(Collectors.toSet()));

        Event eventUpdate = new Event();
        eventUpdate.setId(1L);
        eventUpdate.setTitle("Event 1");
        eventUpdate.setBands(Stream.of(band).collect(Collectors.toSet()));

        //Mock
        Mockito.when(eventRepository.findById(1L)).thenReturn(event);
        eventService.updateEvent(1L, eventUpdate);
        Mockito.verify(eventRepository).save(eventArgumentCaptor.capture());
        Event event1 = eventArgumentCaptor.getValue();
        Assert.assertNotNull(event1);
    }

    @Test
    public void deleteTest(){
        eventService.delete(1L);
        Mockito.verify(eventRepository).delete(1L);
    }

}
