package org.testorg.client.util;

import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.segment.PID;
import org.testorg.client.pojo.Observation;
import org.testorg.client.pojo.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class HL7Utils {
    private final String url = "jdbc:postgresql://localhost/myDB";
    private final String user = "postgres";
    private final String password = "root";

    private static final String INSERT_USERS_SQL = "INSERT INTO observation" +
            "  (patient_id, ecg, amount_of_breath, pulse, niad, co2_exp, co2_insp, mds, mdc ) VALUES " +
            " (?, ?, ?, ?, ?, ?, ?, ?, ? );";

//    private static final String FIND_PATIENT_BY_SNILS = "SELECT id FROM patients p WHERE p.snils = ?";


    public static Patient getPatientFromPID(PID pid){
        Patient patient = new Patient();
        patient.setName(pid.getPatientName(0).getGivenName().getValue());
        patient.setSurname(pid.getPatientName(0).getFamilyName().getSurname().getValue());
        String dateInString = pid.getPid7_DateTimeOfBirth().getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.getDefault());
        Date dateTime = Date.valueOf(LocalDate.parse(dateInString, formatter));
        patient.setBirth(dateTime);
        patient.setSnils(pid.getPatientIdentifierList(0).getIDNumber().getValue());
        return patient;
    }

    public static Observation getObservationFrom (List<ORU_R01_ORDER_OBSERVATION> list){
        Observation observation = new Observation();
        observation.setPulse(Integer.parseInt(list.get(0).getOBSERVATION(7).getOBX().getObx5_ObservationValue()[0].getData().toString()));
        observation.setEcg(Integer.parseInt(list.get(0).getOBSERVATION(2).getOBX().getObx5_ObservationValue()[0].getData().toString()));
        observation.setCo2_exp(Float.parseFloat(list.get(0).getOBSERVATION(4).getOBX().getObx5_ObservationValue()[0].getData().toString()));
        observation.setCo2_insp(Float.parseFloat(list.get(0).getOBSERVATION(5).getOBX().getObx5_ObservationValue()[0].getData().toString()));
        observation.setSat(Integer.parseInt(list.get(0).getOBSERVATION(8).getOBX().getObx5_ObservationValue()[0].getData().toString()));
        return observation;
    }



//    public void insertRecord() throws SQLException {
//        System.out.println(INSERT_USERS_SQL);
//        try (Connection connection = DriverManager.getConnection(url, user, password);
//
//             PreparedStatement preparedStatement = connection.prepareStatement(FIND_PATIENT_BY_SNILS)) {
//            preparedStatement.setInt(1, 1);
//            preparedStatement.setString(2, "Tony");
//            preparedStatement.setString(3, "tony@gmail.com");
//            preparedStatement.setString(4, "US");
//            preparedStatement.setString(5, "secret");
//
//            System.out.println(preparedStatement);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//    }
//
//    public static void printSQLException(SQLException ex) {
//        for (Throwable e: ex) {
//            if (e instanceof SQLException) {
//                e.printStackTrace(System.err);
//                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
//                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
//                System.err.println("Message: " + e.getMessage());
//                Throwable t = ex.getCause();
//                while (t != null) {
//                    System.out.println("Cause: " + t);
//                    t = t.getCause();
//                }
//            }
//        }
//    }
}
