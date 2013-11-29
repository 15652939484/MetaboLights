package uk.ac.ebi.metabolights.species.globalnames.client;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.util.ArrayList;

/**
 * User: conesa
 * Date: 28/11/2013
 * Time: 11:58
 */
public class NameResolversParams {

	private static Logger logger = Logger.getLogger(NameResolversParams.class);
	public static final String NAMES_PARAM_NAME = "names";
	public static final String DATA_SOURCE_PARAM_NAME = "data_source_ids";

	// List of sources: <a href:"http://resolver.globalnames.org/data_sources"/>

	private ArrayList<String> names = new ArrayList<String>();
	private GlobalNamesSources dataSource;

	public NameResolversParams(String name){
		this.names.add(name);
	}

	public NameResolversParams(String name, GlobalNamesSources dataSource){
		this.names.add(name);
		this.dataSource = dataSource;
	}

	public ArrayList<String> getNames() {
		return names;
	}

	public void setNames(ArrayList<String> names) {
		this.names = names;
	}
	/* Behaves as if it were a single string parameter */
	public void setName (String name){

		names.clear();
		names.add(name);
	}
	public String getName(){
		if (names.size() >0) {
			return names.get(0);
		} else {
			return null;
		}
	}

	public GlobalNamesSources getDataSource() {
		return dataSource;
	}

	public void setDataSource(GlobalNamesSources dataSource) {
		this.dataSource = dataSource;
	}
	@Override
	public String toString(){

		String result;

		String namesParam = namesParamToString();

		String dataSourceParam = dataSource==null?"": DATA_SOURCE_PARAM_NAME +  "=" + dataSource.getDataSourceId();

		result = joinParams(namesParam, dataSourceParam);

		try {
			result = URIUtil.encodeQuery(result);
		} catch (URIException e) {
			logger.error("NameResolversParams can't be encoded for the get Request. Value: " + result , e );
		}

		return result;



	}

	private String namesParamToString(){

		String result = "";

		if (names.size()>0){


			for (String name: names){
				if (name !=null && !name.isEmpty()){
					result = result + name + "\t";
				}
			}
		}

		if (result.length()>0)  result = result.substring(0,result.length()-1);

		if (!result.isEmpty()){
			result = NAMES_PARAM_NAME + "=" + result;
		}

		return result;
	}

	private String joinParams(String param1, String param2){

		if (param1 == null) param1 ="";
		if (param2 == null) param2 ="";

		if (param1.equals("") ) return param2;
		if (param2.equals("")) return param1;

		return param1 + "&" + param2;

	}

}
