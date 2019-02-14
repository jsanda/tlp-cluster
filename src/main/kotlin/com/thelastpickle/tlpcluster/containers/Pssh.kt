package com.thelastpickle.tlpcluster.containers

import com.github.dockerjava.api.model.AccessMode
import com.thelastpickle.tlpcluster.Context
import com.thelastpickle.tlpcluster.Docker

class Pssh(val context: Context, val sshKey: String) {
    private val docker = Docker(context)
    private val dockerImageTag = "thelastpickle/tlp-cluster_pssh"
    private val volumeMappings = mutableListOf(
            Triple(sshKey, "/root/.ssh/aws-private-key", AccessMode.ro),
            Triple(context.cwdPath, "/local", AccessMode.rw))
    private val provisionCommand = "cd provisioning; chmod +x install.sh; sudo ./install.sh"

    init {

    }

    fun build() : String {
        return docker.buildContainer("DockerfileSSH", dockerImageTag)
    }

    fun copyProvisioningResources() : Result<String> {
        return docker.runContainer(
                dockerImageTag,
                mutableListOf("/bin/sh", "/local/copy_provisioning_resources.sh"),
                volumeMappings,
                "")
    }

    fun provisionNode(nodeType: String) : Result<String> {
        return docker.runContainer(
                dockerImageTag,
                mutableListOf("/bin/sh", "/local/parallel_ssh.sh", "$provisionCommand $nodeType"),
                volumeMappings,
                "")
    }

    fun startService(nodeType: String) : Result<String> {
        return docker.runContainer(
                dockerImageTag,
                mutableListOf("/bin/sh", "/local/parallel_ssh.sh", "sudo service $nodeType start"),
                volumeMappings,
                "")
    }
}