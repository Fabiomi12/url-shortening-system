package com.example.url.shortener.service.redirect;

import com.example.url.shortener.exception.ForbiddenUserActionException;
import com.example.url.shortener.service.shortener.UrlShortenerService;
import com.example.url.shortener.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlRedirectServiceImpl implements UrlRedirectService {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final UrlShortenerService urlShortenerService;

    @Override
    @SneakyThrows
    public void redirect(String hash, HttpServletRequest request, HttpServletResponse response) {
        var currentUserId = RequestUtils.getCurrentAuthentication().getName();
        var entity = urlShortenerService.findEntityByHash(hash);
        if (!entity.getUserId().equals(currentUserId)) {
            throw new ForbiddenUserActionException("User cannot perform this action.");
        }
        urlShortenerService.incrementClickCount(entity.getId());
        redirectStrategy.sendRedirect(request, response, entity.getOriginalUrl());
    }
}
