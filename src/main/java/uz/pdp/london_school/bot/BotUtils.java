package uz.pdp.london_school.bot;

import com.pengrad.telegrambot.model.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.entity.*;
import uz.pdp.london_school.enums.Language;
import uz.pdp.london_school.i18n.BotConstant;
import uz.pdp.london_school.repo.CourseRepository;
import uz.pdp.london_school.repo.HolidayRepository;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BotUtils {
    private final CourseRepository courseRepository;
    private final HolidayRepository holidayRepository;

    public Keyboard generateContactBtn(TelegramUser telegramUser) {
        KeyboardButton keyboardButton = new KeyboardButton("☎️ " + telegramUser.getText(BotConstant.SHARE_CONTACT, "message"));
        keyboardButton.requestContact(true);
        return new ReplyKeyboardMarkup(keyboardButton).resizeKeyboard(true);
    }

    public Keyboard generateMenuBtn(TelegramUser telegramUser) {
        KeyboardButton[][] menu = new KeyboardButton[][]{
                new KeyboardButton[]{new KeyboardButton(telegramUser.getText(BotConstant.ALL_COURSES, "message"))},
                new KeyboardButton[]{new KeyboardButton(telegramUser.getText(BotConstant.MY_COURSES, "message"))},
                new KeyboardButton[]{
                        new KeyboardButton(telegramUser.getText(BotConstant.FEEDBACK, "message")),
                        new KeyboardButton(telegramUser.getText(BotConstant.SEND_MESSAGE, "message"))
                },
                new KeyboardButton[]{new KeyboardButton(telegramUser.getText(BotConstant.ABOUT_US, "message"))}
        };
        return new ReplyKeyboardMarkup(menu);
    }

    public Keyboard generateSuperAdminMenu(TelegramUser telegramUser) {
        KeyboardButton[] rows = new KeyboardButton[]{
                new KeyboardButton(telegramUser.getText(BotConstant.COURSE_CONTROL, "message")),
                new KeyboardButton(telegramUser.getText(BotConstant.TEACHER_CONTROL, "message")),
                new KeyboardButton(telegramUser.getText(BotConstant.GET_REPORT, "message"))
        };
        return new ReplyKeyboardMarkup(rows).resizeKeyboard(true);
    }

    public Keyboard generateCoursesBtns(TelegramUser telegramUser) {
        List<Course> courses = courseRepository.findAll();
        int rowSize = courses.size() / 3;
        InlineKeyboardButton[][] matrix = new InlineKeyboardButton[rowSize][3];
        InlineKeyboardMarkup btns = new InlineKeyboardMarkup(matrix);
        int r = courses.size() % 3;
        IntStream.range(0, rowSize).forEach(i ->
                IntStream.range(0, 3).forEach(j -> {
                    Course course = courses.get(i * 3 + j);
                    String name = course.getName();
                    UUID courseId = course.getId();
                    String userText = telegramUser.getText(name, "message");
                    matrix[i][j] = new InlineKeyboardButton(userText)
                            .callbackData(courseId.toString());
                })
        );
        Course lastCourse = courses.get(courses.size() - 1);
        String lastCourseName = telegramUser.getText(lastCourse.getName(), "message");
        UUID lastCourseId = lastCourse.getId();
        if (r == 1) {
            btns.addRow(new InlineKeyboardButton(telegramUser.getText(lastCourse.getName(), "message")).callbackData(lastCourseId.toString()));
        }
        if (r == 2) {
            Course beforeLast = courses.get(courses.size() - 2);
            UUID beforeLastId = beforeLast.getId();
            String beforeLastCourseName = telegramUser.getText(beforeLast.getName(), "message");
            btns.addRow(
                    new InlineKeyboardButton(beforeLastCourseName).callbackData(beforeLastId.toString()),
                    new InlineKeyboardButton(lastCourseName).callbackData(lastCourseId.toString())
            );
        }
        btns.addRow(new InlineKeyboardButton(telegramUser.getText(BotConstant.BACK, "message")).callbackData("back"));
        return btns;
    }

    public Keyboard generateLangBtns() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF" + " UZ").callbackData(Language.UZ.name()),
                new InlineKeyboardButton("\uD83C\uDDF7\uD83C\uDDFA" + " RU").callbackData(Language.RU.name()),
                new InlineKeyboardButton("\uD83C\uDDEC\uD83C\uDDE7" + " EN").callbackData(Language.EN.name())
        );
    }

    public InlineKeyboardMarkup generateCourseTypeBtn(List<CourseType> courseTypes, TelegramUser telegramUser) {
        InlineKeyboardButton[][] buttons = courseTypes.stream().map(courseType -> {
                    String name = courseType.getName();
                    String translated = telegramUser.getText(name, "courseTypename");
                    InlineKeyboardButton button = new InlineKeyboardButton(translated)
                            .callbackData(courseType.getId().toString());
                    return new InlineKeyboardButton[]{button}; // Each button is in a single-element array
                })
                .toArray(InlineKeyboardButton[][]::new); // Convert stream to 2D array

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);

        InlineKeyboardButton backButton = new InlineKeyboardButton(telegramUser.getText(BotConstant.BACK, "message"))
                .callbackData("back"); // Add appropriate callback data if necessary

        markup.addRow(backButton);
        return markup;
    }

    public Keyboard generateTimetableBtns(TelegramUser telegramUser, Map<TimeTable, Integer> timePrice) {
        Set<Map.Entry<TimeTable, Integer>> entrySet = timePrice.entrySet();
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);

        InlineKeyboardButton[][] buttons = entrySet.stream()
                .map(entry -> {
                    TimeTable timeTable = entry.getKey();
                    Integer value = entry.getValue();
                    String price = format.format(value);
                    return new InlineKeyboardButton[]{
                            new InlineKeyboardButton(telegramUser.getText(timeTable.getDescription(), "timetablename") + " : " + price)
                                    .callbackData(timeTable.getId().toString() + "," + value.toString())
                    };
                })
                .toArray(InlineKeyboardButton[][]::new);

        return new InlineKeyboardMarkup(buttons)
                .addRow(new InlineKeyboardButton(telegramUser.getText(BotConstant.BACK, "message"))
                        .callbackData("back"));
    }

    public Keyboard generateCalendar(TelegramUser telegramUser) {
        List<Holiday> holidays = holidayRepository.findAll();

        LocalDate today = LocalDate.now();//bugun
        Month month = today.getMonth();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        LocalDate nextForNextMonday = today.plusWeeks(2).with(DayOfWeek.MONDAY);

        InlineKeyboardButton[][] btns = new InlineKeyboardButton[3][7];
        //0-qator month so'zi va hafta kunlari nomi
        btns[0][0] = new InlineKeyboardButton("month").callbackData("ignored");
        Locale locale = Locale.forLanguageTag(telegramUser.getLanguage().name());
        IntStream.range(1, 7).forEach(i -> {
            DayOfWeek[] values = DayOfWeek.values();
            IntStream.range(0, values.length - 1).filter(j -> i == j + 1).forEach(j -> {
                DayOfWeek value = values[j];
                btns[0][i] = new InlineKeyboardButton(value.getDisplayName(TextStyle.SHORT, locale))
                        .callbackData("ignored");
            });
        });
        LocalDate nextMonday = today.plusWeeks(1).with(DayOfWeek.MONDAY);
        //1-qator joriy hafta kunlari
        List<LocalDate> currentWeekDays = monday.datesUntil(nextMonday).toList();
        btns[1][0] = new InlineKeyboardButton(month.getDisplayName(TextStyle.SHORT, locale)).callbackData("ignored");
        IntStream.range(1, 7).forEach(i ->
                IntStream.range(0, currentWeekDays.size() - 1).forEach(j -> {
                    LocalDate date = currentWeekDays.get(j);
                    if (i == j + 1) {
                        if (today.isAfter(date)) {
                            btns[1][i] = new InlineKeyboardButton("❌").callbackData("ignored");
                        } else if (checkHoliday(date, holidays)) {
                            btns[1][i] = new InlineKeyboardButton("🟩").callbackData("holiday");
                        } else {
                            btns[1][i] = new InlineKeyboardButton(date.getDayOfMonth() + "").callbackData(date.format(DateTimeFormatter.ofPattern("dd_MM_yyyy")));
                        }
                    }
                })
        );
        //2-qator keyingi hafta kunlari
        List<LocalDate> nextWeekDays = nextMonday.datesUntil(nextForNextMonday).toList();
        boolean match = nextWeekDays.stream().anyMatch(day -> day.getDayOfMonth() == 1);

        btns[2][0] = new InlineKeyboardButton(
                (match ? month.plus(1) : month).getDisplayName(TextStyle.SHORT, locale))
                .callbackData("ignored");
        IntStream.range(1, 7).forEach(i ->
                IntStream.range(0, nextWeekDays.size() - 1).forEach(j -> {
                    LocalDate date = nextWeekDays.get(j);
                    if (i == j + 1) {
                        if (today.isAfter(date)) {
                            btns[2][i] = new InlineKeyboardButton("❌").callbackData("ignored");
                        } else if (checkHoliday(date, holidays)) {
                            btns[2][i] = new InlineKeyboardButton("🟩").callbackData("holiday");
                        } else {
                            btns[2][i] = new InlineKeyboardButton(date.getDayOfMonth() + "").callbackData(date.format(DateTimeFormatter.ofPattern("dd_MM_yyyy")));
                        }
                    }
                })
        );
        return new InlineKeyboardMarkup(btns);
    }

    private boolean checkHoliday(LocalDate date, List<Holiday> holidays) {
        Predicate<LocalDate> isHoliday = localDate -> holidays.stream()
                .anyMatch(holiday -> holiday.getDate().equals(localDate));

        // Sana bayram kuni yoki yakshanba ekanligini tekshirish
        return isHoliday.test(date);
    }
}