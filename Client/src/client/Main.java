
package client;

import room.*;
import utilities.Bill;
import utilities.Login;
import utilities.Reservation;
import guest.Guest;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 8080)) {

            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String loginResponse = null;
            boolean loginState = false;
            int choice = 0;
            Room[] rooms = null;

            do {

                /***** Login Phase ******/

                do {
                    String[] tokens = getLoginInfo().clone();

                    Login admin = new Login(Integer.parseInt(tokens[0]), tokens[1]);
                    out.writeObject(admin);
                    out.flush();

                    loginResponse = input.readLine();
                    if (loginResponse.equals("true")) {
                        loginState = true;
                        System.out.println("Successful Login\n");

                    } else System.out.println("Login failed, Please Try Again \n");

                } while (!loginState);

                /***** Menu Phase ******/

                do {

                    displayMenu();
                    choice = menuChoice(1, 5);

                    switch (choice) {
                        case 1:
                            output.println(choice);
                            rooms = (Room[]) in.readObject();
                            Reservation reservation = bookRoom(rooms);
                            out.writeObject(reservation);
                            out.flush();
                            break;
                        case 2:
                            output.println(choice);
                            out.writeObject(billService((Reservation[]) in.readObject()));
                            out.flush();
                            break;
                        case 3:
                            output.println(choice);
                            currentBookings((Reservation[]) in.readObject());
                            break;
                        case 4:
                            output.println(choice);
                            rooms = (Room[]) in.readObject();
                            availableRooms(rooms);
                            break;
                    }

                    if (choice == 5) {
                        output.println(choice);
                        break;
                    }

                } while (true);

                if(input.readLine().equals("exit")) break;
            } while (true);

        } catch (IOException | ClassNotFoundException | ParseException e) {
            System.out.println("Error from client: " + e.getMessage());
        }

    }

    // randomizer
    static ThreadLocalRandom generator = ThreadLocalRandom.current();

    // verify loginID value()
    static int verifyLoginID() {

        int ID = -1;
        boolean valid = false;

        do {

            try {
                Scanner inputID = new Scanner(System.in);
                System.out.println("Please enter your Login ID: ");
                ID = inputID.nextInt();
                valid = true;

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input! \n");
            }

        } while (!valid);

        return ID;
    }

    // verify string Value() // cant contain any number values or symbols
    static boolean verifyString(String input) {
        return ((!input.equals("")) && (input.matches("^[a-zA-Z]*$")));
    }

    // verify phone value()
    static long verifyPhone() {

        boolean valid = false;
        long number = -1;

        do {
            try {
                Scanner input = new Scanner(System.in);
                number = input.nextLong();

                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input!, Please Enter Phone Number: ");
            }
        } while (!valid);

        return number;
    }

    // verify Guest Number
    static int verifyGuests() {

        boolean valid = false;
        int numberGuests = -1;

        do {
            try {
                Scanner input = new Scanner(System.in);
                System.out.println("No. of guests: ");
                numberGuests = input.nextInt();

                while (numberGuests < 0 || numberGuests > 8) {
                    System.out.println("Sorry, System unable to process that amount of guests\n");
                    System.out.println("No. of guests: ");
                    numberGuests = input.nextInt();
                }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Sorry, System unable to process that amount of guests\n");
            }
        } while (!valid);

        return numberGuests;
    }

    // verify Email() // must include @ and .
    static boolean verifyEmail(String input) {
        return input.contains("@") && input.contains(".");
    }

    // verify Date() // format and cant be earlier then current time
    static boolean verifyDate(String input) {
        String regex = "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
        return input.matches(regex);
    }

    // verify Confirmation()
    static boolean verifyConfirmation(String input) {
        return input.equals("y") || input.equals("n") || input.equals("Y") || input.equals("N");
    }

    // Handle Menu Option
    static int menuChoice(int lowerBound, int upperBound) {

        boolean valid = false;
        int number = -1;

        do {
            try {
                Scanner input = new Scanner(System.in);
                System.out.println("Please select an option>");
                number = input.nextInt();

                while (number < lowerBound || number > upperBound) {
                    System.out.println("Invalid Input!, Please select an option>");
                    number = input.nextInt();
                }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input!, ");
            }
        } while (!valid);

        return number;
    }

    // Application Logo
    static void renderLogo() {
        System.out.println("\nHotel Reservation System");
        System.out.println("------------------------\n");
    }

    // Verify login Into System
    static String[] getLoginInfo() {

        String[] tokens = new String[2];
        Scanner inputPass = new Scanner(System.in);

        renderLogo();

        int ID = verifyLoginID();

        System.out.println("Please enter your Password: ");
        String password = inputPass.nextLine();

        tokens[0] = Integer.toString(ID);
        tokens[1] = password;

        return tokens;
    }

    // Display Menu
    static void displayMenu() {
        renderLogo();
        System.out.println(
                "[1]  Book a room      \n" +
                        "[2]  Bill service     \n" +
                        "[3]  Current bookings \n" +
                        "[4]  Available rooms  \n" +
                        "[5]  Exit             \n"
        );
    }

    // main booking method
    static Reservation bookRoom(Room[] rooms) throws ParseException {

        Scanner input = new Scanner(System.in);

        ArrayList<String> reserveInfo = reservationInfo(rooms);
        Guest client = guestInfo();

        System.out.println("\nPlease Confirm Reservation(y/n)");
        String choice = input.nextLine();

        while (!verifyConfirmation(choice)) {
            System.out.println(choice);
            System.out.println("\nInvalid Input!, Please Confirm Reservation(y/n)");
            choice = input.nextLine();
        }

        if (choice.equals("y") || choice.equals("Y")) {

            ArrayList<String> roomIDs = new ArrayList<String>();

            for (int i = 0; i < reserveInfo.size(); i++) {
                if (i + 2 == reserveInfo.size()) break;
                roomIDs.add(reserveInfo.get(i));
            }

            String tempCheckInDate = reserveInfo.get(reserveInfo.size() - 2);
            Integer durationOfStay = Integer.parseInt(reserveInfo.get(reserveInfo.size() - 1));
            ArrayList<Room> roomsBooked = new ArrayList<Room>();

            for (Room room : rooms) {
                for (String ID : roomIDs) {
                    if (room.getRoom_ID() == Integer.parseInt(ID)) {
                        room.setAvailability(false);
                        roomsBooked.add(room);
                    }
                }
            }

            LocalDateTime localDateTime = LocalDateTime.now();
            Date bookDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            Date checkInDate = new SimpleDateFormat("dd/MM/yyyy").parse(tempCheckInDate);
            Calendar c = Calendar.getInstance();
            c.setTime(checkInDate);
            c.add(Calendar.DATE, durationOfStay);
            Date checkOutDate = c.getTime();

            Reservation reservation = new Reservation(generator.nextInt(10000, 95000), durationOfStay, bookDate, checkInDate, checkOutDate);
            reservation.setClient(client);
            reservation.setRooms(roomsBooked);

            System.out.println("\nReservation created successfully \n");
            return reservation;

        } else System.out.println("\nReservation aborted \n");

        return null;
    }

    static ArrayList<String> reservationInfo(Room[] rooms) {
        renderLogo();

        Scanner input = new Scanner(System.in);
        Scanner inputDate = new Scanner(System.in);
        ArrayList<String> selectionInfo = new ArrayList<String>();
        ArrayList<ArrayList<Room>> options = null;
        ArrayList<Room> optionSelection = null;
        int optionNumber = 1;
        int roomOption = 0;
        double totalCostPerDay = 0;

        int numberGuests = verifyGuests();

        if (numberGuests <= 4) options = suggestOptions(numberGuests, rooms);
        if (numberGuests > 4) options = suggestOptionsLarge(numberGuests, rooms);

        System.out.println("\nHere are the available rooms: ");

        for (ArrayList<Room> option : options) {

            System.out.println("\noption" + " [" + optionNumber + "]");
            System.out.println("-----------");
            option.forEach((room) -> System.out.println(room));
            optionNumber++;
        }

        System.out.print("\n");
        roomOption = menuChoice(1, options.size());

        optionSelection = options.get(roomOption - 1);
        totalCostPerDay = costPerDay(optionSelection);

        System.out.println("\nNumber of Days for the booking(Max 30): ");
        optionNumber = menuChoice(1, 30);

        // Enter Check-in Date // verify Date here
        System.out.println("Please Enter Check In Date('dd/mm/yyyy'): ");
        String check_in = inputDate.nextLine();
        while (!verifyDate(check_in)) {
            System.out.println("Invalid Date Input, Please Enter Check In Date('dd/mm/yyyy'): ");
            check_in = inputDate.nextLine();
        }

        System.out.println("\nRate to be offered per day: $" + totalCostPerDay);

        for (Room room : optionSelection) selectionInfo.add((Integer.toString(room.getRoom_ID())));
        selectionInfo.add(check_in);
        selectionInfo.add(Integer.toString(optionNumber));

        return selectionInfo;
    }

    // create list of possible room configurations
    static ArrayList<ArrayList<Room>> suggestOptions(int guestNo, Room[] rooms) {

        ArrayList<ArrayList<Room>> options = new ArrayList<ArrayList<Room>>();

        if (guestNo == 1) {
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Single")) {
                    ArrayList<Room> option = new ArrayList<Room>();
                    option.add(room);
                    options.add(option);
                    break;
                }
            }
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Pent House")) {
                    ArrayList<Room> option = new ArrayList<Room>();
                    option.add(room);
                    options.add(option);
                    break;
                }
            }
        }

        if (guestNo == 2) {
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Single")) {
                    ArrayList<Room> option = new ArrayList<Room>();
                    option.add(room);
                    options.add(option);
                    break;
                }
            }

            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Double")) {
                    ArrayList<Room> option = new ArrayList<Room>();
                    option.add(room);
                    options.add(option);
                    break;
                }
            }

            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Delux")) {
                    ArrayList<Room> option = new ArrayList<Room>();
                    option.add(room);
                    options.add(option);
                    break;
                }
            }

            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Pent House")) {
                    ArrayList<Room> option = new ArrayList<Room>();
                    option.add(room);
                    options.add(option);
                    break;
                }
            }

        }

        if (guestNo == 3) {
            ArrayList<Room> optionOne = new ArrayList<Room>();
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Single")) {
                    if (optionOne.size() == 2) {
                        options.add(optionOne);
                        break;
                    }
                    optionOne.add(room);
                }
            }

            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Double")) {
                    ArrayList<Room> optionTwo = new ArrayList<Room>();
                    optionTwo.add(room);
                    options.add(optionTwo);
                    break;
                }
            }
        }

        if (guestNo == 4) {
            ArrayList<Room> optionOne = new ArrayList<Room>();
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Double")) {
                    if (optionOne.size() == 2) {
                        options.add(optionOne);
                        break;
                    }
                    optionOne.add(room);
                }
            }

            ArrayList<Room> optionTwo = new ArrayList<Room>();
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Single")) {
                    optionTwo.add(room);
                    break;
                }
            }
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Double")) {
                    optionTwo.add(room);
                    options.add(optionTwo);
                    break;
                }
            }

            ArrayList<Room> optionThree = new ArrayList<Room>();
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Single")) {
                    if (optionThree.size() == 2) break;
                    optionThree.add(room);

                }
            }
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Double")) {
                    optionThree.add(room);
                    options.add(optionThree);
                    break;
                }
            }

        }

        return options;
    }


    static ArrayList<ArrayList<Room>> suggestOptionsLarge(int guestNo, Room[] rooms) {

        ArrayList<ArrayList<Room>> options = new ArrayList<ArrayList<Room>>();

        if (guestNo % 2 == 0) {

            ArrayList<Room> optionOne = new ArrayList<Room>();

            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Double")) {
                    if (optionOne.size() == guestNo / 2) break;
                    optionOne.add(room);

                }
            }
            options.add(optionOne);

        }
        if (guestNo % 2 != 0) {

            ArrayList<Room> optionOne = new ArrayList<Room>();

            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Double")) {
                    if (optionOne.size() == guestNo / 2) break;
                    optionOne.add(room);
                }
            }
            for (Room room : rooms) {
                if (room.getAvailability() && room.getRoom_Type().equals("Single")) {
                    optionOne.add(room);
                    break;
                }
            }
            options.add(optionOne);
        }

        return options;
    }

    // get guest information
    static Guest guestInfo() {
        renderLogo();

        Scanner input = new Scanner(System.in);
        Scanner inputTwo = new Scanner(System.in);
        Guest client = null;

        System.out.println("Title: ");
        String clientTitle = input.nextLine();
        while (!verifyString(clientTitle)) {
            System.out.println("Invalid Entry, please re-enter Title");
        }

        System.out.println("First Name: ");
        String clientFname = input.nextLine();
        while (!verifyString(clientFname)) {
            System.out.println("Invalid Entry, please re-enter First Name: ");
        }

        System.out.println("Last Name: ");
        String clientLname = input.nextLine();
        while (!verifyString(clientFname)) {
            System.out.println("Invalid Entry, please re-enter Last Name: ");
        }

        System.out.println("Address: ");
        String clientAddress = input.nextLine();

        System.out.println("Phone: ");
        Long clientPhone = verifyPhone();

        System.out.println("Email: ");
        String clientEmail = inputTwo.nextLine();
        while (!verifyEmail(clientEmail)) {
            System.out.println("invalid Email, please re-enter Email: ");
            clientEmail = inputTwo.nextLine();
        }

        // might need change to statically incremented value
        client = new Guest(generator.nextInt(50, 700), clientTitle, clientFname, clientLname, clientAddress, clientPhone, clientEmail);

        return client;
    }

    //calculate cost per day
    static double costPerDay(ArrayList<Room> option) {
        double totalCost = 0.00;
        for (Room room : option) totalCost += room.getRate();
        return totalCost;
    }

    /* Handle creation of Bill service */

    // Handle creation of Bill Service
    static Reservation billService(Reservation[] reservationList) {

        Reservation reservation = null;
        Scanner input = new Scanner(System.in);
        Scanner inputDiscount = new Scanner(System.in);
        Scanner inputConfirmation = new Scanner(System.in);
        boolean foundResult = false;
        boolean valid = false;
        int discountValue = 0;
        int ID = 0;
        double discountTotal = 0;
        double totalValue = 0;
        String choice = null;

        do {
            try {

                Scanner inputID = new Scanner(System.in);
                System.out.println("Enter Booking ID: ");
                ID = inputID.nextInt();
                valid = true;

            } catch (InputMismatchException e) {
                System.out.println("\nInvalid Input! ");
            }
        } while (!valid);
        valid = false;


        for (Reservation reservationElement : reservationList) {
            if (reservationElement.getBook_ID() == ID) {
                foundResult = true;
                reservation = reservationElement;
            }
        }

        if (foundResult) {

            System.out.println("Booking ID: " + reservation.getBook_ID() + "\n");
            System.out.println("Guest Name: " + reservation.getClient().getLast_name() + "\n");
            System.out.println("No. of rooms booked: " + reservation.getRooms().size() + "\n");

            System.out.println("Types of Rooms: " + "\n");
            reservation.getRooms().forEach(room -> {
                System.out.println(room.getRoom_Type());
            });
            System.out.print("\n");
            System.out.println("Rate per night: " + costPerDay(reservation.getRooms()) + "\n");

            totalValue = reservation.getBooking_Period() * costPerDay(reservation.getRooms());

            System.out.println("Would you like to enter a discount? (y/n): ");
            choice = inputConfirmation.nextLine();

            while (!verifyConfirmation(choice)) {
                System.out.println(choice);
                System.out.println("\nInvalid Input!, Please Confirm(y/n)");
                choice = input.nextLine();
            }
            if (choice.equals("y") || choice.equals("Y")) {

                do {
                    try {

                        Scanner inputDisc = new Scanner(System.in);
                        System.out.println("Discounts(5-25%): ");
                        discountValue = inputDisc.nextInt();

                        while (discountValue < 5 || discountValue > 25) {
                            System.out.println("\nOut of range, Discounts(5-25%): ");
                            discountValue = input.nextInt();
                        }
                        valid = true;

                    } catch (InputMismatchException e) {
                        System.out.println("\nInvalid Input!");
                    }
                } while (!valid);

                discountTotal = totalValue * discountValue / 100;
                totalValue -= discountTotal;
            }

            System.out.println("\nTotal Amount: $" + totalValue);
            System.out.println("Create Bill for Reservation #" + reservation.getBook_ID() + " (y/n)?");
            choice = inputConfirmation.nextLine();

            while (!verifyConfirmation(choice)) {
                System.out.println(choice);
                System.out.println("\nInvalid Input!, Please Confirm(y/n)");
                choice = input.nextLine();
            }
            if (choice.equals("y") || choice.equals("Y")) {

                Bill bill = new Bill(generator.nextInt(1000, 3000), totalValue);
                reservation.setBill(bill);

                System.out.println("\nBill successfully created \n");

                return reservation;

            } else System.out.println("\nBill service cancelled \n");

        } else System.out.println("\nSorry, Reservation not found \n");

        return reservation;
    }

    /* Display Current Bookings */

    // Display Current Bookings
    static void currentBookings(Reservation[] reservationList) {
        renderLogo();

        System.out.println("No. of current bookings are: " + reservationList.length);
        System.out.println("Booking #  " + "  Customer Name  " + "  Room Type  " + "  No of Rooms  " + "  No of Days");
        System.out.println("--------------------------------------------------------------------");
        for (Reservation reservation : reservationList) {

            for (int i = 0; i < reservation.getRooms().size(); i++) {

                if (i == 0) {
                    System.out.print("\n" + reservation.getBook_ID());
                    calculateSpace(9, Integer.toString(reservation.getBook_ID()).length());
                    System.out.print(reservation.getClient().getLast_name());
                    calculateSpace(13, reservation.getClient().getLast_name().length());
                    System.out.print(reservation.getRooms().get(i).getRoom_Type());
                    calculateSpace(10, reservation.getRooms().get(i).getRoom_Type().length());
                    System.out.print(reservation.getRooms().size());
                    calculateSpace(11, Integer.toString(reservation.getRooms().size()).length());
                    System.out.print(reservation.getBooking_Period() + "\n");
                } else{
                    printSpace(30);
                    System.out.print(reservation.getRooms().get(i).getRoom_Type() + "\n");
                }
            }
            System.out.println("--------------------------------------------------------------------");
        }

        System.out.println("\n");
    }

    // calculate Spaces

    static void calculateSpace(int titleLength, int fieldLength) {
        final int standardSpace = 4;
        int distance = titleLength - fieldLength;
        for (int i = 0; i < standardSpace + distance; i++) {
            System.out.print(" ");
        }
    }

    static void printSpace(int length){
        for (int i = 0; i < length; i++) System.out.print(" ");
    }

    /* Display Available Rooms */

    // Available Rooms
    static void availableRooms(Room[] rooms) {

        renderLogo();

        int numberAvailable = 0;
        for (Room room : rooms) {
            if (room.getAvailability()) numberAvailable++;
        }

        System.out.println("No of Available rooms: " + numberAvailable + "\n");
        System.out.println("Room ID  " + "  Room Type \n");
        for (Room room : rooms) {
            if (room.getAvailability()) {
                System.out.println(room.getRoom_ID() + "        " + room.getRoom_Type() + "\n");
            }
        }

    }

}
