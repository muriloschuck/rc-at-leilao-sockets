package model;

import java.io.Serial;
import java.io.Serializable;

public class RespostaLance implements Serializable {
    @Serial
    private static final long serialVersionUID = -7160993804806556862L;
    final boolean ehValido;
    final String motivo;
    final Object apendice;

    public RespostaLance(final boolean ehValido, final String motivo) {
        this.ehValido = ehValido;
        this.motivo = motivo;
        this.apendice = null;
    }

    public RespostaLance(final boolean ehValido) {
        this.ehValido = ehValido;
        this.motivo = null;
        this.apendice = null;
    }

    public RespostaLance(final boolean ehValido, final String motivo, final Object apendice) {
        this.ehValido = ehValido;
        this.motivo = motivo;
        this.apendice = apendice;
    }

    public boolean ehValido() {
        return ehValido;
    }

    public String getMotivo() {
        return motivo;
    }

    public Object getApendice() {
        return apendice;
    }
}
