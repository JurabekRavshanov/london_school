package uz.pdp.london_school.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import uz.pdp.london_school.entity.TelegramUser;

public interface BotService {
    TelegramUser getOrCreateTelegramUser(Long chatId);

    void acceptStartAskLang(TelegramUser telegramUser);

    void acceptLangAskContact(TelegramUser telegramUser, CallbackQuery callbackQuery);

    void acceptContactShowUserMenu(TelegramUser telegramUser, Message message);

    void acceptMenuShowAllCourses(TelegramUser telegramUser);

    void acceptMenuShowCoursesOfUser(TelegramUser telegramUser);

    void acceptCourseShowCourseTypes(TelegramUser telegramUser, CallbackQuery callbackQuery);

    void backFromCoursesToMenu(TelegramUser telegramUser);

    void backFromCourseTypesToAllCourses(TelegramUser telegramUser);

    void acceptCourseTypeShowTimetable(TelegramUser telegramUser, CallbackQuery callbackQuery);

    void backFromTimetableToCourseTypes(TelegramUser telegramUser);

    void acceptTimetableAskVisitDay(TelegramUser telegramUser, String data);
}
