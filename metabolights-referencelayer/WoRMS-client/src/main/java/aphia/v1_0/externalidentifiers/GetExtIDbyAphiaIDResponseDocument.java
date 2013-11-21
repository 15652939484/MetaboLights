/*
 * An XML document type.
 * Localname: getExtIDbyAphiaIDResponse
 * Namespace: http://aphia/v1.0/externalidentifiers
 * Java type: aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument
 *
 * Automatically generated - do not modify.
 */
package aphia.v1_0.externalidentifiers;


/**
 * A document containing one getExtIDbyAphiaIDResponse(@http://aphia/v1.0/externalidentifiers) element.
 *
 * This is a complex type.
 */
public interface GetExtIDbyAphiaIDResponseDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GetExtIDbyAphiaIDResponseDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sC975E981773C6D5C52067BB5301352BB").resolveHandle("getextidbyaphiaidresponse8f14doctype");
    
    /**
     * Gets the "getExtIDbyAphiaIDResponse" element
     */
    aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument.GetExtIDbyAphiaIDResponse getGetExtIDbyAphiaIDResponse();
    
    /**
     * Sets the "getExtIDbyAphiaIDResponse" element
     */
    void setGetExtIDbyAphiaIDResponse(aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument.GetExtIDbyAphiaIDResponse getExtIDbyAphiaIDResponse);
    
    /**
     * Appends and returns a new empty "getExtIDbyAphiaIDResponse" element
     */
    aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument.GetExtIDbyAphiaIDResponse addNewGetExtIDbyAphiaIDResponse();
    
    /**
     * An XML getExtIDbyAphiaIDResponse(@http://aphia/v1.0/externalidentifiers).
     *
     * This is a complex type.
     */
    public interface GetExtIDbyAphiaIDResponse extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GetExtIDbyAphiaIDResponse.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sC975E981773C6D5C52067BB5301352BB").resolveHandle("getextidbyaphiaidresponsef6d0elemtype");
        
        /**
         * Gets the "return" element
         */
        aphia.v1_0.Externalidentifiers getReturn();
        
        /**
         * Tests for nil "return" element
         */
        boolean isNilReturn();
        
        /**
         * Sets the "return" element
         */
        void setReturn(aphia.v1_0.Externalidentifiers xreturn);
        
        /**
         * Appends and returns a new empty "return" element
         */
        aphia.v1_0.Externalidentifiers addNewReturn();
        
        /**
         * Nils the "return" element
         */
        void setNilReturn();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument.GetExtIDbyAphiaIDResponse newInstance() {
              return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument.GetExtIDbyAphiaIDResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument.GetExtIDbyAphiaIDResponse newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument.GetExtIDbyAphiaIDResponse) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument newInstance() {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (aphia.v1_0.externalidentifiers.GetExtIDbyAphiaIDResponseDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
