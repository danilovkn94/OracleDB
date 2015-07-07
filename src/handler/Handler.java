package handler;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import connector.DBConnector;

public class Handler {
	
	private ResultSet rsEmployees;
	private ResultSet rsDepartments;
	private DBConnector connector;
	
	public Handler(){
		Scanner sc = new Scanner(System.in);
		String username, password, sid, host;
		
		System.out.println("Username [system]:");
		username = sc.nextLine();		
		
		System.out.println("Password:");
		password = sc.nextLine();
		
		System.out.println("Sid [xe]:");
		sid = sc.nextLine();		
		
		System.out.println("Hostname and port [localhost:1521]:");
		host = sc.nextLine();
		
		sc.close();
		
		connector = new DBConnector(username, password, 
				sid, host);
		connector.intialization();
		
		rsEmployees = connector.query("select * from HR.EMPLOYEES");
		rsDepartments = connector.query("select * from HR.DEPARTMENTS");
	}
	
	public void maxSalary() throws SQLException{
		HashMap<Integer, String> mapName = new HashMap<>();
		HashMap<Integer, Integer> mapSalary = new HashMap<>();
		
		if (!rsEmployees.isBeforeFirst())	rsEmployees.beforeFirst();		
		
		while(rsEmployees.next()){
			
			int departmentId = rsEmployees.getInt(11);
			int salary = rsEmployees.getInt(8);
			String name = rsEmployees.getString(2) + " " + rsEmployees.getString(3);
			
			if (mapName.containsKey(departmentId)){
				
				if (mapSalary.get(departmentId) < salary){
					mapName.put(departmentId, name);
					mapSalary.put(departmentId, salary);						
				} else if (mapSalary.get(departmentId) == salary)
					mapName.put(departmentId, mapName.get(departmentId) + ", " + name);				
				
			} else {
				mapName.put(departmentId, name);
				mapSalary.put(departmentId, salary);
			}
		}
		
		System.out.println("\n1.The list of employees with max salary:");			
		mapName.forEach((k, v) -> System.out.println("    - " + v));
	}
	
	
	public void depWithEmpMore5() throws SQLException{		
		HashMap<Integer, String> mapDepartments = new HashMap<>();
		HashMap<Integer, Integer> mapNumber = new HashMap<>();
		
		if (!rsDepartments.isBeforeFirst()) rsDepartments.beforeFirst();
		if (!rsEmployees.isBeforeFirst())	rsEmployees.beforeFirst();
		
		while(rsDepartments.next()){
			mapDepartments.put(rsDepartments.getInt(1), rsDepartments.getString(2));
			mapNumber.put(rsDepartments.getInt(1), 0);
		}
		
		while(rsEmployees.next()){
			int departmentId = rsEmployees.getInt(11);			
			if(mapDepartments.containsKey(departmentId))
				mapNumber.put(departmentId, mapNumber.get(departmentId) + 1);			
		}
		
		System.out.println("\n2.List of departments, the number of employees in which more five:");
		mapNumber.forEach((k, v) -> {
			if(v > 5) System.out.println("    - " + mapDepartments.get(k));			
		});
	}
	
	
	public void depAndEmp() throws SQLException{		
		HashMap<Integer, ArrayList<String>> mapDE = new HashMap<>();
		HashMap<Integer, String> mapDepartments = new HashMap<>();
		
		if (!rsDepartments.isBeforeFirst()) rsDepartments.beforeFirst();
		if (!rsEmployees.isBeforeFirst())	rsEmployees.beforeFirst();
		
		while(rsDepartments.next()) {			
			mapDE.put(rsDepartments.getInt(1), new ArrayList<String>());
			mapDepartments.put(rsDepartments.getInt(1), rsDepartments.getString(2));
		}			
		
		while(rsEmployees.next()){
			int depId = rsEmployees.getInt(11);
			String name = rsEmployees.getString(2) + " " + rsEmployees.getString(3);
			ArrayList<String> empList = mapDE.get(depId);
			if (mapDE.get(depId) == null) 
				empList = new ArrayList<>();
			empList.add(name);
			mapDE.put(depId, empList);
		}
		
		int i = 0;		
		System.out.println("\n3.List of departments and employees:");
		Iterator<Integer> iterator = mapDE.keySet().iterator();
		while(iterator.hasNext()){			
			int key = iterator.next();
			String departmentName;
			if (mapDepartments.get(key) == null) departmentName = "Unknown";
			else departmentName = mapDepartments.get(key).toString();
			System.out.println("    " + ++i + ")" + departmentName + ":");
			for(String l : mapDE.get(key)){
				System.out.println("        - " + l);
			}				
		}
	}
	
