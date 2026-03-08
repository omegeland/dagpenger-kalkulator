package no.nav.saksbehandling;

public class Sak {

    private final double dagsats;
    private final Spesialiseringer spesialisering;
    private Behandlingsstatus behandlingsstatus;

    public Sak(double dagsats, Spesialiseringer spesialisering) {
        this.dagsats = dagsats;
        this.spesialisering = spesialisering;
        this.behandlingsstatus = Behandlingsstatus.IKKE_BEHANDLET;
    }

    public double hentDagsats() {
        return dagsats;
    }

    public Spesialiseringer hentSpesialisering() {
        return spesialisering;
    }

    public Behandlingsstatus hentBehandlingsstatus() {
        return behandlingsstatus;
    }

    public void godkjenn() {
        this.behandlingsstatus = Behandlingsstatus.GODKJENT;
    }

    public void avslå() {
        this.behandlingsstatus = Behandlingsstatus.AVSLÅTT;
    }
}