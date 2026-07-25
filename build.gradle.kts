import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
	base
	alias(libs.plugins.architectury)
	alias(libs.plugins.loom.remap) apply false
	alias(libs.plugins.loom.no.remap) apply false
	alias(libs.plugins.shadow) apply false
	alias(libs.plugins.modpublisher) apply false
}

val rootLibs = libs
val modId = libs.versions.archives.name.get()
val modVersion = libs.versions.mod.get()
val minecraftVersion = libs.versions.minecraft.get()
val javaVersion = libs.versions.java.get().toInt()
val deobfuscatedMinecraft = minecraftVersion.startsWith("26.")

group = libs.versions.maven.group.get()
version = modVersion

base {
	archivesName.set(modId)
}

architectury {
	minecraft = minecraftVersion
}

subprojects {
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")
	apply(plugin = "architectury-plugin")
	apply(plugin = if (deobfuscatedMinecraft) "dev.architectury.loom-no-remap" else "dev.architectury.loom")

	group = rootProject.group
	version = "$modVersion-${project.name}.$minecraftVersion"

	extensions.configure<BasePluginExtension> {
		archivesName.set(modId)
	}

	repositories {
		mavenCentral()
	}

	extensions.configure<LoomGradleExtensionAPI> {
		if (deobfuscatedMinecraft) {
			noIntermediateMappings()
		}
	}

	dependencies {
		add("minecraft", rootLibs.minecraft)
		if (!deobfuscatedMinecraft) {
			add("mappings", project.extensions.getByType<LoomGradleExtensionAPI>().officialMojangMappings())
		}
	}

	extensions.configure<JavaPluginExtension> {
		toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
		withSourcesJar()
	}

	tasks.withType<JavaCompile>().configureEach {
		options.encoding = "UTF-8"
		options.release.set(javaVersion)
	}

	tasks.withType<ProcessResources>().configureEach {
		val properties = mapOf(
			"version" to modVersion,
			"displayName" to rootLibs.versions.display.name.get(),
			"javaVersion" to javaVersion,
			"fabricMinecraftVersionRange" to rootLibs.versions.fabric.minecraft.range.get(),
			"fabricLoaderVersion" to rootLibs.versions.fabric.loader.get(),
			"javaFmlVersionRange" to "[${rootLibs.versions.javafml.get()},)",
			"neoforgeVersionRange" to rootLibs.versions.neoforge.range.get(),
			"neoforgeMinecraftVersionRange" to rootLibs.versions.neoforge.minecraft.range.get(),
			"splasherVersion" to rootLibs.versions.splasher.get()
		)

		inputs.properties(properties)
		filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
			expand(properties)
		}
	}

	tasks.named<Jar>("jar") {
		from(rootProject.file("LICENSE"))
	}
}

tasks.named("build") {
	dependsOn(subprojects.map { "${it.path}:build" })
}

tasks.register("publishMod") {
	group = "publishing"
	description = "Publishes the Fabric and NeoForge artifacts."
	dependsOn(":fabric:publishMod", ":neoforge:publishMod")
}
