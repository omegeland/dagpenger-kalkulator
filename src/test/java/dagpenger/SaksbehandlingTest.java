package dagpenger;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.saksbehandling.*;
import no.nav.årslønn.Årslønn;

public class SaksbehandlingTest {

    @Test
    void skalOppretteAvslagssakVedForLavInntekt() {
        DagpengerKalkulator kalkulator = new DagpengerKalkulator();

        kalkulator.leggTilÅrslønn(new Årslønn(2025, 80000));
        kalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        kalkulator.leggTilÅrslønn(new Årslønn(2023, 70000));

        Sak sak = kalkulator.kalkulerSak();

        assertEquals(0, sak.hentDagsats());
        assertEquals(Spesialiseringer.AVSLAG_PAA_GRUNN_AV_FOR_LAV_INTEKT, sak.hentSpesialisering());
        assertEquals(Behandlingsstatus.IKKE_BEHANDLET, sak.hentBehandlingsstatus());
    }

    @Test
    void skalOppretteInnvilgetSak() {
        DagpengerKalkulator kalkulator = new DagpengerKalkulator();

        kalkulator.leggTilÅrslønn(new Årslønn(2025, 550000));
        kalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        kalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));

        Sak sak = kalkulator.kalkulerSak();

        assertEquals(2116, sak.hentDagsats());
        assertEquals(Spesialiseringer.INNVILGET, sak.hentSpesialisering());
    }

    @Test
    void skalOppretteMakssatsSak() {
        DagpengerKalkulator kalkulator = new DagpengerKalkulator();

        kalkulator.leggTilÅrslønn(new Årslønn(2025, 830000));
        kalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        kalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));

        Sak sak = kalkulator.kalkulerSak();

        assertEquals(3004, sak.hentDagsats());
        assertEquals(Spesialiseringer.INNVILGET_MED_MAKSSATS, sak.hentSpesialisering());
    }

    @Test
    void saksbehandlerSkalKunHenteUbehandledeSakerInnenforSinSpesialisering() {

        Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", Spesialiseringer.INNVILGET);

        Sak sak1 = new Sak(2116, Spesialiseringer.INNVILGET);
        Sak sak2 = new Sak(3004, Spesialiseringer.INNVILGET_MED_MAKSSATS);
        Sak sak3 = new Sak(1365, Spesialiseringer.INNVILGET);

        sak3.godkjenn();

        List<Sak> saker = new ArrayList<>();
        saker.add(sak1);
        saker.add(sak2);
        saker.add(sak3);

        List<Sak> resultat = saksbehandler.hentUbehandledeSaker(saker);

        assertEquals(1, resultat.size());
        assertTrue(resultat.contains(sak1));
    }

    @Test
    void saksbehandlerSkalKunneGodkjenneSak() {
        Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", Spesialiseringer.INNVILGET);
        Sak sak = new Sak(2116, Spesialiseringer.INNVILGET);

        saksbehandler.godkjennSak(sak);

        assertEquals(Behandlingsstatus.GODKJENT, sak.hentBehandlingsstatus());
    }

    @Test
    void saksbehandlerSkalKunneAvslaaSak() {
        Saksbehandler saksbehandler = new Saksbehandler("Ola Nordmann", Spesialiseringer.INNVILGET);
        Sak sak = new Sak(2116, Spesialiseringer.INNVILGET);

        saksbehandler.avslåSak(sak);

        assertEquals(Behandlingsstatus.AVSLÅTT, sak.hentBehandlingsstatus());
    }

    @Test
    void saksbehandlerSkalIkkeKunneBehandleFeilSpesialisering() {
        Saksbehandler saksbehandler =
                new Saksbehandler("Kari Nordmann", Spesialiseringer.AVSLAG_PAA_GRUNN_AV_FOR_LAV_INTEKT);

        Sak sak = new Sak(2116, Spesialiseringer.INNVILGET);

        assertThrows(IllegalArgumentException.class, () -> saksbehandler.godkjennSak(sak));
    }
}