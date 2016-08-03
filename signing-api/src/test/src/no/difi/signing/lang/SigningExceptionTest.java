package no.difi.signing.lang;

import org.testng.annotations.Test;

public class SigningExceptionTest {

    @Test(expectedExceptions = SigningException.class)
    public void throwWithMessage() throws SigningException {
        throw new SigningException("Message");
    }

    @Test(expectedExceptions = SigningException.class)
    public void throwWithMessageAndThrowable() throws SigningException {
        throw new SigningException("Message", new Exception());
    }

}
