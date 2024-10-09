package uz.pdp.london_school.controller;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.bot.BotServiceImpl;
import uz.pdp.london_school.entity.TelegramUser;
import uz.pdp.london_school.enums.TelegramState;
import uz.pdp.london_school.service.SuperAdminService;

@Service
@RequiredArgsConstructor
public class SuperAdminController {

    private final BotServiceImpl botService;
    private final SuperAdminService superAdminService;

    public void messages(TelegramUser telegramUser, Message message) {
        if (message.text() != null) {
            String text = message.text();
            if (text.equals("/start")) {
                botService.acceptStartAskLang(telegramUser);
            }
        }
    }

    public void callbackQueries(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        if (telegramUser.checkState(TelegramState.SELECT_LANG)) {
            superAdminService.acceptLangShowSuperAdminMenu(telegramUser, callbackQuery);
        }
    }


}
