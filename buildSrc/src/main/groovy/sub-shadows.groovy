import org.gradle.api.Plugin
import org.gradle.api.Project

class SubShadowsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create("subshadows", SubShadowsPluginExtension)

        project.task("velocity", type: VelocityTask) {
            def shadowsCoreProject = project.findProject(":robolectric-shadows/shadows-core")
//            source = shadowsCoreProject.sourceSets.main.resources.matching { include "**/*.vm" }
            source = shadowsCoreProject.files("src/main/resources").asFileTree.matching { include "/**/*.vm" }
            println("source:::::::::::::::::::${source.files} ${shadowsCoreProject.files(".").files}")

            doFirst {
                def androidApi = project.subshadows.apiLevel
                def robolectricVersion = AndroidSdk.versions[androidApi]
                logger.info "Android API: $androidApi — $robolectricVersion"
                println "Android API: $androidApi — $robolectricVersion"

                contextValues = [api: androidApi]
                if (androidApi >= 21) {
                    contextValues.putAll(ptrClass: "long", ptrClassBoxed: "Long")
                } else {
                    contextValues.putAll(ptrClass: "int", ptrClassBoxed: "Integer")
                }
            }

            outputDir = project.file("${project.buildDir}/generated-shadows")

            doLast {
                def shadowsCoreProj = project.findProject(":robolectric-shadows/shadows-core")
                project.copy {
                    from shadowsCoreProj.files("src/main/java")
                    from shadowsCoreProj.fileTree("src/main/resources").include("META-INF/**")
                    into outputDir
                }
            }
        }

    }
}


class SubShadowsPluginExtension {
    int apiLevel
}