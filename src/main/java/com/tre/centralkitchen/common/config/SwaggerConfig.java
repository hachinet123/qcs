package com.tre.centralkitchen.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.tre.centralkitchen.common.properties.SwaggerProperties;
import com.tre.centralkitchen.common.utils.SpringUtils;
import com.tre.centralkitchen.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableKnife4j
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;
    private final OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    @SuppressWarnings("all")
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                mappings.removeIf(mapping -> mapping.getPatternParser() != null);
            }

            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    /**
     * create API
     */
    @PostConstruct
    public void createRestApi() {
        for (SwaggerProperties.Groups group : swaggerProperties.getGroups()) {
            String basePackage = group.getBasePackage();
            Docket docket = new Docket(DocumentationType.OAS_30)
                    .enable(swaggerProperties.getEnabled())
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(basePackage))
                    .paths(PathSelectors.any())
                    .build()
                    .groupName(group.getName())
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts())
                    .extensions(openApiExtensionResolver.buildExtensions(group.getName()))
                    .pathMapping(swaggerProperties.getPathMapping());
            String beanName = StringUtil.substringAfterLast(basePackage, ".") + "Docket";
            SpringUtils.registerBean(beanName, docket);
        }
    }

//    private List<SecurityScheme> securitySchemes() {
//        List<SecurityScheme> apiKeyList = new ArrayList<>();
//        String header = saTokenConfig.getTokenName();
//        apiKeyList.add(new ApiKey(header, header, In.HEADER.toValue()));
//        return apiKeyList;
//    }

//    private List<SecurityContext> securityContexts() {
//        List<SecurityContext> securityContexts = new ArrayList<>();
//        securityContexts.add(
//            SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
//                .build());
//        return securityContexts;
//    }

//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        List<SecurityReference> securityReferences = new ArrayList<>();
//        securityReferences.add(new SecurityReference(saTokenConfig.getTokenName(), authorizationScopes));
//        return securityReferences;
//    }

    private ApiInfo apiInfo() {
        SwaggerProperties.Contact contact = swaggerProperties.getContact();
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }
}
