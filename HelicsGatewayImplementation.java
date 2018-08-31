/* NOTE: The HelicsGatewayImplementation.java file was modified from the GatewayImplementation.java file (/ucef-gateway/test-federation/ExampleGateway/src/main/java/gov/nist/hla) that was downloaded as    part of the ucef-gateway repository.
   The ucef-gateway repository was cloned from here: https://github.com/usnistgov/ucef-gateway.git
   The code on the feature/refactor branch of the ucef-gateway repository was used.

   The License of the ucef-gateway repository has been copied in the "Third Party License" directory of this repo (file name is "LICENSE").
*/

/*
Copyright © 2017-2018,
Alliance for Sustainable Energy, LLC
All rights reserved.
*/

package gov.nist.hla;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nist.hla.gateway.GatewayCallback;
import gov.nist.hla.gateway.GatewayFederate;
import gov.nist.hla.gateway.GatewayFederateConfig;
import hla.rti.AttributeNotOwned;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotPublished;
import hla.rti.InvalidFederationTime;
import hla.rti.NameNotFound;
import hla.rti.ObjectAlreadyRegistered;
import hla.rti.ObjectClassNotPublished;
import hla.rti.ObjectNotKnown;

import com.java.helics.helics; // for HELICS-HLA Integration
import com.java.helics.SWIGTYPE_p_void;
import com.java.helics.helics_status;
import java.util.concurrent.TimeUnit;

public class HelicsGatewayImplementation implements GatewayCallback {
    private static final Logger log = LogManager.getLogger();

    private static final String TEST_INTERACTION = "InteractionRoot.C2WInteractionRoot.TestInteraction";

    private GatewayFederate gateway;

    private SWIGTYPE_p_void vFed; // all variables that would be needed to access a federate's pubs and subs. These include the federate object, and the subs and pubs objects.
    private SWIGTYPE_p_void[] subid;
    private SWIGTYPE_p_void[] pubid;
    private double[] grantedTime;
    private boolean isTimeAdvanced;
    private double[] val;

    private Map<String, String> objectState = new HashMap<String, String>();
    private Map<String, String> helicsSubValues = new HashMap<String,String>();
    private Map<String, String> UCEFSubValues = new HashMap<String,String>();
    private Map<String, SWIGTYPE_p_void> hlaHelicsPubidMap = new HashMap<String,SWIGTYPE_p_void>();

    public static void main(String[] args)
            throws IOException {
        if (args.length != 1) {
            log.error("missing command line argument for JSON configuration file");
            return;
        }

        System.loadLibrary("JNIhelics");
        System.out.println("HELICS Version: " + helics.helicsGetVersion());

        GatewayFederateConfig config = GatewayFederate.readConfiguration(args[0]);
        HelicsGatewayImplementation gatewayFederate = new HelicsGatewayImplementation(config);


        try {
        	gatewayFederate.createHelicsFederate();
        }
        catch(Exception e) {
        	System.out.println("exception occurred");
		try {
			System.in.read();
		}
		catch (Exception e1) {
		}

        }

        gatewayFederate.run();
    }

    public HelicsGatewayImplementation(GatewayFederateConfig configuration) {
        this.gateway = new GatewayFederate(configuration, this);
        grantedTime = new double[]{0.0};
        val = new double[]{0.0};
        pubid = new SWIGTYPE_p_void[4];
        subid = new SWIGTYPE_p_void[2];

        /* assigning default values to object attributes, which will be
         * sent to HELICs at time t=0 */
        helicsSubValues.put("HelicsHlaObject_sequenceNumber", "1");
        helicsSubValues.put("HelicsHlaObject_doubleValue", "2.0");
        helicsSubValues.put("HelicsHlaObject_intValue", "3");
        helicsSubValues.put("HelicsHlaObject_stringValue", "4");
    }

    public void run() {
        log.trace("run");
        gateway.run();
    }

    public void initializeSelf() {
        log.trace("initializeSelf");
    }

    public void initializeWithPeers() {
        log.trace("initializeWithPeers");
        try {
            //gateway.registerObjectInstance("ObjectRoot.TestObject", "GatewayObject");
        	gateway.registerObjectInstance("ObjectRoot.HelicsPublications", "HelicsPublicationsObject1");
        } catch (FederateNotExecutionMember | NameNotFound | ObjectClassNotPublished | ObjectAlreadyRegistered e) {
            throw new RuntimeException("failed to register object", e);
        }

    }

