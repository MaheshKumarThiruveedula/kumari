package com.ojas;

import java.util.Hashtable;
import javax.security.auth.login.LoginException;
import oracle.iam.platform.OIMClient;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

public class ReadProcessFormData {
	// OIM Login details
	OIMClient client;
	private static String OIMUserName = "xelsysadm";
	private static String OIMPassword = "Ojas1525";
	private static String OIMURL = "t3://TRAINING00003:14000";
	private static String OIMInitialContextFactory = "weblogic.jndi.WLInitialContextFactory";
	
	//Set System Properties
	public OIMClient loginWithCustomEnv() {
		Hashtable env = new Hashtable();
		env.put(client.JAVA_NAMING_FACTORY_INITIAL, OIMInitialContextFactory);
		env.put(client.JAVA_NAMING_PROVIDER_URL, OIMURL);
		System.setProperty("OIMAppServerType", "wls");
		System.setProperty("APPSERVER_TYPE", "wls");
		System.setProperty("java.security.auth.login.config",
				"C:\\Oracle\\Middleware\\Oracle_IDM\\server\\config\\authwl.conf");
		client = null;
		try {
			// Creating Object to OIM Client
			client = new OIMClient(env);
			System.out.println("Initiating Oim Client");
			client.login(OIMUserName, OIMPassword.toCharArray());
			System.out.println("got OIM client successfully");
			System.out.println("Client - " + client);
		} catch (LoginException e) {
			System.out.println("Error : " + e);
		}
		return client;

	}

	public void ProcessFormData() throws tcColumnNotFoundException {
		OIMClient client = new ReadProcessFormData().loginWithCustomEnv();
			//Get the services from  tcFormInstanceOperationsIntf Interface
		tcFormInstanceOperationsIntf formOperationsIntf = client
				.getService(tcFormInstanceOperationsIntf.class);

		try {
			//Get the data from processInstancekey
			tcResultSet resultSet = formOperationsIntf.getProcessFormData(Long
					.valueOf(63));
			// Iterating the resultset data to display
			int count = resultSet.getRowCount();
			for (int i = 0; i < count; i++) {
				resultSet.goToRow(i);
				String columnNames[] = resultSet.getColumnNames();
				for (String fieldName : columnNames) {
					System.out.println(fieldName + " :: "
							+ resultSet.getStringValue(fieldName));
				}
			}

		} catch (tcAPIException | tcNotAtomicProcessException
				| tcFormNotFoundException | tcProcessNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws tcColumnNotFoundException {

		ReadProcessFormData form = new ReadProcessFormData();
		form.ProcessFormData();
	}
}
