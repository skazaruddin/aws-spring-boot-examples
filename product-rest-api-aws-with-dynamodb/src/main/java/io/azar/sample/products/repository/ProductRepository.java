package io.azar.sample.products.repository;


import io.azar.sample.products.model.Product;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class ProductRepository {
    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private final String tableName = "Products";

    public ProductRepository(DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    public Flux<Product> findAll() {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();
        return Mono.fromFuture(() -> dynamoDbAsyncClient.scan(scanRequest))
                .flatMapMany(scanResponse -> Flux.fromIterable(scanResponse.items())
                        .map(this::mapToProduct));
    }

    public Flux<Product> findById(String id) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("id = :id")
                .expressionAttributeValues(Map.of(":id", AttributeValue.builder().s(id).build()))
                .build();
        return Mono.fromFuture(() -> dynamoDbAsyncClient.query(queryRequest))
                .flatMapMany(queryResponse -> Flux.fromIterable(queryResponse.items())
                        .map(this::mapToProduct));
    }

    public Mono<Product> save(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }

        if (product.getVersion() == null) {
            product.setVersion(1L);
        } else {
            product.setVersion(product.getVersion() + 1);
        }

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(product.getId()).build());
        item.put("version", AttributeValue.builder().n(product.getVersion().toString()).build());
        item.put("name", AttributeValue.builder().s(product.getName()).build());
        item.put("description", AttributeValue.builder().s(product.getDescription()).build());
        item.put("price", AttributeValue.builder().n(Double.toString(product.getPrice())).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.putItem(request))
                .thenReturn(product);
    }

    public Mono<Void> deleteByIdAndVersion(String id, Long version) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());
        key.put("version", AttributeValue.builder().n(version.toString()).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.deleteItem(request))
                .then();
    }

    private Product mapToProduct(Map<String, AttributeValue> item) {
        Product product = new Product();
        product.setId(item.get("id").s());
        product.setVersion(Long.parseLong(item.get("version").n()));
        product.setName(item.get("name").s());
        product.setDescription(item.get("description").s());
        product.setPrice(Double.parseDouble(item.get("price").n()));
        return product;
    }
}




//import io.azar.sample.products.model.Product;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
//import software.amazon.awssdk.enhanced.dynamodb.Key;
//import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
//import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
//import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
//import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
//import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
//import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
//
//import java.util.UUID;
//
//@Repository
//public class ProductRepository {
//    private final DynamoDbAsyncTable<Product> productTable;
//
//    public ProductRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
//        this.productTable = dynamoDbEnhancedAsyncClient.table("products", TableSchema.fromBean(Product.class));
//    }
//
//    public Flux<Product> findAll() {
//        return Flux.from(productTable.scan(ScanEnhancedRequest.builder().build()).items());
//    }
//
//    public Flux<Product> findById(String id) {
//        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(id).build());
//        QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder()
//                .queryConditional(queryConditional)
//                .build();
//        return Flux.from(productTable.query(queryEnhancedRequest).items())
//                .onErrorMap(DynamoDbException.class, e -> new RuntimeException("Error querying products by id", e));
//    }
//
//    public Mono<Product> save(Product product) {
//        if (product.getId() == null) {
//            product.setId(UUID.randomUUID().toString());
//        }
//        return Mono.fromFuture(() -> productTable.putItem(PutItemEnhancedRequest.builder(Product.class).item(product).build()))
//                .thenReturn(product)
//                .onErrorMap(DynamoDbException.class, e -> new RuntimeException("Error saving product", e));
//    }
//
//    public Mono<Void> deleteById(String id) {
//        return Mono.fromFuture(() -> productTable.deleteItem(getKey(id)))
//                .then()
//                .onErrorMap(DynamoDbException.class, e -> new RuntimeException("Error deleting product", e));
//    }
//
//    private Product getKey(String id) {
//        Product product = new Product();
//        product.setId(id);
//        return product;
//    }
//}
