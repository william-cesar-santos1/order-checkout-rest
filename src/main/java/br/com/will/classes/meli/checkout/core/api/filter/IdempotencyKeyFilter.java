package br.com.will.classes.meli.checkout.core.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotencyKeyFilter extends OncePerRequestFilter {

    private static final String HEADER = "Idempotency-Key";
    private static final Duration TTL = Duration.ofMinutes(5);
    private static final int SC_UNPROCESSABLE_ENTITY = 422;

    private final Map<String, EntradaIdempotente> cache = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String key = request.getHeader(HEADER);
        if (key == null || !"POST".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        String hash = sha256(body);

        EntradaIdempotente existente = cache.get(key);
        if (existente != null && existente.expirado()) {
            cache.remove(key);
            existente = null;
        }

        if (existente != null) {
            if (!existente.hashCorpo().equals(hash)) {
                response.setStatus(SC_UNPROCESSABLE_ENTITY);
                response.setContentType("application/problem+json");
                response.getWriter().write("""
                        {"title":"Idempotency-Key reutilizada com corpo diferente","status":422}""");
                return;
            }
            // Mesma chave, mesmo corpo: devolve resposta original.
            response.setStatus(existente.status());
            response.setContentType(existente.contentType());
            response.getWriter().write(existente.corpoResposta());
            return;
        }

        CapturaResponse wrapper = new CapturaResponse(response);
        chain.doFilter(new CorpoRecuperavelRequest(request, body), wrapper);

        if (wrapper.getStatus() < 500) {
            cache.put(key, new EntradaIdempotente(
                    hash,
                    wrapper.getStatus(),
                    wrapper.getContentType() != null ? wrapper.getContentType() : "application/json",
                    wrapper.getCapturado(),
                    Instant.now().plus(TTL)));
        }
    }

    private static String sha256(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

}