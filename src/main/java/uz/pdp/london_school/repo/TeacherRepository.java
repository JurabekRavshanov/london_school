package uz.pdp.london_school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.london_school.entity.Teacher;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
}