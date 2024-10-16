package uz.pdp.london_school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.london_school.entity.Course;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

}