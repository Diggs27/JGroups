package org.jgroups.util;

import org.jgroups.Global;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Subclass of {@link UUID} which adds a string as payload. An instance of this can be fed to
 * {@link org.jgroups.JChannel#setAddressGenerator(org.jgroups.stack.AddressGenerator)}, with the address generator
 * creating PayloadUUIDs.
 * @author Bela Ban
 */
public class PayloadUUID extends UUID {
    private static final long serialVersionUID=-7042544908572216601L;

    // don't need this as we already added PayloadUUID to jg-magic-map.xml
    //    static {
    //        ClassConfigurator.add((short)2222, PayloadUUID.class);
    //    }

    protected String payload;

    public PayloadUUID() {
    }

    protected PayloadUUID(byte[] data, String payload) {
        super(data);
        this.payload=payload;
    }

    public static PayloadUUID randomUUID(String payload) {
        return new PayloadUUID(generateRandomBytes(), payload);
    }

    public static PayloadUUID randomUUID(String logical_name, String payload) {
        PayloadUUID retval=new PayloadUUID(generateRandomBytes(), payload);
        UUID.add(retval, logical_name);
        return retval;
    }


    protected static byte[] generateRandomBytes() {
        SecureRandom ng=numberGenerator;
        if(ng == null)
            numberGenerator=ng=new SecureRandom();

        byte[] randomBytes=new byte[16];
        ng.nextBytes(randomBytes);
        return randomBytes;
    }

    public int size() {
        int retval=super.size() + Global.BYTE_SIZE;
        if(payload != null)
            retval+=payload.length() +2;
        return retval;
    }

    public void writeTo(DataOutputStream out) throws IOException {
        super.writeTo(out);
        Util.writeString(payload, out);
    }

    public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
        super.readFrom(in);
        payload=Util.readString(in);
    }

    public String toString() {
        return super.toString() + "-" + payload;
    }

    public String toStringLong() {
        return super.toStringLong() + "-" + payload;
    }
}