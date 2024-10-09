package uz.pdp.london_school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.entity.CourseType;
import uz.pdp.london_school.repo.CourseTypeRepository;

@Service
@RequiredArgsConstructor
public class CourseTypeServiceImpl implements CourseTypeService {

    private final CourseTypeRepository courseTypeRepository;

    @Override
    public CourseType create(CourseType courseType) {
        return courseTypeRepository.save(courseType);
    }
}
