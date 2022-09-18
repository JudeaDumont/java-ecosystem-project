package com.webapi.databasedrivers;

// Currently, we do not throw exceptions if a model is not found by ID,
// but we do throw this exception if two entities show up for one primary key
public class DuplicatePrimaryKeyException extends Throwable {
}
