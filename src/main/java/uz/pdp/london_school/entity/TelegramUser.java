package uz.pdp.london_school.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.london_school.enums.Language;
import uz.pdp.london_school.enums.Role;
import uz.pdp.london_school.enums.TelegramState;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Builder
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {
    @Id
    private Long chatId;
    @Enumerated(EnumType.STRING)
    private TelegramState telegramState;
    @Enumerated(EnumType.STRING)
    private Language language;
    private String phone;
    private String firstname;
    private String lastname;
    @ManyToOne
    private Course selectedCourse;
    @ManyToOne
    private CourseType selectedCourseType;
    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();
    //    @Transient
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> deletingMessages = new ArrayList<>();

    public boolean checkState(TelegramState telegramState) {
        return this.telegramState.equals(telegramState);
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    public String getText(String text, String basename) {
        ResourceBundle bundle = ResourceBundle.getBundle(basename, Locale.forLanguageTag(language.name()));
        return bundle.getString(text);
    }


}