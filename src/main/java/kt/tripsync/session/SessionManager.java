package kt.tripsync.session;

import java.util.Optional;

public interface SessionManager {

    public String createSession(Long userId);

    public Optional<Long> getUidBySessionId(String sessionId);

    public void expireSession(String sessionId);
}
