package kt.tripsync.session;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManagerImpl implements SessionManager {

    private static final Map<String, Long> sessionStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        sessionStore.put("TEST_SESSION", 1L);
    }

    @Override
    public String createSession(Long userId) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, userId);

        return sessionId;
    }

    @Override
    public Optional<Long> getUidBySessionId(String sessionId) {
        return Optional.ofNullable(sessionStore.get(sessionId));
    }

    @Override
    public void expireSession(String sessionId) {
        sessionStore.remove(sessionId);
    }
}
