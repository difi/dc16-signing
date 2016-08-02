package no.difi.sigining.mock;

import no.difi.signing.mock.DigipostServerMock;

import java.io.IOException;

public class DigipostServerMockMain {

    public static void main(String... args) throws IOException {
        new DigipostServerMock().start();
    }
}
