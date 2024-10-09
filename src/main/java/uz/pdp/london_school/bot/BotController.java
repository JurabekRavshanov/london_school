package uz.pdp.london_school.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.controller.AdminController;
import uz.pdp.london_school.controller.SuperAdminController;
import uz.pdp.london_school.controller.UserController;
import uz.pdp.london_school.enums.Role;
import uz.pdp.london_school.entity.TelegramUser;

@Service
@RequiredArgsConstructor
public class BotController {
    private final BotService botService;
    private final SuperAdminController superAdminController;
    private final AdminController adminController;
    private final UserController userController;

    @Async
    public void handle(Update update) {

        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }
    }

    private void handleMessage(Message message) {
        Long chatId = message.from().id();
        TelegramUser telegramUser = botService.getOrCreateTelegramUser(chatId);
        Role role = telegramUser.getRole();

        switch (role) {
            case SUPER_ADMIN -> superAdminController.messages(telegramUser, message);
            case ADMIN -> adminController.messages(telegramUser, message);
            case USER -> userController.messages(telegramUser, message);
        }
    }


    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.from().id();
        TelegramUser telegramUser = botService.getOrCreateTelegramUser(chatId);
        Role role = telegramUser.getRole();
        switch (role){
            case SUPER_ADMIN -> superAdminController.callbackQueries(telegramUser,callbackQuery);
            case ADMIN -> adminController.callbackQueries(telegramUser,callbackQuery);
            case USER -> userController.callbackQueries(telegramUser,callbackQuery);
        }

    }
}




