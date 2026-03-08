package no.nav.saksbehandling;

/**
 * Klasse som representerer en sak i saksbehandlingen.
 */
public class Sak {

    private final double dagsats;
    private final Spesialiseringer spesialisering;
    private Behandlingsstatus behandlingsstatus;

    public Sak(double dagsats, Spesialiseringer spesialisering) {
        this.dagsats = dagsats;
        this.spesialisering = spesialisering;
        this.behandlingsstatus = Behandlingsstatus.IKKE_BEHANDLET;
    }
    /**
     * Henter dagsatsen for en sak.
     * @return dagsatsen for en sak.
     */
    public double hentDagsats() {
        return dagsats;
    }

    /**
     * Henter spesialiseringen for en sak.
     * @return spesialiseringen for en sak.
     */
    public Spesialiseringer hentSpesialisering() {
        return spesialisering;
    }

    /**
     * Henter behandlingsstatusen for en sak.
     * @return behandlingsstatusen for en sak.
     */
    public Behandlingsstatus hentBehandlingsstatus() {
        return behandlingsstatus;
    }

    /**
     * Godkjenner en sak, og setter behandlingsstatusen til GODKJENT.
     */
    public void godkjenn() {
        this.behandlingsstatus = Behandlingsstatus.GODKJENT;
    }

    /**
     * Avslår en sak, og setter behandlingsstatusen til AVSLÅTT.
     */
    public void avslå() {
        this.behandlingsstatus = Behandlingsstatus.AVSLÅTT;
    }
}