package no.nav.saksbehandling;

import java.util.ArrayList;
import java.util.List;

/**
* Representerer en saksbehandler som kan godkjenne eller avslå saker innenfor sin spesialisering.
*/
public class Saksbehandler {
    private final String navn;
    private final Spesialiseringer spesialisering;

    public Saksbehandler (String navn, Spesialiseringer spesialisering) {
        this.navn = navn;
        this.spesialisering = spesialisering;
    }
    /**
     * Henter navnet til saksbehandleren.
     * @return navnet til saksbehandleren.
     */
    public String hentNavn() {
        return navn;
    }
    
    /**
     * Henter spesialiseringen til saksbehandleren.
     * @return spesialiseringen til saksbehandleren.
     */
    public Spesialiseringer hentSpesialisering() {
        return spesialisering;
    }

    /**
     * Sjekker om saksbehandleren kan behandle en sak basert på spesialisering.
     * @param sak saken som skal sjekkes.
     * @return true hvis saksbehandleren kan behandle saken, false ellers.
     */
    public boolean kanBehandle(Sak sak) {
        return sak.hentSpesialisering() == this.spesialisering;
    }

    /**
     * Henter en liste over ubehandlede saker som saksbehandleren kan behandle basert på spesialisering.
     * @param saker listen over saker som skal sjekkes.
     * @return en liste over ubehandlede saker som saksbehandleren kan behandle.
     */
    public List<Sak> hentUbehandledeSaker(List<Sak> saker) {
        List<Sak> relevanteSaker = new ArrayList<>();

        for (Sak sak : saker) {
            if (kanBehandle(sak)
                    && sak.hentBehandlingsstatus() == Behandlingsstatus.IKKE_BEHANDLET) {
                relevanteSaker.add(sak);
            }
        }

        return relevanteSaker;
    }
    
    /**
     * Godkjenner en sak hvis saksbehandleren kan behandle den, ellers kaster en IllegalArgumentException.
     * @param sak saken som skal godkjennes.
     */
    public void godkjennSak(Sak sak) {
        validerAtKanBehandle(sak);
        sak.godkjenn();
    }

    /**
     * Avslår en sak hvis saksbehandleren kan behandle den, ellers kaster en IllegalArgumentException.
     * @param sak saken som skal avslås.
     */
    public void avslåSak(Sak sak) {
        validerAtKanBehandle(sak);
        sak.avslå();
    }

    private void validerAtKanBehandle(Sak sak) {
        if (!kanBehandle(sak)) {
            throw new IllegalArgumentException("Saksbehandler kan ikke behandle saker utenfor sin spesialisering");
        }
    }

}

    