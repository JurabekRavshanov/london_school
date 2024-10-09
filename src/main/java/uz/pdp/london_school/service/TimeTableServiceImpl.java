package uz.pdp.london_school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.entity.TimeTable;
import uz.pdp.london_school.repo.TimeTableRepository;

@Service
@RequiredArgsConstructor
public class TimeTableServiceImpl implements TimeTableService {

    private final TimeTableRepository timeTableRepository;

    @Override
    public TimeTable create(TimeTable timeTable) {
        return timeTableRepository.save(timeTable);
    }
}
