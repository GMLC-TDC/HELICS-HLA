/* NOTE: The TestFederate.java file was modified from the original TestFederate.java file (/ucef-gateway/test-federation/GatewayTest_generated/GatewayTest-java-federates/GatewayTest-impl-java/TestFederate/src/main/java/GatewayTest) that was downloaded as part of the ucef-gateway repository.

   The ucef-gateway repository was cloned from here: https://github.com/usnistgov/ucef-gateway.git
   The code on the feature/refactor branch of the ucef-gateway repository was used.

   The License of the ucef-gateway repository has been copied in the "Third Party License" directory of this repo (file name is "LICENSE").
*/

/*
Copyright (c) 2017-2018,
Alliance for Sustainable Energy, LLC
All rights reserved.
*/

package GatewayTest;

import org.cpswt.config.FederateConfig;
import org.cpswt.config.FederateConfigParser;
import org.cpswt.hla.base.ObjectReflector;
import org.cpswt.hla.ObjectRoot;
import org.cpswt.hla.InteractionRoot;
import org.cpswt.hla.base.AdvanceTimeRequest;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The TestFederate type of federate for the federation designed in WebGME.
 *
 */
public class TestFederate extends TestFederateBase {

    private final static Logger log = LogManager.getLogger(TestFederate.class);

    private static final String VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    double currentTime = 0;

    TestObject testObjectInstance = new TestObject();

    boolean booleanValue;

    double doubleValue;

    int intValue;
    int sequenceNumber = 0;
    int lastInteraction = 0;
    int lastObjectUpdate = 0;

    String stringValue;

    public TestFederate(FederateConfig params) throws Exception {
        super(params);
	/* HELICS-HLA Modifications Begin */
        testObjectInstance.registerObject(getLRC(),"HelicsHlaObject");

	/* HELICS-HLA Modifications End */
    }

    private void CheckReceivedSubscriptions(String s) {

        InteractionRoot interaction = null;
        while ((interaction = getNextInteractionNoWait()) != null) {
            if (interaction instanceof TestInteraction) {
                handleInteractionClass((TestInteraction) interaction);
            }
        }

        ObjectReflector reflector = null;
        while ((reflector = getNextObjectReflectorNoWait()) != null) {
            reflector.reflect();
            ObjectRoot object = reflector.getObjectRoot();
	    /* HELICS-HLA Modifications Begin */
            //if (object instanceof TestObject) {
            //     handleObjectClass((TestObject) object);
	      if (object instanceof HelicsPublications) {
		handleObjectClass((HelicsPublications) object);

	    /* HELICS-HLA Modifications End */
            }
        }
    }

    private void execute() throws Exception {
        if(super.isLateJoiner()) {
            currentTime = super.getLBTS() - super.getLookAhead();
            super.disableTimeRegulation();
        }

        AdvanceTimeRequest atr = new AdvanceTimeRequest(currentTime);
        putAdvanceTimeRequest(atr);

        // check if parameter handles match for child and parent interactions
        int interactionRoot = getLRC().getInteractionClassHandle("InteractionRoot.C2WInteractionRoot");
        int federateJoin = getLRC().getInteractionClassHandle(
                "InteractionRoot.C2WInteractionRoot.FederateJoinInteraction");
        int sourceFedR = getLRC().getParameterHandle("sourceFed", interactionRoot);
        int sourceFedJ = getLRC().getParameterHandle("sourceFed", federateJoin);
        log.debug(String.format("C2WInteractionRoot.sourceFed=%d : FederateJoin.sourceFed=%d", sourceFedR, sourceFedJ));

        if(!super.isLateJoiner()) {
            readyToPopulate();
        }

        if(!super.isLateJoiner()) {
            readyToRun();
        }

        startAdvanceTimeThread();

        // this is the exit condition of the following while loop
        // it is used to break the loop so that latejoiner federates can
        // notify the federation manager that they left the federation
        boolean exitCondition = false;

        while (true) {
            atr.requestSyncStart();
            enteredTimeGrantedState();
            log.info("t = " + currentTime);

            CheckReceivedSubscriptions("Main Loop");

            /* HELICS-HLA Modifications Begin */
            //if (lastInteraction == sequenceNumber) { // && lastObjectUpdate == sequenceNumber) {
                sendNextValues();
            //}
            /* HELICS-HLA Modifications End */

            currentTime += super.getStepSize();
            AdvanceTimeRequest newATR = new AdvanceTimeRequest(currentTime);
            putAdvanceTimeRequest(newATR);
            atr.requestSyncEnd();
            atr = newATR;

            if(exitCondition) {
                break;
            }
        }

        // while loop finished, notify FederationManager about resign
        super.notifyFederationOfResign();
    }