    /* The receiveInteraction callback is not expected to be used for HELICS-HLA integration at this time.
    This callback is kept as is to make the test-federation example in the ucef-gateway repository work.*/

    public void receiveInteraction(Double timeStep, String className, Map<String, String> parameters) {
        log.info(String.format("receiveInteraction %f %s %s", timeStep, className, parameters.toString()));

        if (className.equals("InteractionRoot.C2WInteractionRoot.SimulationControl.SimEnd")) {
            return;
        }

        Map<String, String> interactionValues = new HashMap<String, String>();
        interactionValues.put("sequenceNumber", parameters.get("sequenceNumber"));
        interactionValues.put("booleanValue", parameters.get("booleanValue"));
        interactionValues.put("doubleValue", parameters.get("doubleValue"));
        interactionValues.put("intValue", parameters.get("intValue"));
        interactionValues.put("stringValue", parameters.get("stringValue"));

        try {
            gateway.sendInteraction(TEST_INTERACTION, interactionValues, gateway.getTimeStamp());
            log.info(String.format("t=%f sent %s using %s", timeStep, TEST_INTERACTION, interactionValues.toString()));
        } catch (FederateNotExecutionMember | NameNotFound | InteractionClassNotPublished | InvalidFederationTime e) {
            throw new RuntimeException("failed to send interaction", e);
        }
    }

    public void receiveObject(Double timeStep, String className, String instanceName, Map<String, String> attributes) {
        log.info(String.format("receiveObject %f %s %s %s", timeStep, className, instanceName, attributes.toString()));
        // desired attributes received from Portico are copied into the map that will send data to HELICS
        //System.out.println("receiveObject called");
    	System.out.println("objects received at time: "+timeStep+" "+instanceName+"\n" );
    	/*for (String key: attributes.keySet()) {
        	String fullKeyName=instanceName+"_"+key;
        	if (helicsSubValues.containsKey(fullKeyName)) {
        		helicsSubValues.put(fullKeyName, attributes.get(fullKeyName));
        	}
        }*/

        if (className.startsWith("ObjectRoot.Manager.")) {
            // to demonstrate how to receive ObjectRoot.Manager.Federate (and other RTI managed objects)
            log.info("received RTI managed object {} ({}): {}", instanceName, className, attributes);
            return;
        }

        /*Storing the publications directed towards HELICS in helicsSubValues hash map */
        for (String name : attributes.keySet()){
        	String pubName=instanceName+"_"+name;
        	//System.out.println(pubName);
        	if (hlaHelicsPubidMap.containsKey(pubName) && attributes.get(name)!=null ) { // this check for null is required because the receiveObject function is called at each step, but the attributes can be empty if the object instances weren't updated by the Portico federates
        		helicsSubValues.put(pubName,attributes.get(name));

        	}
        }

        //log.info("received updated object values " + attributes.toString()+" from object instance " + instanceName + " classname"+ className);
    }

    public void doTimeStep(Double timeStep) {
        log.info("doTimeStep " + timeStep);
        //System.out.println("object values to be sent to Portico at time "+ timeStep+ " are: "+ objectState.toString());
        UCEFToHelicsTimeRequest(timeStep);
        try {
            gateway.updateObject("HelicsPublicationsObject1", objectState, gateway.getTimeStamp()); // GatewayObject is the name of the TestObject instance defined in the GatewayImplementation.xml FOM file that this gateway will publish and subscribe to.
            log.info(String.format("t=%f sent %s using %s", timeStep, "HelicsPublicationsObject1", objectState.toString()));
        } catch (FederateNotExecutionMember | ObjectNotKnown | NameNotFound | AttributeNotOwned
                | InvalidFederationTime e) {
            throw new RuntimeException("failed to update object", e);
        }


    }

    public void terminate() {
        log.trace("terminate");
    }

