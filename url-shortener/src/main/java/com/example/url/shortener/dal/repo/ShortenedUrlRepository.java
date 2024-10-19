package com.example.url.shortener.dal.repo;

import com.example.url.shortener.dal.entity.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE shortened_url SET click_count = click_count + 1 WHERE id = :id", nativeQuery = true)
    void incrementClickCount(long id);
    Collection<ShortenedUrl> findAllByUserId(String userId);
    Optional<ShortenedUrl> findByHash(String hash);
    Optional<ShortenedUrl> findByHashAndUserId(String hash, String userId);
    Optional<ShortenedUrl> findByOriginalUrl(String url);
}
