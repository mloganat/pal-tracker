package test.pivotal.pal.trackerapi;

import com.jayway.jsonpath.DocumentContext;
import io.pivotal.pal.tracker.PalTrackerApplication;
import io.pivotal.pal.trackerapi.TimeEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PalTrackerApplication.class, webEnvironment = RANDOM_PORT)
public class TimeEntryApiTest {

    @Autowired
    private TestRestTemplate restTemplate  = new TestRestTemplate();

    private final long projectId = 123L;
    private final long userId = 456L;
    private final long timeEntryId = 1L;
    private TimeEntry timeEntry = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);


    @Test
    public void testCreate() throws Exception {
        ResponseEntity<TimeEntry> createResponse = restTemplate.postForEntity("/time-entries", timeEntry, TimeEntry.class);
        TimeEntry createdTe = createResponse.getBody();
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat((createdTe.getId()).equals(timeEntryId));

//        DocumentContext createJson = parse(createResponse.getBody());
//        assertThat(createJson.read("$.id", Long.class)).isGreaterThan(0);
//        assertThat(createJson.read("$.projectId", Long.class)).isEqualTo(projectId);
//        assertThat(createJson.read("$.userId", Long.class)).isEqualTo(userId);
//        assertThat(createJson.read("$.date", String.class)).isEqualTo("2017-01-08");
//        assertThat(createJson.read("$.hours", Long.class)).isEqualTo(8);
    }

    @Test
    public void testList() throws Exception {
        Long id = createTimeEntry();
        ResponseEntity<TimeEntry[]> listResponse = restTemplate.getForEntity("/time-entries", TimeEntry[].class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        TimeEntry[] list = listResponse.getBody();

        assertThat(listResponse.getBody().length).isEqualTo(1);
        assertThat(list[0].getId()).isEqualTo(id);
//        DocumentContext listJson = parse(listResponse.getBody());
//
//        Collection timeEntries = listJson.read("$[*]", Collection.class);
//        assertThat(timeEntries.size()).isEqualTo(1);
//
//        Long readId = listJson.read("$[0].id", Long.class);
//        assertThat(readId).isEqualTo(id);
    }

    @Test
    public void testRead() throws Exception {
        Long id = createTimeEntry();
        ResponseEntity<TimeEntry> readResponse = this.restTemplate.getForEntity("/time-entries/" + id, TimeEntry.class);
        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResponse.getBody().getProjectId()).isEqualTo(projectId);
//        DocumentContext readJson = parse(readResponse.getBody());
//        assertThat(readJson.read("$.id", Long.class)).isEqualTo(id);
//        assertThat(readJson.read("$.projectId", Long.class)).isEqualTo(projectId);
//        assertThat(readJson.read("$.userId", Long.class)).isEqualTo(userId);
//        assertThat(readJson.read("$.date", String.class)).isEqualTo("2017-01-08");
//        assertThat(readJson.read("$.hours", Long.class)).isEqualTo(8);
    }

    @Test
    public void testUpdate() throws Exception {
        Long id = createTimeEntry();
        long projectId = 2L;
        long userId = 3L;
        TimeEntry updatedTimeEntry = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-09"), 9);
        ResponseEntity<TimeEntry> updateResponse = restTemplate.exchange("/time-entries/" + id, HttpMethod.PUT, new HttpEntity<>(updatedTimeEntry, null), TimeEntry.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getProjectId()).isEqualTo(projectId);
//        DocumentContext updateJson = parse(updateResponse.getBody());
//        assertThat(updateJson.read("$.id", Long.class)).isEqualTo(id);
//        assertThat(updateJson.read("$.projectId", Long.class)).isEqualTo(projectId);
//        assertThat(updateJson.read("$.userId", Long.class)).isEqualTo(userId);
//        assertThat(updateJson.read("$.date", String.class)).isEqualTo("2017-01-09");
//        assertThat(updateJson.read("$.hours", Long.class)).isEqualTo(9);
    }

    @Test
    public void testDelete() throws Exception {
        Long id = createTimeEntry();
        ResponseEntity<String> deleteResponse = restTemplate.exchange("/time-entries/" + id, HttpMethod.DELETE, null, String.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<TimeEntry> deletedReadResponse = this.restTemplate.getForEntity("/time-entries/" + id, TimeEntry.class);
        assertThat(deletedReadResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Long createTimeEntry() {
        HttpEntity<TimeEntry> entity = new HttpEntity<>(timeEntry);
        ResponseEntity<TimeEntry> response = restTemplate.exchange("/time-entries", HttpMethod.POST, entity, TimeEntry.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        return response.getBody().getId();
    }
}
