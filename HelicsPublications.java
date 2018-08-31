/*Certain portions of this software are Copyright (c) 2006-2017 Vanderbilt University, Institute for Software Integrated Systems
Certain portions of this software are contributed as a public service by The National Institute of Standards and Technology (NIST) and are not subject to U.S. Copyright

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above Vanderbilt University copyright notice, NIST contribution notice and this permission and disclaimer notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. THE AUTHORS OR COPYRIGHT HOLDERS SHALL NOT HAVE ANY OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/


package GatewayTest;

import java.util.HashSet;
import java.util.Set;

import hla.rti.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cpswt.utils.CpswtUtils;


import org.cpswt.hla.*;

/**
* The HelicsPublications class implements the HelicsPublications object in the
* GatewayTest simulation.
*/
public class HelicsPublications extends ObjectRoot {

	private static final Logger logger = LogManager.getLogger(HelicsPublications.class);

	/**
	* Default constructor -- creates an instance of the HelicsPublications object
	* class with default attribute values.
	*/
	public HelicsPublications() { }



	private static int _doubleValue_handle;
	private static int _stringValue_handle;


	/**
	* Returns the handle (RTI assigned) of the "doubleValue" attribute of
	* its containing object class.
	*
	* @return the handle (RTI assigned) of the "doubleValue" attribute
	*/
	public static int get_doubleValue_handle() { return _doubleValue_handle; }

	/**
	* Returns the handle (RTI assigned) of the "stringValue" attribute of
	* its containing object class.
	*
	* @return the handle (RTI assigned) of the "stringValue" attribute
	*/
	public static int get_stringValue_handle() { return _stringValue_handle; }



	private static boolean _isInitialized = false;

	private static int _handle;

	/**
	* Returns the handle (RTI assigned) of the HelicsPublications object class.
	* Note: As this is a static method, it is NOT polymorphic, and so, if called on
	* a reference will return the handle of the class pertaining to the reference,\
	* rather than the handle of the class for the instance referred to by the reference.
	* For the polymorphic version of this method, use {@link #getClassHandle()}.
	*/
	public static int get_handle() { return _handle; }

	/**
	* Returns the fully-qualified (dot-delimited) name of the HelicsPublications
	* object class.
	* Note: As this is a static method, it is NOT polymorphic, and so, if called on
	* a reference will return the name of the class pertaining to the reference,\
	* rather than the name of the class for the instance referred to by the reference.
	* For the polymorphic version of this method, use {@link #getClassName()}.
	*/
	public static String get_class_name() { return "ObjectRoot.HelicsPublications"; }

	/**
	* Returns the simple name (the last name in the dot-delimited fully-qualified
	* class name) of the HelicsPublications object class.
	*/
	public static String get_simple_class_name() { return "HelicsPublications"; }

	private static Set< String > _datamemberNames = new HashSet< String >();
	private static Set< String > _allDatamemberNames = new HashSet< String >();

	/**
	* Returns a set containing the names of all of the non-hidden attributes in the
	* HelicsPublications object class.
	* Note: As this is a static method, it is NOT polymorphic, and so, if called on
	* a reference will return a set of parameter names pertaining to the reference,\
	* rather than the parameter names of the class for the instance referred to by
	* the reference.  For the polymorphic version of this method, use
	* {@link #getAttributeNames()}.
	*/
	public static Set< String > get_attribute_names() {
		return new HashSet< String >(_datamemberNames);
	}


	/**
	* Returns a set containing the names of all of the attributes in the
	* HelicsPublications object class.
	* Note: As this is a static method, it is NOT polymorphic, and so, if called on
	* a reference will return a set of parameter names pertaining to the reference,\
	* rather than the parameter names of the class for the instance referred to by
	* the reference.  For the polymorphic version of this method, use
	* {@link #getAttributeNames()}.
	*/
	public static Set< String > get_all_attribute_names() {
		return new HashSet< String >(_allDatamemberNames);
	}



