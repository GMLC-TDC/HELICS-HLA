/*
Copyright © 2017-2018,
Alliance for Sustainable Energy, LLC
All rights reserved.
*/

static char help[] = "Single Federate HELICS Federation to Demonstrate the working of the HELICS-HLA Bridge.\n\n";

#include <stdio.h>
#include <iostream>
#include <cpp98/ValueFederate.hpp>
#include <cpp98/Broker.hpp>
#include <cpp98/helics.hpp> // helicsVersionString
#include <math.h>
#ifdef _MSC_VER
#include <windows.h>
#else
#include <unistd.h>
#endif

int main(int /*argc*/,char ** /*argv*/)
{
  std::string    initstring="2 --name=mainbroker";
  std::string    fedinitstring="--broker=mainbroker --federates=1";
  double         deltat=1.0;
  helics_time_t currenttime=0.0;

  std::string helicsversion = helics::getHelicsVersionString();

  printf("%s",help);

  std::string dataType, strForHLA;
  helics::Subscription sub[4];
  helics::Publication  pub[2];

  /* Create broker */
  helics::Broker broker("zmq","",initstring);

  if(broker.isConnected()) {
    std::cout<< "helics_fed1: Broker created and connected\n";
  }

  /* Federate Initialization */

  helics::FederateInfo fi("HELICSTestFederate", "zmq");
  fi.setCoreInitString(fedinitstring);
  fi.setTimeDelta(deltat);
  fi.setLoggingLevel(1);

  helics::ValueFederate* vfed = new helics::ValueFederate(fi);
  std::cout<< "HELICSTestFederate created\n";

  fflush(NULL);

  /* Subscription declaration*/

  sub[0] = vfed->registerSubscription("HelicsHlaObject_sequenceNumber","int","");
  sub[1] = vfed->registerSubscription("HelicsHlaObject_doubleValue","double","");
  sub[2] = vfed->registerSubscription("HelicsHlaObject_intValue","int","");
  sub[3] = vfed->registerSubscription("HelicsHlaObject_stringValue","string","");
  std::cout<< "subscriptions registered \n";

  /* Publications declaration*/

  pub[0] = vfed->registerGlobalPublication("doubleValue","double","");
  pub[1] = vfed->registerGlobalPublication("stringValue","string","");
  std::cout<< "publications registered \n";


  vfed->enterInitializationMode(); // can throw helics::InvalidStateTransition exception
  std::cout<< "HELICSTestFederate Entered initialization state \n";

  vfed->enterExecutionMode(); //can throw helics::InvalidStateTransition exception
  std::cout<< "HELICSTestFederate Entered execution state \n";

  double time1=0.0;
  int isupdated;
  double doubleValue;
  double requestedtime=0.0;
  while(currenttime < 5000) {


   while (currenttime < requestedtime)   // time synchronization with the HLA Federation
   {
	currenttime = vfed->requestTime(currenttime);

   }
      requestedtime=currenttime+deltat;

    /* All HLA publications are received here.
       The "4" in the for loop is the number of publications of interest from HLA
       This could be made an entry in the configuration file so that HELICS knows
       how many HLA publications are of interest without modifying the code.*/

   for (int i=0;i<4;i++)
	{


    	    isupdated = sub[i].isUpdated();
            //std::cout<<isupdated<<"\n";
    	    /*if(isupdated)
 	        {
		    std::cout<<"subscription update status: " <<isupdated<<"\n";
                }*/
	    dataType = sub[i].getType();
	    if (std::strcmp(dataType.c_str(),"string")==0) {
		std::string stringValue = sub[i].getString();
		printf("helics_fed1: Received String value = %s at time %3.2f from HLA Federation \n",stringValue.c_str(),currenttime);
	    }
	    else if (std::strcmp(dataType.c_str(),"double")==0) {
		doubleValue = sub[i].getDouble();
		printf("helics_fed1: Received Double value = %4.4f at time %3.2f from HLA Federation \n",doubleValue,currenttime);
            }
	    else if (std::strcmp(dataType.c_str(),"int64")==0) {
		int64_t integerValue = sub[i].getInteger();
		printf("helics_fed1: Received Integer value = %ld at time %3.2f from HLA Federation \n",integerValue,currenttime);
	    }
	    else {
		printf("data type undefined \n");
	    }
	}

   /* HELICS Publications for HLA are sent from here */
   time1=time1+1.0;
   pub[0].publish(doubleValue);
   strForHLA =  "HELICS time when the string was sent was " + std::to_string(currenttime) + " seconds";
   pub[1].publish(strForHLA);
   printf("helics_fed1: Sent Double value = %4.4f at time %3.2f to HLA Federation \n",doubleValue,currenttime);
   printf("helics_fed1: Sent String value = %s at time %3.2f to HLA Federation \n\n",strForHLA.c_str(),currenttime);

   std::cout<<"Press any key to continue \n\n";
   std::cin.get();

  }

  vfed->finalize();
  printf("helics_fed1: Federate finalized\n");
  fflush(NULL);
  // Destructor must be called for ValueFederate before close library
  delete vfed;
  helicsCloseLibrary();
  printf("Library Closed\n");
  fflush(NULL);
  return(0);
}

