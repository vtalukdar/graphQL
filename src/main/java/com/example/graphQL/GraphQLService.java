package com.example.graphQL;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

public class GraphQLService {
    public String callGraphQLService(String url, String query)
            throws URISyntaxException, IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        URI uri = new URIBuilder(request.getURI())
                .addParameter("query", query)
                .build();
        System.out.println("URI "+uri.toString());
        request.setURI(uri);
        return getJSONResponse(client.execute(request));
    }

    public String getJSONResponse(HttpResponse httpResponse) throws IOException {
        Reader in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
        StringBuilder builder= new StringBuilder();
        char[] buf = new char[5000];
        int l = 0;
        while (l >= 0) {
            builder.append(buf, 0, l);
            l = in.read(buf);
        }
        JSONTokener token = new JSONTokener( builder.toString() );
        JSONObject jsonObject = new JSONObject(token);
        JSONArray jsonArray = jsonObject.toJSONArray(jsonObject.names());
        return jsonArray.toString();

    }

    public void executeGraphQLService() throws URISyntaxException, IOException {
        String CountryCode = "\"NL\"";
        String URL = "https://countries.trevorblades.com/";
        String Query = "{country(code: "+CountryCode+") { name capital currency languages { code name}}}";
      //  GraphQLService graphQLService = new GraphQLService();
        String responseJSON = this.callGraphQLService(URL,Query);
        System.out.println("JSON Response "+responseJSON);

    }

}