	private static AttributeHandleSet _publishedAttributeHandleSet;
	private static Set< String > _publishAttributeNameSet = new HashSet< String >();

	private static AttributeHandleSet _subscribedAttributeHandleSet;
	private static Set< String > _subscribeAttributeNameSet = new HashSet< String >();



	static {
		_classNameSet.add("ObjectRoot.HelicsPublications");
		_classNameClassMap.put("ObjectRoot.HelicsPublications", HelicsPublications.class);

		_datamemberClassNameSetMap.put("ObjectRoot.HelicsPublications", _datamemberNames);
		_allDatamemberClassNameSetMap.put("ObjectRoot.HelicsPublications", _allDatamemberNames);



		_datamemberNames.add("doubleValue");
		_datamemberNames.add("stringValue");


		_allDatamemberNames.add("doubleValue");
		_allDatamemberNames.add("stringValue");


		_datamemberTypeMap.put("doubleValue", "double");
		_datamemberTypeMap.put("stringValue", "String");


		_classNamePublishAttributeNameMap.put("ObjectRoot.HelicsPublications", _publishAttributeNameSet);
		_publishedAttributeHandleSet = _factory.createAttributeHandleSet();
		_classNamePublishedAttributeMap.put("ObjectRoot.HelicsPublications", _publishedAttributeHandleSet);

		_classNameSubscribeAttributeNameMap.put("ObjectRoot.HelicsPublications", _subscribeAttributeNameSet);
		_subscribedAttributeHandleSet = _factory.createAttributeHandleSet();
		_classNameSubscribedAttributeMap.put("ObjectRoot.HelicsPublications", _subscribedAttributeHandleSet);


	}


	private static String initErrorMessage = "Error:  ObjectRoot.HelicsPublications:  could not initialize:  ";
	protected static void init(RTIambassador rti) {
		if (_isInitialized) return;
		_isInitialized = true;

		ObjectRoot.init(rti);

		boolean isNotInitialized = true;
		while(isNotInitialized) {
			try {
				_handle = rti.getObjectClassHandle("ObjectRoot.HelicsPublications");
				isNotInitialized = false;
			} catch (FederateNotExecutionMember f) {
				logger.error("{} Federate Not Execution Member", initErrorMessage);
				logger.error(f);
				return;
			} catch (NameNotFound n) {
				logger.error("{} Name Not Found", initErrorMessage);
				logger.error(n);
				return;
			} catch (Exception e) {
				logger.error(e);
				CpswtUtils.sleepDefault();
			}
		}

		_classNameHandleMap.put("ObjectRoot.HelicsPublications", get_handle());
		_classHandleNameMap.put(get_handle(), "ObjectRoot.HelicsPublications");
		_classHandleSimpleNameMap.put(get_handle(), "HelicsPublications");


		isNotInitialized = true;
		while(isNotInitialized) {
			try {

				_doubleValue_handle = rti.getAttributeHandle("doubleValue", get_handle());
				_stringValue_handle = rti.getAttributeHandle("stringValue", get_handle());
				isNotInitialized = false;
			} catch (FederateNotExecutionMember f) {
				logger.error("{} Federate Not Execution Member", initErrorMessage);
				logger.error(f);
				return;
			} catch (ObjectClassNotDefined i) {
				logger.error("{} Object Class Not Defined", initErrorMessage);
				logger.error(i);
				return;
			} catch (NameNotFound n) {
				logger.error("{} Name Not Found", initErrorMessage);
				logger.error(n);
				return;
			} catch (Exception e) {
				logger.error(e);
				CpswtUtils.sleepDefault();
			}
		}


		_datamemberNameHandleMap.put("ObjectRoot.HelicsPublications,doubleValue", get_doubleValue_handle());
		_datamemberNameHandleMap.put("ObjectRoot.HelicsPublications,stringValue", get_stringValue_handle());


		_datamemberHandleNameMap.put(get_doubleValue_handle(), "doubleValue");
		_datamemberHandleNameMap.put(get_stringValue_handle(), "stringValue");

	}

