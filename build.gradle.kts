plugins {
    kotlin("jvm") version "1.5.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.projectreactor.netty" , "reactor-netty" , "1.0.+")
    implementation("ch.qos.logback" , "logback-classic" , "1.+")
//    implementation("org.slf4j" , "slf4j-log4j12" , "1.7.30")
    implementation("org.mongodb" , "mongodb-driver-reactivestreams" , "4.2.3")
    implementation("com.fasterxml.jackson.core" , "jackson-databind" , "2.10.4")
    implementation(kotlin("reflect"))
    implementation("com.auth0" , "java-jwt" , "3.10.3")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

application {
    mainClassName = "Server"

    val taskNames = gradle.startParameter.taskNames

    if( taskNames.contains("dev") ){
        applicationDefaultJvmArgs = project.ext.properties.map { "-D${it.key}=${it.value}" } + "-Denv=dev"
        println(">> D E V")
    }
}

task("dev"){
    dependsOn("run")
}