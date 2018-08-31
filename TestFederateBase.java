/*Certain portions of this software are Copyright (c) 2006-2017 Vanderbilt University, Institute for Software Integrated Systems
Certain portions of this software are contributed as a public service by The National Institute of Standards and Technology (NIST) and are not subject to U.S. Copyright

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above Vanderbilt University copyright notice, NIST contribution notice and this permission and disclaimer notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. THE AUTHORS OR COPYRIGHT HOLDERS SHALL NOT HAVE ANY OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

package GatewayTest;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

import org.cpswt.hla.C2WInteractionRoot;
import org.cpswt.hla.InteractionRoot;
import org.cpswt.hla.SubscribedInteractionFilter;
import org.cpswt.hla.SynchronizedFederate;

import org.cpswt.config.FederateConfig;

import org.cpswt.*;

public class TestFederateBase extends SynchronizedFederate {

	private SubscribedInteractionFilter _subscribedInteractionFilter = new SubscribedInteractionFilter();

	// constructor
	public TestFederateBase(FederateConfig config) throws Exception {
		super(config);

		super.createLRC();
		super.joinFederation();

		enableTimeConstrained();

		enableTimeRegulation(getLookAhead());
		enableAsynchronousDelivery();
        // interaction pubsub

        TestInteraction.publish(getLRC());

        TestInteraction.subscribe(getLRC());
        _subscribedInteractionFilter.setFedFilters(
			TestInteraction.get_handle(),
			SubscribedInteractionFilter.OriginFedFilter.ORIGIN_FILTER_DISABLED,
			SubscribedInteractionFilter.SourceFedFilter.SOURCE_FILTER_DISABLED
		);
		// object pubsub


        TestObject.publish_booleanValue();
        TestObject.publish_doubleValue();
        TestObject.publish_intValue();
        TestObject.publish_sequenceNumber();
        TestObject.publish_stringValue();
        TestObject.publish(getLRC());


        HelicsPublications.publish_doubleValue();
        HelicsPublications.publish_stringValue();
        HelicsPublications.publish(getLRC());


        HelicsPublications.subscribe_doubleValue();
        HelicsPublications.subscribe_stringValue();
        HelicsPublications.subscribe(getLRC());


        TestObject.subscribe_booleanValue();
        TestObject.subscribe_doubleValue();
        TestObject.subscribe_intValue();
        TestObject.subscribe_sequenceNumber();
        TestObject.subscribe_stringValue();
        TestObject.subscribe(getLRC());
        	}


	public TestInteraction create_TestInteraction() {
	   TestInteraction interaction = new TestInteraction();
	   interaction.set_sourceFed( getFederateId() );
	   interaction.set_originFed( getFederateId() );
	   return interaction;
	}
	@Override
	public void receiveInteraction(
	 int interactionClass, ReceivedInteraction theInteraction, byte[] userSuppliedTag
	) {
		InteractionRoot interactionRoot = InteractionRoot.create_interaction( interactionClass, theInteraction );
		if ( interactionRoot instanceof C2WInteractionRoot ) {

			C2WInteractionRoot c2wInteractionRoot = (C2WInteractionRoot)interactionRoot;

	        // Filter interaction if src/origin fed requirements (if any) are not met
	        if (  _subscribedInteractionFilter.filterC2WInteraction( getFederateId(), c2wInteractionRoot )  ) {
	        	return;
	        }
		}

		super.receiveInteraction( interactionClass, theInteraction, userSuppliedTag );
	}

	@Override
	public void receiveInteraction(
	 int interactionClass,
	 ReceivedInteraction theInteraction,
	 byte[] userSuppliedTag,
	 LogicalTime theTime,
	 EventRetractionHandle retractionHandle
	) {
		InteractionRoot interactionRoot = InteractionRoot.create_interaction( interactionClass, theInteraction, theTime );
		if ( interactionRoot instanceof C2WInteractionRoot ) {

			C2WInteractionRoot c2wInteractionRoot = (C2WInteractionRoot)interactionRoot;

	        // Filter interaction if src/origin fed requirements (if any) are not met
	        if (  _subscribedInteractionFilter.filterC2WInteraction( getFederateId(), c2wInteractionRoot )  ) {
	        	return;
	        }
		}

		super.receiveInteraction( interactionClass, theInteraction, userSuppliedTag, theTime, retractionHandle );
	}
}