	private static boolean _isPublished = false;
	private static String publishErrorMessage = "Error:  ObjectRoot.HelicsPublications:  could not publish:  ";

	/**
	* Publishes the HelicsPublications object class for a federate.
	*
	* @param rti handle to the Local RTI Component
	*/
	public static void publish(RTIambassador rti) {
		if (_isPublished) return;

		init(rti);


		_publishedAttributeHandleSet.empty();
		for(String attributeName : _publishAttributeNameSet) {
			try {
				_publishedAttributeHandleSet.add(_datamemberNameHandleMap.get("ObjectRoot.HelicsPublications," + attributeName));
			} catch (Exception e) {
				logger.error("{} Could not publish \"" + attributeName + "\" attribute.", publishErrorMessage);
			}
		}


		synchronized(rti) {
			boolean isNotPublished = true;
			while(isNotPublished) {
				try {
					rti.publishObjectClass(get_handle(), _publishedAttributeHandleSet);
					isNotPublished = false;
				} catch (FederateNotExecutionMember f) {
					logger.error("{} Federate Not Execution Member", publishErrorMessage);
					logger.error(f);
					return;
				} catch (ObjectClassNotDefined i) {
					logger.error("{} Object Class Not Defined", publishErrorMessage);
					logger.error(i);
					return;
				} catch (Exception e) {
					logger.error(e);
					CpswtUtils.sleepDefault();
				}
			}
		}

		_isPublished = true;
	}

	private static String unpublishErrorMessage = "Error:  ObjectRoot.HelicsPublications:  could not unpublish:  ";
	/**
	* Unpublishes the HelicsPublications object class for a federate.
	*
	* @param rti handle to the Local RTI Component
	*/
	public static void unpublish(RTIambassador rti) {
		if (!_isPublished) return;

		init(rti);
		synchronized(rti) {
			boolean isNotUnpublished = true;
			while(isNotUnpublished) {
				try {
					rti.unpublishObjectClass(get_handle());
					isNotUnpublished = false;
				} catch (FederateNotExecutionMember f) {
					logger.error("{} Federate Not Execution Member", unpublishErrorMessage);
					logger.error(f);
					return;
				} catch (ObjectClassNotDefined i) {
					logger.error("{} Object Class Not Defined", unpublishErrorMessage);
					logger.error(i);
					return;
				} catch (ObjectClassNotPublished i) {
					logger.error("{} Object Class Not Published", unpublishErrorMessage);
					logger.error(i);
					return;
				} catch (Exception e) {
					logger.error(e);
					CpswtUtils.sleepDefault();
				}
			}
		}

		_isPublished = false;
	}

	private static boolean _isSubscribed = false;
	private static String subscribeErrorMessage = "Error:  ObjectRoot.HelicsPublications:  could not subscribe:  ";
	/**
	* Subscribes a federate to the HelicsPublications object class.
	*
	* @param rti handle to the Local RTI Component
	*/
	public static void subscribe(RTIambassador rti) {
		if (_isSubscribed) return;

		init(rti);

		_subscribedAttributeHandleSet.empty();
		for(String attributeName : _subscribeAttributeNameSet) {
			try {
				_subscribedAttributeHandleSet.add(_datamemberNameHandleMap.get("ObjectRoot.HelicsPublications," + attributeName));
			} catch (Exception e) {
				logger.error("{} Could not subscribe to \"" + attributeName + "\" attribute.", subscribeErrorMessage);
			}
		}


		synchronized(rti) {
			boolean isNotSubscribed = true;
			while(isNotSubscribed) {
				try {
					rti.subscribeObjectClassAttributes(get_handle(), _subscribedAttributeHandleSet);
					isNotSubscribed = false;
				} catch (FederateNotExecutionMember f) {
					logger.error("{} Federate Not Execution Member", subscribeErrorMessage);
					logger.error(f);
					return;
				} catch (ObjectClassNotDefined i) {
					logger.error("{} Object Class Not Defined", subscribeErrorMessage);
					logger.error(i);
					return;
				} catch (Exception e) {
					logger.error(e);
					CpswtUtils.sleepDefault();
				}
			}
		}

		_isSubscribed = true;
	}

