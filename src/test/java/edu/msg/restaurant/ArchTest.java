package edu.msg.restaurant;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("edu.msg.restaurant");

        noClasses()
            .that()
            .resideInAnyPackage("edu.msg.restaurant.service..")
            .or()
            .resideInAnyPackage("edu.msg.restaurant.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..edu.msg.restaurant.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
