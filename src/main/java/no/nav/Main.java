package no.nav;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.saksbehandling.Sak;
import no.nav.saksbehandling.Saksbehandler;
import no.nav.saksbehandling.Spesialiseringer;
import no.nav.årslønn.Årslønn;

public class Main {
    public static void main(String[] args) {
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 500000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2022, 450000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2021, 400000));
        System.out.println("---🤖 Kalkulerer dagsats... 🤖---");
        System.out.println("Personen har rett på følgende dagsats: " + dagpengerKalkulator.kalkulerDagsats());
        System.out.println("---🤖 Dagsats ferdig kalkulert 🤖---");

        Sak sak = dagpengerKalkulator.kalkulerSak();
        Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", Spesialiseringer.INNVILGET);
        System.out.println("Spesialisering: " + sak.hentSpesialisering());
        System.out.println("Spesialisering hos Saksbehandler: " + saksbehandler.hestSpesialisering());
        
        saksbehandler.godkjennSak(sak);

        System.out.println("Behandlingsstatus: " + sak.hentBehandlingsstatus());
        
    }
}