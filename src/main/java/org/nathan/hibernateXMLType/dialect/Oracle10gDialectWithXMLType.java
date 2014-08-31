package org.nathan.hibernateXMLType.dialect;

import oracle.xdb.XMLType;
import org.hibernate.dialect.Oracle10gDialect;

public class Oracle10gDialectWithXMLType extends Oracle10gDialect {
    /**
     * Just registers the new type to recognize from the database
     */
    public Oracle10gDialectWithXMLType() {
        registerHibernateType(XMLType._SQL_TYPECODE, "XMLTYPE");
        registerColumnType(XMLType._SQL_TYPECODE, "XMLTYPE");
    }
}
