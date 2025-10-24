plugins {
	base
	java
	idea
	`maven-publish`
	alias(libs.plugins.architectury.loom)
	alias(libs.plugins.modpublisher)
}

val display = libs.versions.display

group = libs.versions.maven.group.get()
version = "${libs.versions.mod.get()}-${libs.versions.loader.get()}.${libs.versions.minecraft.get()}"

base {
	archivesName.set(libs.versions.archives.name)
}

repositories {
	mavenCentral()
	maven { url = uri("https://jitpack.io") }
	maven { url = uri("https://api.modrinth.com/maven") }
    maven { url = uri("https://maven.neoforged.net/releases/") }
}

dependencies {
	minecraft(libs.minecraft)
    mappings(loom.layered {
        mappings(variantOf(libs.yarn) { classifier("v2") })
        mappings(libs.yarn.patch)
    })
    neoForge(libs.neoforge)

	//modCompileOnly(libs.splasher)
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21

	withSourcesJar()
}

tasks {
	processResources {
		filesMatching("META-INF/neoforge.mods.toml") {
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
	gameVersions.set(listOf("1.21", "1.21.1"))
	loaders.set(listOf("neoforge"))
	curseEnvironment.set("client")

	modrinthDepends.optional("splasher")
	modrinthDepends.embedded()

	curseDepends.optional("splasher")
	curseDepends.embedded()
	
	displayName.set("${display.name.get()} ${libs.versions.mod.get()} for ${display.loader.get()} ${display.version.get()}")

	artifact.set(tasks.remapJar)
	addAdditionalFile(tasks.remapSourcesJar)

	changelog.set(file("CHANGELOG.md"))
}
