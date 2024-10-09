package uz.pdp.london_school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.london_school.entity.TelegramUser;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

}