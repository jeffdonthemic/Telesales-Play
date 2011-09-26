package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

import service.*;
import com.sforce.soap.partner.*;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.partner.sobject.SObject;

public class Account extends Controller {

    public static void create() {
        render();
    }

    public static void createSubmit() {
	
		SaveResult[] results = null;
			        		
        // populate the new opportunity
        SObject a = new SObject();
        a.setType("Account");
        a.setField("Name", params.get("name"));
        a.setField("BillingCity", params.get("city"));
		a.setField("BillingState", params.get("state"));
		a.setField("Phone", params.get("phone"));
		a.setField("Website", params.get("website"));
        
        SObject[] accounts = {a};
        
		// get a reference to the connection
		PartnerConnection connection = ConnectionManager.getConnectionManager().getConnection();
		
		try {
			results = connection.create(accounts);
			
			// check for any errors
			if ( results[0].isSuccess() ) {
			   System.out.println("Successfully created Account: " + results[0].getId());
			} else {
			   System.out.println("Error: could not create Accout: " + results[0].getErrors()[0].getMessage());
			}
			
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        redirect("/account/"+results[0].getId() );
    }

    public static void display() {

		if (params.get("id") != null) {
			
			// add the account id to the array to be retrieved
			String[] accountIds = { params.get("id") };

			SObject[] accounts = null;
			QueryResult result = null;
			
			// get a reference to the connection
			PartnerConnection connection = ConnectionManager.getConnectionManager().getConnection();
			
			try {
				accounts = connection.retrieve("Id, Name, Phone, BillingCity, BillingState, Website","Account", accountIds);
				result = connection.query("Select id, name, stagename, amount, closeDate, probability, ordernumber__c from Opportunity where AccountId = '"+params.get("id")+"'");
			} catch (ConnectionException e) {
				e.printStackTrace();
			} catch (NullPointerException npe) {
				System.out.println("NullPointerException: "+npe.getCause().toString());
			}
			
			renderArgs.put("account", accounts[0]);
			renderArgs.put("opportunities", result.getRecords());

		}

        render();
    }

    public static void edit() {

		if (params.get("id") != null) {
			
			// add the account id to the array to be retrieved
			String[] accountIds = { params.get("id") };
			SObject[] accounts = null;
			
			// get a reference to the connection
			PartnerConnection connection = ConnectionManager.getConnectionManager().getConnection();
			
			try {
				accounts = connection.retrieve("Id, Name, Phone, BillingCity, BillingState, Website","Account", accountIds);
			} catch (ConnectionException e) {
				e.printStackTrace();
			} catch (NullPointerException npe) {
				System.out.println("NullPointerException: "+npe.getCause().toString());
			}
			
			renderArgs.put("account", accounts[0]);

		}

        render();
    }

    public static void editSubmit() {
			        		
		SaveResult[] results = null;
			
        // populate the account to update
        SObject a = new SObject();
        a.setType("Account");
		a.setId(params.get("id"));
        a.setField("Billingcity", params.get("BillingCity"));
        
        SObject[] accounts = {a};
        
		// get a reference to the connection
		PartnerConnection connection = ConnectionManager.getConnectionManager().getConnection();
		
		try {
			results = connection.update(accounts);
			
			// check for any errors
			if ( results[0].isSuccess() ) {
			   System.out.println("Successfully updated Account: " + results[0].getId());
			} else {
			   System.out.println("Error: could not update Account: " + results[0].getErrors()[0].getMessage());
			}
			
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		redirect("/account/"+params.get("id") );
    }

}