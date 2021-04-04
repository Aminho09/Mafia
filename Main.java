import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int counter = 0;
        String[] playerNames = {""};
        Players[] players = new Players[100];
        boolean create_gameFlag = false;
        boolean start_gameFlag = false;
        int day = 1, night = 1;
        boolean isAssignedJoker = false;
        while (scanner.hasNext()) {
            String status = scanner.next();

            if (status.equals("create_game")) {
                if (create_gameFlag)
                    System.out.println("game has already created");
                else {
                    String playerNamesString = scanner.nextLine();
                    playerNames = playerNamesString.split(" ");
                    players = new Players[playerNames.length - 1];
                    create_gameFlag = true;
                }
            }

            if (status.equals("assign_role")) {
                if (create_gameFlag) {
                    String name = scanner.next();
                    boolean isValidName = false;
                    for (String s : playerNames) {
                        if (name.equals(s)) {
                            isValidName = true;
                            String role = scanner.next();
                            switch (role) {
                                case "villager":
                                    players[counter++] = new Villager(name, "villager");
                                    break;
                                case "bulletproof":
                                    players[counter++] = new Bulletproof(name, "bulletproof");
                                    break;
                                case "doctor":
                                    players[counter++] = new Doctor(name, "doctor");
                                    break;
                                case "detective":
                                    players[counter++] = new Detective(name, "detective");
                                    break;
                                case "silencer":
                                    players[counter++] = new Silencer(name, "silencer");
                                    break;
                                case "Joker": {
                                    if (!isAssignedJoker) {
                                        players[counter++] = new Joker(name, "Joker");
                                        isAssignedJoker = true;
                                    } else {
                                        System.out.println("Joker has been assigned");
                                    }
                                    break;
                                }
                                case "mafia":
                                    players[counter++] = new Mafia(name, "mafia");
                                    break;
                                case "godfather":
                                    players[counter++] = new Godfather(name, "godfather");
                                    break;
                                default:
                                    System.out.println("role not found");
                            }
                        }
                    }
                    if (!isValidName)
                        System.out.println("user not found");
                } else
                    System.out.println("no game created");
            }

            Start:
            if (status.equals("start_game")) {
                if (!create_gameFlag) {
                    System.out.println("no game created");
                    break Start;
                }
                else {
                    for (int i = 0; i < players.length; i++) {
                        if (players[i] == null) {
                            System.out.println("one or more player do not have a role");
                            break Start;
                        }
                    }
                }
                for (Players p :
                        players) {
                    System.out.printf("%s: %s\n", p.playerName, p.playerRole);
                }
                System.out.println("Ready? Set! Go.");
                start_gameFlag = true;
            }

            if (start_gameFlag) {
                while (true) {
                    WinnerCheck(players);
                    int[] numberOfSuspects = new int[players.length];
                    for (int i = 0; i < players.length; i++) {
                        numberOfSuspects[i] = 0;
                    }
                    boolean isInvalidName = true;
                    System.out.printf("Day %d\n", day);
                    String voter_name, votee_name;
                    while (true) {
                        voter_name = scanner.next();
                        if (voter_name.equals("start_game")) {
                            System.out.println("game has already started");
                            continue;
                        }
                        if (voter_name.equals("get_game_state")){
                            getStateGame(players);
                            continue;
                        }
                        if (voter_name.equals("end_vote")) {
                            shomareshRay(players, numberOfSuspects);
                            day++;
                            break;
                        }
                        votee_name = scanner.next();
                        if (votee_name.equals("get_game_state")){
                            getStateGame(players);
                            continue;
                        }
                        if (votee_name.equals("start_game")) {
                            System.out.println("game has already started");
                            continue;
                        }
                        for (Players p : players) {
                            if (p.playerName.equals(voter_name)) {
                                if (p.silenceStatus) {
                                    System.out.println("voter is silenced");
                                    isInvalidName = false;
                                } else if (!p.aliveStatus) {
                                    System.out.println("voter has already dead");
                                    isInvalidName = false;
                                } else {
                                    for (int j = 0; j < players.length; j++) {
                                        if (players[j].playerName.equals(votee_name)) {
                                            if (!players[j].aliveStatus) {
                                                System.out.println("votee already dead");
                                                isInvalidName = false;
                                            } else {
                                                isInvalidName = false;
                                                numberOfSuspects[j]++;
                                            }
                                            break;
                                        }
                                    }

                                }
                            }
                        }
                        if (isInvalidName) {
                            System.out.println("user not found");
                        }
                    }
                    WinnerCheck(players);
                    int[] mafia_votes = new int[players.length];
                    for (int i = 0; i < players.length; i++) {
                        mafia_votes[i] = 0;
                    }
                    System.out.printf("Night %d\n", night);
                    for (int i = 0; i < players.length; i++) {
                        if (players[i].aliveStatus && !players[i].playerRole.equals("villager"))
                            System.out.printf("%s: %s\n", players[i].playerName, players[i].playerRole);
                    }
                    boolean silencerDutyFlag = true;
                    boolean doctorSubmit = false;
                    int maxMafiaVote = 0;
                    String savedPlayer = null;
                    while (true) {
                        voter_name = scanner.next();
                        if (voter_name.equals("start_game")) {
                            System.out.println("game has already started");
                            continue;
                        }
                        if (voter_name.equals("get_game_state")){
                            getStateGame(players);
                            continue;
                        }
                        if (voter_name.equals("end_night")) {
                            for (int max_vote: mafia_votes) {
                                if (max_vote > maxMafiaVote){
                                    maxMafiaVote = max_vote;
                                }
                            }
                            shomareshRayMafia(players, mafia_votes);
                            for (int i =0; i < players.length; i++) {
                                if (players[i].playerName.equals(savedPlayer)){
                                    mafia_votes[i] = 0;
                                    players[i].aliveStatus = true;
                                    doctorSubmit = true;
                                    System.out.println("Doctor saved " + savedPlayer);
                                    break;
                                }
                            }
                            for (int i = 0; i < mafia_votes.length; i++) {
                                if ((mafia_votes[i] == maxMafiaVote && doctorSubmit) && maxMafiaVote != 0){
                                    shomareshRayMafia(players, mafia_votes);
                                    break;
                                }
                            }
                            for (int i = 0; i < players.length; i++){
                                if (!players[i].aliveStatus){
                                    System.out.println(players[i].playerName + " was killed");
                                    break;
                                }
                            }
                            for (Players p : players) {
                                if (p.silenceStatus) {
                                    System.out.println("Silenced " + p);
                                    break;
                                }
                            }
                            night++;
                            break;
                        }
                        votee_name = scanner.next();
                        if (votee_name.equals("get_game_state")){
                            getStateGame(players);
                            continue;
                        }
                        if (votee_name.equals("start_game")) {
                            System.out.println("game has already started");
                            continue;
                        }
                        // ********
                        boolean wasFoundPlayer1 = false;
                        boolean wasFoundPlayer2 = false;
                        // ********
                        for (Players p : players) {
                            if (p.playerName.equals(voter_name)) {
                                wasFoundPlayer1 = true;
                                if (!p.aliveStatus) {
                                    System.out.println("user is dead");
                                    break;
                                }
                                else {
                                    for (int i = 0; i < players.length; i++) {
                                        if (players[i].playerName.equals(votee_name)){
                                            wasFoundPlayer2 = true;
                                            if (!players[i].aliveStatus){
                                                System.out.println("votee already dead");
                                            }
                                            else {
                                                if (p instanceof Detective) {
                                                    if (((Detective) p).inquiryStatus)
                                                        ((Detective) p).Inquiry(players[i]);
                                                    else
                                                        System.out.println("detective has already asked");
                                                }
                                                if (p instanceof Silencer) {
                                                    if (silencerDutyFlag) {
                                                        ((Silencer) p).Silent(players[i]);
                                                        silencerDutyFlag = false;
                                                    } else {
                                                        if (!(players[i] instanceof Mafia)) {
                                                            mafia_votes[i]++;
                                                        }
                                                        else
                                                            System.out.println(players[i].playerName + " is mafia -__-");
                                                    }
                                                }
                                                if (p instanceof Doctor) {
                                                    savedPlayer = players[i].playerName;
                                                }
                                                if (p instanceof Mafia && !(p instanceof Silencer)){
                                                    if (!(players[i] instanceof Mafia))
                                                        mafia_votes[i]++;
                                                    else
                                                        System.out.println(players[i].playerName + " is mafia -__-");
                                                }
                                                if (p instanceof Villager && !(p instanceof Detective) && !(p instanceof Doctor) && !(p instanceof Bulletproof)){
                                                    System.out.println("user can not wake up during night");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //*********
                        if (!wasFoundPlayer1 || !wasFoundPlayer2){
                            System.out.println("user not joined");
                            continue;
                        }
                        //*********
                    }
                }
            }
        }
    }
    public static void shomareshRay(Players[] players,int[] numberOfSuspects){
        int temp = 0;
        int tempIndex = 0;
        for (int j = 0; j < players.length; j++) {
            if (temp < numberOfSuspects[j]) {
                temp = numberOfSuspects[j];
                tempIndex = j;
            }
        }
        if (temp == 0){
            System.out.println("nobody died");
        }
        else {
            for (int i = 0; i < numberOfSuspects.length; i++){
                if (temp == numberOfSuspects[i] && tempIndex != i) {
                    System.out.println("nobody died");
                    break;
                }
                else if (temp != numberOfSuspects[i]){
                    players[tempIndex].aliveStatus = false;
                    System.out.printf("%s died\n", players[tempIndex].playerName);
                    break;
                }
            }
        }
    }
    public static void shomareshRayMafia(Players[] players,int[] mafiaVotes){
        int temp = 0;
        int tempIndex = 0;
        for (int j = 0; j < players.length; j++) {
            if (temp < mafiaVotes[j]) {
                temp = mafiaVotes[j];
                tempIndex = j;
            }
        }
        if (temp == 0){
            System.out.println("nobody died");
        }
        else {
            for (int i = 0; i < mafiaVotes.length; i++){
                if (temp == mafiaVotes[i] && tempIndex != i) {
                        System.out.println("nobody died (tied in mafia's voting)");
                        break;
                }
                else if (temp != mafiaVotes[i]){
                    Mafia.Kill(players[tempIndex]);
                    if (!(players[tempIndex] instanceof Bulletproof)) {
                        System.out.printf("mafia tried to kill %s\n", players[tempIndex].playerName);
                    }
                    else {
                        System.out.println("nobody died");
                        players[tempIndex] = BulletproofToVillager(players[tempIndex]);
                    }
                    break;
                }
            }
        }
    }
    public static void WinnerCheck (Players[] player){
        int counterVillagers = 0, counterMafias = 0;
        for (int i = 0; i < player.length; i++){
            if (player[i] instanceof Joker)
                if (!player[i].aliveStatus) {
                    System.out.println("Joker won!");
                    System.exit(0);
                }
            if(player[i] instanceof Mafia) {
                    if (player[i].aliveStatus)
                        counterMafias++;
                }
            if (player[i] instanceof Villager) {
                    if (player[i].aliveStatus)
                        counterVillagers++;
                }
        }
        if (counterMafias == 0){
            System.out.println("Villager won!");
            System.exit(0);
        }
         if (counterMafias >= counterVillagers) {
            System.out.println("Mafia won!");
            System.exit(0);
        }
    }
    public static void getStateGame(Players[] players){
        int mafiaCounter = 0;
        int villagerCounter = 0;
        for (Players p: players) {
            if (p instanceof Mafia)
                mafiaCounter++;
            if (p instanceof Villager)
                villagerCounter++;
        }
        System.out.println("Mafia = " + mafiaCounter);
        System.out.println("Villager = " + villagerCounter);
    }
    public static Players BulletproofToVillager(Players player){
        String name = player.playerName;
        player = new Villager(name, "villager");
        return player;
    }
}