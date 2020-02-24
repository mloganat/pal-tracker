package io.pivotal.pal.tracker;

import io.pivotal.pal.trackerapi.TimeEntry;

import java.util.List;

public interface TimeEntryRepository {

    public TimeEntry create(TimeEntry timeEntry);

    public TimeEntry find(long timeEntryId);

    public List<TimeEntry> list();

    public TimeEntry update(long id, TimeEntry timeEntry);

    public void delete(long id);
}
