package uz.pdp.london_school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.entity.Holiday;
import uz.pdp.london_school.repo.HolidayRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public void saveAll(List<Holiday> holidays) {
        holidayRepository.saveAll(holidays);
    }
}
