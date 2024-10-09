package uz.pdp.london_school.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.london_school.entity.abs.BaseEntity;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Teacher extends BaseEntity {
    private Long chatId;
    private String phone;
    private String name;
    @Column(columnDefinition = "text")
    private String info;
}
