package uz.pdp.london_school.controller;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.i18n.BotConstant;
import uz.pdp.london_school.bot.BotServiceImpl;
import uz.pdp.london_school.entity.TelegramUser;
import uz.pdp.london_school.enums.TelegramState;

@Service
@RequiredArgsConstructor
public class UserController {

    private final BotServiceImpl botService;

    //Message
    public void messages(TelegramUser telegramUser, Message message) {
        if (message.text() != null) {
            String text = message.text();
            if (text.equals("/start")) {
                botService.acceptStartAskLang(telegramUser);
            } else if (telegramUser.checkState(TelegramState.SELECT_COMMAND)) {
                if (text.equals(telegramUser.getText(BotConstant.ALL_COURSES, "message"))) {
                    botService.acceptMenuShowAllCourses(telegramUser);
                } else if (text.equals(telegramUser.getText(BotConstant.MY_COURSES, "message"))) {
                    botService.acceptMenuShowCoursesOfUser(telegramUser);
                }
            }
        } else if (message.contact() != null) {
            if (telegramUser.checkState(TelegramState.SHARE_CONTACT)) {
                botService.acceptContactShowUserMenu(telegramUser, message);
            }
        }
    }

    //CallBackQuery
    public void callbackQueries(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        if (telegramUser.checkState(TelegramState.SELECT_LANG)) {
            botService.acceptLangAskContact(telegramUser, callbackQuery);
        } else if (telegramUser.checkState(TelegramState.SELECT_COURSE)) {
            String data = callbackQuery.data();
            if (data.equals("back")) {
                botService.backFromCoursesToMenu(telegramUser);
                return;
            }
            botService.acceptCourseShowCourseTypes(telegramUser, callbackQuery);
        } else if (telegramUser.checkState(TelegramState.SELECT_COURSE_TYPE)) {
            String data = callbackQuery.data();
            if (data.equals("back")) {
                botService.backFromCourseTypesToAllCourses(telegramUser);
                return;
            }
            botService.acceptCourseTypeShowTimetable(telegramUser, callbackQuery);
        } else if (telegramUser.checkState(TelegramState.SELECT_TIMETABLE)) {
            String data = callbackQuery.data();
            if (data.equals("back")) {
                botService.backFromTimetableToCourseTypes(telegramUser);
            } else {
                botService.acceptTimetableAskVisitDay(telegramUser, data);
            }
        }
    }

}
