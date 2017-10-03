package chat;

import java.net.InetAddress;

public class ClientAddres {
    private final InetAddress addres;
    private final int port;

    public ClientAddres(InetAddress addres, int port) {
        this.addres = addres;
        this.port = port;
    }

    public InetAddress getAddres() {
        return addres;
    }

    public int getPort() {
        return port;
    }

    public boolean isSender(InetAddress addres, int port) {
        return getAddres().equals(addres) & getPort()==port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientAddres)) return false;

        ClientAddres that = (ClientAddres) o;

        if (getPort() != that.getPort()) return false;
        return getAddres() != null ? getAddres().equals(that.getAddres()) : that.getAddres() == null;
    }

    @Override
    public int hashCode() {
        int result = getAddres() != null ? getAddres().hashCode() : 0;
        result = 31 * result + getPort();
        return result;
    }
}
