package kr.leedox.repository;

import kr.leedox.entity.SiteMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiteMessageRepository extends JpaRepository<SiteMessage, Integer> {
    Optional<SiteMessage> findByCodeAndLocale(String code, String locale);
}
