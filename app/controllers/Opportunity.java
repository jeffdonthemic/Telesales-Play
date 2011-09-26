package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import models.*;

import service.*;
import com.sforce.soap.partner.*;
import com.sforce.ws.ConnectionException;
import com.sforce.soap.partner.sobject.SObject;

import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Opportunity extends Controller {
	
    public static void index() {
	
		if (params.get("name") != null) {
			
			// get a reference to the connection
			PartnerConnection connection = ConnectionManager.getConnectionManager().getConnection();
			
			QueryResult result = null;
			try {
				result = connection.query("Select Id, Name, Phone, BillingCity, BillingState from Account Where Name = '"+params.get("name")+"'");
			} catch (ConnectionException e) {
				e.printStackTrace();
			} catch (NullPointerException npe) {
				System.out.println("NullPointerException: "+npe.getCause().toString());
			}
			
			System.out.println("Records found for "+params.get("name")+": "+result.getSize());
			
			// add the results of the search to the context			
			renderArgs.put("accounts", result.getRecords());

		}

		render();

    }

	public static void create() {

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
			
			// add the new account id to the context
			renderArgs.put("account", accounts[0]);

		}

        render();

	}
	
	public static void createSubmit() {
		
		SaveResult[] results = null;
		
		try {
			        		
 	       // populate the new opportunity
	        SObject opp = new SObject();
	        opp.setType("Opportunity");
	        opp.setField("Name", params.get("name"));
	        opp.setField("Amount", new Double(params.get("amount")));
	        opp.setField("StageName", params.get("stageName"));
	        opp.setField("Probability", new Double(params.get("probability")));
	        opp.setField("CloseDate", new SimpleDateFormat("yyyy-MM-dd").parse(params.get("closeDate")));
	        opp.setField("OrderNumber__c", String.valueOf(params.get("orderNumber")));
	        opp.setField("AccountId", params.get("id"));
        
			// add the opportunity sobject to the array for the create command
	        SObject[] opportunities = {opp};
        
			// get a reference to the connection
			PartnerConnection connection = ConnectionManager.getConnectionManager().getConnection();

			// create the opportunity
			results = connection.create(opportunities);
			
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException pe) {
			// TODO Auto-generated catch block
			pe.printStackTrace();
		}
		
		redirect("/account/"+params.get("id") );
		
	}

}