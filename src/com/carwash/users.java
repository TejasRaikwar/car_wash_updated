package com.carwash;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class users {
    static void checkAvailabilityAndBooking2(String name, String placename, String service, Date date) {
        Scanner sc = new Scanner(System.in);
        PreparedStatement insert, insert2;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/car_wash", "root", "")) {
            insert = connection.prepareStatement("SELECT placeName, date , COUNT(date) from bookings GROUP BY placeName, date HAVING ((COUNT(placeName) < 5) AND (COUNT(date) < 5 ) AND (date=?) AND placeName=?);");
            insert.setDate(1, date);
            insert.setString(2, placename);
            ResultSet rs = insert.executeQuery();
            if (rs.next()) {
                System.out.println("Booking Available press 0 to continue");
                int cont = sc.nextInt();
                if (cont == 0) {
                    insert2 = connection.prepareStatement("INSERT INTO bookings (id, name, placeName, service, status, date) VALUES (NULL, ?,?,?,?,?)");
                    insert2.setString(1, name);
                    insert2.setString(2, placename);
                    insert2.setString(3, service);
                    insert2.setString(4, "Pending");
                    insert2.setDate(5, date);
                    insert2.executeUpdate();
                    System.out.println("Slot Booked Successfully!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    public static void main(String[] args) {
//        int id;
//        String placeName, services, name;
//        String strDate;
//        Scanner sc = new Scanner(System.in);
//        placeName = sc.next();
//        services = sc.next();
//        name = sc.next();
//        strDate = sc.next();
//        LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
//        checkAvailabilityAndBooking(name,placeName,services, Date.valueOf(date));
//    }
}