package de.networkchallenge.lambda.model;

public class ResponseObject
{

    private Status status;

    public Status getStatus()
    {
        return status;
    }

    public ResponseObject setStatus(final Status status)
    {
        this.status = status;
        return this;
    }

    public enum Status {
        OK, ERROR, UNAUTHORIZED, BAD_REQUEST, URL_INVALID
    }

}