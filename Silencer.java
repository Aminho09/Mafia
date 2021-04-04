public class Silencer extends Mafia{

    public Silencer(String playerName, String playerRole) {
        super(playerName, playerRole);
    }

    public void Silent(Players player){
        player.silenceStatus = true;
    }
}
