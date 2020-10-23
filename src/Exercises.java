import com.sun.jdi.ArrayReference;
import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.ScatteringByteChannel;
import java.sql.*;
import java.util.*;

public class Exercises {

    private static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/minions_db";

    private  Connection connection;

    public void connection(String user, String password) throws SQLException {
        Properties properties = new Properties();
         properties.setProperty("user",user);
         properties.setProperty("password",password);

        connection = DriverManager.getConnection(CONNECTION_STRING,properties);
    }


    public void ex2() throws SQLException {
        String query = "SELECT name,COUNT(*) AS `count` FROM minions_villains JOIN villains v on v.id = minions_villains.villain_id\n" +
                "GROUP BY id\n" +
                "HAVING count>15\n" +
                "ORDER BY count DESC;";
        PreparedStatement statement= connection.prepareStatement(query);

        ResultSet resultSet=statement.executeQuery();

        while (resultSet.next()){
            System.out.printf("| %s | %d |%n",resultSet.getString("name"),resultSet.getInt("count"));
        }
    }

    public void ex3() throws IOException, SQLException {
        System.out.println("Enter villain id:");
        BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
        int villainId=0;
        String message = bf.readLine();
        try{
        villainId=Integer.parseInt(message);
        }catch (NumberFormatException e){
            System.out.printf("No villain with ID %s exists in the database.%n", message);
            return;
        }

        if(checkIfTheIdIsCorrect(villainId)) {
            System.out.printf("No villain with ID %d exists in the database.%n", villainId);
            return;
        }
        System.out.printf("Villain: %s%n",vNameByvId(villainId));

        minionsByvId(villainId);

    }

    public void ex4() throws SQLException, IOException {
        BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Minion name,age and town:");
        String[] input=bf.readLine().split("\\s+");
        String minionName =input[0];
        int age=Integer.parseInt(input[1]);
        String townName=input[2];
        System.out.println("Villain name:");
        String villainName=bf.readLine();

        if(insertIntoTownsByName(townName)){
            System.out.printf("Town %s was added to the database.%n",townName);
        }
        if(insertIntoVillainsByName(villainName)){
            System.out.printf("Villain %s was added to the database%n",villainName);
        }
        //If there the minion already exist it is not inserted again
        insertIntoMinions(minionName,age,townName);

        if(insertIntoMinions_Villains(villainName,minionName)) {
            //If  the minion and the villain are connected in minions_villains it returns false
            System.out.printf("Successfully added %s to be minion of %s.%n", minionName, villainName);
        }
    }

    public void ex5() throws SQLException, IOException {
        System.out.println("Enter country name:");
        BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
        String country=bf.readLine();
        int townsUpdated=updateTownName(country);
        if(townsUpdated == 0){
            System.out.println("No town names were affected.");
        }else{
            System.out.printf("%d town names were affected.%n",townsUpdated);
            System.out.println(affectedTownsName(country));
        }

    }

    public void ex6() throws IOException, SQLException {
        System.out.println("Enter villain id:");
        BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
        int villainId=0;
        String message = bf.readLine();
        try{
            villainId=Integer.parseInt(message);
        }catch (NumberFormatException e){
            System.out.println("No such villain was found");
            return;
        }
        if(vNameByvId(villainId) ==null){
            System.out.println("No such villain was found");
            return;
        }

        String name=vNameByvId(villainId);
        int number=deleteFromMinions_villainsById(villainId);
        deleteVillainById(villainId);


        System.out.printf("%s was deleted%n",name);
        System.out.printf("%d minions released%n",number);

    }

    public void ex7() throws SQLException {
        String query="SELECT id,name FROM minions";
        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        LinkedHashMap<Integer,String> minionsMap=new LinkedHashMap<>();
        while (resultSet.next()){
            minionsMap.put(resultSet.getInt("id"),resultSet.getString("name"));
        }
        for (int i = 1; i <minionsMap.size()/2+1 ; i++) {
            System.out.println(minionsMap.get(i));
            System.out.println(minionsMap.get(minionsMap.size() - i+1));
        }
    }

    public void ex8() throws IOException, SQLException {
        System.out.println("Enter minion id:");
        BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
        String[] stringInput=bf.readLine().split("\\s+");
        int[] input=new int[stringInput.length];
        for (int i = 0; i <stringInput.length ; i++) {
            input[i]=Integer.parseInt(stringInput[i]);
        }

        String query = "UPDATE minions\n" +
                "SET name=LOWER(name),age=age+1\n" +
                "WHERE id in (?);";
        PreparedStatement statement=connection.prepareStatement(query);
        for (int i = 0; i <input.length ; i++) {
            statement.setInt(1,input[i]);
            statement.executeUpdate();
        }

        printFullTable();
    }

    public void ex9() throws IOException, SQLException {
        System.out.println("Enter minion id:");
        BufferedReader bf= new BufferedReader(new InputStreamReader(System.in));
        int id=Integer.parseInt(bf.readLine());

        //CREATE PROCEDURE usp_get_older(minion_id INT)
        //BEGIN
        //    UPDATE minions
        //    SET age=age+1
        //    WHERE id=minion_id;
        //END;

        String query= "CALL usp_get_older(?)";
        CallableStatement cS=connection.prepareCall(query);
        cS.setInt(1,id);
        cS.execute();

        printFullTable();
    }
    //---------------

    private int deleteVillainById(int villainId) throws SQLException {
        String query = "DELETE FROM villains WHERE id = ?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setInt(1,villainId);
        return statement.executeUpdate();
    }

