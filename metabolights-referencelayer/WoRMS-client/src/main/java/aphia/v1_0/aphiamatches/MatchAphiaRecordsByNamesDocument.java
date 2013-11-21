/*
 * An XML document type.
 * Localname: matchAphiaRecordsByNames
 * Namespace: http://aphia/v1.0/AphiaMatches
 * Java type: aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument
 *
 * Automatically generated - do not modify.
 */
package aphia.v1_0.aphiamatches;


/**
 * A document containing one matchAphiaRecordsByNames(@http://aphia/v1.0/AphiaMatches) element.
 *
 * This is a complex type.
 */
public interface MatchAphiaRecordsByNamesDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MatchAphiaRecordsByNamesDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sC975E981773C6D5C52067BB5301352BB").resolveHandle("matchaphiarecordsbynamesb8f0doctype");
    
    /**
     * Gets the "matchAphiaRecordsByNames" element
     */
    aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument.MatchAphiaRecordsByNames getMatchAphiaRecordsByNames();
    
    /**
     * Sets the "matchAphiaRecordsByNames" element
     */
    void setMatchAphiaRecordsByNames(aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument.MatchAphiaRecordsByNames matchAphiaRecordsByNames);
    
    /**
     * Appends and returns a new empty "matchAphiaRecordsByNames" element
     */
    aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument.MatchAphiaRecordsByNames addNewMatchAphiaRecordsByNames();
    
    /**
     * An XML matchAphiaRecordsByNames(@http://aphia/v1.0/AphiaMatches).
     *
     * This is a complex type.
     */
    public interface MatchAphiaRecordsByNames extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MatchAphiaRecordsByNames.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sC975E981773C6D5C52067BB5301352BB").resolveHandle("matchaphiarecordsbynamese8a7elemtype");
        
        /**
         * Gets the "scientificnames" element
         */
        aphia.v1_0.Scientificnames getScientificnames();
        
        /**
         * Tests for nil "scientificnames" element
         */
        boolean isNilScientificnames();
        
        /**
         * Sets the "scientificnames" element
         */
        void setScientificnames(aphia.v1_0.Scientificnames scientificnames);
        
        /**
         * Appends and returns a new empty "scientificnames" element
         */
        aphia.v1_0.Scientificnames addNewScientificnames();
        
        /**
         * Nils the "scientificnames" element
         */
        void setNilScientificnames();
        
        /**
         * Gets the "marine_only" element
         */
        boolean getMarineOnly();
        
        /**
         * Gets (as xml) the "marine_only" element
         */
        org.apache.xmlbeans.XmlBoolean xgetMarineOnly();
        
        /**
         * Tests for nil "marine_only" element
         */
        boolean isNilMarineOnly();
        
        /**
         * Sets the "marine_only" element
         */
        void setMarineOnly(boolean marineOnly);
        
        /**
         * Sets (as xml) the "marine_only" element
         */
        void xsetMarineOnly(org.apache.xmlbeans.XmlBoolean marineOnly);
        
        /**
         * Nils the "marine_only" element
         */
        void setNilMarineOnly();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument.MatchAphiaRecordsByNames newInstance() {
              return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument.MatchAphiaRecordsByNames) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument.MatchAphiaRecordsByNames newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument.MatchAphiaRecordsByNames) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument newInstance() {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (aphia.v1_0.aphiamatches.MatchAphiaRecordsByNamesDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
