package uz.pdp.london_school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.london_school.entity.abs.BaseEntity;

import java.util.Map;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseType extends BaseEntity {
    private String name;
    @Column(columnDefinition = "text")
    private String info;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "time_price", joinColumns = @JoinColumn(name = "course_type_id"))
    @MapKeyColumn(name = "time_table")
    @Column(name = "price")
    private Map<TimeTable, Integer> timePrice;
    @ManyToOne
    private Course course;
}