    public void createHelicsFederate() {
		SWIGTYPE_p_void fi = helics.helicsFederateInfoCreate();
		String coreInit="--federates=1";
		String fedName="GatewayMain";
		String coreName="zmq";
		double deltat=1.0;
		byte[] str = new byte[100];

		// creation of a value federate
		helics_status status = helics.helicsFederateInfoSetFederateName(fi, fedName);
		//System.out.println(status);
		status = helics.helicsFederateInfoSetCoreTypeFromString(fi, coreName);
		//System.out.println(status);
		status = helics.helicsFederateInfoSetCoreInitString(fi, coreInit);
		//System.out.println(status);
		status = helics.helicsFederateInfoSetTimeDelta(fi, deltat);
		//System.out.println(status);
		status = helics.helicsFederateInfoSetLoggingLevel(fi, 1);
		//System.out.println(status);
		vFed = helics.helicsCreateValueFederate(fi);
		System.out.println("value federate created");

		// Registering the subscription (data that we need to get from the HELICS federation)
		/* The subscriptions and their datatypes must be defined in the JSON file. They will have the same keys as the attributes of the
		 * HelicsPublications class */

		subid[0] = helics.helicsFederateRegisterSubscription(vFed, "doubleValue", "double","");
		subid[1] = helics.helicsFederateRegisterSubscription(vFed, "stringValue", "string","");
		System.out.println("subscriptions registered");

        UCEFSubValues.put("doubleValue", "1");
        UCEFSubValues.put("stringValue", "2.0");
        UCEFSubValues.forEach(objectState::putIfAbsent);
        System.out.println("default helics publications for UCEF added to the object instance attributes");

		/*status = helics.helicsSubscriptionGetKey(sub, str);

		String s = new String(str).trim();
		System.out.println(s);*/

		// registering the publication "testB" that can be subscribed by HELICS federates

        /* We can automate the objectInstance_attribute pair and pubid (publications for HELICS) mapping in this function
         * Since the HELICS publications bound for Portico are being sent only from this federate, we don't need to automate the
         * objectIntsance registeration for the HelicsPublications class. We can just hard code a name and let that be known to
         * the entire portico federation via the federation document.
         *
         * The order in which the Portico object class attributes are mapped to the pubids should be determined by the JSON file in
         * which the objectInstance names and the attribute names are listed. This should be the order when HELICS publications in
         * the gateway are created.
         *
         * The JSON file should also include the dataypes of the attributes that are being published or subscribed by HELICS
         *
         * The publications by HELICS for Portico can be have any key name as long as the role of the publication is well-defined because
         * HELICS currently doesn't provide a way for adding multiple attributes to a single publication, which makes it hard to decipher
         * what the publication key stands for. The HELICS publication (which will be denoted by subids in the gateway) keys will have the
         * the same names as the attributes of the HelicsPublications class
         * */
        pubid[0] = helics.helicsFederateRegisterGlobalPublication(vFed, "HelicsHlaObject_sequenceNumber","int","");
        pubid[1] = helics.helicsFederateRegisterGlobalPublication(vFed, "HelicsHlaObject_doubleValue","double","");
        pubid[2] = helics.helicsFederateRegisterGlobalPublication(vFed, "HelicsHlaObject_intValue","int","");
        pubid[3] = helics.helicsFederateRegisterGlobalPublication(vFed, "HelicsHlaObject_stringValue","string","");

        hlaHelicsPubidMap.put("HelicsHlaObject_sequenceNumber", pubid[0]);
        hlaHelicsPubidMap.put("HelicsHlaObject_doubleValue", pubid[1]);
        hlaHelicsPubidMap.put("HelicsHlaObject_intValue", pubid[2]);
        hlaHelicsPubidMap.put("HelicsHlaObject_stringValue", pubid[3]);
        System.out.println("pub map created");

        System.out.println("ready to initialize");

		// readying the value federate to participate in the HELICS federation
		status = helics.helicsFederateEnterInitializationMode(vFed);
		System.out.println("federate initialized");
		status = helics.helicsFederateEnterExecutionMode(vFed);
		//System.out.println(status);
    	}

