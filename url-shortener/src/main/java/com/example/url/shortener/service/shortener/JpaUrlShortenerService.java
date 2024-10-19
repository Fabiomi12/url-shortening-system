package com.example.url.shortener.service.shortener;

import com.example.url.shortener.config.ApplicationProperties;
import com.example.url.shortener.dal.entity.ShortenedUrl;
import com.example.url.shortener.dal.repo.ShortenedUrlRepository;
import com.example.url.shortener.dto.ShortenedUrlGetDto;
import com.example.url.shortener.dto.ShortenedUrlPostDto;
import com.example.url.shortener.exception.ForbiddenUserActionException;
import com.example.url.shortener.exception.IncorrectUrlException;
import com.example.url.shortener.exception.UrlAlreadyExistsException;
import com.example.url.shortener.exception.UrlNotFoundException;
import com.example.url.shortener.mapper.ShortenedUrlMapper;
import com.example.url.shortener.quartz.UrlDeletionScheduler;
import com.example.url.shortener.util.RequestUtils;
import com.example.url.shortener.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collection;

import static com.example.url.shortener.util.Constants.SHORT_URL_PREFIX;

@Service
@RequiredArgsConstructor
public class JpaUrlShortenerService implements UrlShortenerService {

    private final ShortenedUrlRepository repository;
    private final ShortenedUrlMapper mapper;
    private final ApplicationProperties applicationProperties;
    private final UrlDeletionScheduler urlDeletionScheduler;

    @Override
    @SneakyThrows
    public ShortenedUrlGetDto save(ShortenedUrlPostDto dto) {
        if (!UrlValidator.isValidUrl(dto.getOriginalUrl())) {
            throw new IncorrectUrlException();
        }
        var existingUrl = repository.findByOriginalUrl(dto.getOriginalUrl());
        if (existingUrl.isPresent()) {
            throw new UrlAlreadyExistsException();
        }
        var entity = mapper.fromDto(dto);
        var savedEntity = repository.save(entity);
        savedEntity.setHash(Base64.getUrlEncoder().withoutPadding().encodeToString(entity.getId().toString().getBytes()));
        repository.save(savedEntity);
        urlDeletionScheduler.scheduleUrlDeletion(entity.getId(), entity.getExpiresAt());
        return mapper.toDto(savedEntity);
    }

    @Override
    public Collection<ShortenedUrlGetDto> findAllCurrentUser() {
        var currentUserId = RequestUtils.getCurrentAuthentication().getName();
        return repository.findAllByUserId(currentUserId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ShortenedUrl findEntityByHash(String hash) {
        return repository.findByHash(hash).orElseThrow(() -> new UrlNotFoundException(hash));
    }

    @Override
    public ShortenedUrlGetDto findByShortUrlForCurrentUser(String shortUrl) {
        var currentUserId = RequestUtils.getCurrentAuthentication().getName();
        var hash = shortUrl.replace(applicationProperties.getApplicationUrl() + SHORT_URL_PREFIX, "");
        var entity = findEntityByHash(hash);
        if (!entity.getUserId().equals(currentUserId))
            throw new ForbiddenUserActionException("User '" + currentUserId + "' has no access to perform this action." );
        return mapper.toDto(entity);
    }

    @Override
    public void incrementClickCount(long id) {
        repository.incrementClickCount(id);
    }
}
