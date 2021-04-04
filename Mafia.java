public class Mafia extends Players{

    public static void Kill(Players player){
        if (!(player instanceof Mafia)){
            if (!(player instanceof Bulletproof)) {
                player.aliveStatus = false;
            }
        }
    }

    public Mafia(String playerName, String playerRole) {
        super(playerName, playerRole);
    }

}
