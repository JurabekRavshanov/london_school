package uz.pdp.london_school.init;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.london_school.entity.*;
import uz.pdp.london_school.enums.Role;
import uz.pdp.london_school.i18n.CourseName;
import uz.pdp.london_school.i18n.CourseTypeInfo;
import uz.pdp.london_school.i18n.CourseTypeName;
import uz.pdp.london_school.i18n.TimeTableName;
import uz.pdp.london_school.repo.TeacherRepository;
import uz.pdp.london_school.repo.TelegramUserRepository;
import uz.pdp.london_school.service.CourseService;
import uz.pdp.london_school.service.CourseTypeService;
import uz.pdp.london_school.service.HolidayService;
import uz.pdp.london_school.service.TimeTableService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final TelegramUserRepository telegramUserRepository;
    private final TeacherRepository teacherRepository;
    private final TimeTableService timeTableService;
    private final CourseService courseService;
    private final CourseTypeService courseTypeService;
    private final HolidayService holidayService;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args) {
        if (ddl.equals("create")) {
            generateAdmins();
            generateTeachers();
            generateCourse();
            generateHolidays();
        }
    }

    private void generateHolidays() {
        for (int i = 2024; i <= 2030; i++) {
            Holiday newYear = Holiday.builder()
                    .name("New Year")
                    .date(LocalDate.of(i, 1, 1))
                    .build();
            Holiday dayOfWomen = Holiday.builder()
                    .name("Day Of Women")
                    .date(LocalDate.of(i, 3, 8))
                    .build();
            Holiday navruz = Holiday.builder()
                    .name("Navruz")
                    .date(LocalDate.of(i, 3, 21))
                    .build();
            Holiday memory = Holiday.builder()
                    .name("Xotira va qadrlash kuni")
                    .date(LocalDate.of(i, 5, 9))
                    .build();
            Holiday independence = Holiday.builder()
                    .name("Mustaqillik kuni")
                    .date(LocalDate.of(i, 9, 1))
                    .build();
            Holiday dayOfTeacher = Holiday.builder()
                    .name("Day of Teacher")
                    .date(LocalDate.of(i, 10, 1))
                    .build();
            Holiday constitution = Holiday.builder()
                    .name("Konstitutsiya kuni")
                    .date(LocalDate.of(i, 12, 8))
                    .build();
            holidayService.saveAll(List.of(newYear, dayOfWomen, navruz, memory, independence, dayOfTeacher, constitution));
        }
    }

    private void generateCourse() {
        TimeTable twoPerWeekBy1 = timeTableService.create(TimeTable.builder().name("twoPerWeekBy1").description(TimeTableName.twoPerWeekBy1).build());
        TimeTable twoPerWeek = timeTableService.create(TimeTable.builder().name("twoPerWeek").description(TimeTableName.twoPerWeek).build());
        TimeTable threePerWeekBy1 = timeTableService.create(TimeTable.builder().name("threePerWeekBy1").description(TimeTableName.threePerWeekBy1).build());
        TimeTable threePerWeek = timeTableService.create(TimeTable.builder().name("threePerWeek").description(TimeTableName.threePerWeek).build());
        TimeTable withFoodEnglish = timeTableService.create(TimeTable.builder().name("withFoodEnglish").description(TimeTableName.withFoodEnglish).build());
        TimeTable withFood = timeTableService.create(TimeTable.builder().name("withFood").description(TimeTableName.withFood).build());
        TimeTable withoutFood = timeTableService.create(TimeTable.builder().name("withoutFood").description(TimeTableName.withoutFood).build());
        TimeTable ourPupil = timeTableService.create(TimeTable.builder().name("ourPupil").description(TimeTableName.ourPupil).build());
        TimeTable otherAll = timeTableService.create(TimeTable.builder().name("otherAll").description(TimeTableName.otherAll).build());
        TimeTable individual = timeTableService.create(TimeTable.builder().name("individual").description(TimeTableName.INDIVIDUAL).build());
        TimeTable visit = timeTableService.create(TimeTable.builder().name("visit").description(TimeTableName.visit).build());
        TimeTable camp_day = timeTableService.create(TimeTable.builder().name("day").description(TimeTableName.CAMP_DAY).build());

        //English
        Course english = courseService.create(Course.builder().name(CourseName.ENGLISH).build());
        courseTypeService.create(CourseType.builder()
                .course(english)
                .name(CourseTypeName.ENGLISH_FOR_KIDS)
                .timePrice(Map.of(twoPerWeekBy1, 600000, threePerWeekBy1, 600000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(english)
                .name(CourseTypeName.ENGLISH_FOR_SCHOOL)
                .timePrice(Map.of(threePerWeek, 700000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(english)
                .name(CourseTypeName.GENERAL_ENGLISH)
                .timePrice(Map.of(threePerWeek, 800000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(english)
                .name(CourseTypeName.IELTS)
                .timePrice(Map.of(threePerWeek, 900000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(english)
                .name(CourseTypeName.CEFR)
                .timePrice(Map.of(threePerWeek, 900000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(english)
                .name(CourseTypeName.DTM)
                .timePrice(Map.of(threePerWeek, 900000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(english)
                .name(CourseTypeName.INDIVIDUAL_ENGLISH)
                .info(CourseTypeInfo.INDIVIDUAL_ENGLISH)
                .timePrice(Map.of(individual, 170000))
                .build());
        //English end
        //Russian
        Course russian = courseService.create(Course.builder().name(CourseName.RUSSIAN).build());
        courseTypeService.create(CourseType.builder()
                .course(russian)
                .name(CourseTypeName.RUSSIAN)
                .timePrice(Map.of(threePerWeekBy1, 700000))
                .build());
        //Russian end
        //Deutch
        Course deutch = courseService.create(Course.builder().name(CourseName.DEUTCH).build());
        courseTypeService.create(CourseType.builder()
                .course(deutch)
                .name(CourseTypeName.DEUTCH)
                .timePrice(Map.of(twoPerWeek, 600000))
                .build());
        //Deutch end
        //Maths
        Course maths = courseService.create(Course.builder().name(CourseName.MATHS).build());
        courseTypeService.create(CourseType.builder()
                .course(maths)
                .name(CourseTypeName.MATHS)
                .timePrice(Map.of(twoPerWeek, 600000, threePerWeek, 700000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(maths)
                .name(CourseTypeName.MATHS_IN_ENGLISH)
                .timePrice(Map.of(threePerWeek, 1200000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(maths)
                .name(CourseTypeName.SAT)
                .timePrice(Map.of(threePerWeek, 1200000))
                .build());
        //Maths end
        //Korean
        Course korean = courseService.create(Course.builder().name(CourseName.KOREAN).build());
        courseTypeService.create(CourseType.builder()
                .course(korean)
                .name(CourseTypeName.KOREAN)
                .timePrice(Map.of(threePerWeekBy1, 700000))
                .build());
        //Korean end
        //whatsWhy
        Course whatsWhy = courseService.create(Course.builder().name(CourseName.POCHEMUCHKA).build());
        courseTypeService.create(CourseType.builder()
                .course(whatsWhy)
                .name(CourseTypeName.BABY)
                .timePrice(Map.of(twoPerWeekBy1, 500000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(whatsWhy)
                .name(CourseTypeName.CHILD)
                .timePrice(Map.of(twoPerWeek, 600000))
                .build());
        //whatsWhy end
        //Uzbek
        Course uzbek = courseService.create(Course.builder().name(CourseName.UZBEK).build());
        courseTypeService.create(CourseType.builder()
                .course(uzbek)
                .name(CourseTypeName.UZBEK_FOR_SCHOOL)
                .timePrice(Map.of(twoPerWeek, 700000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(uzbek)
                .name(CourseTypeName.UZBEK_FOR_ADULT)
                .timePrice(Map.of(twoPerWeek, 800000))
                .build());
        //Uzbek end
        //painting
        Course painting = courseService.create(Course.builder().name(CourseName.PAINTING).build());
        courseTypeService.create(CourseType.builder()
                .course(painting)
                .name(CourseTypeName.PAINTING)
                .timePrice(Map.of(twoPerWeek, 600000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(painting)
                .name(CourseTypeName.PAINTING_MASTER_CLASS)
                .timePrice(Map.of(visit, 120000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(painting)
                .name(CourseTypeName.PAINTING_MASTER_CLASS)
                .timePrice(Map.of(visit, 200000))
                .build());
        //painting end
        //chess
        Course chess = courseService.create(Course.builder().name(CourseName.CHESS).build());
        courseTypeService.create(CourseType.builder()
                .course(chess)
                .name(CourseTypeName.CHESS)
                .timePrice(Map.of(twoPerWeek, 600000))
                .build());
        //chess end
        //knitting
        Course knitting = courseService.create(Course.builder().name(CourseName.KNITTING).build());
        courseTypeService.create(CourseType.builder()
                .course(knitting)
                .name(CourseTypeName.KNITTING)
                .timePrice(Map.of(twoPerWeek, 600000))
                .build());
        //knitting end
        //beading
        Course beading = courseService.create(Course.builder().name(CourseName.BEADING).build());
        courseTypeService.create(CourseType.builder()
                .course(beading)
                .name(CourseTypeName.BEADING)
                .timePrice(Map.of(twoPerWeekBy1, 500000))
                .build());
        //beading end
        //Dance
        Course dance = courseService.create(Course.builder().name(CourseName.DANCE).build());
        courseTypeService.create(CourseType.builder()
                .course(dance)
                .name(CourseTypeName.DANCE)
                .timePrice(Map.of(threePerWeekBy1, 500000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(dance)
                .name(CourseTypeName.CHOREOGRAPHY)
                .timePrice(Map.of(individual, 200000))
                .build());
        //dance end
        //IT
        Course IT = courseService.create(Course.builder().name(CourseName.IT_COURSE).build());
        courseTypeService.create(CourseType.builder()
                .course(IT)
                .name(CourseTypeName.WEB)
                .timePrice(Map.of(threePerWeek, 1200000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(IT)
                .name(CourseTypeName.COMPUTER_GRAPHICS)
                .timePrice(Map.of(threePerWeek, 700000))
                .build());
        courseTypeService.create(CourseType.builder()
                .course(IT)
                .name(CourseTypeName.COMPUTER_LITERACY)
                .timePrice(Map.of(threePerWeek, 700000))
                .build());
        //IT end
        //Speaking club
        Course speaking_club = courseService.create(Course.builder().name(CourseName.SPEAKING_CLUB).build());
        courseTypeService.create(CourseType.builder()
                .course(speaking_club)
                .info("в пятницу,субботу")
                .name(CourseTypeName.SPEAKING_CLUB)
                .timePrice(Map.of(ourPupil, 0, otherAll, 120000))
                .build());
        //Speaking club end
        //Продлёнка
        Course afterSchoolProgram = courseService.create(Course.builder().name(CourseName.AFTER_SCHOOL_PROGRAM).build());
        courseTypeService.create(CourseType.builder()
                .course(afterSchoolProgram)
                .name(CourseTypeName.AFTER_SCHOOL_PROGRAM)
                .timePrice(Map.of(withoutFood, 1100000, withFood, 1700000, withFoodEnglish, 2200000))
                .build());
        //Продлёнка end
        //Consulting
        Course consulting = courseService.create(Course.builder().name(CourseName.CONSULTING).build());
        courseTypeService.create(CourseType.builder()
                .course(consulting)
                .name(CourseTypeName.CONSULTING_TEACHING)
                .timePrice(Map.of(visit, 200000))
                .build());
        //Consulting end
        //Psychology
        Course psychology = courseService.create(Course.builder().name(CourseName.PSYCHOLOGY).build());
        courseTypeService.create(CourseType.builder()
                .course(psychology)
                .name(CourseTypeName.PSYCHOLOGY)
                .timePrice(Map.of(visit, 300000))
                .build());
        //Psychology end
        //Summer camp
        Course summer_camp = courseService.create(Course.builder().name(CourseName.SUMMER_CAMP).build());
        courseTypeService.create(CourseType.builder()
                .course(summer_camp)
                .name(CourseTypeName.SUMMER_CAMP)
                .timePrice(Map.of(camp_day, 250000))
                .build());
        //Summer camp end
    }


    private void generateTeachers() {
        Teacher teacher1 = Teacher.builder().name("Ахмедова Ситора Эркиновна").info("""
                директор и преподаватель,London school. Общий стаж работы 16 лет, имеет высшее
                филологическое образование ( специализация английский
                язык), имеет сертификаты TOEFL, Японского культурного центра UJС,
                ASA Corporation и т.д. В 2022 году Ситора Эркиновна по
                гранту съездила в США, где пребывала 1 месяц в командировке
                изучая образовательную систему и ведение бизнеса. По мимо
                этого, Ситора Эркиновна изучает управление бизнеса в
                университете Вебстер, (получает степень магистра) специализация Project
                management. В добавок, Ситора Эркиновна является ментором
                Американской программы AWE (Academy for  women
                entrepreneurs), где делится своим опытом и помогает
                женщинам достигнуть определенных целей в бизнесе. Более
                того, Ситора Эркиновна принимает активное участие на
                образовательных и бизнес конференциях, где часто выступает
                в роли спикера.
                В учебном центре London school Ситора Эркиновна ведет
                курсы General English, Pre-IELTS, IELTS и дает уроки на курсах
                переподготовки и повышение квалификации преподавателей.
                """).build();
        teacherRepository.save(teacher1);
        Teacher teacher2 = Teacher.builder().name("Аванесова Ануш Артуровна").info("""
                преподаватель английского языка,
                психолог. Общий стаж работы - 8 лет, имеет высшее
                психологическое и педагогическое образование.  Окончила
                МГУ им. М.В.Ломоносова,ФИПКиП г.Москва, а также получила
                степень магистра по психологии в НИУ ВШЭ г.Москва.
                Является сертифицированным коучем International Coach
                Union, а также коучем в сфере образования.  В учебном центре
                London School Ануш Артуровна ведет курсы General English,
                English for teens and English for kids. А также проводит
                индивидуальные психологические консультации с
                детьми,подростками и женщинами.
                В своей педагогической деятельности Ануш Артуровна
                учитывает интересы каждого ребенка, использует
                современные методы преподавания, учитывает
                индивидуальные психологические особенности учеников.
                """).build();
        teacherRepository.save(teacher2);
        Teacher teacher3 = Teacher.builder().name("Ян Наталия Сергеевна").info("""
                преподаватель по дошкольному
                образованию, корейского языка и методической подготовки
                кадров в УЦ «London school». Закончила институт Востоковедения на кафедре
                корейско - английского языка и литературы.
                Проходила курсы по переподготовки кадров для дошкольного образования(online
                формат). А также курсы по ведению уроков ментальной
                арифметики и элементарной математике.
                Стаж работы более 12 лет. Ученики Наталии Сергеевны,
                успешно проходят собеседования при поступлении в
                спецшколы, а также в школы с математическим уклоном. Есть
                также ученики которые успешно поступили в Вузы Кореи и
                получили сертификаты Topic(3-4уровень)
                """).build();
        teacherRepository.save(teacher3);
        Teacher teacher4 = Teacher.builder().name("Хван Мария Владимировна").info("""
                Преподователь английского языка.
                Общий стаж работы 5 лет. Имеет высшее образование
                (специализация английский язык для детей дошкольного и
                начального школьного возраста). Имеет сертификат IELTS
                Overall 6.5 (Listening 6.0, Reading 6.5, Writing 6.0, Speaking 6.5).
                Также владеет разговорным уровнем корейского языка. В
                London school ведет уроки General English. Находит
                индивидуальный подход для каждого ребенка вне зависимости
                от возраста. Использует современные методики преподования
                языка.
                """).build();
        teacherRepository.save(teacher4);
    }

    private void generateAdmins() {
        TelegramUser super_admin = TelegramUser.builder()
                .chatId(1314108L)
//                .phone("998946651917")
                .role(Role.SUPER_ADMIN)
                .build();
        telegramUserRepository.save(super_admin);
        TelegramUser me = TelegramUser.builder()
                .chatId(368571060L)
//                .phone("998905148239")
                .role(Role.USER)
                .build();
        telegramUserRepository.save(me);

        TelegramUser admin = TelegramUser.builder()
                .chatId(5059809834L)
                .role(Role.ADMIN)
                .build();
        telegramUserRepository.save(admin);
    }
}