	private static String unsubscribeErrorMessage = "Error:  ObjectRoot.HelicsPublications:  could not unsubscribe:  ";
	/**
	* Unsubscribes a federate from the HelicsPublications object class.
	*
	* @param rti handle to the Local RTI Component
	*/
	public static void unsubscribe(RTIambassador rti) {
		if (!_isSubscribed) return;

		init(rti);
		synchronized(rti) {
			boolean isNotUnsubscribed = true;
			while(isNotUnsubscribed) {
				try {
					rti.unsubscribeObjectClass(get_handle());
					isNotUnsubscribed = false;
				} catch (FederateNotExecutionMember f) {
					logger.error("{} Federate Not Execution Member", unsubscribeErrorMessage);
					logger.error(f);
					return;
				} catch (ObjectClassNotDefined i) {
					logger.error("{} Object Class Not Defined", unsubscribeErrorMessage);
					logger.error(i);
					return;
				} catch (ObjectClassNotSubscribed i) {
					logger.error("{} Object Class Not Subscribed", unsubscribeErrorMessage);
					logger.error(i);
					return;
				} catch (Exception e) {
					logger.error(e);
					CpswtUtils.sleepDefault();
				}
			}
		}

		_isSubscribed = false;
	}

	/**
	* Return true if "handle" is equal to the handle (RTI assigned) of this class
	* (that is, the HelicsPublications object class).
	*
	* @param handle handle to compare to the value of the handle (RTI assigned) of
	* this class (the HelicsPublications object class).
	* @return "true" if "handle" matches the value of the handle of this class
	* (that is, the HelicsPublications object class).
	*/
	public static boolean match(int handle) { return handle == get_handle(); }

	/**
	* Returns the handle (RTI assigned) of this instance's object class .
	*
	* @return the handle (RTI assigned) if this instance's object class
	*/
	public int getClassHandle() { return get_handle(); }

	/**
	* Returns the fully-qualified (dot-delimited) name of this instance's object class.
	*
	* @return the fully-qualified (dot-delimited) name of this instance's object class
	*/
	public String getClassName() { return get_class_name(); }

	/**
	* Returns the simple name (last name in its fully-qualified dot-delimited name)
	* of this instance's object class.
	*
	* @return the simple name of this instance's object class
	*/
	public String getSimpleClassName() { return get_simple_class_name(); }

	/**
	* Returns a set containing the names of all of the non-hiddenattributes of an
	* object class instance.
	*
	* @return set containing the names of all of the attributes of an
	* object class instance
	*/
	public Set< String > getAttributeNames() { return get_attribute_names(); }

	/**
	* Returns a set containing the names of all of the attributes of an
	* object class instance.
	*
	* @return set containing the names of all of the attributes of an
	* object class instance
	*/
	public Set< String > getAllAttributeNames() { return get_all_attribute_names(); }

	/**
	* Publishes the object class of this instance of the class for a federate.
	*
	* @param rti handle to the Local RTI Component
	*/
	public void publishObject(RTIambassador rti) { publish(rti); }

	/**
	* Unpublishes the object class of this instance of this class for a federate.
	*
	* @param rti handle to the Local RTI Component
	*/
	public void unpublishObject(RTIambassador rti) { unpublish(rti); }

	/**
	* Subscribes a federate to the object class of this instance of this class.
	*
	* @param rti handle to the Local RTI Component
	*/
	public void subscribeObject(RTIambassador rti) { subscribe(rti); }

	/**
	* Unsubscribes a federate from the object class of this instance of this class.
	*
	* @param rti handle to the Local RTI Component
	*/
	public void unsubscribeObject(RTIambassador rti) { unsubscribe(rti); }


