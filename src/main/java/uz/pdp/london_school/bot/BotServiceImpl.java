package uz.pdp.london_school.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.entity.Course;
import uz.pdp.london_school.entity.CourseType;
import uz.pdp.london_school.entity.TelegramUser;
import uz.pdp.london_school.entity.TimeTable;
import uz.pdp.london_school.enums.Language;
import uz.pdp.london_school.enums.Role;
import uz.pdp.london_school.enums.TelegramState;
import uz.pdp.london_school.i18n.BotConstant;
import uz.pdp.london_school.repo.CourseRepository;
import uz.pdp.london_school.repo.CourseTypeRepository;
import uz.pdp.london_school.repo.TelegramUserRepository;
import uz.pdp.london_school.repo.TimeTableRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {
    private final TelegramUserRepository telegramUserRepository;
    private final BotUtils botUtils;
    private final TelegramBot telegramBot;
    private final CourseRepository courseRepository;
    private final CourseTypeRepository courseTypeRepository;
    private final TimeTableRepository timeTableRepository;


    @Override
    public TelegramUser getOrCreateTelegramUser(Long chatId) {
        Optional<TelegramUser> byId = telegramUserRepository.findById(chatId);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            TelegramUser user = TelegramUser.builder()
                    .chatId(chatId)
                    .telegramState(TelegramState.START)
                    .role(Role.USER)
                    .build();
            telegramUserRepository.save(user);
            return user;
        }
    }

    @Override
    public void acceptStartAskLang(TelegramUser telegramUser) {
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(),
                BotConstant.ASK_LANG);
        sendMessage.replyMarkup(botUtils.generateLangBtns());
        telegramUser.setTelegramState(TelegramState.SELECT_LANG);
        telegramUserRepository.save(telegramUser);
        telegramBot.execute(sendMessage);
    }

    @Override
    public void acceptLangAskContact(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        Language language = Language.valueOf(callbackQuery.data());
        telegramUser.setLanguage(language);
        telegramUser.setLastname(callbackQuery.from().lastName());
        telegramUser.setFirstname(callbackQuery.from().firstName());
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(),
                telegramUser.getText(BotConstant.ASK_CONTACT, "message").formatted(telegramUser.getFullName()));
        telegramUser.setTelegramState(TelegramState.SHARE_CONTACT);
        telegramUserRepository.save(telegramUser);
        sendMessage.replyMarkup(botUtils.generateContactBtn(telegramUser));
        telegramBot.execute(sendMessage);
    }

    @Override
    public void acceptContactShowUserMenu(TelegramUser telegramUser, Message message) {
        Contact contact = message.contact();
        String phone = contact.phoneNumber();
        telegramUser.setPhone(phone);
        goToMenu(telegramUser);
    }

    public void goToMenu(TelegramUser telegramUser) {
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(),
                telegramUser.getText(BotConstant.WELCOME_USER, "message").formatted(telegramUser.getFullName()));
        sendMessage.replyMarkup(botUtils.generateMenuBtn(telegramUser));
        telegramUser.setTelegramState(TelegramState.SELECT_COMMAND);
        SendResponse resp = telegramBot.execute(sendMessage);
        telegramUser.getDeletingMessages().add(resp.message().messageId());
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public void acceptMenuShowAllCourses(TelegramUser telegramUser) {
        clearMessages(telegramUser);
        goToAllCourses(telegramUser);
    }

    private void clearMessages(TelegramUser telegramUser) {
        telegramUser.getDeletingMessages().stream()
                .map(messageId -> new DeleteMessage(telegramUser.getChatId(), messageId))
                .forEach(telegramBot::execute);
        telegramUser.getDeletingMessages().clear();
    }

    @Override
    public void acceptMenuShowCoursesOfUser(TelegramUser telegramUser) {
        StringBuilder string = new StringBuilder();
        List<Course> courses = telegramUser.getCourses();
        AtomicInteger index = new AtomicInteger();
        courses.stream()
                .flatMap(course -> course.getCourseTypes().stream())
                .flatMap(courseType -> courseType.getTimePrice().entrySet().stream())
                .forEachOrdered(entry -> {
                    TimeTable timeTable = entry.getKey();
                    Integer price = entry.getValue();
                    String description = timeTable.getDescription();

                    string.append(string.isEmpty() ? "" : "\n")
                            .append(index.getAndIncrement())
                            .append(". TimeTable = ")
                            .append(telegramUser.getText(description, "message"))
                            .append(", price = ")
                            .append(price);
                });
        telegramUser.setTelegramState(TelegramState.SEE_PERSONAL_COURSES);
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(), string.toString());
        sendMessage.replyMarkup(new ReplyKeyboardMarkup(telegramUser.getText(BotConstant.BACK, "message")));
        SendResponse resp = telegramBot.execute(sendMessage);
        telegramUser.getDeletingMessages().add(resp.message().messageId());
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public void acceptCourseShowCourseTypes(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        clearMessages(telegramUser);
        String data = callbackQuery.data();
        UUID courseId = UUID.fromString(data);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("course topilmadi"));
        telegramUser.setSelectedCourse(course);
        showCourseTypes(telegramUser);
    }

    private void showCourseTypes(TelegramUser telegramUser) {
        Course selectedCourse = telegramUser.getSelectedCourse();
        telegramUser.setSelectedCourseType(null);
        List<CourseType> courseTypes = courseTypeRepository.findAllByCourse(selectedCourse);
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(),
                telegramUser.getText(BotConstant.SELECT_COURSE_TYPE, "message")
                        .formatted(telegramUser.getText(selectedCourse.getName(), "message")));
        sendMessage.replyMarkup(botUtils.generateCourseTypeBtn(courseTypes, telegramUser));
        telegramUser.setTelegramState(TelegramState.SELECT_COURSE_TYPE);
        SendResponse resp = telegramBot.execute(sendMessage);
        telegramUser.getDeletingMessages().add(resp.message().messageId());
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public void backFromCoursesToMenu(TelegramUser telegramUser) {
        clearMessages(telegramUser);
        goToMenu(telegramUser);
    }

    @Override
    public void backFromCourseTypesToAllCourses(TelegramUser telegramUser) {
        clearMessages(telegramUser);
        goToAllCourses(telegramUser);
    }

    @Override
    public void acceptCourseTypeShowTimetable(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        clearMessages(telegramUser);
        String data = callbackQuery.data();
        UUID courseTypeId = UUID.fromString(data);
        CourseType courseType = courseTypeRepository.findById(courseTypeId)
                .orElseThrow(() -> new RuntimeException("courseType topilmadi"));
        telegramUser.setSelectedCourseType(courseType);
        Map<TimeTable, Integer> timePrice = courseType.getTimePrice();
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(), telegramUser.getText(BotConstant.SELECT_TIMETABLE, "message"));
        sendMessage.replyMarkup(botUtils.generateTimetableBtns(telegramUser, timePrice));
        telegramUser.setTelegramState(TelegramState.SELECT_TIMETABLE);
        SendResponse resp = telegramBot.execute(sendMessage);
        telegramUser.getDeletingMessages().add(resp.message().messageId());
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public void backFromTimetableToCourseTypes(TelegramUser telegramUser) {
        clearMessages(telegramUser);
        showCourseTypes(telegramUser);
    }

    @Override
    public void acceptTimetableAskVisitDay(TelegramUser telegramUser, String data) {
        String[] strings = data.split(",");
        TimeTable timeTable = timeTableRepository.findById(Integer.parseInt(strings[0]))
                .orElseThrow(() -> new RuntimeException("time table topilmadi"));
//        telegramUser.getSelectedCourseType().setTimePrice(Map.of(timeTable, Integer.parseInt(strings[1])));
        StringJoiner joiner = new StringJoiner("\n");

        joiner.add("Kun tanlang");
        joiner.add("❌ - eski sana");
        joiner.add("🟩 - bayram");
        joiner.add("Iltimos yuqoridagi belgilar bor tugmani bosmang");
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(), joiner.toString());
        sendMessage.replyMarkup(botUtils.generateCalendar(telegramUser));
        telegramUser.setTelegramState(TelegramState.SELECT_VISIT_DAY);
        telegramUserRepository.save(telegramUser);
        telegramBot.execute(sendMessage);
    }


    private void goToAllCourses(TelegramUser telegramUser) {
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(), telegramUser.getText(BotConstant.ALL_COURSES, "message"));
        sendMessage.replyMarkup(botUtils.generateCoursesBtns(telegramUser));
        telegramUser.setTelegramState(TelegramState.SELECT_COURSE);
        SendResponse resp = telegramBot.execute(sendMessage);
        telegramUser.getDeletingMessages().add(resp.message().messageId());
        telegramUserRepository.save(telegramUser);
    }

}
