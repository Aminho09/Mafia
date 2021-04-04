public class Players {
    protected final String playerRole;
    protected final String playerName;
    protected boolean aliveStatus = true;
    protected boolean silenceStatus = false;

    public Players(String playerName, String playerRole) {
        this.playerRole = playerRole;
        this.playerName = playerName;
    }

    public boolean isSilenceStatus() {
        return silenceStatus;
    }

    public boolean isAliveStatus() {
        return aliveStatus;
    }
}
