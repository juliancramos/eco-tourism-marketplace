package graphql;

import org.springframework.stereotype.Component;

import com.marketplace.servicecatalog.model.ServiceCategory;
import com.marketplace.servicecatalog.model.ServiceEntity;
import com.marketplace.servicecatalog.repository.ServiceCategoryRepository;
import com.marketplace.servicecatalog.repository.ServiceRepository;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

@Component
public class ServiceGraphQLResolver {
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;

    public ServiceGraphQLResolver(ServiceRepository serviceRepository,
                                  ServiceCategoryRepository categoryRepository) {
        this.serviceRepository = serviceRepository;
        this.categoryRepository = categoryRepository;
    }

    @QueryMapping
    public List<ServiceEntity> allServices() {
        return serviceRepository.findAll();
    }

    @QueryMapping
    public ServiceEntity serviceById(@Argument Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<ServiceCategory> allCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public ServiceCategory categoryById(@Argument Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
