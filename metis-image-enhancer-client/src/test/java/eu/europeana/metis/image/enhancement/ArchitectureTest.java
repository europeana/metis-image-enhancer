package eu.europeana.metis.image.enhancement;


import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ArchitectureTest {
    private final JavaClasses allProjectClasses = new ClassFileImporter().importPackages("eu.europeana.metis.image.enhancement");

    @Test
    void domain_independent_of_infrastructure() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..client..")
                .check(allProjectClasses);
    }

    @Test
    void domain_independent_of_configuration() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..config..")
                .check(allProjectClasses);
    }

    @Test
    void project_free_cycles_check() {
        slices().matching("eu.europeana.metis.image.enhancement..").should().beFreeOfCycles();
    }
}
