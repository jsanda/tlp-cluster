package com.thelastpickle.tlpcluster.containers

import com.github.dockerjava.api.model.AccessMode
import com.thelastpickle.tlpcluster.Context
import com.thelastpickle.tlpcluster.Docker
import java.io.File

class CassandraBuildJava8(val context: Context) {

    private val docker = Docker(context)
    private val dockerImageTag = "thelastpickle/tlp-cluster-java8"

    val home = File(System.getProperty("user.home"))
    val builds = File(home, ".tlp-cluster/builds/")
    val mavenCache = File(home, ".tlp-cluster/maven-cache/").toString()

    fun build() : String {
        return docker.buildContainer("DockerfileCassandra", dockerImageTag)

    }

    /**
     * name of the build in the end
     */
    fun start(location: String, name: String) : Result<String> {



        val volumes = mutableListOf(
                Triple(location, "/cassandra", AccessMode.ro ),
                Triple(File(builds, name).toString(), "/builds/", AccessMode.rw),
                Triple(mavenCache, "/root/.m2/", AccessMode.rw)

        )

        println("Volumes: $volumes")

        return docker.runContainer(
                dockerImageTag,
                mutableListOf("/usr/bin/build_cassandra.sh"),
                volumes,
                "/cassandra"
        )

    }



}