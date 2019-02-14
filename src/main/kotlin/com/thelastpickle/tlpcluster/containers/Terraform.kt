package com.thelastpickle.tlpcluster.containers

import com.thelastpickle.tlpcluster.Context
import com.thelastpickle.tlpcluster.Docker
import com.github.dockerjava.api.model.AccessMode


class Terraform(val context: Context) {
    private val docker = Docker(context)
    private val dockerImageTag = "hashicorp/terraform"
    private var volumeMapping = mutableListOf(Triple(context.cwdPath, "/local", AccessMode.rw))
    private var localDirectory = "/local"

    init {

    }

    fun init() : Result<String> {
        return docker.runContainer(dockerImageTag, mutableListOf("init", "/local"), volumeMapping, localDirectory)
    }

    fun up() : Result<String> {
        return docker.runContainer(dockerImageTag, mutableListOf("apply", "/local"), volumeMapping, localDirectory)
    }

    fun down() : Result<String> {
        return docker.runContainer(dockerImageTag, mutableListOf("destroy", "/local"), volumeMapping, localDirectory)
    }

    fun cassandraIps(): Result<String> {
        return docker.runContainer(dockerImageTag, mutableListOf("output", "cassandra_ips"), volumeMapping, localDirectory)
    }

    fun cassandraInternalIps(): Result<String> {
        return docker.runContainer(dockerImageTag, mutableListOf("output", "cassandra_internal_ips"), volumeMapping, localDirectory)
    }

    fun stressIps() : Result<String> {
        return docker.runContainer(dockerImageTag, mutableListOf("output", "stress_ips"), volumeMapping, localDirectory)
    }

}