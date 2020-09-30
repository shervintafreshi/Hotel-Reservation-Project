package server;

import room.*;
import utilities.Bill;
import utilities.Login;
import utilities.Reservation;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ServerAssistant extends Thread {

    private Socket socket;

    public ServerAssistant(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            boolean loginState = false;
            String process = null;
            Reservation reservation = null;

            do {

                /***** verify Login Phase ******/

                do {
                    Login admin = (Login) in.readObject();
                    loginState = verifyLogin(admin);

                    if (loginState) output.println("true");
                    else output.println("false");

                } while (!loginState);

                /***** Serve Client Requests ******/

                do {
                    process = input.readLine();

                    switch (process) {
                        case "1":
                            out.writeObject(availableRooms());
                            out.flush();
                            reservation = (Reservation) in.readObject();
                            bookRoom(reservation);
                            break;
                        case "2":
                            out.writeObject(currentBookings());
                            out.flush();
                            reservation = (Reservation) in.readObject();
                            billService(reservation);
                            break;
                        case "3":
                            out.writeObject(currentBookings());
                            out.flush();
                            break;
                        case "4":
                            out.writeObject(availableRooms());
                            out.flush();
                            break;
                    }

                    if (process.equals("5")){
                        logout();
                        break;
                    }

                } while (true);

                output.println("exit");
                break;
            } while (true);

            System.out.println("Client Disconnected");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client Error " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Client Error: " + e.getMessage());
            }
        }

    }

    // Administrator Accounts
    static ArrayList<Login> accounts = new ArrayList<Login>();

    // currently Logged
    Login currentAdmin = null;

    // intiialization state

    // Database
    static ArrayList<Room> hotelRooms = new ArrayList<Room>();
    static ArrayList<Reservation> reservationList = new ArrayList<Reservation>();

    // Initialize Accounts
    static void initializeAccounts(){
        accounts.add(new Login(5641, "adminpass"));
        accounts.add(new Login(4563, "adminpass"));
        accounts.add(new Login(3411, "adminpass"));
    }

    // Initialize Database
    static void initializeRooms() {

        for (int i = 0; i < 16; i++) hotelRooms.add(new SingleRoom(generator.nextInt(1000, 2500)));
        for (int i = 0; i < 16; i++) hotelRooms.add(new DoubleRoom(generator.nextInt(2501, 5000)));
        for (int i = 0; i < 8; i++) hotelRooms.add(new DeluxRoom(generator.nextInt(5001, 7500)));
        for (int i = 0; i < 8; i++) hotelRooms.add(new PentHouse(generator.nextInt(7501, 9950)));
    }

    // verify login status
    boolean verifyLogin(Login admin) {
        boolean verified = false;

        for(Login account : accounts){
            if(admin.equals(account) && !account.getLogged_In()){
                verified = true;
                account.setLogged_In(true);
                currentAdmin = account;
                break;
            }
        }
        return verified;
    }

    void logout(){
        for (Login account : accounts) {
            if(account.equals(currentAdmin)) {
                account.setLogged_In(false);
                currentAdmin = null;
                break;
            }
        }
    }

    // Randomizer
    static ThreadLocalRandom generator = ThreadLocalRandom.current();

    void bookRoom(Reservation reservationNew) {

        boolean state = false;
        do {
            state = false;
            for (Reservation reservation : reservationList) {
                if (reservation.getBook_ID() == reservationNew.getBook_ID()) {
                    reservationNew.setBook_ID(generator.nextInt(10000, 95000));
                    state = true;
                }
            }

        } while (state);

        for (Room room : hotelRooms){
            for(Room reserved : reservationNew.getRooms()) {
                if (room.getRoom_ID() == reserved.getRoom_ID()) room.setAvailability(false);
            }
        }
        reservationList.add(reservationNew);
    }

    void billService(Reservation reservationBilled) {

        if (reservationBilled != null) {
            for (Reservation reservation : reservationList) {
                if (reservation.getBook_ID() == reservationBilled.getBook_ID()) {
                    reservation.setBill(reservationBilled.getBill());
                }
            }
        }
    }

    Reservation[] currentBookings() {
        Reservation[] reservations = new Reservation[reservationList.size()];
        reservations = reservationList.toArray(reservations);
        return reservations;
    }

    Room[] availableRooms() {
        Room[] rooms = new Room[hotelRooms.size()];
        rooms = hotelRooms.toArray(rooms);
        return rooms;
    }

}
