package uz.pdp.london_school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.london_school.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {
}