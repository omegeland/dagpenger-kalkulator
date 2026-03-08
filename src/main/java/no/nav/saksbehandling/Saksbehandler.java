package no.nav.saksbehandling;

import java.util.ArrayList;
import java.util.List;

/**
* Representerer en skaksbehandler
* Holder informasjon om spesialisering
*/
public class Saksbehandler {
    private final String navn;
    private final Spesialiseringer spesialisering;

    public Saksbehandler (String navn, Spesialiseringer spesialisering) {
        this.navn = navn;
        this.spesialisering = spesialisering;
    }

    public String hentNavn() {
        return navn;
    }

    public Spesialiseringer hestSpesialisering() {
        return spesialisering;
    }

    public boolean kanBehandle(Sak sak) {
        return sak.hentSpesialisering() == this.spesialisering;
    }

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

    public void godkjennSak(Sak sak) {
        validerAtKanBehandle(sak);
        sak.godkjenn();
    }

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

    