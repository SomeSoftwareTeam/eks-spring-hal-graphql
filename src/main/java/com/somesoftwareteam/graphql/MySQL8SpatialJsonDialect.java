package com.somesoftwareteam.graphql;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.spatial.dialect.mysql.MySQL8SpatialDialect;

import java.sql.Types;

/**
 * https://vladmihalcea.com/hibernate-no-dialect-mapping-for-jdbc-type/
 */
public class MySQL8SpatialJsonDialect extends MySQL8SpatialDialect {

    public MySQL8SpatialJsonDialect() {
        super();
        this.registerHibernateType(Types.OTHER, JsonStringType.class.getName());
    }
}