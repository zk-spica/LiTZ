package framework;

import java.net.*;

class InvitationCode {
    static String getCode(InetAddress addr, int port) {
        String addrHostAddress = addr.getHostAddress();
        String[] splitAddress = addrHostAddress.split("\\.");
        long hash = 0;
        for (String x : splitAddress) {
            int tmp = Integer.parseInt(x);
            hash = ( hash << 8 ) + tmp;
        }
        if (hash == 0) {
            try {
                return getCode(InetAddress.getLocalHost(), port);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                return "";
            }
        }
        hash = ( hash << 16 ) + port;
        StringBuffer buffer = new StringBuffer();
        for (int iter = 0; iter < 8; ++iter, hash >>= 6) {
            long tmp = hash & 0x3F;
            if (tmp < 10)
                buffer.append((char)('0'+tmp));
            else if (tmp < 10+26)
                buffer.append((char)('a'+tmp-10));
            else if (tmp < 10+26+26)
                buffer.append((char)('A'+tmp-10-26));
            else if (tmp == 62)
                buffer.append('?');
            else
                buffer.append('!');
        }
        return new String(buffer);
    }
    static InetAddress getInetAddress(String code) {
        long hash = decodeCode(code) >> 16;
        byte[] splitAddress = new byte[4];
        for (int i = 3; i >= 0; --i) {
            splitAddress[i] = (byte)(hash & 0xFF);
            hash >>= 8;
        }
        try {
            return InetAddress.getByAddress(splitAddress);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    static int getPort(String code)
    {
        return (int)(decodeCode(code) & 0xFFFF);
    }
    private static long decodeCode(String code)
    {
        code = new StringBuffer(code).reverse().toString();
        long hash = 0;
        for (char x : code.toCharArray())
        {
            int tmp;
            if ('0' <= x && x <= '9')
                tmp = x-'0';
            else if ('a' <= x && x <= 'z')
                tmp = x-'a'+10;
            else if ('A' <= x && x <= 'Z')
                tmp = x-'A'+10+26;
            else if (x == '?')
                tmp = 62;
            else
                tmp = 63;
            hash = ( hash << 6 ) + tmp;
        }
        return hash;
    }
}
