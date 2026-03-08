import org.gradle.api.tasks.compile.JavaCompile

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


plugins {
    id("java")
}

group = "no.nav"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20240303")
    implementation("io.github.cdimascio:dotenv-java:3.0.2")
}

tasks.test {
    useJUnitPlatform()
}