    private int deleteFromMinions_villainsById(int villainId) throws SQLException {
        String query="DELETE FROM minions_villains WHERE villain_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, villainId);
        return statement.executeUpdate();
    }

    private void printFullTable() throws SQLException {
        String query = "SELECT id,name,age FROM minions";
        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        while (resultSet.next()){
            System.out.printf("%d | %s | %d%n",resultSet.getInt("id"),resultSet.getString("name"),resultSet.getInt("age"));
        }
    }

    private String affectedTownsName(String country) throws SQLException {
        String query = "SELECT name FROM towns WHERE country= ?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1,country);
        ResultSet resultSet=statement.executeQuery();
        StringBuilder sb=new StringBuilder();
        sb.append("[");
        while (resultSet.next()){
            sb.append(resultSet.getString("name"));
            if(!resultSet.isLast()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString().trim();
    }
    private int updateTownName(String country) throws SQLException {
        String query ="UPDATE towns\n" +
                "SET name=UPPER(name)\n" +
                "WHERE country=?;";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1,country);
        return statement.executeUpdate();

    }


    private boolean insertIntoMinions_Villains(String villainName,String minionName) throws SQLException {
        if(doesTheLinkExistInMinions_Villains(villainName,minionName)==0) {
            String query = "INSERT INTO minions_villains (minion_id,villain_id) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, doesMinionExistByName(minionName));
            statement.setInt(2, doesVillainExistByName(villainName));
            statement.execute();
            return true;
        }
        return false;
    }
    private int doesTheLinkExistInMinions_Villains(String villainName,String minionName) throws SQLException {
        String query="SELECT minion_id FROM minions_villains WHERE minion_id = ? AND villain_id = ? ";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setInt(1,doesMinionExistByName(minionName));
        statement.setInt(2,doesVillainExistByName(villainName));
        ResultSet resultSet=statement.executeQuery();
        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    private int doesMinionExistByName(String name) throws SQLException {
        String query="SELECT id FROM minions WHERE name = ? ";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1,name);
        ResultSet resultSet=statement.executeQuery();
        return resultSet.next() ? resultSet.getInt(1) : 0;

    }

    private void insertIntoMinions(String name, int age, String town) throws SQLException {
        if(doesMinionExistByNameAgeTown(name, age, town) == 0){
            String query= "INSERT INTO minions (name,age,town_id) VALUES (?,?,?)";
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setString(1,name);
            statement.setInt(2,age);
            statement.setInt(3,doesTownExistByName(town));
            statement.execute();
        }
    }
    private int doesMinionExistByNameAgeTown(String name, int age, String town) throws SQLException {
        String query="SELECT id FROM minions WHERE name = ? AND age = ? AND town_id = ?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1,name);
        statement.setInt(2,age);
        statement.setInt(3,doesTownExistByName(town));
        ResultSet resultSet=statement.executeQuery();
        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    private boolean insertIntoTownsByName(String townName) throws SQLException {
        if(doesTownExistByName(townName) == 0){
            String query= "INSERT INTO towns(name) VALUES (?)";
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setString(1,townName);
            statement.execute();
            return true;
        }
        return false;
    }
    private int doesTownExistByName(String townName) throws SQLException {
        String query="SELECT id FROM towns WHERE name = ?";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setString(1,townName);
        ResultSet resultSet=statement.executeQuery();
        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    private boolean insertIntoVillainsByName(String villainName) throws SQLException {
        if(doesVillainExistByName(villainName)==0){
            String query="INSERT INTO villains (name,evilness_factor) VALUES (?,?)";
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setString(1,villainName);
            statement.setString(2,"evil");
            statement.execute();
            return true;
        }
        return false;
    }
    private int doesVillainExistByName(String villainName) throws SQLException {
        String query= "SELECT id FROM villains WHERE name=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, villainName);
        ResultSet resultSet=statement.executeQuery();
        return resultSet.next() ? resultSet.getInt(1) : 0;
    }

    private boolean checkIfTheIdIsCorrect(int villainId) throws SQLException {
        String queryBounds = "SELECT COUNT(*) AS count FROM villains\n" +
                "WHERE id = ?;";
        PreparedStatement statementBounds =connection.prepareStatement(queryBounds);
        statementBounds.setInt(1,villainId);
        ResultSet resultSet=statementBounds.executeQuery();
        while (resultSet.next()){
            if(resultSet.getInt("count")==0){
                return true;
            }
        }
        return false;
    }

    private String vNameByvId(int villainId) throws SQLException {
        String queryVillainName = "SELECT v.name FROM villains v\n" +
                "WHERE v.id=?;";
        PreparedStatement statementVillain = connection.prepareStatement(queryVillainName);
        statementVillain.setInt(1 , villainId);

        ResultSet resultSetVillain = statementVillain.executeQuery();
        while (resultSetVillain.next()) {
            return  resultSetVillain.getString("name");
        }
        return null;
    }

    private void minionsByvId(int villainId) throws SQLException {
        String queryMinionsNameAge = "SELECT name, age FROM minions JOIN minions_villains mv on minions.id = mv.minion_id\n" +
                "WHERE mv.villain_id=?;";

        PreparedStatement statementMinions = connection.prepareStatement(queryMinionsNameAge);
        statementMinions.setInt(1,villainId);

        ResultSet resultSetMinions = statementMinions.executeQuery();
        int counter=1;
        while (resultSetMinions.next()){
            System.out.println(String.format("%d. %s %d",counter,resultSetMinions.getString("name"),resultSetMinions.getInt("age")));
            counter++;
        }
    }


}
