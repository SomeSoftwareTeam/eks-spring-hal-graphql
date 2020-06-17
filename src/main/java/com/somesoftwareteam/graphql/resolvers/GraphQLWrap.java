package com.somesoftwareteam.graphql.resolvers;

import graphql.GraphQLException;

import java.util.concurrent.Callable;

/**
 * This class catches exceptions and wraps them with GraphQLException in order to provide consistent error responses for
 * all graphQL operations.
 */
public final class GraphQLWrap {

    public static <T> T wrap(Callable<T> callable) throws GraphQLException {

        try {

            return callable.call();

        } catch (GraphQLException e) {

            throw e;

        } catch (Exception e) {

            throw new GraphQLException(e);
        }
    }
}
