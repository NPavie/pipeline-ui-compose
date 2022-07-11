
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "org.daisy.pipeline.ui"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://maven.restlet.talend.com")
}



kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }

        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.google.guava:guava:11.0.2")
                implementation("org.daisy.pipeline:framework-parent:1.14.6")
                implementation("org.daisy.pipeline:framework-core:5.1.0")
                implementation("org.osgi:org.osgi.compendium:5.0.0")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            /* full jdk modules for now, to be cleaned up later */
            modules("java.base",
                "java.compiler",
                "java.datatransfer",
                "java.desktop",
                "java.instrument",
                "java.logging",
                "java.management",
                "java.management.rmi",
                "java.naming",
                "java.net.http",
                "java.prefs",
                "java.rmi",
                "java.scripting",
                "java.se",
                "java.security.jgss",
                "java.security.sasl",
                "java.smartcardio",
                "java.sql",
                "java.sql.rowset",
                "java.transaction.xa",
                "java.xml",
                "java.xml.crypto",
                "jdk.accessibility",
                "jdk.attach",
                "jdk.charsets",
                "jdk.compiler",
                "jdk.crypto.cryptoki",
                "jdk.crypto.ec",
                "jdk.crypto.mscapi",
                "jdk.dynalink",
                "jdk.editpad",
                "jdk.hotspot.agent",
                "jdk.httpserver",
                "jdk.incubator.foreign",
                "jdk.incubator.vector",
                "jdk.internal.ed",
                "jdk.internal.jvmstat",
                "jdk.internal.le",
                "jdk.internal.opt",
                "jdk.internal.vm.ci",
                "jdk.internal.vm.compiler",
                "jdk.internal.vm.compiler.management",
                "jdk.jartool",
                "jdk.javadoc",
                "jdk.jcmd",
                "jdk.jconsole",
                "jdk.jdeps",
                "jdk.jdi",
                "jdk.jdwp.agent",
                "jdk.jfr",
                "jdk.jlink",
                "jdk.jpackage",
                "jdk.jshell",
                "jdk.jsobject",
                "jdk.jstatd",
                "jdk.localedata",
                "jdk.management",
                "jdk.management.agent",
                "jdk.management.jfr",
                "jdk.naming.dns",
                "jdk.naming.rmi",
                "jdk.net",
                "jdk.nio.mapmode",
                "jdk.random",
                "jdk.sctp",
                "jdk.security.auth",
                "jdk.security.jgss",
                "jdk.unsupported",
                "jdk.unsupported.desktop",
                "jdk.xml.dom",
                "jdk.zipfs")
            /*modules(
                "java.base",
                "java.desktop",
                "java.xml",
                "java.compiler",
                "java.instrument",
                "java.management",
                "java.naming",
                "java.rmi",
                "java.security.jgss",
                "java.sql",
                "jdk.management.agent",
                "jdk.zipfs",
                "jdk.httpserver",
                "jdk.unsupported"
            )*/
            packageName = "pipeline-ui-compose"
            packageVersion = "1.0.0"
        }

    }
}
