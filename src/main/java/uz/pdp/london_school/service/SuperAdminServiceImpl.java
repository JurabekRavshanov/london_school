package uz.pdp.london_school.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.london_school.i18n.BotConstant;
import uz.pdp.london_school.bot.BotUtils;
import uz.pdp.london_school.entity.TelegramUser;
import uz.pdp.london_school.enums.Language;
import uz.pdp.london_school.enums.TelegramState;
import uz.pdp.london_school.repo.TelegramUserRepository;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final TelegramBot telegramBot;
    private final TelegramUserRepository telegramUserRepository;
    private final BotUtils botUtils;


    @Override
    public void acceptLangShowSuperAdminMenu(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        Language language = Language.valueOf(callbackQuery.data());
        telegramUser.setLanguage(language);
        telegramUser.setLastname(callbackQuery.from().lastName());
        telegramUser.setFirstname(callbackQuery.from().firstName());
        SendMessage sendMessage = new SendMessage(telegramUser.getChatId(),
                telegramUser.getText(BotConstant.WELCOME_SUPERADMIN,"message").formatted(telegramUser.getFullName()));
        telegramUser.setTelegramState(TelegramState.SELECT_COMMAND);
        telegramUserRepository.save(telegramUser);
        sendMessage.replyMarkup(botUtils.generateSuperAdminMenu(telegramUser));
        telegramBot.execute(sendMessage);
    }
}