	public void randNum() throws NoSuchAlgorithmException{
		SecureRandom secRand = SecureRandom.getInstance("SHA1PRNG");
		
		System.out.print("\n4.Random numbers:\n    ");
		int[] randNums = new int[10];
		for(int i : randNums){
			randNums[i] = secRand.nextInt(100);
			System.out.print(randNums[i] + "  ");
		}
		System.out.println();
	}
	
	public void managEmpl() throws SQLException{
		HashMap<Integer, String> mapNames = new HashMap<>();
		HashMap<Integer, Integer> mapManagEmpl = new HashMap<>();
		
		if (!rsEmployees.isBeforeFirst()) rsEmployees.beforeFirst();
		
		while(rsEmployees.next()){
			String name = rsEmployees.getString(2) + " " + rsEmployees.getString(3);
			mapNames.put(rsEmployees.getInt(1), name);
			mapManagEmpl.put(rsEmployees.getInt(1), rsEmployees.getInt(10));
		}		
		
		System.out.println("\n5.Employees in tree view:");
		
		Iterator<Integer> iterator = mapManagEmpl.keySet().iterator();
		while(iterator.hasNext()){
			int key = iterator.next();
			String tree = mapNames.get(key);
			while(mapManagEmpl.containsKey(mapManagEmpl.get(key))){
				key = mapManagEmpl.get(key);
				tree = mapNames.get(key) + "\\" + tree;				
			}			
			System.out.println("    " + tree);
		}		
	}
	
	public void checkA() throws SQLException{		
		HashMap<Integer, String> mapDepartments = new HashMap<>();
		
		if (!rsEmployees.isBeforeFirst()) rsEmployees.beforeFirst();
		if (!rsDepartments.isBeforeFirst()) rsDepartments.beforeFirst();		
		
		while(rsDepartments.next()) {			
			mapDepartments.put(rsDepartments.getInt(1), rsDepartments.getString(2));
		}		
		
		System.out.println("\n6.Employees, names of which contain 'a', and their departments:");
		while(rsEmployees.next()){
			if(rsEmployees.getString(2).contains("a"))
				System.out.println("    - " + rsEmployees.getString(2) + " " +
						rsEmployees.getString(3) + " : " + 
						mapDepartments.get(rsEmployees.getInt(11)));
		}
	}
	
	public void YMDFromDateOfBirth(){
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.YEAR, -1989);
		calendar.add(Calendar.MONTH, -7);
		calendar.add(Calendar.DAY_OF_MONTH, -5);
		int years = calendar.get(Calendar.YEAR);
		int months = calendar.get(Calendar.MONTH);
		int days = calendar.get(Calendar.DAY_OF_MONTH);
		
		System.out.println("\n7.Date of birth(05.08.1989) was " + years + " years, " + months + 
				" months and " + days + " day(s) ago.");
	}
	
	public void dupField() throws SQLException{
		ResultSet rs = connector.query("select 1 as id,'num1' as name FROM dual "
				+ "union all select 2 as id,'num1' as name FROM dual "
				+ "union all select 3 as id,'num2' as name FROM dual");
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()){
			if(!list.contains(rs.getString(2)))	list.add(rs.getString(2));
			else System.out.println("\n8.Duplicate field: " + rs.getString(2));				
		}
	}
	
	public void _9And10out() throws SQLException{
		ResultSet rs = connector.query("select 1 NUM, 'test1' NAM from dual "
				+ "union all select 2, 'test2' from dual "
				+ "union all select 3, 'test3' from dual "
				+ "union all select 4, 'test4' from dual");
		
		ArrayList<String> list = new ArrayList<>();
		
		while(rs.next()) list.add(rs.getString(2));
		
		System.out.println("\n9.");
		list.forEach(k -> {
			list.forEach(v -> {
				if(!k.equals(v)) System.out.println("    " + k + " | " + v);
			});
		});
		
		System.out.println("\n10.");
		
		for(int i = 0; i < list.size() - 1; i++){
			for(int k = i + 1; k < list.size(); k++)
				System.out.println("    " + list.get(i) + " | " + list.get(k));
		}
	}

}
