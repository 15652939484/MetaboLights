package uk.ac.ebi.metabolights.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.bioinvindex.search.hibernatesearch.BIIQueryBuilder;
import uk.ac.ebi.metabolights.search.LuceneSearchResult;

@Service
public class SearchServiceImpl implements SearchService{
	
	private static Logger logger = Logger.getLogger(SearchServiceImpl.class);
	private final int topN=10000; // some max value of results we want

	@Autowired
	private IndexProviderService indexProvider; 
		
	public HashMap<Integer, List<LuceneSearchResult>> search(String queryText) throws IOException, ParseException {
		List<LuceneSearchResult> resultSet = new ArrayList<LuceneSearchResult>(); 
		Integer numDocs = 0;
		HashMap<Integer, List<LuceneSearchResult>> searchResultHash = new HashMap<Integer, List<LuceneSearchResult>>();

		
		if (queryText==null || queryText.trim().equals("")) {
			IndexReader indexReader = indexProvider.getReader();
			int loops = indexReader.maxDoc()<topN?indexReader.maxDoc():topN;
			numDocs = indexReader.numDocs(); //Total number of documents found
			
			for (int i = 0; i < loops; i++) {
				Document doc = indexReader.document(i);
				if (doc==null)
					break;
				LuceneSearchResult searchResult = new LuceneSearchResult(doc,1);
				resultSet.add(searchResult);
				//logger.debug(searchResult);
			}
		}
		else {	
			IndexSearcher indexSearcher = indexProvider.getSearcher();
			BIIQueryBuilder<Study> queryBuilder = new BIIQueryBuilder<Study>();
			Query query=queryBuilder.buildQuery(queryText);
			TopDocs results = indexSearcher.search(query,topN);
			ScoreDoc[] hits = results.scoreDocs;
			numDocs = results.totalHits;  //Total number of documents found

			int i=0;
			for (ScoreDoc hit : hits) {
				Document doc = indexSearcher.doc(hit.doc);
				LuceneSearchResult searchResult = new LuceneSearchResult(doc,hit.score);
				resultSet.add(searchResult);
				//logger.debug(searchResult);
				i++;
			}
		}
		searchResultHash.put(numDocs, resultSet);
		return searchResultHash;
	}

	public String getIndexDirectory () {
		return indexProvider.getIndexDirectory(); 
	}

}
