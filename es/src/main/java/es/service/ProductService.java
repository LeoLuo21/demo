package es.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import common.domain.Product;
import es.config.ElasticsearchClientConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author leo
 * @date 20220711 18:30:11
 */
public class ProductService {
    private ElasticsearchClient client = ElasticsearchClientConfig.getDefaultElasticsearchClient();

    public void indexSingleDoc(Product product) {
        try {
            client.index(i -> i
                    .index("product")
                    .id(product.getId().toString())
                    .document(product)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void indexInButch(Collection<Product> products) {
        BulkRequest.Builder builder = new BulkRequest.Builder();
        for (Product p : products) {
            builder.operations(op -> op
                    .index(i -> i
                            .index("product")
                            .id(p.getId().toString())
                            .document(p)
                    )
            );
        }
        try {
            client.bulk(builder.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void search() {
        try {
            SearchResponse<Product> searchResponse = null;
            searchResponse = client.search(s -> s
                            .index("product")
                            .query(q -> q
                                    .bool(b -> b
                                            .must(m -> m.match(t -> t.field("name").query("bike")))
                                            .must(m -> m.range(r -> r
                                                            .field("price")
                                                            .gte(JsonData.of(100))
                                                            .lte(JsonData.of(500))
                                                    )
                                            )
                                    )
                            )
                    , Product.class);
            System.out.println("searchResponse.hits().total() = " + searchResponse.hits().total());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void aggregate() {
        try {
            SearchResponse<Void> searchResponse = client.search(s -> s
                            .index("product")
                            .query(q -> q.range(r -> r
                                    .field("price").gte(JsonData.of(100)).lte(JsonData.of(1000))
                            )).aggregations("price-histogram",a->a
                                    .histogram(h -> h
                                            .field("price")
                                            .interval(50.0)
                                    )
                            )
                    , Void.class);
            List<HistogramBucket> array = searchResponse.aggregations().get("price-histogram").histogram().buckets().array();
            for (HistogramBucket bucket: array) {
                System.out.printf("There are %d bikes under %7.2f\n",bucket.docCount(),bucket.key());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        /**
        Product product = new Product();
        product.setId(1L);
        product.setName("great bike");
        product.setCategory("bike");
        product.setShop("leo");
        product.setPrice(new BigDecimal("299.0"));
        product.setCreatedAt(ZonedDateTime.now());
        product.setUpdatedAt(ZonedDateTime.now());
        productService.indexSingleDoc(product);*/
        ProductService productService = new ProductService();
        productService.search();
        productService.aggregate();
    }
}