	/**
	* Returns a data structure containing the handles of all attributes for this object
	* class that are currently marked for subscription.  To actually subscribe to these
	* attributes, a federate must call <objectclassname>.subscribe(RTIambassador rti).
	*
	* @return data structure containing the handles of all attributes for this object
	* class that are currently marked for subscription
	*/
	public AttributeHandleSet getSubscribedAttributeHandleSet() { return _subscribedAttributeHandleSet; }


	public String toString() {
		return "HelicsPublications("


			+ "doubleValue:" + get_doubleValue()
			+ "," + "stringValue:" + get_stringValue()
			+ ")";
	}






	/**
	* Publishes the "doubleValue" attribute of the attribute's containing object
	* class for a federate.
	* Note:  This method only marks the "doubleValue" attribute for publication.
	* To actually publish the attribute, the federate must (re)publish its containing
	* object class.
	* (using <objectClassName>.publish( RTIambassador rti ) ).
	*/
	public static void publish_doubleValue() {
		_publishAttributeNameSet.add( "doubleValue" );
	}

	/**
	* Unpublishes the "doubleValue" attribute of the attribute's containing object
	* class for a federate.
	* Note:  This method only marks the "doubleValue" attribute for unpublication.
	* To actually publish the attribute, the federate must (re)publish its containing
	* object class.
	* (using <objectClassName>.publish( RTIambassador rti ) ).
	*/
	public static void unpublish_doubleValue() {
		_publishAttributeNameSet.remove( "doubleValue" );
	}

	/**
	* Subscribes a federate to the "doubleValue" attribute of the attribute's
	* containing object class.
	* Note:  This method only marks the "doubleValue" attribute for subscription.
	* To actually subscribe to the attribute, the federate must (re)subscribe to its
	* containing object class.
	* (using <objectClassName>.subscribe( RTIambassador rti ) ).
	*/
	public static void subscribe_doubleValue() {
		_subscribeAttributeNameSet.add( "doubleValue" );
	}

	/**
	* Unsubscribes a federate from the "doubleValue" attribute of the attribute's
	* containing object class.
	* Note:  This method only marks the "doubleValue" attribute for unsubscription.
	* To actually unsubscribe to the attribute, the federate must (re)subscribe to its
	* containing object class.
	* (using <objectClassName>.subscribe( RTIambassador rti ) ).
	*/
	public static void unsubscribe_doubleValue() {
		_subscribeAttributeNameSet.remove( "doubleValue" );
	}


	/**
	* Publishes the "stringValue" attribute of the attribute's containing object
	* class for a federate.
	* Note:  This method only marks the "stringValue" attribute for publication.
	* To actually publish the attribute, the federate must (re)publish its containing
	* object class.
	* (using <objectClassName>.publish( RTIambassador rti ) ).
	*/
	public static void publish_stringValue() {
		_publishAttributeNameSet.add( "stringValue" );
	}

	/**
	* Unpublishes the "stringValue" attribute of the attribute's containing object
	* class for a federate.
	* Note:  This method only marks the "stringValue" attribute for unpublication.
	* To actually publish the attribute, the federate must (re)publish its containing
	* object class.
	* (using <objectClassName>.publish( RTIambassador rti ) ).
	*/
	public static void unpublish_stringValue() {
		_publishAttributeNameSet.remove( "stringValue" );
	}

	/**
	* Subscribes a federate to the "stringValue" attribute of the attribute's
	* containing object class.
	* Note:  This method only marks the "stringValue" attribute for subscription.
	* To actually subscribe to the attribute, the federate must (re)subscribe to its
	* containing object class.
	* (using <objectClassName>.subscribe( RTIambassador rti ) ).
	*/
	public static void subscribe_stringValue() {
		_subscribeAttributeNameSet.add( "stringValue" );
	}

