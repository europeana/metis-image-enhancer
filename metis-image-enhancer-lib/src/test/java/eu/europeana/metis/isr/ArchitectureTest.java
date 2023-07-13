package eu.europeana.metis.isr;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ArchitectureTest {
    private final JavaClasses allProjectClasses = new ClassFileImporter().importPackages("eu.europeana.metis.isr");

    @Test
    void domain_independent_of_infrastructure() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..infra..")
                .check(allProjectClasses);
    }

    @Test
    void domain_independent_of_library() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..library..")
                .check(allProjectClasses);
    }

    @Test
    void domain_independent_of_configuration() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..configuration..")
                .check(allProjectClasses);
    }

    @Test
    void project_free_cycles_check() {
        slices().matching("eu.europeana.metis.isr..").should().beFreeOfCycles();
    }
}
