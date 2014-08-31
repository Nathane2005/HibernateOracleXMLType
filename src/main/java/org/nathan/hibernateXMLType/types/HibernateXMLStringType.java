package org.nathan.hibernateXMLType.types;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.xdb.XMLType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class HibernateXMLStringType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[]{
                XMLType._SQL_TYPECODE
        };
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public int hashCode(Object o) {
        return o.hashCode();
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        try {
            return ((String) cached);
        } catch (Exception e) {
            throw new HibernateException("Could not assemble String to Document", e);
        }
    }

    @Override
    public Serializable disassemble(Object o) {
        try {
            return (String) o;
        } catch (Exception e) {
            throw new HibernateException("Could not disassemble Document to Serializable", e);
        }
    }

    @Override
    public Object replace(Object original, Object target, Object owner) {
        return deepCopy(original);
    }

    @Override
    public boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor impl,
                              Object owner) throws SQLException {
        XMLType xmlType = null;
        try {
            OracleResultSet ors = rs.unwrap(OracleResultSet.class);
            xmlType = (XMLType) ors.getSQLXML(names[0]);
            return xmlType != null ? xmlType.getStringVal() : null;
        } finally {
            if (null != xmlType) {
                xmlType.close();
            }
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SessionImplementor session) throws SQLException {
        XMLType xmlType = null;
        try {
            if (value != null) {
                xmlType = XMLType.createXML(st.getConnection().unwrap(OracleConnection.class),
                        (String) value);
            }
            st.setObject(index, xmlType);
        } finally {
            if (xmlType != null) {
                xmlType.close();
            }
        }
    }

    @Override
    public Object deepCopy(Object value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }
}