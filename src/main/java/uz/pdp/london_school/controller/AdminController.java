package uz.pdp.london_school.controller;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.entity.TelegramUser;

@Service
@RequiredArgsConstructor
public class AdminController {
    public void messages(TelegramUser telegramUser, Message message) {

    }

    public void callbackQueries(TelegramUser telegramUser, CallbackQuery callbackQuery) {

    }


}
