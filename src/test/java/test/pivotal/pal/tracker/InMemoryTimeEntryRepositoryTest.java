package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.InMemoryTimeEntryRepository;
import io.pivotal.pal.trackerapi.TimeEntry;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTimeEntryRepositoryTest {
    @Test
    public void create() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        TimeEntry createdTimeEntry = repo.create(new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));


        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        assertThat(createdTimeEntry).isEqualToComparingFieldByField(expected);

        TimeEntry readEntry = repo.find(createdTimeEntry.getId());
        assertThat(readEntry).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void find() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        repo.create(new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));


        TimeEntry expected = new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void find_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long timeEntryId = 1L;

        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isNull();
    }

    @Test
    public void list() throws Exception {
        long timeEntryId = 1L;
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        repo.create(new TimeEntry(timeEntryId, 123L, 456L, LocalDate.parse("2017-01-08"), 8));
        timeEntryId = 2L;
        repo.create(new TimeEntry(timeEntryId, 789L, 654L, LocalDate.parse("2017-01-07"), 4));

        List<TimeEntry> expected = asList(
                new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8),
                new TimeEntry(2L, 789L, 654L, LocalDate.parse("2017-01-07"), 4)
        );
        Assert.assertEquals(repo.list().size(),expected.size());
    }

    @Test
    public void update() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        long timeEntryId = 1L;
        TimeEntry created = repo.create(new TimeEntry(timeEntryId, 123L, 456L, LocalDate.parse("2017-01-08"), 8));

        TimeEntry updatedEntry = repo.update(
                created.getId(),
                new TimeEntry(timeEntryId, 321L, 654L, LocalDate.parse("2017-01-09"), 5));

        TimeEntry expected = new TimeEntry(created.getId(), 321L, 654L, LocalDate.parse("2017-01-09"), 5);
        assertThat(updatedEntry).isEqualToComparingFieldByField(expected);
        assertThat(repo.find(created.getId())).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void update_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        long timeEntryId = 1L;
        TimeEntry updatedEntry = repo.update(
                1L,
                new TimeEntry(timeEntryId, 321L, 654L, LocalDate.parse("2017-01-09"), 5));

        assertThat(updatedEntry).isNull();
    }

    @Test
    public void delete() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        TimeEntry created = repo.create(new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));

        repo.delete(created.getId());
        assertThat(repo.list()).isEmpty();
    }

    @Test
    public void deleteKeepsTrackOfLatestIdProperly() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        TimeEntry created = repo.create(new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));

        assertThat(created.getId()).isEqualTo(1);

        repo.delete(created.getId());
        timeEntryId = 2L;
        TimeEntry createdSecond = repo.create(new TimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));

        assertThat(createdSecond.getId()).isEqualTo(2);
    }
}
