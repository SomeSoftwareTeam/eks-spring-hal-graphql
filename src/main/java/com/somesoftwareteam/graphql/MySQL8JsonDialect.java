package com.somesoftwareteam.graphql;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.dialect.MySQL8Dialect;

import java.sql.Types;

/**
 * https://vladmihalcea.com/hibernate-no-dialect-mapping-for-jdbc-type/
 */
public class MySQL8JsonDialect extends MySQL8Dialect {

    public MySQL8JsonDialect() {
        super();
        this.registerHibernateType(Types.OTHER, JsonStringType.class.getName());
    }
}