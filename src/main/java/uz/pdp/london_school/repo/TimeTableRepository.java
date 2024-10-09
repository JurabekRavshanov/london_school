package uz.pdp.london_school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.london_school.entity.TimeTable;

public interface TimeTableRepository extends JpaRepository<TimeTable, Integer> {
}