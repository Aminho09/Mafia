import java.util.Scanner;
public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BRIGHT_BLACK = "\u001B[90m";
    public static final String ANSI_BRIGHT_RED = "\u001B[91m";
    public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
    public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";


    public static int tempIndex = 0;
    public static String saved = "";
    public static boolean killed = false;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Introduction();
        int counter = 0;
        String[] playerNames = {""};
        Players[] players = new Players[100];
        boolean create_gameFlag = false;
        boolean numCheck = false;
        boolean start_gameFlag = false;
        int day = 1, night = 1;
        boolean isAssignedJoker = false;
        boolean savedByDoctor = false;
        while (scanner.hasNext()) {
            String status = scanner.next();

            if (status.equals("create_game")) {
                if (create_gameFlag)
                    System.out.println(ANSI_BRIGHT_YELLOW + "game has already created" + ANSI_RESET);
                else {
                    String playerNamesString = scanner.nextLine();
                    playerNames = playerNamesString.split(" ");
                    players = new Players[playerNames.length - 1];
                    create_gameFlag = true;
                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
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
                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    break;
                                case "bulletproof":
                                    players[counter++] = new Bulletproof(name, "bulletproof");
                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    break;
                                case "doctor":
                                    players[counter++] = new Doctor(name, "doctor");
                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    break;
                                case "detective":
                                    players[counter++] = new Detective(name, "detective");
                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    break;
                                case "silencer":
                                    players[counter++] = new Silencer(name, "silencer");
                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    break;
                                case "Joker": {
                                    if (!isAssignedJoker) {
                                        players[counter++] = new Joker(name, "Joker");
                                        isAssignedJoker = true;
                                        System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    } else {
                                        System.out.println(ANSI_BRIGHT_YELLOW + "Joker has been assigned"+ ANSI_RESET);
                                    }
                                    break;
                                }
                                case "mafia":
                                    players[counter++] = new Mafia(name, "mafia");
                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    break;
                                case "godfather":
                                    players[counter++] = new Godfather(name, "godfather");
                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                    break;
                                default:
                                    System.out.println(ANSI_BRIGHT_YELLOW + "role not found" + ANSI_RESET);
                            }
                        }
                    }
                    if (!isValidName)
                        System.out.println(ANSI_BRIGHT_YELLOW + "user not found" + ANSI_RESET);
                } else
                    System.out.println(ANSI_BRIGHT_YELLOW + "no game created" + ANSI_RESET);
            }
            int num = 0;
            for (Players p :
                    players) {
                if (p != null) {
                    num++;
                }
            }
            if (num == players.length && !numCheck){
                numCheck = true;
                System.out.println(ANSI_BRIGHT_WHITE + "press'start_game'" + ANSI_RESET);
            }
            Start:
            if (status.equals("start_game")) {
                if (!create_gameFlag) {
                    System.out.println(ANSI_BRIGHT_YELLOW + "no game created" + ANSI_RESET);
                    break Start;
                }
                else {
                    for (int i = 0; i < players.length; i++) {
                        if (players[i] == null) {
                            System.out.println(ANSI_BRIGHT_YELLOW +
                                    "one or more player do not have a role" + ANSI_RESET);
                            break Start;
                        }
                    }
                }
                for (Players p :
                        players) {
                    System.out.printf("%s%s: %s%s\n", ANSI_GREEN, p.playerName, p.playerRole, ANSI_RESET);
                }
                System.out.println(ANSI_BRIGHT_BLUE + "Ready? Set! Go." + ANSI_RESET);
                start_gameFlag = true;
            }
            if (!status.equals("create_game") && !status.equals("assign_role") && !status.equals("start_game")){
                System.out.println(ANSI_BRIGHT_YELLOW + "Invalid command" + ANSI_RESET);
            }

            if (start_gameFlag) {
                while (true) {
                    WinnerCheck(players);
                    int[] numberOfSuspects = new int[players.length];
                    for (int i = 0; i < players.length; i++) {
                        numberOfSuspects[i] = 0;
                    }
                    boolean isInvalidName = true;
                    System.out.printf("%sDay %d %s\n",ANSI_BRIGHT_BLUE, day, ANSI_RESET);
                    String voter_name, votee_name;
                    for (Players p :
                            players) {
                        p.isVoted = false;
                    }
                    while (true) {
                        voter_name = scanner.next();
                        if (voter_name.equals("start_game")) {
                            System.out.println(ANSI_BRIGHT_YELLOW + "game has already started" + ANSI_RESET);
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
                            System.out.println(ANSI_BRIGHT_YELLOW + "game has already started" + ANSI_RESET);
                            continue;
                        }
                        for (Players p : players) {
                            if (p.playerName.equals(voter_name)) {
                                if (p.silenceStatus) {
                                    System.out.println(ANSI_BRIGHT_YELLOW + "voter is silenced" + ANSI_RESET);
                                    isInvalidName = false;
                                } else if (!p.aliveStatus) {
                                    System.out.println(ANSI_BRIGHT_YELLOW + "voter has already dead" + ANSI_RESET);
                                    isInvalidName = false;
                                } else {
                                    for (int j = 0; j < players.length; j++) {
                                        if (players[j].playerName.equals(votee_name)) {
                                            if (!players[j].aliveStatus) {
                                                System.out.println("votee already dead");
                                                isInvalidName = false;
                                            } else {
                                                if (!p.isVoted) {
                                                    isInvalidName = false;
                                                    numberOfSuspects[j]++;
                                                    p.isVoted = true;
                                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                                }
                                                else
                                                    System.out.println(ANSI_BRIGHT_YELLOW + p.playerName
                                                            + " has already voted" + ANSI_RESET);
                                            }
                                            break;
                                        }
                                    }

                                }
                            }
                        }
                        if (isInvalidName) {
                            System.out.println(ANSI_BRIGHT_YELLOW + "user not found" + ANSI_RESET);
                        }
                    }
                    WinnerCheck(players);
                    int[] mafia_votes = new int[players.length];
                    for (int i = 0; i < players.length; i++) {
                        mafia_votes[i] = 0;
                    }
                    System.out.printf("%sNight %d%s\n", ANSI_BLUE, night, ANSI_RESET);
                    for (int i = 0; i < players.length; i++) {
                        if (players[i].aliveStatus && !players[i].playerRole.equals("villager"))
                            System.out.printf("%s%s: %s%s\n", ANSI_BRIGHT_GREEN,
                                    players[i].playerName, players[i].playerRole, ANSI_RESET);
                    }
                    for (Players p: players) {
                        p.silenceStatus =false;
                    }
                    for (Players p: players) {
                        if (p instanceof Mafia)
                            ((Mafia) p).vote_to_kill = false;
                    }
                    boolean silencerDutyFlag = true;
                    boolean doctorSubmit = false;
                    int maxMafiaVote = 0;
                    String savedPlayer = null;
                    while (true) {
                        saved = "";
                        voter_name = scanner.next();
                        if (voter_name.equals("start_game")) {
                            System.out.println(ANSI_BRIGHT_YELLOW + "game has already started" + ANSI_RESET);
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
                                    System.out.println(ANSI_CYAN + "Doctor saved " + savedPlayer + ANSI_RESET);
                                    break;
                                }
                            }
                            for (int i = 0; i < mafia_votes.length; i++) {
                                if ((mafia_votes[i] == maxMafiaVote && doctorSubmit) && maxMafiaVote != 0){
                                    shomareshRayMafia(players, mafia_votes);
                                    break;
                                }
                            }
                            if (killed && !savedByDoctor) {
                                System.out.println(ANSI_RED +
                                        players[tempIndex].playerName + " was killed" + ANSI_RESET);
                                break;
                            }
                            for (Players p : players) {
                                if (p.silenceStatus) {
                                    System.out.println(ANSI_BRIGHT_BLACK + "Silenced " + p.playerName);
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
                            System.out.println(ANSI_BRIGHT_YELLOW + "game has already started" + ANSI_RESET);
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
                                    System.out.println(ANSI_BRIGHT_YELLOW + "user is dead" + ANSI_RESET);
                                    wasFoundPlayer2 = true;
                                    break;
                                }
                                else {
                                    for (int i = 0; i < players.length; i++) {
                                        if (players[i].playerName.equals(votee_name)){
                                            wasFoundPlayer2 = true;
                                            if (!players[i].aliveStatus){
                                                System.out.println(ANSI_BRIGHT_YELLOW
                                                        + "votee already dead" + ANSI_RESET);
                                            }
                                            else {
                                                if ((p instanceof Detective)) {
                                                    if (!((Detective) p).inquiryStatus) {
                                                        ((Detective) p).Inquiry(players[i]);
                                                        System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                                    }
                                                    else
                                                        System.out.println(ANSI_BRIGHT_YELLOW
                                                                + "detective has already asked" + ANSI_RESET);
                                                }
                                                if (p instanceof Silencer) {
                                                    if (silencerDutyFlag) {
                                                        ((Silencer) p).Silent(players[i]);
                                                        silencerDutyFlag = false;
                                                        System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                                    } else {
                                                        if (!(players[i] instanceof Mafia)) {
                                                            if (!((Silencer) p).vote_to_kill) {
                                                                mafia_votes[i]++;
                                                                System.out.println(ANSI_BRIGHT_BLUE
                                                                        + "done" + ANSI_RESET);
                                                            }
                                                            else
                                                                System.out.println(ANSI_BRIGHT_YELLOW + p.playerName +
                                                                        " has already voted to kill someone"
                                                                        + ANSI_RESET);
                                                        }
                                                        else
                                                            System.out.println(ANSI_BRIGHT_YELLOW +
                                                                    players[i].playerName + " is mafia 😑" + ANSI_RESET);
                                                    }
                                                }
                                                if (p instanceof Doctor) {
                                                    savedPlayer = players[i].playerName;
                                                    saved = savedPlayer;
                                                    System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                                    savedByDoctor = true;
                                                }
                                                if (p instanceof Mafia && !(p instanceof Silencer)){
                                                    if (!(players[i] instanceof Mafia))
                                                        if (!((Mafia) p).vote_to_kill) {
                                                            mafia_votes[i]++;
                                                            ((Mafia) p).vote_to_kill = true;
                                                            System.out.println(ANSI_BRIGHT_BLUE + "done" + ANSI_RESET);
                                                        }
                                                        else
                                                            System.out.println(ANSI_BRIGHT_YELLOW + p.playerName
                                                                    + " has already voted to kill someone" + ANSI_RESET);
                                                    else
                                                        System.out.println(ANSI_BRIGHT_YELLOW + players[i].playerName
                                                                + " is mafia 😑" + ANSI_RESET);
                                                }
                                                if ((p instanceof Villager && !(p instanceof Detective) &&
                                                        !(p instanceof Doctor) && !(p instanceof Bulletproof))
                                                        || p instanceof Joker){
                                                    System.out.println(ANSI_BRIGHT_YELLOW +
                                                            "user can not wake up during night" + ANSI_RESET);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (!wasFoundPlayer1 || !wasFoundPlayer2){
                            System.out.println(ANSI_BRIGHT_YELLOW + "user not joined" + ANSI_RESET);
                        }
                    }
                }
            }
        }
    }
    public static void shomareshRay(Players[] players,int[] numberOfSuspects){
        int temp = 0;
        int tempIndex = 0;
        for (int j = 0; j < players.length; j++) {
            if (temp <= numberOfSuspects[j]) {
                temp = numberOfSuspects[j];
                tempIndex = j;
            }
        }
        if (temp == 0){
            System.out.println(ANSI_BRIGHT_GREEN + "nobody died" + ANSI_RESET);
        }
        else {
            for (int i = 0; i < numberOfSuspects.length; i++){
                if (temp == numberOfSuspects[i] && tempIndex != i) {
                    System.out.println(ANSI_BRIGHT_GREEN + "nobody died" + ANSI_RESET);
                    break;
                }
                else if (temp != numberOfSuspects[i]){
                    players[tempIndex].aliveStatus = false;
                    System.out.printf("%s%s died%s\n", ANSI_RED, players[tempIndex].playerName, ANSI_RESET);
                    break;
                }
            }
        }
    }
    public static void shomareshRayMafia(Players[] players,int[] mafiaVotes){
        int temp = 0;
        tempIndex = 0;

        for (int j = 0; j < players.length; j++) {
            if (temp < mafiaVotes[j]) {
                temp = mafiaVotes[j];
                tempIndex = j;
            }
        }
        if (temp == 0){
            System.out.println(ANSI_BRIGHT_GREEN + "nobody died" + ANSI_RESET);
        }
        else {
            for (int j = 0; j < players.length; j++) {
                if (players[j].playerName.equals(saved))
                    mafiaVotes[j]--;
            }
            int summ = 0;
            for (int i = 0; i < mafiaVotes.length; i++) {
                if (temp == mafiaVotes[i])
                    summ++;
            }
            if (summ>1){
                System.out.println(ANSI_BRIGHT_GREEN + "nobody died (tied in mafia's voting)" + ANSI_RESET);
                return;
            }
            for (int i = 0; i < mafiaVotes.length; i++){
                if (temp == mafiaVotes[i] && tempIndex != i) {
                    System.out.println(ANSI_BRIGHT_GREEN + "nobody died (tied in mafia's voting)" + ANSI_RESET);
                    break;
                }
                else if (temp != mafiaVotes[i]){
                    Mafia.Kill(players[tempIndex]);
                    if (!(players[tempIndex] instanceof Bulletproof)) {
                        System.out.printf("%smafia tried to kill %s%s\n", ANSI_BRIGHT_RED, players[tempIndex].playerName, ANSI_RESET);
                        killed = true;
                    }
                    else {
                        System.out.println(ANSI_BRIGHT_GREEN + "nobody died" + ANSI_RESET);
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
                    System.out.println(ANSI_BRIGHT_PURPLE + "Joker won! 😈" + ANSI_RESET);
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
            System.out.println(ANSI_GREEN + "Villager won!" + ANSI_RESET);
            System.exit(0);
        }
        if (counterMafias >= counterVillagers) {
            System.out.println(ANSI_RED + "Mafia won!" + ANSI_RESET);
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
        System.out.println(ANSI_CYAN + "Mafia = " + mafiaCounter + ANSI_RESET);
        System.out.println(ANSI_CYAN + "Villager = " + villagerCounter + ANSI_RESET);
    }
    public static Players BulletproofToVillager(Players player){
        String name = player.playerName;
        player = new Villager(name, "villager");
        return player;
    }
    public static void Introduction() {
        System.out.println("Welcome to the game");
        System.out.println("Here's some commands:");
        System.out.println("command 'create_game player1 player2 ...' for make game and inform players");
        System.out.println("command 'assign_role' to assign the roles to the players");
        System.out.println("command 'start_game' to start the game");
        System.out.println("Good Luck!");
    }
}