	/**
	* Unsubscribes a federate from the "stringValue" attribute of the attribute's
	* containing object class.
	* Note:  This method only marks the "stringValue" attribute for unsubscription.
	* To actually unsubscribe to the attribute, the federate must (re)subscribe to its
	* containing object class.
	* (using <objectClassName>.subscribe( RTIambassador rti ) ).
	*/
	public static void unsubscribe_stringValue() {
		_subscribeAttributeNameSet.remove( "stringValue" );
	}




	private Attribute< Double > _doubleValue =
 		new Attribute< Double >(  new Double( 0 )  );

	/**
	* Set the value of the "doubleValue" attribute to "value" for this object.
	*
	* @param value the new value for the "doubleValue" attribute
	*/
	public void set_doubleValue( double value ) {
		_doubleValue.setValue( value );
		_doubleValue.setTime( getTime() );
	}

	/**
	* Returns the value of the "doubleValue" attribute of this object.
	*
	* @return the value of the "doubleValue" attribute
	*/
	public double get_doubleValue() {
		return _doubleValue.getValue();
	}

	/**
	* Returns the current timestamp of the "doubleValue" attribute of this object.
	*
	* @return the current timestamp of the "doubleValue" attribute
	*/
	public double get_doubleValue_time() {
		return _doubleValue.getTime();
	}


	private Attribute< String > _stringValue =
 		new Attribute< String >(  new String( "" )  );

	/**
	* Set the value of the "stringValue" attribute to "value" for this object.
	*
	* @param value the new value for the "stringValue" attribute
	*/
	public void set_stringValue( String value ) {
		_stringValue.setValue( value );
		_stringValue.setTime( getTime() );
	}

	/**
	* Returns the value of the "stringValue" attribute of this object.
	*
	* @return the value of the "stringValue" attribute
	*/
	public String get_stringValue() {
		return _stringValue.getValue();
	}

	/**
	* Returns the current timestamp of the "stringValue" attribute of this object.
	*
	* @return the current timestamp of the "stringValue" attribute
	*/
	public double get_stringValue_time() {
		return _stringValue.getTime();
	}



	protected HelicsPublications( ReflectedAttributes datamemberMap, boolean initFlag ) {
		super( datamemberMap, false );
		if ( initFlag ) setAttributes( datamemberMap );
	}

	protected HelicsPublications( ReflectedAttributes datamemberMap, LogicalTime logicalTime, boolean initFlag ) {
		super( datamemberMap, logicalTime, false );
		if ( initFlag ) setAttributes( datamemberMap );
	}


	/**
	* Creates an instance of the HelicsPublications object class, using
	* "datamemberMap" to initialize its attribute values.
	* "datamemberMap" is usually acquired as an argument to an RTI federate
	* callback method, such as "receiveInteraction".
	*
	* @param datamemberMap data structure containing initial values for the
	* attributes of this new HelicsPublications object class instance
	*/
	public HelicsPublications( ReflectedAttributes datamemberMap ) {
		this( datamemberMap, true );
	}

	/**
	* Like {@link #HelicsPublications( ReflectedAttributes datamemberMap )}, except this
	* new HelicsPublications object class instance is given a timestamp of
	* "logicalTime".
	*
	* @param datamemberMap data structure containing initial values for the
	* attributes of this new HelicsPublications object class instance
	* @param logicalTime timestamp for this new HelicsPublications object class
	* instance
	*/
	public HelicsPublications( ReflectedAttributes datamemberMap, LogicalTime logicalTime ) {
		this( datamemberMap, logicalTime, true );
	}

	/**
	* Creates a new HelicsPublications object class instance that is a duplicate
	* of the instance referred to by HelicsPublications_var.
	*
	* @param HelicsPublications_var HelicsPublications object class instance of which
	* this newly created HelicsPublications object class instance will be a
	* duplicate
	*/
	public HelicsPublications( HelicsPublications HelicsPublications_var ) {
		super( HelicsPublications_var );


		set_doubleValue( HelicsPublications_var.get_doubleValue() );
		set_stringValue( HelicsPublications_var.get_stringValue() );
	}