    private void handleInteractionClass(TestInteraction interaction) {
        log.info("received TestInteraction");
        checkSequenceNumber(interaction.get_sequenceNumber());
        checkBooleanValue(interaction.get_booleanValue());
        checkDoubleValue(interaction.get_doubleValue());
        checkIntValue(interaction.get_intValue());
        checkStringValue(interaction.get_stringValue());
        lastInteraction = interaction.get_sequenceNumber();

        // see what happens to the value when the parameter handles don't match
        log.debug("sourceFed received as " + interaction.get_sourceFed());
    }

    /* HELICS-HLA Modifications Begin */

    /*
    private void handleObjectClass(TestObject object) {
        log.info("received TestObject reflection");
        checkSequenceNumber(object.get_sequenceNumber());
        checkBooleanValue(object.get_booleanValue());
        checkDoubleValue(object.get_doubleValue());
        checkIntValue(object.get_intValue());
        checkStringValue(object.get_stringValue());
        lastObjectUpdate = object.get_sequenceNumber();
    }*/
    private void handleObjectClass(HelicsPublications object) {
        log.info("received HelicsPublications reflection \n");
  	log.info("\treceived double from HELICS at time " + currentTime + " seconds is: "+ object.get_doubleValue()+"\n");
        log.info("\treceived string from HELICS at time " + currentTime + " seconds is: "+ object.get_stringValue()+"\n");
        //checkDoubleValue(object.get_doubleValue());
        //checkStringValue(object.get_stringValue());
    }

    /* HELICS-HLA Modifications End */

    private String generateStringValue() {
        StringBuffer buffer = new StringBuffer(64);
        for (int i = 0; i < 64; i++) {
            buffer.append(VALID_CHARACTERS.charAt(ThreadLocalRandom.current().nextInt(VALID_CHARACTERS.length())));
        }
        return buffer.toString();
    }

    private void sendNextValues()
            throws Exception {
        booleanValue = ThreadLocalRandom.current().nextBoolean();
        doubleValue = ThreadLocalRandom.current().nextDouble(1000);
        intValue = ThreadLocalRandom.current().nextInt(10000);
        stringValue = generateStringValue();
        sequenceNumber = sequenceNumber + 1;

        TestInteraction testInteraction = create_TestInteraction();
        testInteraction.set_sequenceNumber(sequenceNumber);
        testInteraction.set_booleanValue(booleanValue);
        testInteraction.set_doubleValue(doubleValue);
        testInteraction.set_intValue(intValue);
        testInteraction.set_stringValue(stringValue);
        testInteraction.sendInteraction(getLRC(), currentTime + super.getLookAhead());
        log.info("sent " + testInteraction.toString());

        testObjectInstance.set_sequenceNumber(sequenceNumber);
        testObjectInstance.set_booleanValue(booleanValue);
        testObjectInstance.set_doubleValue(doubleValue);
        testObjectInstance.set_intValue(intValue);
        testObjectInstance.set_stringValue(stringValue);
        testObjectInstance.updateAttributeValues(getLRC(), currentTime + super.getLookAhead());
        log.info("sent " + testObjectInstance.toString());
    }

    private void checkBooleanValue(boolean value) {
        log.info("\treceived boolean " + value);
        log.info("\texpected boolean " + booleanValue);
        if (value != booleanValue) {
            log.error("FAILED - boolean value incorrect");
            //throw new RuntimeException("boolean value incorrect");
        }
    }

    private void checkDoubleValue(double value) {
        log.info("\treceived double " + value);
        log.info("\texpected double " + doubleValue);
        if (value != doubleValue) {
            log.error("FAILED - double value incorrect");
            //throw new RuntimeException("double value incorrect");
        }
    }

    private void checkIntValue(int value) {
        log.info("\treceived int " + value);
        log.info("\texpected int " + intValue);
        if (value != intValue) {
            log.error("FAILED - int value incorrect");
            //throw new RuntimeException("int value incorrect");
        }
    }

    private void checkStringValue(String value) {
        log.info(String.format("\treceived string %s (%d)", value, value.length()));
        log.info(String.format("\texpected string %s (%d)", stringValue, stringValue.length()));
        if (!value.equals(stringValue)) {
            log.error("FAILED - string value incorrect");
            //throw new RuntimeException("string value incorrect");
        }
    }

    private void checkSequenceNumber(int value) {
        log.info("\treceived sequence number " + value);
        log.info("\texpected sequence number " + sequenceNumber);
        if (value != sequenceNumber) {
            log.error("FAILED - sequence number incorrect");
            //throw new RuntimeException("sequence number incorrect");
        }
    }

    public static void main(String[] args) {
        log.info(args);
        try {
            FederateConfigParser federateConfigParser = new FederateConfigParser();
            FederateConfig federateConfig = federateConfigParser.parseArgs(args, FederateConfig.class);
            TestFederate federate = new TestFederate(federateConfig);
            federate.execute();

            System.exit(0);
        } catch (Exception e) {
            log.error("There was a problem executing the TestFederate federate: {}", e.getMessage());
            log.error(e);

            System.exit(1);
        }
    }
}
