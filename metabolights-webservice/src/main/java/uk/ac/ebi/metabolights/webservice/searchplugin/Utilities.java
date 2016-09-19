package uk.ac.ebi.metabolights.webservice.searchplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by kalai on 19/09/2016.
 */
public class Utilities {


    public static List<CompoundSearchResult> combine(List<Future<CompoundSearchResult>> searchResultsFromChebi,
                                               Future<Collection<CompoundSearchResult>> chemSpiderResults,
                                               Future<Collection<CompoundSearchResult>> pubchemResults) {
        List<CompoundSearchResult> totalSearchResults = new ArrayList<>();

        try {
            totalSearchResults.addAll(extract(searchResultsFromChebi));
            totalSearchResults.addAll(extract(chemSpiderResults));
            totalSearchResults.addAll(extract(pubchemResults));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalSearchResults;

    }


    public static List<CompoundSearchResult> extract(List<Future<CompoundSearchResult>> searchResults) throws ExecutionException, InterruptedException {
        List<CompoundSearchResult> searchHits = new ArrayList<>();
        for (Future<CompoundSearchResult> searchResult : searchResults) {
            searchHits.add(searchResult.get());
        }
        return searchHits;
    }

    public static Collection<CompoundSearchResult> extract(Future<Collection<CompoundSearchResult>> results) throws ExecutionException, InterruptedException {
        Collection<CompoundSearchResult> extractedResult = new ArrayList<>();
        if (results != null) {
            if (results.get() != null) {
                extractedResult = results.get();
                return extractedResult;
            }
        }
        return extractedResult;
    }


    public static void sort(List<CompoundSearchResult> searchHits) {
        if (searchHits.size() < 2) {
            return;
        }
        boolean somethingsChanged = false;
        Object o1, o2;
        do {
            somethingsChanged = false;
            for (int f = 0; f < searchHits.size() - 1; f++) {
                if (score(searchHits.get(f)) < score(searchHits.get(f + 1))) {
                    o1 = searchHits.get(f + 1);
                    searchHits.remove(f + 1);
                    searchHits.add(f, (CompoundSearchResult) o1);
                    somethingsChanged = true;
                }
            }
        } while (somethingsChanged);
    }

    public static int score(CompoundSearchResult result) {
        int values = 0;
        if (result.getName() != null) {
            values++;
            if (result.getInchi() != null) {
                values++;
                if (result.getFormula() != null) {
                    values++;
                    if (result.getSmiles() != null) {
                        values++;
                        if (result.getChebiId() != null) {
                            values++;
                        }
                    }
                }
            }
        }
        return values;
    }

    public static List<CompoundSearchResult> convert(String[] curatedMatch) {
        List<CompoundSearchResult> results = new ArrayList<>();
        CompoundSearchResult searchResult = new CompoundSearchResult(SearchResource.CURATED);
        try {
            searchResult.setName(curatedMatch[CuratedMetabolitesFileColumnIdentifier.COMPOUND_NAME.getID()]);
            searchResult.setInchi(curatedMatch[CuratedMetabolitesFileColumnIdentifier.INCHI.getID()].replaceAll("\"", ""));
            searchResult.setSmiles(curatedMatch[CuratedMetabolitesFileColumnIdentifier.SMILES.getID()]);
            searchResult.setFormula(curatedMatch[CuratedMetabolitesFileColumnIdentifier.MOLECULAR_FORMULA.getID()]);
            searchResult.setChebiId(curatedMatch[CuratedMetabolitesFileColumnIdentifier.CHEBI_ID.getID()]);
        } catch (Exception e) {
            return results;
        }
        results.add(searchResult);
        return results;
    }
}
