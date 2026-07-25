import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar

plugins {
	alias(libs.plugins.shadow)
	alias(libs.plugins.modpublisher)
}

architectury {
	platformSetupLoomIde()
	neoForge()
}

val common by configurations.creating
val shadowCommon by configurations.creating

repositories {
	maven("https://maven.neoforged.net/releases/")
}

configurations.named("compileClasspath") {
	extendsFrom(common)
}
configurations.named("runtimeClasspath") {
	extendsFrom(common)
}
configurations.named("developmentNeoForge") {
	extendsFrom(common)
}

dependencies {
	"neoForge"(libs.neoforge)
	common(project(path = ":common")) {
		isTransitive = false
	}
	shadowCommon(project(path = ":common", configuration = "transformProductionNeoForge")) {
		isTransitive = false
	}
}

tasks.jar {
	archiveClassifier.set("raw")
}

tasks.shadowJar {
	configurations = listOf(shadowCommon)
	archiveClassifier.set("")
	destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
	exclude("architectury.common.json")
	from(rootProject.file("LICENSE"))
}

tasks.assemble {
	dependsOn(tasks.shadowJar)
}

tasks.sourcesJar {
	val commonSources = project(":common").tasks.named<Jar>("sourcesJar")
	dependsOn(commonSources)
	from(commonSources.flatMap { it.archiveFile }.map { zipTree(it) })
	from(rootProject.file("LICENSE"))
	destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
}

tasks.named("publishMod") {
	dependsOn(tasks.shadowJar, tasks.sourcesJar)
}

components.getByName<AdhocComponentWithVariants>("java") {
	withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
		skip()
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenNeoForge") {
			artifactId = "${libs.versions.archives.name.get()}-neoforge"
			artifact(tasks.shadowJar)
			artifact(tasks.sourcesJar)
		}
	}
}

publisher {
	apiKeys {
		modrinth(System.getenv("MODRINTH_TOKEN"))
		curseforge(System.getenv("CURSEFORGE_TOKEN"))
	}

	modrinthID.set(libs.versions.id.modrinth)
	curseID.set(libs.versions.id.curseforge)
	versionType.set("release")
	projectVersion.set(project.version.toString())
	gameVersions.set(libs.versions.supported.minecraft.get().split(","))
	loaders.set(listOf("neoforge"))
	curseEnvironment.set("client")
	modrinthDepends.optional("splasher")
	curseDepends.optional("splasher")
	displayName.set("${libs.versions.display.name.get()} ${libs.versions.mod.get()} for NeoForge ${libs.versions.minecraft.get()}")
	artifact.set(tasks.shadowJar)
	addAdditionalFile(tasks.sourcesJar)
	changelog.set(rootProject.file("CHANGELOG.md"))
}
