package org.testorg.client.util;

import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.segment.PID;
import org.testorg.client.pojo.Observation;
import org.testorg.client.pojo.Patient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class HL7Utils {
    private static String url;
    private static String user;
    private static String password;
    private static final Connection connection;
    private static final String INSERT_USER_SQL = "INSERT INTO patient " +
            "(snils, name, surname, birth) VALUES " +
            "(?,?,?,?) ON CONFLICT DO NOTHING;";
    private static final String INSERT_USER_OBSERVATION_SQL = "INSERT INTO observation " +
            "(snils, ecg, pulse, sat, co2_insp, co2_exp, observation_time) VALUES " +
            "(?,?,?,?,?,?,?);";


    static {
        try {
            setProperties("application.properties");
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setProperties(String propFile) throws IOException {
        Properties prop = new Properties();
        InputStream inputStream = HL7Utils.class.getClassLoader().getResourceAsStream(propFile);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFile + "' not found in the classpath");
        }
        user = prop.getProperty("user");
        url = prop.getProperty("url");
        password = prop.getProperty("password");

    }

    public static Patient getPatientFromPID(PID pid){
        Patient patient = new Patient();
        patient.setName(pid.getPatientName(0).getGivenName().getValue());
        patient.setSurname(pid.getPatientName(0).getFamilyName().getSurname().getValue());
        String dateInString = pid.getPid7_DateTimeOfBirth().getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.getDefault());
        Date dateTime = Date.valueOf(LocalDate.parse(dateInString, formatter));
        patient.setBirth(dateTime);
        patient.setSnils(Long.valueOf(pid.getPatientIdentifierList(0).getIDNumber().getValue()));
        return patient;
    }

    public static Observation getObservationFrom (List<ORU_R01_ORDER_OBSERVATION> list){
        Observation observation = new Observation();
        observation.setPulse((int) (Math.random()*10 - 3 + Integer.parseInt(list.get(0).getOBSERVATION(7).getOBX().getObx5_ObservationValue()[0].getData().toString())));
        observation.setEcg((int) (Math.random()*10 - 3 + Integer.parseInt(list.get(0).getOBSERVATION(2).getOBX().getObx5_ObservationValue()[0].getData().toString())));
        observation.setCo2_exp(Float.parseFloat(list.get(0).getOBSERVATION(4).getOBX().getObx5_ObservationValue()[0].getData().toString()));
        observation.setCo2_insp(Float.parseFloat(list.get(0).getOBSERVATION(5).getOBX().getObx5_ObservationValue()[0].getData().toString()));
        observation.setSat((int) (Math.random()*10 - 3 + Integer.parseInt(list.get(0).getOBSERVATION(8).getOBX().getObx5_ObservationValue()[0].getData().toString())));
        observation.setObservation_time(Timestamp.from(Instant.now()));
        return observation;
    }

    public static void insertPatient(Patient patient) {
        System.out.println(INSERT_USER_SQL);
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setLong(1, patient.getSnils() );
            preparedStatement.setString(2, patient.getName());
            preparedStatement.setString(3, patient.getSurname());
            preparedStatement.setString(4, String.valueOf(patient.getBirth()));
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void insertObservation(Long patientId, Observation observation) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_OBSERVATION_SQL)) {

            preparedStatement.setLong(1, patientId );
            preparedStatement.setInt(2, observation.getEcg());
            preparedStatement.setInt(3, observation.getPulse());
            preparedStatement.setInt(4, observation.getSat());
            preparedStatement.setFloat(5, observation.getCo2_insp());
            preparedStatement.setFloat(6, observation.getCo2_exp());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(observation.getObservation_time().toLocalDateTime()));

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
