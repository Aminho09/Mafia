public class Detective extends Villager {

    boolean inquiryStatus = false;

    public Detective(String playerName, String playerRole) {
        super(playerName, playerRole);
    }

    public void Inquiry(Players player){
        if (player instanceof Mafia && !(player instanceof Godfather))
            System.out.println("Yes");
        else
            System.out.println("No");
        inquiryStatus = true;
    }
}
