import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            extensions.configure<CommonExtension<*, *, *, *, *, *>> {
                buildFeatures {
                    compose = true
                }

                dependencies {
                    val bom = libs.findLibrary("compose.bom").get()
                    add("implementation", platform(bom))
                    add("implementation", libs.findLibrary("compose.ui").get())
                    add("implementation", libs.findLibrary("compose.material3").get())
                    add("debugImplementation", libs.findLibrary("compose.ui.tooling").get())
                }
            }
        }
    }
} 