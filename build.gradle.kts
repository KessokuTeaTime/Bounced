class Display {
	lateinit var name: String
	lateinit var loader: String
	lateinit var version: String
}

var display: Display = Display()

plugins {
	base
	java
	idea
	`maven-publish`
	alias(libs.plugins.fabric.loom)
	alias(libs.plugins.modpublisher)
}

group = libs.versions.maven.group.get()
version = "${libs.versions.mod.get()}-${libs.versions.loader.get()}${libs.versions.minecraft.get()}"

display.name = libs.versions.display.name.get()
display.loader = libs.versions.display.loader.get()
display.version = libs.versions.display.version.get()

base {
	archivesName.set(libs.versions.archives.name)
}

repositories {
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
	maven { url = uri("https://api.modrinth.com/maven") }
}

dependencies {
	minecraft(libs.minecraft)
	mappings(libs.yarn) { artifact { classifier = "v2" } }
	modImplementation(libs.bundles.fabric)

	modCompileOnly(libs.splasher)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17

	withSourcesJar()
}

tasks {
	processResources {
		filesMatching("fabric.mod.json") {
			expand(mapOf(
					"version" to libs.versions.mod.get(),
					"display" to display
			))
		}
	}

	jar {
		from("LICENSE")
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {
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
	gameVersions.set(listOf("1.20", "1.20.1", "1.20.2"))
	loaders.set(listOf("fabric", "quilt"))

	modrinthDepends.required("fabric-api")
	modrinthDepends.optional("splasher")
	modrinthDepends.embedded()

	curseDepends.required("fabric-api")
	curseDepends.optional("splasher")
	curseDepends.embedded()
	
	displayName.set("${display.name} ${libs.versions.mod.get()} for ${display.loader} ${display.version}")

	artifact.set(tasks.remapJar)
	addAdditionalFile(tasks.remapSourcesJar)

	changelog.set(file("CHANGELOG.md"))
}
