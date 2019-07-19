package com.erin.sample;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {


    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    //private GraphQL graphQL;

    /*
    @PostConstruct
    public void init() throws IOException {
        //这是初始化方法，读取graphqls配置文件
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        //根据配置文件，初始化组装了一个服务的示例 instace
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }
    */

    @Bean
    public GraphQLSchema getGraphQLSchema() throws IOException {
        //这是初始化方法，读取graphqls配置文件
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        //根据配置文件，初始化组装了一个服务的示例 instace
        System.out.println("GraphQLSchema 已经初始化");
        return buildSchema(sdl);
    }

    private GraphQLSchema buildSchema(String sdl) {
        //初始化Schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())) //这里是处理bookById这个参数的回调方法
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }

    /*@Bean
    public GraphQL graphQL() {
        return graphQL;
    }*/

}