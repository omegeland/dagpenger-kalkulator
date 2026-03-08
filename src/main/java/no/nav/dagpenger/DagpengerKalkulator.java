package no.nav.dagpenger;
import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.saksbehandling.Sak;
import no.nav.saksbehandling.Spesialiseringer;
import no.nav.årslønn.Årslønn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Kalkulator for å beregne hvor mye dagpenger en person har rett på i Norge basert på dagens grunnbeløp (1G).
 * For at en person skal ha rett på dagpenger, må en av de to følgene kravene være møtt:
 *      De siste 3 årene må gjennomsnitslønnen være høyere enn 3G.
 *      Tjent mer det siste året enn 1.5G.
 * Hvis en person har rett på dagpenger, må følgende ting vurderes for å kalkulere dagsatsen:
 *      Hva er størst av gjennomsnittlig årslønn de 3 siste årene og siste årslønn.
 *      Hvis siste årslønn er størst, er årslønnen høyere enn 6G.
 * Antall årlige arbeidsdager i Norge er satt til å være 260, så ved beregning av dagsats må 260 dager
 * brukes og ikke 365.
 *
 * @author Emil Elton Nilsen
 * @version 1.0
 */
public class DagpengerKalkulator {

    private final GrunnbeløpVerktøy grunnbeløpVerktøy;
    private final List<Årslønn> årslønner;

    public DagpengerKalkulator() {
        this.grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        this.årslønner = new ArrayList<>();
    }

    /**
     * Hvis en person har rett på dagpenger, vil den kalkulere dagsatsen en person har rett på.
     * Hvis ikke en person har rett på dagpenger, vil metoden returnere 0kr som dagsats, som en antagelse på at det
     * er det samme som å ikke ha rett på dagpenger.
     * @return dagsatsen en person har rett på.
     */
    public double kalkulerDagsats() {
        
        if (harIkkeGrunnlagForDagpenger()) {
            return 0;
        } 

        int arbeidsdagerIÅret = 260;
        BeregningsMetode metode = velgBeregningsMetode();
        switch (metode) {
            case SISTE_ÅRSLØNN:
                return Math.ceil(hentÅrslønnVedIndeks(0).hentÅrslønn() / arbeidsdagerIÅret);

            case GJENNOMSNITTET_AV_TRE_ÅR:
                return Math.ceil((summerNyligeÅrslønner(3) / 3) / arbeidsdagerIÅret);

            case MAKS_ÅRLIG_DAGPENGERGRUNNLAG:
                return Math.ceil(grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag() / arbeidsdagerIÅret);

            default:
                throw new IllegalStateException("Ukjent beregningsmetode: " + metode);
        }
    }
    
    public Sak kalkulerSak() {

        if (harIkkeGrunnlagForDagpenger()) {
            return new Sak(0, Spesialiseringer.AVSLAG_PAA_GRUNN_AV_FOR_LAV_INTEKT);
        }

        double dagsats = kalkulerDagsats();
        BeregningsMetode metode = velgBeregningsMetode();

        switch (metode) {
            case MAKS_ÅRLIG_DAGPENGERGRUNNLAG:
                return new Sak(dagsats, Spesialiseringer.INNVILGET_MED_MAKSSATS);
            case SISTE_ÅRSLØNN:
            case GJENNOMSNITTET_AV_TRE_ÅR:
                return new Sak(dagsats, Spesialiseringer.INNVILGET);

            default:
                throw new IllegalStateException("Ukjent beregningsmetode: " + metode);
        }
    }

    public boolean harIkkeGrunnlagForDagpenger() {
        return årslønner.isEmpty() || !harRettigheterTilDagpenger();  
    }

    /**
     * Sjekker om en person har rettighet til dagpenger eller ikke.
     * @return om personen har rett på dagpenger.
     */
    public boolean harRettigheterTilDagpenger() {

        if (summerNyligeÅrslønner(3) >= grunnbeløpVerktøy.hentTotaltGrunnbeløpForGittAntallÅr(3)) {
            return true;
        } else if (hentÅrslønnVedIndeks(0).hentÅrslønn() >= grunnbeløpVerktøy.hentMinimumÅrslønnForRettPåDagpenger()) {
            return true;
        }
        return false;
    }

    /**
     * Velger hva som skal være beregnings metode for dagsats ut ifra en person sine årslønner.
     * @return beregnings metode for dagsats.
     */
    public BeregningsMetode velgBeregningsMetode() {
        BeregningsMetode beregningsMetode;

        if (hentÅrslønnVedIndeks(0).hentÅrslønn() > (summerNyligeÅrslønner(3) / 3)) {
            beregningsMetode = BeregningsMetode.SISTE_ÅRSLØNN;
            if (hentÅrslønnVedIndeks(0).hentÅrslønn() > grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag()) {
                beregningsMetode = BeregningsMetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG;
            }
        } else {
            beregningsMetode = BeregningsMetode.GJENNOMSNITTET_AV_TRE_ÅR;
        }
        return beregningsMetode;
    }

    public void leggTilÅrslønn(Årslønn årslønn) {
        this.årslønner.add(årslønn);
        this.sorterÅrslønnerBasertPåNyesteÅrslønn();
    }

    /**
     * Henter årslønnen i registeret basert på dens posisjon i registeret ved gitt indeks.
     * @param indeks posisjonen til årslønnen.
     * @return årslønnen ved gitt indeks.
     */
    public Årslønn hentÅrslønnVedIndeks(int indeks) {
        return this.årslønner.get(indeks);
    }

    /**
     * Summerer sammen antall årslønner basert på gitt parameter.
     * @param antallÅrÅSummere antall år med årslønner vi vil summere.
     * @return summen av årslønner.
     */
    public double summerNyligeÅrslønner(int antallÅrÅSummere) {
        double sumAvNyligeÅrslønner = 0;

        if (antallÅrÅSummere <= this.årslønner.size()) {

            for (int i = 0; i<antallÅrÅSummere;i++) {
                sumAvNyligeÅrslønner += årslønner.get(i).hentÅrslønn();
            }
        }
        return sumAvNyligeÅrslønner;
    }

    /**
     * Sorterer registeret slik at den nyligste årslønnen er det først elementet i registeret.
     * Først blir årslønnene i registeret sortert ut at den eldstre årslønnen skal først i registeret,
     * deretter blir registeret reversert.
     */
    public void sorterÅrslønnerBasertPåNyesteÅrslønn() {
        this.årslønner.sort(Comparator.comparingInt(Årslønn::hentÅretForLønn).reversed());
    }
}