	public void UCEFToHelicsTimeRequest(double requestedTime) {
		helics_status status;
		byte[] subKey = new byte[100];
		byte[] subType = new byte[100];
		byte[] pubType = new byte[100];
		int[] length = new int[1];

		//System.out.println("Value received from HELICS at "+ grantedTime[0] + " seconds is " + val[0]);


		while (grantedTime[0]<requestedTime) {
			status = helics.helicsFederateRequestTime(vFed, requestedTime, grantedTime);
			//System.out.println("Value received from HELICS at "+ grantedTime[0] + " seconds is " + val[0]);
		}




		/*publishing to helics*/
        for (String name : helicsSubValues.keySet()){
        	String value=helicsSubValues.get(name);
			status = helics.helicsPublicationGetType(hlaHelicsPubidMap.get(name),pubType);
			String tempTypeString = new String(pubType);
			String pubTypeString = tempTypeString.trim();
			//System.out.println(pubTypeString+" "+value);
			if (pubTypeString.equals("int64")) {
				long pubValue=Integer.parseInt(value);
				System.out.println("this integer value will be sent to HELICS : "+pubValue+ " at federate time "+grantedTime[0]);
				status = helics.helicsPublicationPublishInteger(hlaHelicsPubidMap.get(name),pubValue);
			}
			else if (pubTypeString.equals("double")) {
				double pubValue1=Double.parseDouble(value);
				System.out.println("this double value will be sent to HELICS: "+pubValue1+ " at federate time "+grantedTime[0]);
				status = helics.helicsPublicationPublishDouble(hlaHelicsPubidMap.get(name),pubValue1);
			}
			else if (pubTypeString.equals("string")) {
				status = helics.helicsPublicationPublishString(hlaHelicsPubidMap.get(name), value);
				System.out.println("this string value will be sent to HELICS: "+value+ " at federate time "+grantedTime[0]);
			}
        	//System.out.println("UCEF publication name is: "+name+" \n"+" and value is "+hlaHelicsPubidMap.get(name));
			//System.out.println(status +"means value successfully sent");
        }

		// synchronizing the HELICS and HLA federation times and getting values for the subscription from the HELICS federation
		/*while(requestedTime!=grantedTime[0]) {
			status = helics.helicsFederateRequestTime(vFed, requestedTime, grantedTime);
		}*/
        	/* As long as the gateway federate and helics federate time steps are equal, the while loop is not needed
        	 * because the helics time advance happens after the doTimeStep function is executed. So, helics time gets
        	 * the opportunity to advance by one time step and become equal to the gate federate time.*/


			/* receiving HLA subscriptions from HELICS*/
		for (int i=0; i<2;i++)
		{
			status = helics.helicsSubscriptionGetKey(subid[i],subKey);
			String temp = new String(subKey);
			String subKeyString = temp.trim();
			status = helics.helicsSubscriptionGetType(subid[i],subType);
			String tempTypeString = new String(subType);
			String subTypeString = tempTypeString.trim();
			//System.out.println(subKeyString + " " +subTypeString);
			int isUpdated = helics.helicsSubscriptionIsUpdated(subid[i]);
			//System.out.println(isUpdated);
			if ((subTypeString.equals("double")) && (isUpdated==1)) {
				double[] doubleVal=new double[1];
				status = helics.helicsSubscriptionGetDouble(subid[i], doubleVal);
				String stringEqvOfDoubleForUCEF = Double.toString(doubleVal[0]);
				UCEFSubValues.put(subKeyString.trim(),stringEqvOfDoubleForUCEF);
				objectState.put(subKeyString.trim(),stringEqvOfDoubleForUCEF);
				System.out.println("this double value is received from HELICS - "+stringEqvOfDoubleForUCEF+ " - at federate time "+grantedTime[0]);
			}
			else if ((subTypeString.equals("string")) && (isUpdated==1)) {
				byte[] tempString = new byte[100];
				status = helics.helicsSubscriptionGetString(subid[i], tempString, length);
				String stringValue = new String(tempString);
				String stringForUCEF=stringValue.trim();
				UCEFSubValues.put(subKeyString,stringForUCEF);
				objectState.put(subKeyString,stringForUCEF);
				System.out.println("this string value is received from HELICS - "+stringForUCEF+ " - at federate time "+grantedTime[0]);
			}
		}
		try {
    		TimeUnit.MILLISECONDS.sleep(500);
		}
		catch(InterruptedException e) {
    		System.out.println("exception occurred");
		}
	}
}
