package es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.PropertyVariant;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import common.domain.Product;
import es.config.ElasticsearchClientConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author leo
 * @date 20220709 17:41:55
 */
public class AppEntryPoint {
    public static void main(String[] args) throws IOException {
        ElasticsearchClient client = ElasticsearchClientConfig.getDefaultElasticsearchClient();
        System.out.println(client);
        SearchResponse<Product> search = client.search(s -> s
                        .index("products")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("bicycle"))
                                )),
                Product.class);

        for (Hit<Product> hit: search.hits().hits()) {
            System.out.println("hit.source() = " + hit.source());
        }
    }
}
