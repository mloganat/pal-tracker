package io.pivotal.pal.tracker;

import io.pivotal.pal.trackerapi.TimeEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    public Map<Long,TimeEntry> dbMap = new HashMap<>();
    @Override
    public TimeEntry create(TimeEntry te) {
        dbMap.put(te.getId(),te);
        return te;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return dbMap.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        TimeEntry te;
        List<TimeEntry> listTe = new ArrayList<>();
        for (Map.Entry<Long,TimeEntry> entry : dbMap.entrySet()) {
            te = entry.getValue();
            listTe.add(te);
        }
        return listTe;
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry te) {
        if (dbMap.get(timeEntryId) != null) {
            dbMap.put(timeEntryId,te);
            return te;
        }
        return null;
    }

    @Override
    public void delete(long timeEntryId) {
        if (dbMap.get(timeEntryId) != null) {
            dbMap.remove(timeEntryId);
        }
    }
}
