package uz.pdp.london_school.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import uz.pdp.london_school.entity.TelegramUser;

public interface SuperAdminService {

    void acceptLangShowSuperAdminMenu(TelegramUser telegramUser, CallbackQuery callbackQuery);
}