	/**
	* Returns the value of the attribute whose name is "datamemberName"
	* for this object.
	*
	* @param datamemberName name of attribute whose value is to be
	* returned
	* @return value of the attribute whose name is "datamemberName"
	* for this object
	*/
	public Object getAttribute( String datamemberName ) {



		if (  "doubleValue".equals( datamemberName )  ) return new Double(get_doubleValue());
		else if (  "stringValue".equals( datamemberName )  ) return get_stringValue();
		else return super.getAttribute( datamemberName );
	}

	/**
	* Returns the value of the attribute whose handle (RTI assigned)
	* is "datamemberHandle" for this object.
	*
	* @param datamemberHandle handle (RTI assigned) of attribute whose
	* value is to be returned
	* @return value of the attribute whose handle (RTI assigned) is
	* "datamemberHandle" for this object
	*/
	public Object getAttribute( int datamemberHandle ) {



		if ( get_doubleValue_handle() == datamemberHandle ) return new Double(get_doubleValue());
		else if ( get_stringValue_handle() == datamemberHandle ) return get_stringValue();
		else return super.getAttribute( datamemberHandle );
	}

	protected boolean setAttributeAux( int param_handle, String val ) {
		boolean retval = true;



		if ( param_handle == get_doubleValue_handle() ) set_doubleValue( Double.parseDouble(val) );
		else if ( param_handle == get_stringValue_handle() ) set_stringValue( val );
		else retval = super.setAttributeAux( param_handle, val );

		return retval;
	}

	protected boolean setAttributeAux( String datamemberName, String val ) {
		boolean retval = true;



		if (  "doubleValue".equals( datamemberName )  ) set_doubleValue( Double.parseDouble(val) );
		else if (  "stringValue".equals( datamemberName )  ) set_stringValue( val );
		else retval = super.setAttributeAux( datamemberName, val );

		return retval;
	}

	protected boolean setAttributeAux( String datamemberName, Object val ) {
		boolean retval = true;



		if (  "doubleValue".equals( datamemberName )  ) set_doubleValue( (Double)val );
		else if (  "stringValue".equals( datamemberName )  ) set_stringValue( (String)val );
		else retval = super.setAttributeAux( datamemberName, val );

		return retval;
	}

	protected SuppliedAttributes createSuppliedDatamembers( boolean force ) {
		SuppliedAttributes datamembers = super.createSuppliedDatamembers( force );


		boolean isPublished = false;


			try {
				isPublished = _publishedAttributeHandleSet.isMember( get_doubleValue_handle() );
			} catch ( Exception e ) {
				logger.error("ERROR:  ObjectRoot.HelicsPublications.createSuppliedAttributes:  could not determine if doubleValue is published.");
				isPublished = false;
			}
			if (  isPublished && _doubleValue.shouldBeUpdated( force )  ) {
				datamembers.add( get_doubleValue_handle(), Double.toString(get_doubleValue()).getBytes() );
				_doubleValue.setHasBeenUpdated();
			}
			try {
				isPublished = _publishedAttributeHandleSet.isMember( get_stringValue_handle() );
			} catch ( Exception e ) {
				logger.error("ERROR:  ObjectRoot.HelicsPublications.createSuppliedAttributes:  could not determine if stringValue is published.");
				isPublished = false;
			}
			if (  isPublished && _stringValue.shouldBeUpdated( force )  ) {
				datamembers.add( get_stringValue_handle(), get_stringValue().getBytes() );
				_stringValue.setHasBeenUpdated();
			}

		return datamembers;
	}


	public void copyFrom( Object object ) {
		super.copyFrom( object );
		if ( object instanceof HelicsPublications ) {
			HelicsPublications data = (HelicsPublications)object;


				_doubleValue = data._doubleValue;
				_stringValue = data._stringValue;

		}
	}
}

