package uz.pdp.london_school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.london_school.entity.Course;
import uz.pdp.london_school.entity.CourseType;

import java.util.List;
import java.util.UUID;

public interface CourseTypeRepository extends JpaRepository<CourseType, UUID> {
    List<CourseType> findAllByCourse(Course course);
}